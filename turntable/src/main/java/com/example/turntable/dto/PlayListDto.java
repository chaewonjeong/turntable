package com.example.turntable.dto;

import com.example.turntable.domain.PlayList;
import com.example.turntable.domain.PlayListStatus;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDate;

@Getter
@Builder
public class PlayListDto {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final PlayListStatus state;

    public static PlayListDto from(PlayList playList) {
        // return new PlayListDto(playList.getId(), playList.getName(), playList.getDate(), playList.getState());
        return PlayListDto.builder()
                .id(playList.getId())
                .name(playList.getName())
                .date(playList.getDate())
                .state(playList.getState())
                .build();
    }

    public static PlayListDto of(Long id, String name, LocalDate date, PlayListStatus state) {
        return PlayListDto.builder()
                .id(id)
                .name(name)
                .date(date)
                .state(state)
                .build();
    }

}
