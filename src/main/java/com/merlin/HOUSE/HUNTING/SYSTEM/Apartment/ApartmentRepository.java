package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment,Long> {

    List<Apartment> findByLandlord (User landlord);
    List<Apartment> findByCampusesContaining(Campus campus );
    List<Apartment> findByApartmentNameContaining(String apartmentName);
    List<Apartment> findAllByOrderByAverageRatingDesc();
}
