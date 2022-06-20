package com.example.triple.repository;

import com.example.triple.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    @Query(value = "SELECT EXIST (SELECT r.id FROM reviews r WHERE place_id = :placeId)", nativeQuery = true)
    int exist(UUID placeId);
}
