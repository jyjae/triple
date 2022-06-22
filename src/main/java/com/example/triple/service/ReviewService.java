package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.constant.EventAction;
import com.example.triple.constant.PointAction;
import com.example.triple.domain.*;
import com.example.triple.dto.CreateReviewResponse;
import com.example.triple.dto.ReviewDto;
import com.example.triple.dto.UpdateReviewResponse;
import com.example.triple.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
public class ReviewService {
    Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final PlaceRepository placeRepository;

    private final PointService pointService;
    private final RecentImgRepository recentImgRepository;


    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            PlaceRepository placeRepository,
            PointService pointService,
            ReviewImgRepository reviewImgRepository,
            RecentImgRepository recentImgRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.reviewImgRepository = reviewImgRepository;
        this.placeRepository = placeRepository;
        this.pointService = pointService;
        this.recentImgRepository = recentImgRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long deleteReview(ReviewDto reviewDto) throws BaseException {
        if(!userRepository.existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE")
        ) {
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        if(!reviewRepository.existsByIdAndUserIdAndStatus
                        (reviewDto.getReviewId(), reviewDto.getUserId(), "ACTIVE")
        ) {
            throw new BaseException(BaseResponseStatus.REVIEW_NOT_EXISTS);
        }

        try {
            // 1. 리뷰 INACTIVE
            Long result = reviewRepository.modifyReviewStatusInActive(reviewDto.getReviewId());
            Review deletedReview = reviewRepository.findById(reviewDto.getReviewId()).get();
            // 2. 리뷰 이미지가 있다면 INACTIVE
            reviewImgRepository.modifyReviewImgStatusInActive(reviewDto.getReviewId());
            // 3. 해당 리뷰의 포인트 차감
            pointService.deletePoint(deletedReview);

            return result;
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Object upsertReview(ReviewDto reviewDto) throws BaseException {

        if(!userRepository.existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        if(!placeRepository.existsByIdAndStatus(reviewDto.getPlaceId(), "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.PLACE_NOT_EXISTS);
        }

        if(reviewDto.getAction() == EventAction.ADD && reviewDto.getReviewId() == null) {
             return createReview(reviewDto);
        } else {
             return modifyReview(reviewDto);
        }

    }

    private Review getReview(ReviewDto reviewDto) throws BaseException {
        try{
            return reviewRepository.findByIdAndUserIdAndPlaceIdAndStatus(
                    reviewDto.getReviewId(),
                    reviewDto.getUserId(),
                    reviewDto.getPlaceId(),
                    "ACTIVE");
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private UpdateReviewResponse modifyReview(ReviewDto reviewDto) throws BaseException {

        Review savedReview = getReview(reviewDto);

        if(savedReview==null) {
            throw new BaseException(BaseResponseStatus.REVIEW_NOT_EXISTS);
        }

        try {
            addPoint(savedReview, pointService.modifyPoint(reviewDto, savedReview));

            Review modifiedReview = reviewRepository.findReviewByReviewImgStatus(
                    reviewRepository.save(reviewDto.updateEntity(savedReview)).getId(),
                    "ACTIVE");

            modifyReviewImg(reviewDto.getAttachedPhotoIds(), modifiedReview);

            return UpdateReviewResponse.of(
                    modifiedReview.getContent(),
                    modifiedReview.getReviewImgs());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private void modifyReviewImg(List<UUID> attachedPhotoIds, Review review) throws BaseException {
        List<ReviewImg> deleteReviewImgs = new ArrayList<>();
        //INACTIVE reviewImgs
        if(review.getReviewImgs()!=null) {
            for (ReviewImg reviewImg : review.getReviewImgs()) {
                if (attachedPhotoIds == null || !attachedPhotoIds.contains(reviewImg.getId())) {
                    reviewImg.setStatus("INACTIVE");
                    deleteReviewImgs.add(reviewImg);
                }
            }
        }

        for(ReviewImg deleteReviewImg : deleteReviewImgs) {
            review.getReviewImgs().remove(deleteReviewImg);
        }

        //ADD reviewImgs
        if(attachedPhotoIds != null) {
            for(UUID imgId : attachedPhotoIds) {
                boolean check = false;
                if(review.getReviewImgs()!=null) {
                    for (ReviewImg reviewImg : review.getReviewImgs()) {
                        if (reviewImg.getRecentImg().toString().equals(imgId.toString())) {
                            check = true;
                        }
                    }
                }

                if (!check) {
                    RecentImg recentImg = recentImgRepository.findByIdAndUserIdAndStatus(
                                    imgId,
                                    review.getUser().getId(),
                                    "ACTIVE")
                            .orElseThrow(() -> new BaseException(BaseResponseStatus.IMG_NOT_EXISTS));
                    review.getReviewImgs().add(reviewImgRepository.save(ReviewImg.of(review, recentImg)));
                }
            }
        }
    }


    private void addPoint(Review savedReview, int pointCnt) throws BaseException {
        if(pointCnt >0) {
            pointService.addPoint(
                    savedReview.getUser(),
                    savedReview,
                    PointAction.ADD,
                    pointCnt);

            logger.info("{}의 리뷰가 수정되었습니다 +{}",savedReview.getPlace().getName(), pointCnt);
        }else if(pointCnt <0) {
            pointService.addPoint(
                    savedReview.getUser(),
                    savedReview,
                    PointAction.DELETE,
                    pointCnt);

            logger.info("{}의 리뷰가 수정되었습니다 -{}",savedReview.getPlace().getName(), pointCnt);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private CreateReviewResponse createReview(ReviewDto reviewDto) throws BaseException {
        try {

           User user = userRepository.findByIdAndStatus(
                   reviewDto.getUserId(),
                   "ACTIVE")
                   .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXISTS));

           Place place = placeRepository.findByIdAndStatus(
                   reviewDto.getPlaceId(),
                   "ACTIVE")
                   .orElseThrow(() -> new BaseException(BaseResponseStatus.PLACE_NOT_EXISTS));

           int point = pointService.countPoint(
                   place.getId(),
                   reviewDto.getContent(),
                   reviewDto.getAttachedPhotoIds()
           );

           Review review = reviewRepository.save(reviewDto.toEntity(user, place));
           createReviewImgs(reviewDto, review);

           pointService.addPoint(user, review, PointAction.ADD, point);
           if(point!=0) logger.info("{}의 리뷰를 작성했습니다 +{}",place.getName(), point);

           return new CreateReviewResponse(review.getId());

        }catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    private void createReviewImgs(ReviewDto reviewDto, Review review) throws BaseException {
        if(reviewDto.getAttachedPhotoIds()==null) return;

        for(UUID imgId : reviewDto.getAttachedPhotoIds()) {
            RecentImg recentImg = recentImgRepository.findByIdAndUserIdAndStatus(
                    imgId,
                    review.getUser().getId(),
                    "ACTIVE")
                    .orElseThrow(()-> new BaseException(BaseResponseStatus.IMG_NOT_EXISTS));

            review.getReviewImgs().add(reviewImgRepository.save(ReviewImg.of(review, recentImg)));
        }
    }




}
