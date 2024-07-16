package com.example.turntable.event;

import com.example.turntable.domain.Song;
import com.example.turntable.spotify.dto.TrackResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


import java.util.List;

@RequiredArgsConstructor
@Getter
public class TrackSavedEvent {
    private final List<Song> songs;


}
