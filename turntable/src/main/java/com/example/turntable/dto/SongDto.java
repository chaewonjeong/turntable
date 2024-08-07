package com.example.turntable.dto;

import com.example.turntable.domain.Song;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SongDto {
    private Long id;
    private String name;
    private List<String> artists;
    private String albumName;
    private String albumImgUrl;
    private String youtubeUrl;

    public static SongDto from(Song song, List<String> artists) {
        return SongDto.builder()
            .id(song.getId())
            .name(song.getName())
            .artists(artists)
            .albumName(song.getAlbumName())
            .albumImgUrl(song.getAlbumImgUrl())
                .youtubeUrl(song.getYoutubeUrl())
            .build();
    }

    //== 생성 메서드 ==//
    public static Song toSong(SongDto songDto) {
        return Song.builder()
                .name(songDto.getName())
                .albumName(songDto.getAlbumName())
                .albumImgUrl(songDto.getAlbumImgUrl())
                .build();
    }
}
