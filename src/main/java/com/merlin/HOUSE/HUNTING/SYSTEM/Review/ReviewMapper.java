package com.merlin.HOUSE.HUNTING.SYSTEM.Review;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review toReview(ReviewDto dto) {
        Review review = new Review();

        review.setComment(dto.comment());
        review.setRating(dto.rating());
        review.setCreatedAt(LocalDateTime.now());

        return review;
    }

    public ReviewResponseDto toReviewResponseDto(Review review) {
        return new ReviewResponseDto(review.getId(), review.getComment(), review.getRating());
    }
}
