package com.example.triple.dto;

import com.example.triple.constant.EventAction;
import com.example.triple.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateReviewResponse {
    private UUID reviewId;

}
