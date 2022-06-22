package com.example.triple.repository;

import com.example.triple.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PointRepository extends JpaRepository<Point, UUID> {
    List<Point> findAllByReviewIdAndStatus(UUID reviewId, String active);
}
