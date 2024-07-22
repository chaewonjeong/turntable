package com.example.turntable.dto;

import com.example.turntable.domain.DailyComment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String comment;
    private LocalDateTime date;
    private Long songId;
    private String title;
    private List<String> artists;
    private int commentCount;
    private String youtubeUrl;

    public static CommentResponseDto of(DailyComment comment, List<String> artists,
        int commentCount) {
        return CommentResponseDto.builder()
            .id(comment.getId())
            .comment(comment.getComment())
            .date(comment.getCreatedAt())
            .songId(comment.getSong().getId())
            .title(comment.getSong().getName())
            .artists(artists)
            .commentCount(commentCount)
            .youtubeUrl(comment.getSong().getYoutubeUrl())
            .build();
    }
}
