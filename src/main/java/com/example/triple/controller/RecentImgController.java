package com.example.triple.controller;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponse;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.dto.RecentImgResponse;
import com.example.triple.service.RecentImgService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recentImgs")
public class RecentImgController {
    private final RecentImgService recentImgService;

    public RecentImgController(RecentImgService recentImgService) {
        this.recentImgService = recentImgService;
    }

    @GetMapping()
    @ResponseBody
    public BaseResponse<List<RecentImgResponse>> getRecentImgIds(@RequestParam("user-id") UUID userId) {
        if(userId==null) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USER_ID);
        }
        try {
            return new BaseResponse<>(recentImgService.getRecentImgIds(userId));
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
