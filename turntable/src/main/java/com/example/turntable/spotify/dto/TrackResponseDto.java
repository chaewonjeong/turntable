package com.example.turntable.spotify.dto;

import com.example.turntable.domain.Song;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TrackResponseDto {
    private String id;
    private String name;
    private List<String> artists;
    private String albumName;
    private String albumImgUrl;

    public static TrackResponseDto of(String id, String name, List<String> artists, String albumName, String albumImgUrl) {
        return TrackResponseDto.builder()
            .id(id)
            .name(name)
            .artists(artists)
            .albumName(albumName)
            .albumImgUrl(albumImgUrl)
            .build();
    }

    public Song toEntity(){
        return Song.builder()
            .name(name)
            .albumName(albumName)
            .albumImgUrl(albumImgUrl)
            .build();
    }
}
