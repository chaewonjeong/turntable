package com.example.turntable.youtube.dto;

import com.example.turntable.domain.Song;
import com.example.turntable.spotify.dto.TrackResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class SongRequest {

    private final Long songId;
    private final String name;
    private final List<String> artists;
    private final String albumName;

    public static SongRequest from(TrackResponseDto track) {
        return builder()
                .name(track.getName())
                .artists(track.getArtists())
                .albumName(track.getAlbumName())
                .build();
    }

    public static SongRequest fromSong(Song song, List<String> artists) {

        return builder()
                .songId(song.getId())
                .name(song.getName())
                .artists(artists)
                .albumName(song.getAlbumName())
                .build();
    }

}
