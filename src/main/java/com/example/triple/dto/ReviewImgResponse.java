package com.example.triple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReviewImgResponse {
    private UUID uuid;
    private UUID reviewId;
    private UUID recentImgId;
    private String imgUrl;
}
