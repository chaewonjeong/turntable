package com.example.turntable.dto;

import com.example.turntable.domain.DailyComment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String comment;
    private LocalDateTime date;
    private String title;
    private List<String> artists;
    private int commentCount;

    public static CommentResponseDto of(DailyComment comment, List<String> artists,
        int commentCount) {
        return CommentResponseDto.builder()
            .id(comment.getId())
            .comment(comment.getComment())
            .date(comment.getCreatedAt())
            .title(comment.getSong().getName())
            .artists(artists)
            .commentCount(commentCount)
            .build();
    }
}
