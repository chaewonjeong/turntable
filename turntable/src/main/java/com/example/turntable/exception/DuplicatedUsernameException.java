package com.example.turntable.exception;

import lombok.Getter;

@Getter
public class DuplicatedUsernameException extends RuntimeException {
    private final CustomErrorCode errorCode;

    public DuplicatedUsernameException(CustomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
