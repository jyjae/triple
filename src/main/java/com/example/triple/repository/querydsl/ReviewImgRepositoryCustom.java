package com.example.triple.repository.querydsl;

import java.util.UUID;

public interface ReviewImgRepositoryCustom {
    Long modifyReviewImgStatusInActive(UUID reviewId);
}
