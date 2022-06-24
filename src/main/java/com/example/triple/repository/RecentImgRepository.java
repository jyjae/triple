package com.example.triple.repository;


import com.example.triple.domain.RecentImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecentImgRepository extends JpaRepository<RecentImg, UUID> {
    Optional<RecentImg> findByIdAndUserIdAndStatus(UUID id, UUID userId, String active);

    boolean existsByIdAndStatus(UUID imgId, String active);

    List<RecentImg> findByUserIdAndStatus(UUID userId, String active);
}
