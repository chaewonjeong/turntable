package com.example.turntable.dto;

import com.example.turntable.domain.GuestComment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GuestCommentResponseDto {
    private Long Id;
    private String comment;
    private LocalDateTime date;
    private String guestName;
    private String guestBgImgUrl;

    public static GuestCommentResponseDto from (GuestComment guestComment) {
        return GuestCommentResponseDto.builder()
            .Id(guestComment.getId())
            .comment(guestComment.getComment())
            .date(guestComment.getCreatedAt())
            .guestName(guestComment.getVisitorMember().getName())
            .guestBgImgUrl(guestComment.getVisitorMember().getBackGroundImage())
            .build();
    }
}
