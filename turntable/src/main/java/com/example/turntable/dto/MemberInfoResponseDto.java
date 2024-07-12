package com.example.turntable.dto;


import com.example.turntable.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDto {
    private Long memberId;
    private String memberName;
    private String bgImgUrl;
    private int playlistCount;

    public static MemberInfoResponseDto of(Member member, int playlistCount) {
        return MemberInfoResponseDto.builder()
            .memberId(member.getId())
            .memberName(member.getName())
            .bgImgUrl(member.getBackGroundImage())
            .playlistCount(playlistCount)
            .build();
    }
}
