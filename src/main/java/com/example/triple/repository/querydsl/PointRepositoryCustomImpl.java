package com.example.triple.repository.querydsl;

import com.example.triple.domain.Point;
import com.example.triple.domain.QPoint;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.UUID;

public class PointRepositoryCustomImpl extends QuerydslRepositorySupport implements PointRepositoryCustom {

    public PointRepositoryCustomImpl() {
        super(Point.class);
    }

    @Override
    public List<Point> findAllByReviewIdAndStatus(UUID reviewId, UUID userId, String active) {
        QPoint point = QPoint.point;

        JPQLQuery<Point> query = from(point)
                .select(Projections.constructor(
                        Point.class,
                        point.id,
                        point.user,
                        point.review,
                        point.pointCnt,
                        point.action,
                        point.status,
                        point.createdAt,
                        point.updatedAt
                ));

        if(reviewId != null) {
            query.where(point.review.id.eq(reviewId));
        }
        if(userId != null) {
           query.where(point.user.id.eq(userId));
        }

        query.where(point.status.eq(active));
        return query.fetch();
    }
}
