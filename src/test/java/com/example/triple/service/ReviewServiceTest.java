package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.constant.EventAction;
import com.example.triple.constant.EventType;
import com.example.triple.domain.*;
import com.example.triple.dto.EventResponse;
import com.example.triple.dto.ReviewDto;
import com.example.triple.dto.UpdateReviewResponse;
import com.example.triple.repository.*;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("비즈니스 로직 - 리뷰")
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserService userService;
    @Mock
    private PlaceService placeService;
    @Mock
    private RecentImgRepository recentImgRepository;
    @Mock
    private PointService pointService;
    @Mock
    private ReviewImgRepository reviewImgRepository;


    @DisplayName("해당 장소에 특정 유저가 작성한 리뷰가 이미 있을 경우 예외를 던진다.")
    @Test
    void givenExistsReview_whenCreate_thenThrowException() throws BaseException {
        // Given
        ReviewDto reviewDto = createReviewDto("좋아요");
        given(reviewRepository.existsByUserIdAndPlaceIdAndStatus(reviewDto.getUserId(), reviewDto.getPlaceId(), "ACTIVE")).willReturn(true);

        // When & Then
        BaseException exception = assertThrows(BaseException.class, ()-> reviewService.createReview(reviewDto));
        assertThat(exception.getStatus().getMessage()).isEqualTo("이미 해당 장소에 리뷰를 작성하셨습니다.");
        then(reviewRepository).should().existsByUserIdAndPlaceIdAndStatus(reviewDto.getUserId(), reviewDto.getPlaceId(), "ACTIVE");

    }


    @DisplayName("리뷰 정보 (content, photoIds 다 포함)를 주면 리뷰를 생성하고 결과로 생성 ID를 보여준다.")
    @Test
    void givenReview_whenCreate_thenCreateReviewAndReturnUUID() throws BaseException {
        // Given
        ReviewDto reviewDto = createReviewDto("좋아요");
        User user = createUser();
        Place place = createPlace();
        Review review = createReview(reviewDto);
        RecentImg recentImg = createRecentImg(user);
        ReviewImg reviewImg = createReviewImg(reviewDto);

        given(reviewRepository.existsByUserIdAndPlaceIdAndStatus(reviewDto.getUserId(), reviewDto.getPlaceId(), "ACTIVE")).willReturn(false);
        given(userService.findByIdAndStatus(reviewDto.getUserId(), "ACTIVE")).willReturn(user);
        given(placeService.findByIdAndStatus(reviewDto.getPlaceId(),"ACTIVE")).willReturn(place);
        given(reviewRepository.save(any(Review.class))).willReturn(review);
        given(recentImgRepository.findByIdAndUserIdAndStatus(reviewDto.getAttachedPhotoIds().get(0), reviewDto.getUserId(), "ACTIVE")).willReturn(Optional.of(recentImg));
        given(reviewImgRepository.save(any(ReviewImg.class))).willReturn(reviewImg);

        // When
        UUID result = reviewService.createReview(reviewDto);

        // Then
        assertThat(result.toString()).isEqualTo(review.getId().toString());
        then(reviewRepository).should().existsByUserIdAndPlaceIdAndStatus(reviewDto.getUserId(), reviewDto.getPlaceId(), "ACTIVE");
        then(userService).should().findByIdAndStatus(reviewDto.getUserId(), "ACTIVE");
        then(placeService).should().findByIdAndStatus(reviewDto.getPlaceId(), "ACTIVE");
        then(recentImgRepository.findByIdAndUserIdAndStatus(reviewDto.getAttachedPhotoIds().get(0), reviewDto.getUserId(), "ACTIVE"));
        then(reviewImgRepository).should().save(any(ReviewImg.class));
    }

    @DisplayName("존재하지 않은 리뷰 정보를 주면 리뷰 수정하지 않고 예외를 던진다")
    @Test
    void givenNotExistsUser_whenModify_thenThrowException() throws BaseException {
        // Given
        ReviewDto reviewDto = modifyReviewDto("좋아요");
        given(reviewService.getReview(reviewDto)).willReturn(null);

        BaseException exception = assertThrows(BaseException.class, ()->reviewService.modifyReview(reviewDto));

        assertThat(exception.getStatus().getMessage()).isEqualTo("존재하지 않은 리뷰입니다.");
        then(reviewService.getReview(reviewDto));
    }

    @DisplayName("리뷰 정보를 주면 정상 수정하도록 한다")
    @Test
    void givenReview_whenModify_thenReturnReview() throws BaseException {
        User user = createUser();
        Place place = createPlace();
        Review savedReview = Review.of("좋아요", user, place);
        ReviewDto modifyReviewDto = modifyReviewDto("싫어요");
        Review modifyReview = modifyReviewDto.updateEntity(savedReview);

        ReflectionTestUtils.setField(savedReview,"id",modifyReviewDto.getReviewId());

        given(reviewService.getReview(modifyReviewDto)).willReturn(savedReview);
        given(pointService.modifyPoint(modifyReviewDto,savedReview)).willReturn(0);
        given(reviewRepository.save(modifyReview)).willReturn(modifyReview);
        given(reviewRepository.findReviewByReviewImgStatus(savedReview.getId(), "ACTIVE")).willReturn(modifyReview);


        UpdateReviewResponse result = reviewService.modifyReview(modifyReviewDto);

        assertThat(result.getContent()).isEqualTo("싫어요");
        assertThat(result.getReviewImgs()).hasSize(0);
    }

    @DisplayName("리뷰 수정 정보에 이미지 id가 없으면 기존 리뷰의 이미지들을 상태를 INACTIVE로 변경한다. ")
    @Test
    void givenReviewImage_whenModify() throws BaseException {
        // Given
        User user = createUser();
        Place place = createPlace();
        ReviewDto reviewDto = modifyReviewDto("싫어요");
        Review savedReview = Review.of("좋아요", user, place);
        ReviewImg reviewImg = createReviewImg(reviewDto);

        savedReview.getReviewImgs().add(reviewImg);
        ReflectionTestUtils.setField(savedReview,"id",reviewDto.getReviewId());
        ReflectionTestUtils.setField(reviewImg,"id",UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"));

        // When
        reviewService.modifyReviewImg(reviewDto.getAttachedPhotoIds(), savedReview);

        // Then
        assertThat(savedReview.getReviewImgs()).hasSize(0);

    }

    @DisplayName("존재하지 않은 유저 정보를 주면 리뷰 삭제하지 않고 예외를 던진다")
    @Test
    void givenNotExistsUser_whenDelete_thenThrowException() throws BaseException {
        ReviewDto reviewDto = deleteReviewDto();
        Review review = createReview(reviewDto);

        given(userService.existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE")).willReturn(false);

        BaseException exception = assertThrows(BaseException.class, () -> reviewService.deleteReview(reviewDto));

        assertThat(exception.getStatus().getMessage()).isEqualTo("존재하지 않은 유저입니다.");
        then(userService).should().existsByIdAndStatus(reviewDto.getUserId(),"ACTIVE");
    }

    @DisplayName("존재하지 않은 리뷰 정보를 주면 리뷰 삭제하지 않고 예외를 던진다")
    @Test
    void givenNotExistsPlace_whenDelete_thenThrowException() throws BaseException {
        // Given
        ReviewDto reviewDto = deleteReviewDto();

        given(userService.existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE")).willReturn(true);
        given(reviewRepository.existsByIdAndUserIdAndStatus(reviewDto.getReviewId(), reviewDto.getUserId(), "ACTIVE")).willReturn(false);

        // When
        BaseException exception = assertThrows(BaseException.class, () -> reviewService.deleteReview(reviewDto));

        // Then
        assertThat(exception.getStatus().getMessage()).isEqualTo("존재하지 않은 리뷰입니다.");
        then(userService).should().existsByIdAndStatus(reviewDto.getUserId(),"ACTIVE");
        then(reviewRepository).should().existsByIdAndUserIdAndStatus(reviewDto.getReviewId(), reviewDto.getUserId(), "ACTIVE");
    }

    @DisplayName("리뷰 id를 주고 리뷰를 삭제한다. (정상 작동)")
    @Test
    void givenReviewId_whenDelete_thenReturnSuccessInt() throws BaseException {
        // Given
        ReviewDto reviewDto = deleteReviewDto();
        Review deleteReview = createReview(reviewDto);

        given(userService.existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE")).willReturn(true);
        given(reviewRepository.existsByIdAndUserIdAndStatus(reviewDto.getReviewId(), reviewDto.getUserId(), "ACTIVE")).willReturn(true);
        given(reviewRepository.modifyReviewStatusInActive(reviewDto.getReviewId())).willReturn(1L);
        given(reviewRepository.findById(reviewDto.getReviewId())).willReturn(Optional.of(deleteReview));
        given(pointService.deletePoint(deleteReview)).willReturn(2);

        EventResponse<Long> result = reviewService.deleteReview(reviewDto);

        assertThat(result.getData()).isEqualTo(1L);
        then(userService).should().existsByIdAndStatus(reviewDto.getUserId(), "ACTIVE");
        then(reviewRepository).should().existsByIdAndUserIdAndStatus(reviewDto.getReviewId(), reviewDto.getUserId(), "ACTIVE");
        then(reviewRepository).should().modifyReviewStatusInActive(reviewDto.getReviewId());
        then(pointService).should().deletePoint(deleteReview);

    }


    private ReviewDto deleteReviewDto() {
        return deleteReviewDto("240a0658-dc5f-4878-9381-ebb7b2667772");
    }

    private ReviewDto deleteReviewDto(String id) {
        return ReviewDto.of(
                EventType.REVIEW,
                EventAction.DELETE,
                UUID.fromString(id),
                null,
                null,
                UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772"),
                null
        );

    }


    private Review getOriginalReview(User user, Place place, ReviewDto review) {
        Review originalReview = Review.of("좋아요", user, place);
        ReflectionTestUtils.setField(originalReview, "id", review.getReviewId());
        return originalReview;
    }


    private ReviewImg createReviewImg(ReviewDto reviewDto) {
        return createReviewImg(createReview(reviewDto), createRecentImg());
    }

    private ReviewImg createReviewImg(Review review, RecentImg recentImg) {
        ReviewImg reviewImg =  ReviewImg.of(review, recentImg);
        ReflectionTestUtils.setField(recentImg, "id", UUID.fromString("a047b334-5427-4c1c-b1f9-7ee4dc822ee7"));
        return reviewImg;
    }

    private ReviewDto createReviewIdDto(String content) {
        return createReviewIdDto("240a0658-dc5f-4878-9381-ebb7b2667772", content);
    }

    private ReviewDto createReviewIdDto(String id, String content) {
        List<UUID> photoIds = new ArrayList<>();
        photoIds.add(UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"));

        return ReviewDto.of(
                EventType.REVIEW,
                EventAction.ADD,
                UUID.fromString(id),
                content,
                photoIds,
                UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"),
                UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
        );
    }


    private ReviewDto modifyReviewDto(String content) {
        List<UUID> photoIds = new ArrayList<>();
        photoIds.add(UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"));

        return ReviewDto.of(
                EventType.REVIEW,
                EventAction.ADD,
                UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"),
                content,
                null,
                UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"),
                UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
        );
    }

    private ReviewDto createReviewDto(String content) {
        List<UUID> photoIds = new ArrayList<>();
        photoIds.add(UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"));

        return ReviewDto.of(
                EventType.REVIEW,
                EventAction.ADD,
                null,
                content,
                photoIds,
                UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"),
                UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
        );
    }


    private Place createPlace() {
        return createPlace("2e4baf1c-5acb-4efb-a1af-eddada31b00f", "괌 파라다이스 호텔");
    }

    private Place createPlace(String id, String name) {
        Place place = Place.of(name);
        ReflectionTestUtils.setField(place, "id", UUID.fromString(id));
        return place;
    }

    private Review createReview(ReviewDto reviewDto) {
        return createReview("240a0658-dc5f-4878-9381-ebb7b2667772", reviewDto.getContent());
    }

    private Review createReview(String content) {
        return createReview("240a0658-dc5f-4878-9381-ebb7b2667772", content);
    }

    private Review createReview(String id, String content) {
        Review review =  Review.of(content, createUser(), createPlace());
        ReflectionTestUtils.setField(review, "id", UUID.fromString(id));
        return review;
    }


    private User createUser() {
        return createUser("3ede0ef2-92b7-4817-a5f3-0c575361f745", "정연재");
    }

    private User createUser(String id, String name) {
        User user = User.of(name);
        ReflectionTestUtils.setField(user, "id", UUID.fromString(id));
        return user;
    }

    private RecentImg createRecentImg() {
        return createRecentImg(createUser());
    }

    private RecentImg createRecentImg(User user) {
        RecentImg recentImg1 = RecentImg.of(
                user,
                "https://triple.com/images/ASDDFDFJF99ALSKDHJJFLD.png"
        );

        ReflectionTestUtils.setField(recentImg1, "id", UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"));

        return recentImg1;
    }


}