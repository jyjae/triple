package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.constant.PointAction;
import com.example.triple.domain.Point;
import com.example.triple.domain.Review;
import com.example.triple.domain.ReviewImg;
import com.example.triple.domain.User;
import com.example.triple.dto.ReviewDto;
import com.example.triple.repository.PointRepository;
import com.example.triple.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PointService {
    Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final PointRepository pointRepository;
    private final ReviewRepository reviewRepository;

    public PointService(PointRepository pointRepository, ReviewRepository reviewRepository) {
        this.pointRepository = pointRepository;
        this.reviewRepository = reviewRepository;
    }


    private boolean isContentEmptyOrNull(String str) {
        if (str != null && !str.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isImgEmptyOrNull(List<ReviewImg> imgUrls) {
        if(imgUrls != null && !imgUrls.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public int countPoint(UUID placeId, String content, List<UUID> imgId)
            throws BaseException {
        int pointCnt = 0;
        try {
            // 해당 place의 첫 리뷰면 보너스 점수
            if(!reviewRepository
                    .existsByPlaceIdAndStatus(placeId, "ACTIVE")) {
                pointCnt+=1;
            }
            // content 한글자라도 있음 +1
            if(content!=null && !content.isEmpty()) {
                pointCnt+=1;
            }
            // img가 하나라도 있음 +1
            if(imgId!=null && ! imgId.isEmpty()) {
                pointCnt+=1;
            }

            return pointCnt;
        }catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public void addPoint(User user, Review review, PointAction action, int point)
            throws BaseException {
        try {
            if(point==0) return;
            pointRepository.save(
                    Point.of(user, review, action, point));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int modifyPoint(ReviewDto reviewDto, Review review) throws BaseException {
        int pointCnt = 0;

       if((review.getContent()==null ||
               review.getContent().isEmpty()) &&
               (reviewDto.getContent()!=null &&
                       !reviewDto.getContent().isEmpty())
       ) {
           pointCnt+=1;
       }
       else if((review.getContent()!=null &&
               !review.getContent().isEmpty() &&
               (reviewDto.getContent()==null ||
                       reviewDto.getContent().isEmpty()))
       ) {
           pointCnt-=1;
        }

        if((review.getReviewImgs()==null ||
                review.getReviewImgs().isEmpty()) &&
                (reviewDto.getAttachedPhotoIds()!=null &&
                        !reviewDto.getAttachedPhotoIds().isEmpty())
        ) {
            pointCnt+=1;
        }
        else if((review.getReviewImgs()!=null &&
                !review.getReviewImgs().isEmpty()) &&
                (reviewDto.getAttachedPhotoIds()==null ||
                        reviewDto.getAttachedPhotoIds().isEmpty())
        ) {
            pointCnt-=1;
        }

        return pointCnt;

    }

    public int getReviewPoint(UUID reviewId) throws BaseException {
        try {
            return calculatePoint(pointRepository.findAllByReviewIdAndStatus(
                    reviewId,
                    "ACTIVE")
            );

        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private int calculatePoint(List<Point> points) {
        int pointCnt = 0;
        for(Point point : points) {
            pointCnt += point.getPointCnt();
        }
        return pointCnt;
    }

    public void deletePoint(Review deletedReview) throws BaseException {
        try {
            pointRepository
                    .save(Point.of(deletedReview.getUser(), deletedReview,
                            PointAction.DELETE, getReviewPoint(deletedReview.getId()) * (-1)));
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
