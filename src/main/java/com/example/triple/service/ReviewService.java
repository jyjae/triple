package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.constant.EventAction;
import com.example.triple.constant.PointAction;
import com.example.triple.domain.*;
import com.example.triple.dto.ReviewDto;
import com.example.triple.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
public class ReviewService {
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

    public String deleteReview(ReviewDto toDto) {
        return "";
    }

    public Object upsertReview(ReviewDto reviewDto) throws BaseException {

        if(!userRepository.existsById(reviewDto.getUserId())) {
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        if(!placeRepository.existsById(reviewDto.getPlaceId())) {
            throw new BaseException(BaseResponseStatus.PLACE_NOT_EXISTS);
        }


        try {
            if(reviewDto.getAction() == EventAction.ADD && reviewDto.getReviewId() == null) {
                return createReview(reviewDto);
            } else {
                return modifyReview(reviewDto);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private Object modifyReview(ReviewDto reviewDto) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    private UUID createReview(ReviewDto reviewDto) throws BaseException {
        try {
           User user = userRepository.findById(reviewDto.getUserId()).orElseThrow(
                   () -> new BaseException(BaseResponseStatus.USER_NOT_EXISTS));
           Place place = placeRepository.findById(reviewDto.getPlaceId()).orElseThrow(
                    () -> new BaseException(BaseResponseStatus.PLACE_NOT_EXISTS));

           Review review = reviewRepository.save(reviewDto.toEntity(user, place));
           review.setReviewImgs(createReviewImg(reviewDto.getAttachedPhotoIds(), review));

           pointService.addPoint(user, review, PointAction.ADD);

           return review.getId();

        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    private List<ReviewImg> createReviewImg(List<UUID> recentImgIds, Review review) throws BaseException {
        try {
            List<ReviewImg> reviewImgs = new ArrayList<>();

            for(UUID imgId : recentImgIds) {
                RecentImg recentImg = recentImgRepository.findById(imgId).orElseThrow(
                        ()-> new BaseException(BaseResponseStatus.IMG_NOT_EXISTS));

                reviewImgs.add(reviewImgRepository.save(ReviewImg.of(review, recentImg)));
            }
            return reviewImgs;

        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

//    @Transactional(rollbackFor = Exception.class)
//    private Review modifyReview(ReviewDto reviewDto) throws BaseException {
//        try {
//
//            if(reviewDto.getUserId() == null || reviewDto == null) {
//                throw new BaseException(BaseResponseStatus.POST_REVIEW_NULL);
//            }
//
//             Optional<Review> savedReview = reviewRepository.findById(reviewDto.getUserId());
//
//            if(savedReview.isPresent()){
//               // pointService.createPoint(reviewDto.updateEntity(savedReview.get()), savedReview.get());
//                modifyReviewImg(reviewDto);
//
//                //return reviewRepository.save(reviewDto.updateEntity(savedReview.get()));
//            }else {
//                throw new BaseException(BaseResponseStatus.REVIEW_NOT_EXISTS);
//            }
//
//        } catch (Exception e) {
//            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
//        }
//    }
//
//    private void modifyReviewImg(ReviewDto reviewDto) {
//        List<ReviewImg> reviewImgs =  reviewImgRepository.findByReviewId(reviewDto.getReviewId());
//        for(ReviewImg savedImg : reviewImgs) {
//            if(!reviewDto.getAttachedPhotoIds().contains(savedImg.getId())) {
//                reviewImgRepository.delete(savedImg);
//            }
//        }
//    }




}
