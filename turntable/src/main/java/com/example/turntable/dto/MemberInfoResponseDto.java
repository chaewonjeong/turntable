package com.example.turntable.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberInfoResponseDto {
    private final Long memberId;
    private final String memberName;
    private final String bgImgUrl;
}
