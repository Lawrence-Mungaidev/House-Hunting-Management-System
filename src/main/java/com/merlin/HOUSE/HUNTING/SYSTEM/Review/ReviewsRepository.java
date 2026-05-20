package com.merlin.HOUSE.HUNTING.SYSTEM.Review;

import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByApartmentUnit(Long apartmentUnitId);
    boolean existsByStudentAndApartmentUnit(User authetnicatedUser, ApartmentUnit apartmentUnit);

    Double countReviewsByApartmentUnit(ApartmentUnit apartmentUnit);
}
