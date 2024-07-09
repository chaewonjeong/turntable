package com.example.turntable.dto;

import com.example.turntable.domain.PlayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PlayListCreateDto {
    private final String name;
    private final List<SongDto> tracks;

}
