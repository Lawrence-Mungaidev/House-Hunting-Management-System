package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.Location;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.LocationRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;
    private final LocationRepository locationRepository;
    private final CampusRepository campusRepository;

    public LandLordApartmentResponse createApartment(User authenticatedUser, ApartmentDto apartmentDto){
        Apartment apartment = apartmentMapper.toApartment(apartmentDto);

        apartment.setLandlord(authenticatedUser);
        Location apartmentLocation = new Location(apartmentDto.apartmentLat(),  apartmentDto.apartmentLong());

        setCampusToApartment(apartmentDto, apartment);

        var savedLocation = locationRepository.save(apartmentLocation);

        apartment.setLocation(savedLocation);

        var savedApartment = apartmentRepository.save(apartment);

        return apartmentMapper.toLandLordApartmentResponse(savedApartment);
    }


    public LandLordApartmentResponse updateApartment(Long apartmentId, ApartmentDto dto){
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFound("Sorry but the apartment wasn't found"));

        if(dto.apartmentName() != null ){
            apartment.setApartmentName(dto.apartmentName());
        }
        if(dto.description() != null ){
            apartment.setDescription(dto.description());
        }
        if(dto.profilePicture() != null ){
            apartment.setProfilePicture(dto.profilePicture());
        }
        if (dto.apartmentLat() != null && dto.apartmentLong() != null){
            Location location = new Location(dto.apartmentLat(), dto.apartmentLong());
            var savedLocation = locationRepository.save(location);
            apartment.setLocation(savedLocation);
        }

        if(dto.campusId() != null){
            setCampusToApartment(dto, apartment);

        }

        var savedApartment = apartmentRepository.save(apartment);

        return apartmentMapper.toLandLordApartmentResponse(savedApartment);
    }

    private void setCampusToApartment(ApartmentDto dto, Apartment apartment) {
        List<Campus> campusList = new ArrayList<>();

        for(Long campusId : dto.campusId() ){
            Campus campus = campusRepository.findById(campusId)
                    .orElseThrow(() -> new ResourceNotFound("Campus noot found"));

            campusList.add(campus);

        }

        apartment.setCampus(campusList);
    }

    public void activateApartment(Long apartmentId){
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFound("Sorry but the apartment wasn't found"));

        apartment.setActive(true);
        apartmentRepository.save(apartment);
    }

    public void deactivateApartment(Long apartmentId){
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFound("Sorry but the apartment wasn't found"));

        apartment.setActive(false);
        apartmentRepository.save(apartment);
    }

    public void deleteApartment(Long apartmentId){
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(()-> new ResourceNotFound("Sorry but the apartment wasn't found"));

        if (apartment.isActive() || !apartment.getApartmentUnits().isEmpty()){
            throw new BusinessRuleException("You cant delete an apartment because its Active or has apartment units");
        }

        apartmentRepository.delete(apartment);

    }

    public List<ApartmentResponseDto> getAllApartments(){
        return apartmentRepository.findAll()
                .stream()
                .map(apartment -> apartmentMapper.toApartmentResponseDto(apartment,null))
                .toList();
    }

    public ApartmentResponseDto getApartmentById(User authenticatedUser,Long apartmentId){
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFound("Sorry but the apartment wasn't found"));

        if (authenticatedUser.getCampus() == null){
            return apartmentMapper.toApartmentResponseDto(apartment,null);
        }

        Double apartmentLat = apartment.getLocation().getLatitude();
        Double apartmentLong = apartment.getLocation().getLongitude();

        Double campusLat = authenticatedUser.getCampus().getLocation().getLatitude();
        Double campusLong = authenticatedUser.getCampus().getLocation().getLongitude();

        Double campusDistance = calculateDistance(apartmentLat,apartmentLong,campusLat,campusLong);

        return apartmentMapper.toApartmentResponseDto(apartment,campusDistance);
    }

    public List<LandLordApartmentResponse> getMyApartment(User authenticatedUser){
        return apartmentRepository.findByLandlord(authenticatedUser)
                .stream()
                .map(apartmentMapper :: toLandLordApartmentResponse)
                .toList();
    }

    private Double calculateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        final int EARTH_RADIUS = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public List<ApartmentResponseDto> getApartmentsByCampus(User authenticatedUser){
        Campus campus =authenticatedUser.getCampus();

        if (campus == null){
            throw new BusinessRuleException("Only Students can view by Campus");
        }

        return apartmentRepository.findByCampusesContaining(campus)
                .stream()
                .map(apartment -> {
                    Double campusDistance = calculateDistance(
                            apartment.getLocation().getLatitude(),
                            apartment.getLocation().getLongitude(),
                            campus.getLocation().getLatitude(),
                            campus.getLocation().getLongitude()
                    );
                    return apartmentMapper.toApartmentResponseDto(apartment,campusDistance);
                } )
                .toList();

    }

}
