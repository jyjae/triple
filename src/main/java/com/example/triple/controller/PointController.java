package com.example.triple.controller;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponse;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.dto.GetPointResponse;
import com.example.triple.service.PointService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/points")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public BaseResponse<GetPointResponse> getAmountPoint(@PathVariable("userId") UUID userId) {
        if(userId==null) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USER_ID);
        }

        try {
            int userPointCnt = pointService.getReviewPoint(null, userId);
            return new BaseResponse<>(new GetPointResponse(userPointCnt));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
