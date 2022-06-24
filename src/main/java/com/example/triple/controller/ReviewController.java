package com.example.triple.controller;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponse;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.dto.ReviewResponse;
import com.example.triple.service.ReviewService;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    @ResponseBody
    public BaseResponse<List<ReviewResponse>> getReviewIds(@RequestParam("user-id") UUID userId) {
        if(userId == null) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USER_ID);
        }
        try {
            return new BaseResponse<>(reviewService.getReviewIds(userId));
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
