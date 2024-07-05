package com.example.turntable.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongNameInfo {
    private Long songId;
    private String name;

    public SongNameInfo(Long songId, String name) {
        this.songId = songId;
        this.name = name;
    }
}
