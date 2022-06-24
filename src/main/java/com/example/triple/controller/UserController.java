package com.example.triple.controller;

import com.example.triple.config.BaseException;
import com.example.triple.config.BaseResponse;
import com.example.triple.config.BaseResponseStatus;
import com.example.triple.dto.UserResponse;
import com.example.triple.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @ResponseBody
    public BaseResponse<List<UserResponse>> getUserId(@RequestParam("user-name") String userName) {
        if(userName == null) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USER_NAME);
        }
        try {
            return new BaseResponse<>(userService.getUserId(userName));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
