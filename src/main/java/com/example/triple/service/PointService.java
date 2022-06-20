package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.constant.PointAction;
import com.example.triple.domain.Point;
import com.example.triple.domain.Review;
import com.example.triple.domain.ReviewImg;
import com.example.triple.domain.User;
import com.example.triple.repository.PointRepository;
import com.example.triple.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    private final PointRepository pointRepository;
    private final ReviewRepository reviewRepository;

    public PointService(PointRepository pointRepository, ReviewRepository reviewRepository) {
        this.pointRepository = pointRepository;
        this.reviewRepository = reviewRepository;
    }


    public void createPoint(Review modifyReview, Review savedReview) {
        if(isContentEmptyOrNull(savedReview.getContent())
                && !isContentEmptyOrNull(modifyReview.getContent())) {

            pointRepository.save(new Point(savedReview.getUser(), savedReview, PointAction.MOD));

        } else if(!isContentEmptyOrNull(savedReview.getContent())
                && isContentEmptyOrNull(modifyReview.getContent())) {

            pointRepository.save(new Point(savedReview.getUser(), savedReview, PointAction.DELETE));

        }

        if(isImgEmptyOrNull(savedReview.getReviewImgs())
                && !isImgEmptyOrNull(modifyReview.getReviewImgs())) {

            pointRepository.save(new Point(savedReview.getUser(), savedReview, PointAction.MOD));

        } else if(!isImgEmptyOrNull(savedReview.getReviewImgs())
                && isImgEmptyOrNull(modifyReview.getReviewImgs())) {

            pointRepository.save(new Point(savedReview.getUser(), savedReview, PointAction.DELETE));

        }
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

    public int countPoint(Review review) throws BaseException {
        int pointCnt = 0;
        try {
            // 해당 place의 첫 리뷰면 보너스 점수
            if(reviewRepository.exist(review.getPlace().getId())!=0) {
                pointCnt+=1;
            }
            // content 한글자라도 있음 +1
            if(review.getContent()!=null && !review.getContent().isEmpty()) {
                pointCnt+=1;
            }
            // img가 하나라도 있음 +1
            if(review.getReviewImgs()!=null && !review.getReviewImgs().isEmpty()) {
                pointCnt+=1;
            }

            return pointCnt;
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public void addPoint(User user, Review review, PointAction action) throws BaseException {
        try {
            int point = countPoint(review);
            pointRepository.save(Point.of(user, review, action));
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
