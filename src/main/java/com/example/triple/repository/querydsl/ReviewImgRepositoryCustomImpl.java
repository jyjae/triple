package com.example.triple.repository.querydsl;

import com.example.triple.domain.QReviewImg;
import com.example.triple.domain.ReviewImg;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.UUID;

public class ReviewImgRepositoryCustomImpl extends QuerydslRepositorySupport implements ReviewImgRepositoryCustom{

    public ReviewImgRepositoryCustomImpl() {
        super(ReviewImg.class);
    }

    @Override
    public Long modifyReviewImgStatusInActive(UUID reviewId) {
        QReviewImg reviewImg = QReviewImg.reviewImg;

        UpdateClause<JPAUpdateClause> updateBuilder = update(reviewImg)
                .set(reviewImg.status, "INACTIVE")
                .where(reviewImg.review.id.eq(reviewId));;

        return updateBuilder.execute();
    }
}
