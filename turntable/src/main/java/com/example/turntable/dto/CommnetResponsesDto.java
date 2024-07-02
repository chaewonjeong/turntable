package com.example.turntable.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommnetResponsesDto {
    private final List<CommentResponseDto> comments;
}
