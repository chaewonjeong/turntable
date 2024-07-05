package com.example.turntable.dto;

import com.example.turntable.domain.DailyComment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentResponseDto {
    private final Long id;
    private final String comment;
    private final LocalDateTime date;
    private final String title;
    private final List<String> artists;
    private final int commentCount;
}
