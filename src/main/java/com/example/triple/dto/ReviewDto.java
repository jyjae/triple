package com.example.triple.dto;

import com.example.triple.constant.EventAction;
import com.example.triple.constant.EventType;
import com.example.triple.domain.Place;
import com.example.triple.domain.Review;
import com.example.triple.domain.ReviewImg;
import com.example.triple.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReviewDto {
    private EventType type;
    private EventAction action;
    private UUID reviewId;
    private String content;
    private List<UUID> attachedPhotoIds;
    private UUID userId;
    private UUID placeId;

    public static ReviewDto of(
            EventType type,
            EventAction action,
            UUID reviewId,
            String content,
            List<UUID> attachedPhotoIds,
            UUID userId,
            UUID placeId
    ) {        return new ReviewDto(
            type,
            action,
            reviewId,
            content,
            attachedPhotoIds,
            userId,
            placeId
    );
    }


    public Review toEntity(User user, Place place) {
        return Review.of(
              content,
              user,
              place
        );
    }

    public Review updateEntity(Review savedReview) {
        savedReview.setContent(content);
        return savedReview;
    }
}
