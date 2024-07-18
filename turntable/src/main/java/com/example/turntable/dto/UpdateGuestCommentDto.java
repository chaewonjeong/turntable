package com.example.turntable.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateGuestCommentDto {
    private Long commentId;
    private String comment;
    private String date;
}
