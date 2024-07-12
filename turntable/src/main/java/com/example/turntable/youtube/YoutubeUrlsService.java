package com.example.turntable.youtube;

import com.example.turntable.youtube.dto.SongsRequest;
import com.example.turntable.youtube.dto.SongsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class YoutubeUrlsService {

    private final RestTemplate restTemplate;

    public SongsResponse getYoutubeUrls(SongsRequest request) {
        String url = "http://localhost:8000/getYoutubeUrls";

        ResponseEntity<SongsResponse> response = restTemplate.postForEntity(url, request, SongsResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get YouTube URLs");
        }
    }
}
