package com.example.triple.repository;


import com.example.triple.domain.RecentImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecentImgRepository extends JpaRepository<RecentImg, UUID> {
}
