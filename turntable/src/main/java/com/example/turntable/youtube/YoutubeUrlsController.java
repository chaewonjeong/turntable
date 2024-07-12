package com.example.turntable.youtube;

import com.example.turntable.youtube.dto.SongsRequest;
import com.example.turntable.youtube.dto.SongsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/youtubeVideo")
public class YoutubeUrlsController {

    private final YoutubeUrlsService youtubeVideoService;

    @PostMapping("/getUrls")
    public ResponseEntity<SongsResponse> getYoutubeUrls(@RequestBody SongsRequest request) {
        SongsResponse response = youtubeVideoService.getYoutubeUrls(request);
        return ResponseEntity.ok(response);
    }

}
