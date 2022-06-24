package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.constant.EventAction;
import com.example.triple.constant.EventType;
import com.example.triple.constant.PointAction;
import com.example.triple.domain.Place;
import com.example.triple.domain.Point;
import com.example.triple.domain.Review;
import com.example.triple.domain.User;
import com.example.triple.dto.ReviewDto;
import com.example.triple.repository.PointRepository;
import com.example.triple.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.spel.support.ReflectiveConstructorExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("비즈니스 로직 - 포인트")
@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService pointService;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @DisplayName("특정 장소의 첫 리뷰 생성 시(내용, 이미지 포함) 포인트 계산")
    @Test
    void givenPlaceIdAndContentAndImage_whenCreateReview_thenReturnCountPoint() throws BaseException {
        // Given
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        given(reviewRepository.existsByPlaceIdAndStatus(placeId,"ACTIVE")).willReturn(false);

        // When
        int pointCnt = pointService.countPoint(placeId, "좋아요", List.of(UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8")));

        // Then
        assertThat(pointCnt).isEqualTo(3);
        then(reviewRepository).should().existsByPlaceIdAndStatus(placeId,"ACTIVE");

    }

    @DisplayName("내용만 있는 기존 리뷰에 이미지 추가 포인트 계산")
    @Test
    void givenReview_whenModify_thenReturnCountPoint() throws BaseException {
        // Given
        User user = User.of("정연재");
        Place place = Place.of("괌 파라다이스 호텔");

        ReflectionTestUtils.setField(user, "id",  UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"));
        ReflectionTestUtils.setField(place,"id",UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f"));

        ReviewDto reviewDto = ReviewDto.of(
                EventType.REVIEW,
                EventAction.MOD,
                UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772"),
                "굳",
                List.of(UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8")),
                UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"),
                UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f"));

        Review savedReview = Review.of("좋아요", user, place);
        ReflectionTestUtils.setField(savedReview,"id",UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772"));

        // When
       int pointCnt = pointService.modifyPoint(reviewDto, savedReview);

       // Then
       assertThat(pointCnt).isEqualTo(1);
    }

    @DisplayName("리뷰 삭제시 삭제할 포인트 계산")
    @Test
    void givenReviewIdAndUserId_whenDeleteReview_thenReturnCountPoint() throws BaseException {
        //Given
        User user = User.of("정연재");
        Place place = Place.of("괌 파라다이스 호텔");

        ReflectionTestUtils.setField(user, "id",  UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"));
        ReflectionTestUtils.setField(place,"id",UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f"));

        Review savedReview = Review.of("좋아요", user, place);
        ReflectionTestUtils.setField(savedReview,"id",UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772"));


        Point point1 = Point.of(user, savedReview, PointAction.ADD, 3);
        Point point2 = Point.of(user, savedReview, PointAction.MOD, -1);
        Point point3 = Point.of(user, savedReview, PointAction.MOD, -1);


        // When
        int pointCnt = pointService.calculatePoint(List.of(point1,point2,point3));

        // Then
        assertThat(pointCnt).isEqualTo(1);
    }

    @DisplayName("특정 유저의 포인트 계산")
    @Test
    void givenUserId_whenGetPoint_thenReturnCountPoint() {
        //Given
        User user = User.of("정연재");
        Place place1 = Place.of("괌 파라다이스 호텔");
        Place place2 = Place.of("뉴욕 파라다이스 호텔");

        ReflectionTestUtils.setField(user, "id",  UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"));
        ReflectionTestUtils.setField(place1,"id",UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f"));
        ReflectionTestUtils.setField(place2,"id",UUID.fromString("c5f0b54c-9617-428f-b18b-b82d621acf5f"));

        Review savedReview1 = Review.of("좋아요", user, place1);
        ReflectionTestUtils.setField(savedReview1,"id",UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772"));

        Review savedReview2 = Review.of("좋아요", user, place1);
        ReflectionTestUtils.setField(savedReview2,"id",UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772"));


        Point point1 = Point.of(user, savedReview1, PointAction.ADD, 3);
        Point point2 = Point.of(user, savedReview1, PointAction.MOD, -1);
        Point point3 = Point.of(user, savedReview1, PointAction.MOD, -1);
        Point point4 = Point.of(user, savedReview2, PointAction.ADD, 1);

        // When
        int pointCnt = pointService.calculatePoint(List.of(point1,point2,point3,point4));

        // Then
        assertThat(pointCnt).isEqualTo(2);


    }


}