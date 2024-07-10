package com.example.turntable.youtube.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SongRequest {
    private String name;
    private List<String> artists;
    private String albumName;
}
