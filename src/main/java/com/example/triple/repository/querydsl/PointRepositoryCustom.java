package com.example.triple.repository.querydsl;

import com.example.triple.domain.Point;

import java.util.List;
import java.util.UUID;

public interface PointRepositoryCustom {
    List<Point> findAllByReviewIdAndStatus(UUID reviewId, UUID userId, String active);
}
