package com.example.triple;

import com.example.triple.constant.PointAction;
import com.example.triple.domain.*;
import com.example.triple.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class TripleApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PlaceRepository placeRepository;
	private final RecentImgRepository recentImgRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewImgRepository reviewImgRepository;
	private final PointRepository pointRepository;


	public static void main(String[] args) {
		SpringApplication.run(TripleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		User user1 = User.of("정연재");
		User user2 = User.of("유재석");
		User savedUser1 = userRepository.save(user1);
		User savedUser2 = userRepository.save(user2);


		Place place1 = Place.of("괌리프 호텔");
		Place place2 = Place.of("하얏트 리젠시 괌");
		Place place3 = Place.of("뉴욕 리젠시 호텔");
		Place savedPlace1 = placeRepository.save(place1);
		Place savedPlace2 = placeRepository.save(place2);
		Place savedPlace3 = placeRepository.save(place3);

		RecentImg recentImg3 = RecentImg.of(savedUser1, "https://triple.com/images/54SDFSDESDFS78244G.jpg");
		RecentImg recentImg4 = RecentImg.of(savedUser1, "https://triple.com/images/54SDAAADESDFS78244G.jpg");
		RecentImg recentImg5 = RecentImg.of(savedUser1, "https://triple.com/images/094SDFGSDESDFS78244G.jpg");
		RecentImg recentImg6 = RecentImg.of(savedUser2, "https://triple.com/images/94SDFGSDESDFS78244G.jpg");
		RecentImg recentImg7 = RecentImg.of(savedUser2, "https://triple.com/images/74SDFGSDESDFS78244G.jpg");
		RecentImg recentImg8 = RecentImg.of(savedUser2, "https://triple.com/images/488DFGSDESDFS78244G.jpg");

		RecentImg savedRecentImg3 = recentImgRepository.save(recentImg3);
		RecentImg savedRecentImg4 = recentImgRepository.save(recentImg4);
		RecentImg savedRecentImg5 = recentImgRepository.save(recentImg5);
		RecentImg savedRecentImg6 = recentImgRepository.save(recentImg6);
		RecentImg savedRecentImg7 = recentImgRepository.save(recentImg7);
		RecentImg savedRecentImg8 = recentImgRepository.save(recentImg8);

		Review review1 = Review.of("재밌어요", savedUser1, savedPlace1);
		Review review2 = Review.of("좋아요", savedUser1, savedPlace2);
		Review review3 = Review.of("그럭저럭이네요", savedUser2, savedPlace2);

		Review savedReview1 = reviewRepository.save(review1);
		Review savedReview2 = reviewRepository.save(review2);
		Review savedReview3 = reviewRepository.save(review3);

		ReviewImg savedReviewImg1 = reviewImgRepository.save(ReviewImg.of(savedReview1, savedRecentImg3));
		savedReview1.getReviewImgs().add(savedReviewImg1);

		ReviewImg savedReviewImg2 = reviewImgRepository.save(ReviewImg.of(savedReview2, savedRecentImg4));
		savedReview2.getReviewImgs().add(savedReviewImg2);


		Point point1 = Point.of(savedUser1, savedReview1, PointAction.ADD, 3);
		pointRepository.save(point1);

		Point point2 = Point.of(savedUser1, savedReview2, PointAction.ADD, 3);
		pointRepository.save(point2);

		Point point3 = Point.of(savedUser2, savedReview3, PointAction.ADD, 2);
		pointRepository.save(point3);

	}
}
