package com.example.turntable.domain;

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
    private Long id;

    @Column(nullable = false, name = "song_name")
    private String name;

    private String albumName;
    private String albumImgUrl;

    @Column(length = 50000)
    private String youtubeUrl;


    public void changeYoutubeUrl(final String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }
}
