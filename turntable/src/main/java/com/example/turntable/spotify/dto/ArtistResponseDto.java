package com.example.turntable.spotify.dto;

import com.example.turntable.domain.Artist;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtistResponseDto {
    private String id;
    private String name;
}
