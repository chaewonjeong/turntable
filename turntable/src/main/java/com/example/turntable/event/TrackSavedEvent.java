package com.example.turntable.event;

import com.example.turntable.spotify.dto.TrackResponseDto;
import lombok.RequiredArgsConstructor;


import java.util.List;

@RequiredArgsConstructor
public class TrackSavedEvent {
    private final List<TrackResponseDto> tracks;

    public List<TrackResponseDto> getTracks() {
        return tracks;
    }

}
