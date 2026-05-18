package com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.ApartmentRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentUnitService {

    private final ApartmentUnitRepository apartmentUnitRepository;
    private final ApartmentUnitMapper apartmentUnitMapper;
    private final ApartmentRepository apartmentRepository;

    public ApartmentUnitResponse createApartmentUnit(apartmentUnitDto dto){
        Apartment apartment = apartmentRepository.findById(dto.apartmentId())
                .orElseThrow(() -> new ResourceNotFound("apartment not found"));

        ApartmentUnit apartmentUnit = apartmentUnitMapper.toApartmentUnit(dto);
        apartmentUnit.setApartment(apartment);

        var savedApartmentUnit = apartmentUnitRepository.save(apartmentUnit);

        return apartmentUnitMapper.toApartmentUnitResponse(savedApartmentUnit);

    }

    public ApartmentUnitResponse updateApartmentUnit(Long apartmentUnitId, apartmentUnitDto dto){
        ApartmentUnit apartmentUnit = apartmentUnitRepository.findById(apartmentUnitId)
                .orElseThrow(()-> new ResourceNotFound("The apartment Unit is currently not available"));

        if (dto.unitType() != null) {
            apartmentUnit.setUnitType(dto.unitType());
        }
        if(dto.numberOfUnits() != null){
            apartmentUnit.setNumberOfUnits(dto.numberOfUnits());
        }
        if (dto.vacantUnits() != null){
            apartmentUnit.setVacantUnits(dto.vacantUnits());
        }
        if(dto.rentPrice() != null){
            apartmentUnit.setRentPrice(dto.rentPrice());
        }

        var savedUnit = apartmentUnitRepository.save(apartmentUnit);

        return apartmentUnitMapper.toApartmentUnitResponse(savedUnit);
    }

    public void activateUnit(Long apartmentUnitId){
        ApartmentUnit apartmentUnit = apartmentUnitRepository.findById(apartmentUnitId)
                .orElseThrow(() -> new ResourceNotFound("The apartment Unit is currently not available"));

        apartmentUnit.setAvailable(true);

        apartmentUnitRepository.save(apartmentUnit);
    }

    public void deactivateUnit(Long apartmentUnitId){
        ApartmentUnit apartmentUnit = apartmentUnitRepository.findById(apartmentUnitId)
                .orElseThrow(() -> new ResourceNotFound("The apartment Unit is currently not available"));

        apartmentUnit.setAvailable(false);

        apartmentUnitRepository.save(apartmentUnit);
    }

    public List<ApartmentUnitResponse> getApartmentUnitsByApartment(Long apartmentId){
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(()-> new ResourceNotFound("Apartment isnt currently available"));

        return apartmentUnitRepository.findAllByApartment(apartment)
                .stream()
                .map(apartmentUnitMapper :: toApartmentUnitResponse)
                .toList();
    }

    public ApartmentUnitResponse getApartmentUnitById(Long apartmentUnitId){
        ApartmentUnit apartmentUnit = apartmentUnitRepository.findById(apartmentUnitId)
                .orElseThrow(() -> new ResourceNotFound("The apartment Unit is currently not available"));

        return apartmentUnitMapper.toApartmentUnitResponse(apartmentUnit);
    }

    public void deleteApartmentUnit(Long apartmentUnitId){
        ApartmentUnit apartmentUnit = apartmentUnitRepository.findById(apartmentUnitId)
                .orElseThrow(() -> new  ResourceNotFound("The apartment Unit couldn't be found"));

        if (apartmentUnit.isAvailable()){
            throw new BusinessRuleException("Cannot delete apartment unit because its currently active");
        }

        apartmentUnitRepository.delete(apartmentUnit);
    }


}
