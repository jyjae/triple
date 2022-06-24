package com.example.triple.repository;

import com.example.triple.domain.Review;
import com.example.triple.repository.querydsl.ReviewRepositoryCustom;
import org.slf4j.MDC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends ReviewRepositoryCustom, JpaRepository<Review, UUID> {
    boolean existsByPlaceIdAndStatus(UUID placeId, String active);

    boolean existsByIdAndUserIdAndStatus(UUID reviewId, UUID userId, String active);

    boolean existsByUserIdAndPlaceIdAndStatus(UUID userId, UUID placeId, String active);

    Optional<List<Review>> findByUserIdAndStatus(UUID userId,String active);

    //Review findByIdAndUserIdAndPlaceIdAndStatus(UUID reviewId, UUID userId, UUID placeId, String active);

}
