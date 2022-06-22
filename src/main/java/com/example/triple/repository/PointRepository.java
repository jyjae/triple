package com.example.triple.repository;

import com.example.triple.domain.Point;
import com.example.triple.repository.querydsl.PointRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PointRepository extends PointRepositoryCustom, JpaRepository<Point, UUID> {

}
