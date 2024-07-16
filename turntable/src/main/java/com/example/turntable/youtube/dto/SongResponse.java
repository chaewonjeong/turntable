package com.example.turntable.youtube.dto;


import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {
    private Long songId;
    private String name;
    private List<String> artists;
    private String albumName;
    private String youtubeUrl;
}

