package com.example.turntable.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    // == 생성 메서드 == //
    public static PlayList of(Member member, String name, PlayListStatus state) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy년 M월 d일");
        if(state == PlayListStatus.DAILY){
            return PlayList.builder()
                    .member(member)
                    .name(LocalDate.now().format(formatter))
                    .date(LocalDate.now())
                    .state(state)
                    .build();
        } else {
            return PlayList.builder()
                    .member(member)
                    .name(name)
                    .date(LocalDate.now())
                    .state(state)
                    .build();

        }
    }
}
