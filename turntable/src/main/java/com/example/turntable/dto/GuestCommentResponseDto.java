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
    private boolean isOwner;

    public static GuestCommentResponseDto from (GuestComment guestComment, Long currentUserId) {
        return GuestCommentResponseDto.builder()
            .Id(guestComment.getId())
            .comment(guestComment.getComment())
            .date(guestComment.getCreatedAt())
            .guestName(guestComment.getVisitorMember().getNickname())
            .guestBgImgUrl(guestComment.getVisitorMember().getBackGroundImage())
                .isOwner(guestComment.getVisitorMember().getId().equals(currentUserId))
                .build();
    }
}
