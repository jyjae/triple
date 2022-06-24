package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.constant.EventAction;
import com.example.triple.constant.PointAction;
import com.example.triple.domain.*;
import com.example.triple.dto.*;
import com.example.triple.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ReviewService {
    Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    private final UserService userService;
    private final ReviewImgRepository reviewImgRepository;
    private final PlaceService placeService;

    private final PointService pointService;
    private final RecentImgRepository recentImgRepository;


    public ReviewService(
            ReviewRepository reviewRepository,
            UserService userService,
            PlaceService placeService,
            PointService pointService,
            ReviewImgRepository reviewImgRepository,
            RecentImgRepository recentImgRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.reviewImgRepository = reviewImgRepository;
        this.placeService = placeService;
        this.pointService = pointService;
        this.recentImgRepository = recentImgRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public EventResponse<Long> deleteReview(ReviewDto reviewDto) throws BaseException {
        if(!userService.existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE")
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
            int pointCnt = pointService.deletePoint(deletedReview);
            logger.info("Id:{}, {}님의 {}의 리뷰가 삭제되었습니다 {}",deletedReview.getUser().getId(), deletedReview.getUser().getName(),deletedReview.getPlace().getName(), pointCnt);

            return new EventResponse<>(result);
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Object upsertReview(ReviewDto reviewDto) throws BaseException {

        if(!userService.existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        if(!placeService.existsByIdAndStatus(reviewDto.getPlaceId(), "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.PLACE_NOT_EXISTS);
        }

        if(reviewDto.getAction() == EventAction.ADD && reviewDto.getReviewId() == null) {
             return new CreateReviewResponse(createReview(reviewDto));
        } else {
             return modifyReview(reviewDto);
        }

    }

    public Review getReview(ReviewDto reviewDto) throws BaseException {
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
    public UpdateReviewResponse modifyReview(ReviewDto reviewDto) throws BaseException {

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

    public void modifyReviewImg(List<UUID> attachedPhotoIds, Review review) throws BaseException {
        List<ReviewImg> deleteReviewImgs = new ArrayList<>();
        //INACTIVE reviewImgs
        if(review.getReviewImgs()!=null) {
            for (ReviewImg reviewImg : review.getReviewImgs()) {
                if (attachedPhotoIds == null || !attachedPhotoIds.contains(reviewImg.getRecentImg().getId())) {
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
                        if (reviewImg.getRecentImg().getId().toString().equals(imgId.toString())) {
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


    public void addPoint(Review savedReview, int pointCnt) throws BaseException {
        if(pointCnt >0) {
            pointService.addPoint(
                    savedReview.getUser(),
                    savedReview,
                    PointAction.ADD,
                    pointCnt);

            logger.info("Id:{}, {}님의 {}의 리뷰가 수정되었습니다 +{}",savedReview.getUser().getId(), savedReview.getUser().getName(), savedReview.getPlace().getName(), pointCnt);
        }else if(pointCnt <0) {
            pointService.addPoint(
                    savedReview.getUser(),
                    savedReview,
                    PointAction.DELETE,
                    pointCnt);

            logger.info("Id:{}, {}님의 {}의 리뷰가 수정되었습니다 {}",savedReview.getUser().getId(), savedReview.getUser().getName(), savedReview.getPlace().getName(), pointCnt);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public UUID createReview(ReviewDto reviewDto) throws BaseException {
        if(reviewRepository.existsByUserIdAndPlaceIdAndStatus(reviewDto.getUserId(), reviewDto.getPlaceId(), "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.EXISTS_REVIEW_BY_USER_AND_PLACE);
        }

        try {

           User user = userService.findByIdAndStatus(
                   reviewDto.getUserId(),
                   "ACTIVE");

           Place place = placeService.findByIdAndStatus(
                   reviewDto.getPlaceId(),
                   "ACTIVE");

           int point = pointService.countPoint(
                   place.getId(),
                   reviewDto.getContent(),
                   reviewDto.getAttachedPhotoIds()
           );

           Review review = reviewRepository.save(reviewDto.toEntity(user, place));
           createReviewImgs(reviewDto, review);

           pointService.addPoint(user, review, PointAction.ADD, point);
           if(point!=0) logger.info("Id:{}, {}님의 {}의 리뷰를 작성했습니다 +{}",user.getId(), user.getName(), place.getName(), point);

           return review.getId();

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


    public List<ReviewResponse> getReviewIds(UUID userId) throws BaseException {
        if(!userService.existsByIdAndStatus(userId, "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }

        try {
            return reviewRepository.findByUserIdAndStatus(userId,"ACTIVE").get()
                    .stream().map(r ->
                            new ReviewResponse(r.getId(), r.getContent(), r.getUser().getId(), r.getPlace().getId(),
                                    r.getReviewImgs()
                                            .stream().map(i -> new ReviewImgResponse(i.getId(), i.getReview().getId(), i.getRecentImg().getId(), i.getRecentImg().getImgUrl()))
                                            .collect(Collectors.toList())))
                    .collect(Collectors.toList());

        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
