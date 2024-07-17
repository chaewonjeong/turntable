package com.example.turntable.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class SignupRequestDto {
    private String name;
    private String nickname;
    private String password;
    private MultipartFile bgImg;

    public static SignupRequestDto of(String name, String nickname, String password, MultipartFile bgImg) {
        return SignupRequestDto.builder()
            .name(name)
            .nickname(nickname)
            .password(password)
            .bgImg(bgImg)
            .build();
    }
}
