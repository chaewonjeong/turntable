package com.example.turntable.youtube.dto;


import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongsResponse{
    private List<SongResponse> results;
}
