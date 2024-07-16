package com.example.turntable.service;

import com.example.turntable.domain.Song;
import com.example.turntable.event.TrackSavedEvent;
import com.example.turntable.repository.SongArtistRepository;
import com.example.turntable.repository.SongRepository;
import com.example.turntable.spotify.dto.TrackResponseDto;
import com.example.turntable.youtube.YoutubeUrlsService;
import com.example.turntable.youtube.dto.SongRequest;
import com.example.turntable.youtube.dto.SongResponse;
import com.example.turntable.youtube.dto.SongsRequest;
import com.example.turntable.youtube.dto.SongsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YoutubeUrlsUpdateService {

    private final YoutubeUrlsService youtubeUrlsService;
    private final SongRepository songRepository;
    private final SongArtistService songArtistService;
    private final SongArtistRepository songArtistRepository;
    private static Logger logger = LoggerFactory.getLogger(YoutubeUrlsUpdateService.class);

    @Async
    @EventListener
    @Transactional
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handleTrackSavedEvent(TrackSavedEvent event) {
        List<Song> songs = event.getSongs();

        if (songs == null || songs.isEmpty()) {
            logger.warn("Empty or null track list received in TrackSavedEvent");
            return;
        }

        // 크롤링 서버에 요청 -> url 획득 -> DB update
        SongsRequest songsRequest = convertToSongsRequest(songs);
        SongsResponse songsResponse = fetchYoutubeUrls(songsRequest);

        if (songsResponse == null) {
            logger.error("Failed to fetch YouTube URLs for tracks: {}", songs);
            return;
        }
        updateSongsWithYoutubeUrls(songsResponse);
    }

    // tracks -> SongsRequest
    private SongsRequest convertToSongsRequest (List<Song> songs) {
        List<SongRequest> songRequests = new ArrayList<>();
        for(Song song : songs) {
            List<String> artists = songArtistService.findArtistsBySong(song.getId()).stream()
                            .map(songArtist -> songArtist.getName())
                                    .collect(Collectors.toList());
            songRequests.add(SongRequest.fromSong(song, artists));
        }
        return SongsRequest.of(songRequests);
    }



    private SongsResponse fetchYoutubeUrls (SongsRequest songsRequest) {
        return youtubeUrlsService.getYoutubeUrls(songsRequest);
    }

    // URL을 DB에 업데이트하는 메서드
    @Transactional
    public void updateSongsWithYoutubeUrls(SongsResponse songsResponse) {
        if (songsResponse == null) {
            return;
        }

        List<SongResponse> songResponses = songsResponse.getResults();

        for (SongResponse songResponse : songResponses) {
            String youtubeUrl = songResponse.getYoutubeUrl();
            Long songId = songResponse.getSongId();
            updateYoutubeUrl(songId, youtubeUrl);
        }
    }

    @Transactional
    public void updateYoutubeUrl(Long songId, String youtubeUrl){
        if (songId == null) {
            logger.error("Received null songId in updateYoutubeUrl");
            return;
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        song.changeYoutubeUrl(youtubeUrl); // 여기서 youtubeUrl을 업데이트
        songRepository.save(song);
    }

}
