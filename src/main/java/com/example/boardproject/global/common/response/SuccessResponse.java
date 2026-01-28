package com.example.boardproject.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "성공 응답")
public class SuccessResponse<T> {

    @Schema(description = "성공 여부", example = "true")
    private final boolean success = true;
    @Schema(description = "응답 데이터")
    private final T data;
    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    /**
     * 성공 응답 (데이터만)
     */
    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data, null);
    }

    /**
     * 성공 응답 (데이터 + 메시지)
     */
    public static <T> SuccessResponse<T> of(T data, String message) {
        return new SuccessResponse<>(data, message);
    }

    /**
     * 성공 응답 (메시지만)
     */
    public static <T> SuccessResponse<T> of(String message) {
        return new SuccessResponse<>(null, message);
    }

    /**
     * 성공 응답 (응답 코드만)
     */
    public static <T> SuccessResponse<T> of() {
        return new SuccessResponse<>(null, null);
    }
}