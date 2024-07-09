package com.example.turntable.controller;

import com.example.turntable.domain.PlayListStatus;
import com.example.turntable.dto.PlayListCreateDto;
import com.example.turntable.dto.PlayListDetailDto;
import com.example.turntable.dto.PlayListDto;
import com.example.turntable.service.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;

    @PostMapping("/create")
    public ResponseEntity<Void> savePlayList(@SessionAttribute(name = "userId", required=false) Long userId,
                                             @RequestParam PlayListStatus state,
                                             @RequestBody PlayListCreateDto playListCreateDto) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        playListService.savePlayList(userId,playListCreateDto,state);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{state}")
    public List<PlayListDto> getPlayList(@SessionAttribute(name = "userId", required = false) Long userId,
                                                         @PathVariable PlayListStatus state) {
        return playListService.getPlayListsByStatus(userId, state);
    }

    @GetMapping("/detail")
    public PlayListDetailDto getPlayListDetail(@RequestParam Long playListId) {
        return playListService.getPlayListDetails(playListId);
    }
}



