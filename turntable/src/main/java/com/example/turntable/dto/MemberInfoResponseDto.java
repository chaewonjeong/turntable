package com.example.turntable.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberInfoResponseDto {
    private Long memberId;
    private String memberName;
    private String bgImgUrl;
}
