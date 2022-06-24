package com.example.triple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReviewResponse {
    private UUID id;
    private String content;
    private UUID userId;
    private UUID placeId;
    private List<ReviewImgResponse> reviewImgs;
}
