package com.example.turntable.youtube.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongsRequest {
    private List<SongRequest> songs;

    public static SongsRequest of(List<SongRequest> tracks) {
        return SongsRequest.builder()
                .songs(tracks)
                .build();
    }
}
