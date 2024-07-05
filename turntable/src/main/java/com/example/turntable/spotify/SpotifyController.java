package com.example.turntable.spotify;

import com.example.turntable.service.SongArtistService;
import com.example.turntable.spotify.dto.ArtistResponseDto;
import com.example.turntable.spotify.dto.GenreResponsDto;
import com.example.turntable.spotify.dto.RecommendRequestDto;
import com.example.turntable.spotify.dto.TrackResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyService spotifyService;
    private final SongArtistService songArtistService;


    @GetMapping("/search/track")
    public List<TrackResponseDto> searchTracks(@RequestParam String keyword) {
        List<TrackResponseDto> result = spotifyService.searchTracks(keyword);
        songArtistService.saveTrackInfo(result);
        return result;
    }

    @GetMapping("/search/artist")
    public List<ArtistResponseDto> searchTracksByArtist(@RequestParam String keyword) {
        return spotifyService.searchArtist(keyword);
    }

    @GetMapping("/search/genre")
    public List<GenreResponsDto> searchGenres() {
        return spotifyService.searchGenre();
    }

    @PostMapping("/recommendations")
    public List<TrackResponseDto> getRecommendations(@RequestBody RecommendRequestDto recommendationsRequestDto) {
        return spotifyService.getRecommends(recommendationsRequestDto);
    }
}
