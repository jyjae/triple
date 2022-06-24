package com.example.triple.dto;

import com.example.triple.constant.EventAction;
import com.example.triple.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EventRequest {
    @NotNull
    private EventType type;
    @NotNull
    private EventAction action;
    private UUID reviewId;
    private String content;
    private List<UUID> attachedPhotoIds;
    private UUID userId;
    private UUID placeId;

    private EventRequest(
            EventType type,
            EventAction action,
            UUID reviewId,
            String content,
            List<UUID> attachedPhotoIds,
            UUID userId,
            UUID placeId) {
        this.type = type;
        this.action = action;
        this.reviewId = reviewId;
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;
        this.userId = userId;
        this.placeId = placeId;
    }

    public ReviewDto toDto() {
        return ReviewDto.of(
                this.type,
                this.action,
                this.reviewId,
                this.content,
                this.attachedPhotoIds,
                this.userId,
                this.placeId
        );
    }

    public static EventRequest of(
            EventType type,
            EventAction action,
            UUID reviewId,
            String content,
            List<UUID> attachedPhotoIds,
            UUID userId,
            UUID placeId
    ) {
        return new EventRequest(
                type,
                action,
                reviewId,
                content,
                attachedPhotoIds,
                userId,
                placeId
        );
    }
}
