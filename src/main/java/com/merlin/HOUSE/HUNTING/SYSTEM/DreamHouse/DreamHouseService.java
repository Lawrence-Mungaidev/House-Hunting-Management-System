package com.merlin.HOUSE.HUNTING.SYSTEM.DreamHouse;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnitRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DreamHouseService {

    private final DreamHouseRepository dreamHouseRepository;
    private final DreamHouseMapper dreamHouseMapper;
    private final ApartmentUnitRepository apartmentUnitRepository;

    public DreamHouseResponse addDreamHouse(Long apartmentUnit, User authenticatedUser){
        ApartmentUnit unit = apartmentUnitRepository.findById(apartmentUnit)
                .orElseThrow(() -> new ResourceNotFound("apartment unit not found"));

        if (dreamHouseRepository.existsByStudentAndApartmentUnit(authenticatedUser, unit)) {
            throw new BusinessRuleException("You've already saved this unit");
        }

        DreamHouse dreamHouse = dreamHouseMapper.toDreamHouse(unit);
        dreamHouse.setStudent(authenticatedUser);

       var savedDreamHouse = dreamHouseRepository.save(dreamHouse);

        return dreamHouseMapper.toDreamHouseResponse(unit, savedDreamHouse.getId());
    }

    public void deleteDreamHouse(Long dreamHouseId, User authenticatedUser){

        DreamHouse dreamHouse = dreamHouseRepository.findById(dreamHouseId)
                .orElseThrow(() -> new ResourceNotFound("dream house not found"));

        if (!dreamHouse.getStudent().equals(authenticatedUser)) {
            throw new BusinessRuleException("You can only remove your own saved units");
        }

        dreamHouseRepository.delete(dreamHouse);

    }

    public List<DreamHouseResponse> getAllDreamHouse(User authenticatedUser){

        return dreamHouseRepository.findByStudent(authenticatedUser)
                .stream()
                .map(house -> {
                    Long houseId = house.getId();
                    ApartmentUnit apartmentUnit = house.getApartmentUnit();

                   return dreamHouseMapper.toDreamHouseResponse(apartmentUnit, houseId);
                })
                .toList();
    }
}
