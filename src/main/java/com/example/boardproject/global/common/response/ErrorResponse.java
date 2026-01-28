package com.example.boardproject.global.common.response;

import com.example.boardproject.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "에러 응답")
public class ErrorResponse {

    @Schema(description = "성공 여부", example = "false")
    private final boolean success = false;
    @Schema(description = "에러 코드", example = "BAD_REQUEST")
    private final String errorCode;
    @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
    private final String message;
    @Schema(description = "Validation 에러 상세 정보", example = "\"loginId\": \"아이디는 영문과 숫자만 가능합니다\"")
    private final Object errors;

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