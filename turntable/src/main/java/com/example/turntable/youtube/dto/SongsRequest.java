package com.example.turntable.youtube.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SongsRequest {
    private List<SongRequest> songs;
}
