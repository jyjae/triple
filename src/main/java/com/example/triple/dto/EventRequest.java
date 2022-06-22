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
@AllArgsConstructor
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
}
