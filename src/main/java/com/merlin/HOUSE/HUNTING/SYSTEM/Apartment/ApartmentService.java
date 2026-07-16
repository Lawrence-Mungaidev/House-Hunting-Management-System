package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnitRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.Location;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.LocationRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;
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
    private final ApartmentUnitRepository apartmentUnitRepository;

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
        return apartmentRepository.findAllByOrderByAverageRatingDesc()
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

    public List<ApartmentResponseDto> searchApartment(User authenticatedUser , SearchDto dto){
        List<ApartmentUnit> apartmentUnitList = apartmentUnitRepository.searchApartmentUnits(dto.maxRent(),dto.minRent(),dto.unitType());

        List<ApartmentResponseDto> apartmentList = new ArrayList<>();

        if(authenticatedUser.getCampus() == null){
            throw new BusinessRuleException("The user isn't under any campus");
        }

        Double campusLat = authenticatedUser.getCampus().getLocation().getLatitude();
        Double campusLong = authenticatedUser.getCampus().getLocation().getLongitude();

        for (ApartmentUnit apartmentUnit : apartmentUnitList){
           Apartment apartment = apartmentUnit.getApartment();

           Double apartmentLat = apartment.getLocation().getLatitude();
           Double apartmentLong = apartment.getLocation().getLongitude();

           Double campusDistance = calculateDistance(campusLat, campusLong, apartmentLat,apartmentLong);

           if(apartmentList.stream().noneMatch(a -> a.apartmentId().equals(apartment.getId()))){
               ApartmentResponseDto apartmentResponseDto = apartmentMapper.toApartmentResponseDto(apartment, campusDistance);
               apartmentList.add(apartmentResponseDto);
           }
        }

        return apartmentList;
    }

    public List<ApartmentResponseDto> getApartmentByName(String apartmentName, User authenticatedUser){
       List<Apartment>   apartment = apartmentRepository.findByApartmentNameContaining(apartmentName);


        if( authenticatedUser.getRole().equals(Role.SUPER_ADMIN)){


            return apartment
                    .stream()
                    .map(a -> apartmentMapper.toApartmentResponseDto(a , null) )
                    .toList();
        }

        if(authenticatedUser.getCampus() == null) {
            throw new BusinessRuleException("The user isn't under any campus");
        }

        double campusLat = authenticatedUser.getCampus().getLocation().getLatitude();
        double campusLong = authenticatedUser.getCampus().getLocation().getLongitude();

        return apartment
                .stream()
                .map(

                        a->  {
                            double campusDistance = calculateDistance(a.getLocation().getLatitude(),a.getLocation().getLongitude(), campusLat,campusLong);
                            return apartmentMapper.toApartmentResponseDto(a,campusDistance);
                        }
                ).toList();

    }


}
