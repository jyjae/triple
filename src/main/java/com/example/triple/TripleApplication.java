package com.example.triple;

import com.example.triple.constant.PointAction;
import com.example.triple.domain.*;
import com.example.triple.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		User user1 = new User("정연재");
		User user2 = new User("유재석");

		User savedUser = userRepository.save(user1);
		userRepository.save(user2);

		Place place1 = new Place("괌리프 호텔");
		Place place2 = new Place("하얏트 리젠시 괌");

		Place savedPlace = placeRepository.save(place1);
		placeRepository.save(place2);

		RecentImg recentImg3 = new RecentImg(user1, "https://triple.com/images/54SDFSDESDFS78244G.jpg");
		RecentImg recentImg4 = new RecentImg(user1, "https://triple.com/images/54SDAAADESDFS78244G.jpg");
		RecentImg recentImg5 = new RecentImg(user1, "https://triple.com/images/094SDFGSDESDFS78244G.jpg");
		RecentImg recentImg6 = new RecentImg(user2, "https://triple.com/images/94SDFGSDESDFS78244G.jpg");
		RecentImg recentImg7 = new RecentImg(user2, "https://triple.com/images/74SDFGSDESDFS78244G.jpg");
		RecentImg recentImg8 = new RecentImg(user2, "https://triple.com/images/488DFGSDESDFS78244G.jpg");

		RecentImg savedRecentImg1 = recentImgRepository.save(new RecentImg(user1, "https://triple.com/images/54SDFGSDESDFSGSDDFG.jpg"));
		RecentImg savedRecentImg2 = recentImgRepository.save(new RecentImg(user1, "https://triple.com/images/78JGSDESDFDF8244G.jpg"));
		RecentImg savedRecentImg3 = recentImgRepository.save(new RecentImg(user2, "https://triple.com/images/84SDFGSDESDFDF8244G.jpg"));
		RecentImg savedRecentImg4 = recentImgRepository.save(new RecentImg(user2, "https://triple.com/images/94SDFGSDESDFDF8244G.jpg"));
		RecentImg savedRecentImg5 = recentImgRepository.save(new RecentImg(user2, "https://triple.com/images/E4SDFGSDESDFDF8244G.jpg"));

		recentImgRepository.save(recentImg3);
		recentImgRepository.save(recentImg4);
		recentImgRepository.save(recentImg5);
		recentImgRepository.save(recentImg6);
		recentImgRepository.save(recentImg7);
		recentImgRepository.save(recentImg8);

		Review review = reviewRepository.save(new Review("재밌어요", user1, place1));
		Review review2 = reviewRepository.save(new Review("좋아요", user2, place1));
		Review review3 = reviewRepository.save(new Review("그럭저럭이네요", user2, place2));

		ReviewImg reviewImg = reviewImgRepository.save(new ReviewImg(review, savedRecentImg1));
		review.getReviewImgs().add(reviewImg);

		ReviewImg reviewImg2 = reviewImgRepository.save(new ReviewImg(review2, savedRecentImg3));
		review2.getReviewImgs().add(reviewImg2);


		Point point1 = new Point(user1, review, PointAction.ADD, 3);
		pointRepository.save(point1);

		Point point2 = new Point(user1, review2, PointAction.ADD, 3);
		pointRepository.save(point2);

		Point point3 = new Point(user2, review3, PointAction.ADD, 2);
		pointRepository.save(point3);

		System.out.println(savedUser.getId());
		System.out.println(savedPlace.getId());
		System.out.println(review.getId());
		System.out.println(savedRecentImg1.getId());
		System.out.println(savedRecentImg2.getId());

		//System.out.println(review.getId());

	}
}
