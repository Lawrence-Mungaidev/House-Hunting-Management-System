package com.merlin.HOUSE.HUNTING.SYSTEM.Appeal;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppealRepository extends JpaRepository<Appeal,Long> {

    boolean existsByLandLordANDApartmentAndStatus(User user, Apartment apartment, Status status);
    List<Appeal> findByLandLordOrderByCreatedAtDesc(User user);
    List<Appeal> findByAdminOrderBYResponseDate(User user);
    List<Appeal> findByAdminAndStatus(User user, Status status);
    List<Appeal> findByLandLordAndStatus(User user, Status status);
}
