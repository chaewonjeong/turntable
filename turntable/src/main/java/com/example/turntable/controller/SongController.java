package com.example.turntable.controller;

import com.example.turntable.dto.GuestCommentResponseDto;
import com.example.turntable.service.SongArtistService;
import com.example.turntable.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class SongController {
    private final SongService songService;

    @GetMapping("/song/youtube-url")
    @ResponseBody
    public String getYoutubeUrl(@RequestParam Long songId){
        return songService.getYoutubeUrl(songId);
    }
}
