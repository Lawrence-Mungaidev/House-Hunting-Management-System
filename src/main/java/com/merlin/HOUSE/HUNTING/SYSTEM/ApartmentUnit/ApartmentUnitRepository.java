package com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ApartmentUnitRepository extends JpaRepository<ApartmentUnit,Long> {

    List<ApartmentUnit> findAllByApartment(Apartment apartment);

    @Query("SELECT a FROM ApartmentUnit a WHERE " +
            "(:minRent IS NULL OR a.rentPrice >= :minRent) AND " +
            "(:maxRent IS NULL OR a.rentPrice <= :maxRent) AND " +
            "(:unitType IS NULL OR a.unitType = :unitType)")
    List<ApartmentUnit> searchApartmentUnits(
            @Param("maxRent") BigDecimal maxRent,
            @Param("minRent") BigDecimal minRent,
            @Param("unitType") UnitType unitType);


}
