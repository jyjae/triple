package com.example.triple.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000: Request 오류
     */
    POST_REVIEW_NULL(false, 2000, "리뷰 데이터를 확인해주세요"),
    REVIEW_NOT_EXISTS(false, 2010, "존재하지 않은 리뷰입니다."),
    IMG_NOT_EXISTS(false, 2030,"존재하지 않은 이미지입니다."),


    USER_NOT_EXISTS(false, 2050, "존재하지 않은 유저입니다."),
    PLACE_NOT_EXISTS(false, 2060, "존재하지 않은 장소입니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다.");




    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
