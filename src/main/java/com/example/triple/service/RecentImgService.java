package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.domain.RecentImg;
import com.example.triple.repository.RecentImgRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecentImgService{
    private final RecentImgRepository recentImgRepository;

    public RecentImgService(RecentImgRepository recentImgRepository) {
        this.recentImgRepository = recentImgRepository;
    }

    void checkImgId(List<UUID> attachedPhotoIds) throws BaseException {
        for (UUID imgId : attachedPhotoIds) {
            recentImgRepository.findById(imgId).orElseThrow(
                    ()-> new BaseException(BaseResponseStatus.IMG_NOT_EXISTS)
            );
        }
    }
}
