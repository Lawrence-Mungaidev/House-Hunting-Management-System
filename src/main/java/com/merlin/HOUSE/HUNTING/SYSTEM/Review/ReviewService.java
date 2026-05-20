package com.merlin.HOUSE.HUNTING.SYSTEM.Review;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.ApartmentRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnitRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewsRepository reviewsRepository;
    private final ReviewMapper reviewMapper;
    private final ApartmentUnitRepository apartmentUnitRepository;
    private final ApartmentRepository apartmentRepository;

    public ReviewResponseDto createReview(User authenticatedUser, Long apartmentUnitId,  ReviewDto dto) {
        ApartmentUnit apartmentUnit = apartmentUnitRepository.findById(apartmentUnitId)
                .orElseThrow(() -> new ResourceNotFound("apartment unit not found"));

        if(reviewsRepository.existsByStudentAndApartmentUnit(authenticatedUser, apartmentUnit)) {
            throw new BusinessRuleException("You've already reviewed this unit");
        }

        Review review = reviewMapper.toReview(dto);
        review.setApartmentUnit(apartmentUnit);
        review.setStudent(authenticatedUser);

        var savedReview = reviewsRepository.save(review);

        Double numberOfReviews = reviewsRepository.countReviewsByApartmentUnit(apartmentUnit);

        double totalRating = apartmentUnit.getAverageRating() * (numberOfReviews - 1);
        apartmentUnit.setAverageRating((totalRating + review.getRating()) / numberOfReviews);
        apartmentUnitRepository.save(apartmentUnit);

        Apartment apartment = apartmentUnit.getApartment();

        OptionalDouble apartmentAverageRating = apartment.getApartmentUnits()
                .stream()
                .mapToDouble(ApartmentUnit :: getAverageRating)
                .average();

        apartmentAverageRating.ifPresent(apartment::setAverageRating);
        apartmentRepository.save(apartment);


        return reviewMapper.toReviewResponseDto(savedReview);
    }

    public List<ReviewResponseDto> getReviewsByUnit(Long apartmentUnitId) {

        return  reviewsRepository.findReviewsByApartmentUnit(apartmentUnitId)
                .stream()
                .map(reviewMapper :: toReviewResponseDto)
                .toList();
    }

    public void  deleteReview(User authenticatedUser, Long reviewId) {
        Review review = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFound("review not found"));

        if (review.getStudent().equals(authenticatedUser)) {
            reviewsRepository.deleteById(reviewId);
        }else {
            throw new BusinessRuleException("You can only delete your own review");
        }
    }

}
