package com.example.boardproject.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private final boolean success = false;
    private final String errorCode;
    private final String message;
    private final Object errors;  // validation 에러 상세 정보

    /**
     * 기본 에러 응답
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                null
        );
    }

    /**
     * 커스텀 메시지 에러 응답
     */
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
                errorCode.getCode(),
                message,
                null
        );
    }

    /**
     * Validation 에러 응답(상세 정보 포함)
     */
    public static ErrorResponse of(ErrorCode errorCode, Object errors) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                errors
        );
    }
}