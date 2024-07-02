package com.example.turntable.dto;

import com.example.turntable.domain.DailyComment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentResponseDto {
    private final String comment;
    private final LocalDateTime date;
    private final String title;
    private final String artist;
    private final int commentCount;
}
