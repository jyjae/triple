package com.example.triple.controller;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponse;
import com.example.triple.dto.PlaceResponse;
import com.example.triple.repository.PlaceRepository;
import com.example.triple.service.PlaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/places")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    @ResponseBody
    public BaseResponse<List<PlaceResponse>> getPlaces() {
        try{
            return new BaseResponse<>(placeService.findByStatusAll());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
