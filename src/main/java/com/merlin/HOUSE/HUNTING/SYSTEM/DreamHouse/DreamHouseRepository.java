package com.merlin.HOUSE.HUNTING.SYSTEM.DreamHouse;

import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DreamHouseRepository extends JpaRepository<DreamHouse,Long> {

    List<DreamHouse> findByStudent(User user);
    boolean existsByStudentAndApartmentUnit(User user, ApartmentUnit apartmentUnit);
}
