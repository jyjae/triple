package com.example.triple.repository;

import com.example.triple.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, UUID> {

}
