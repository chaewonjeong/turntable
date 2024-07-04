package com.example.turntable.dto;

import com.example.turntable.domain.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongDto {
    private  String name;
    private  String artist;
    private  String albumName;
    private  String albumImgUrl;


    //== 생성 메서드 ==//
    public static Song toSong(SongDto songDto) {
        return Song.builder()
                .name(songDto.getName())
                .albumName(songDto.getAlbumName())
                .albumImgUrl(songDto.getAlbumImgUrl())
                .build();
    }
}
