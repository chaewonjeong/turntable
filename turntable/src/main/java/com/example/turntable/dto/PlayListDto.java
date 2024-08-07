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
    private final String madeBy;

    public static PlayListDto from(PlayList playList) {
        return PlayListDto.builder()
                .id(playList.getId())
                .name(playList.getName())
                .date(playList.getDate())
                .state(playList.getState())
                .madeBy(playList.getMember().getNickname())
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
