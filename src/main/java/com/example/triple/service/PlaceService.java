package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.domain.Place;
import com.example.triple.dto.PlaceResponse;
import com.example.triple.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public boolean existsByIdAndStatus(UUID placeId, String active) throws BaseException {
        try {
            return placeRepository.existsByIdAndStatus(placeId, active);
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public Place findByIdAndStatus(UUID placeId, String active) throws BaseException {
        try {
            return placeRepository.findByIdAndStatus(placeId, active).orElse(null);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<PlaceResponse> findByStatusAll() throws BaseException {
        try {
            return placeRepository.findByStatus("ACTIVE")
                    .stream().map(p -> new PlaceResponse(p.getId(), p.getName())).collect(Collectors.toList());
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
