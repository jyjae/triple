package com.example.triple.service;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponse;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.domain.User;
import com.example.triple.dto.UserResponse;
import com.example.triple.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsByIdAndStatus(UUID userId, String active) throws BaseException {
        try{
            return userRepository.existsByIdAndStatus(userId, active);
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public User findByIdAndStatus(UUID userId, String active) throws BaseException {
        try {
            return userRepository.findByIdAndStatus(userId, active).orElse(null);
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<UserResponse> getUserId(String userName) throws BaseException{
        if(!userRepository.existsByNameAndStatus(userName, "ACTIVE")) {
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        try {
            List<UserResponse> userIds = userRepository.findByNameAndStatus(userName,"ACTIVE").get()
                    .stream().map( u -> new UserResponse(u.getId(), u.getName()))
                    .collect(Collectors.toList());

            return userIds;
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
