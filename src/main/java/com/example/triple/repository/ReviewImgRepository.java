package com.example.triple.repository;

import com.example.triple.domain.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, UUID> {
    List<ReviewImg> findByReviewId(UUID reviewId);
}
