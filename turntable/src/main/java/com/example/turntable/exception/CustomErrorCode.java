package com.example.turntable.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
    ALREADY_EXIST_USERNAME("이미 존재하는 아이디 입니다.");

    private final String code;
    private final String message;

    CustomErrorCode(String message) {
        this.code = name();
        this.message = message;
    }
}
