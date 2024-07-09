package com.example.turntable.dto;

import com.example.turntable.domain.PlayList;
import lombok.*;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class PlayListDetailDto {
    private final Long id;
    private final String name;
    private final List<SongDto> songs;


    public static PlayListDetailDto from(PlayList playList, List<SongDto> songs) {
        return PlayListDetailDto.builder()
                .id(playList.getId())
                .name(playList.getName())
                .songs(songs)
                .build();
    }

}
