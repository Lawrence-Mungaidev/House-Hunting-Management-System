package com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentUnitRepository extends JpaRepository<ApartmentUnit,Long> {

    List<ApartmentUnit> findAllByApartment(Apartment apartment);
}
