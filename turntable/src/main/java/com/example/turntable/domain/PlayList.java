package com.example.turntable.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "playlist")
public class PlayList {
    @Id @GeneratedValue
    @Column(name = "playlist_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private PlayListStatus state;

}
