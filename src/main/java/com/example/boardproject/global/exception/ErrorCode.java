package com.example.boardproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common Errors(공통)
    INVALID_INPUT(400, "INVALID_INPUT", "입력값이 유효하지 않습니다"),
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증이 필요합니다"),
    FORBIDDEN(403, "FORBIDDEN", "권한이 없습니다"),
    NOT_FOUND(404, "NOT_FOUND", "리소스를 찾을 수 없습니다"),
    DUPLICATE(409, "DUPLICATE", "중복된 데이터입니다"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다"),

    // Auth
    INVALID_TOKEN(401, "UNAUTHORIZED", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(401, "UNAUTHORIZED", "만료된 토큰입니다"),
    INVALID_REFRESH_TOKEN(401, "UNAUTHORIZED", "유효하지 않은 리프레시 토큰입니다"),
    EXPIRED_REFRESH_TOKEN(401, "UNAUTHORIZED", "만료된 리프레시 토큰입니다"),
    INVALID_CREDENTIALS(401, "UNAUTHORIZED", "아이디 또는 비밀번호가 일치하지 않습니다"),

    // User
    DUPLICATE_LOGIN_ID(409, "DUPLICATE", "이미 사용 중인 아이디입니다"),
    USER_NOT_FOUND(404, "NOT_FOUND", "사용자를 찾을 수 없습니다"),

    // Post
    POST_NOT_FOUND(404, "NOT_FOUND", "게시글을 찾을 수 없습니다"),
    POST_FORBIDDEN(403, "FORBIDDEN", "게시글을 수정/삭제할 권한이 없습니다");

    private final int status;
    private final String code;
    private final String message;
}