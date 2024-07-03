package com.example.turntable.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class PlayListSong {
    @Id @GeneratedValue
    @Column(name = "playlist_song_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "playlist_id")
    private PlayList playList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "song_id")
    private Song song;


}
