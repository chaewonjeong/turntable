package com.example.turntable.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    // ID
    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "back_ground_image")
    private String backGroundImage;

    public void setOAuth2User(final String name, final String nickname,final String backGroundImage) {
        this.name = name;
        this.nickname = nickname;
        this.backGroundImage=backGroundImage;
    }
    public void changeNickname(final String newNickname) {
        this.nickname = newNickname;
    }
    public void changeBackGroundImage(final String newBackGroundImage) {this.backGroundImage=newBackGroundImage;}
}
