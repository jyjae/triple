package com.example.triple.repository.querydsl;

import com.example.triple.domain.Review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepositoryCustom {
    Review findReviewByReviewImgStatus (UUID reviewId, String status);
    Review findByIdAndUserIdAndPlaceIdAndStatus(UUID reviewId, UUID userId, UUID placeId, String status);

    Long modifyReviewStatusInActive(UUID reviewId);
}
