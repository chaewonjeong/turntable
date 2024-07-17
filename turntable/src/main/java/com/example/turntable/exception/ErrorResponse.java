package com.example.turntable.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private CustomErrorCode errorCode;
    private String message;

    public ErrorResponse (CustomErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
