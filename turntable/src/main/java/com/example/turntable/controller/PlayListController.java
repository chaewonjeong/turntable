package com.example.turntable.controller;

import com.example.turntable.domain.PlayListStatus;
import com.example.turntable.dto.PlayListDto;
import com.example.turntable.service.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;

    @PostMapping("create/daily")
    public ResponseEntity<Void> saveDailyPlayList(@SessionAttribute(name="userId", required = false) Long userId,
                                             @RequestBody PlayListDto playListDto) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PlayListStatus playListStatus = PlayListStatus.DAILY;

        playListService.savePlayList(userId, playListDto, playListStatus);
        return ResponseEntity.ok().build();
    }

    @PostMapping("create/my")
    public ResponseEntity<Void> saveMyPlayList(@SessionAttribute(name="userId", required = false) Long userId,
                                             @RequestBody PlayListDto playListDto) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PlayListStatus playListStatus = PlayListStatus.MY;

        playListService.savePlayList(userId, playListDto, playListStatus);
        return ResponseEntity.ok().build();
    }
}



