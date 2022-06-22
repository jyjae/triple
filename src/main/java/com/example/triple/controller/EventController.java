package com.example.triple.controller;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponse;
import com.example.triple.constant.EventAction;
import com.example.triple.constant.EventType;
import com.example.triple.dto.EventRequest;
import com.example.triple.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {
    private final ReviewService reviewService;

    public EventController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseBody
    public BaseResponse<Object> upsertEvent(@RequestBody @Valid EventRequest eventRequest) {
        EventType eventType = eventRequest.getType();
        Object result = 0;

        try {
            switch (eventType) {
                case REVIEW:
                    result = eventRequest.getAction() == EventAction.DELETE
                            ? reviewService.deleteReview(eventRequest.toDto())
                            : reviewService.upsertReview(eventRequest.toDto());

            }
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
