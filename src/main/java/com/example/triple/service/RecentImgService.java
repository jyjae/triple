package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.dto.RecentImgResponse;
import com.example.triple.repository.RecentImgRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecentImgService{
    private final RecentImgRepository recentImgRepository;
    private final UserService userService;

    public RecentImgService(RecentImgRepository recentImgRepository, UserService userService) {
        this.recentImgRepository = recentImgRepository;
        this.userService = userService;
    }

    void checkImgId(List<UUID> attachedPhotoIds) throws BaseException {
        for (UUID imgId : attachedPhotoIds) {
            recentImgRepository.findById(imgId).orElseThrow(
                    ()-> new BaseException(BaseResponseStatus.IMG_NOT_EXISTS)
            );
        }
    }

    public List<RecentImgResponse> getRecentImgIds(UUID userId) throws BaseException {
        if(!userService.existsByIdAndStatus(userId, "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        try {
            return recentImgRepository.findByUserIdAndStatus(userId, "ACTIVE")
                    .stream().map(r -> new RecentImgResponse(r.getId(), r.getUser().getId()))
                    .collect(Collectors.toList());

        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
