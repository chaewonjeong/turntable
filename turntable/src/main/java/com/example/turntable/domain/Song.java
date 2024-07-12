package com.example.turntable.domain;

import com.example.turntable.dto.SongDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Song {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long Id;

    @Column(nullable = false, name = "song_name")
    private String name;

    private String albumName;
    private String albumImgUrl;

    private String youtubeUrl;
}
