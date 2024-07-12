package com.example.turntable.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PlayListCreateDto {
    private final String name;
    private final List<SongDto> tracks;

}
