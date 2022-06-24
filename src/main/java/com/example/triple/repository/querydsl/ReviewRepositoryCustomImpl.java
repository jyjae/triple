package com.example.triple.repository.querydsl;

import com.example.triple.domain.*;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReviewRepositoryCustomImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom{
    public ReviewRepositoryCustomImpl() {
        super(Review.class);
    }

    @Override
    public Review findReviewByReviewImgStatus(UUID reviewId, String status) {
        QReview review = QReview.review;
        QReviewImg reviewImg = QReviewImg.reviewImg;

        JPQLQuery<Review> query = from(review)
                .leftJoin(review.reviewImgs, reviewImg)
                .on(reviewImg.status.eq(status))
                .where(review.id.eq(reviewId))
                .where(review.status.eq("ACTIVE"));

        Review savedReview= query.fetchOne();
        savedReview.getReviewImgs().removeAll(
                savedReview.getReviewImgs().stream()
                        .filter(x->x.getStatus().equals("INACTIVE"))
                        .collect(Collectors.toList())
        );
        return savedReview;
    }

    @Override
    public Review findByIdAndUserIdAndPlaceIdAndStatus(UUID reviewId, UUID userId, UUID placeId, String status) {
        QReview review = QReview.review;
        QReviewImg reviewImg = QReviewImg.reviewImg;


        JPQLQuery<Review> query = from(review)
                .where(review.user.id.eq(userId))
                .where(review.place.id.eq(placeId))
                .where(review.status.eq(status));

        Optional<Review> savedReviewOptional = Optional.ofNullable(query.fetchOne());
        Review savedReview = savedReviewOptional.orElse(null);
         if(savedReview!=null) {
             savedReview.getReviewImgs().removeAll(
                savedReview.getReviewImgs().stream()
                        .filter(x->x.getStatus().equals("INACTIVE"))
                        .collect(Collectors.toList()));
         }

        return savedReview;
    }

    @Override
    public Long modifyReviewStatusInActive(UUID reviewId) {
        QReview review = QReview.review;

        UpdateClause<JPAUpdateClause> updateBuilder = update(review);
        updateBuilder.set(review.status, "INACTIVE").where(review.id.eq(reviewId));

        return updateBuilder.execute();
    }
}
