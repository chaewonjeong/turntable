package com.example.turntable.service;

import com.example.turntable.domain.Song;
import com.example.turntable.event.TrackSavedEvent;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeUrlsUpdateService {

    private final YoutubeUrlsService youtubeUrlsService;
    private final SongRepository songRepository;
    private static Logger logger = LoggerFactory.getLogger(YoutubeUrlsUpdateService.class);

    @Async
    @EventListener
    public void handleTrackSavedEvent(TrackSavedEvent event) {
        List<TrackResponseDto> tracks = event.getTracks();
        if (tracks == null || tracks.isEmpty()) {
            logger.warn("Empty or null track list received in TrackSavedEvent");
            return;
        }
        // 크롤링 서버에 요청 -> url 획득 -> DB update
        SongsRequest songsRequest = convertToSongsRequest(tracks);
        SongsResponse songsResponse = fetchYoutubeUrls(songsRequest);

        if (songsResponse == null) {
            logger.error("Failed to fetch YouTube URLs for tracks: {}", tracks);
            return;
        }
        updateSongsWithYoutubeUrls(tracks, songsResponse);
    }

    // tracks -> SongsRequest
    private SongsRequest convertToSongsRequest (List<TrackResponseDto> tracks) {
        List<SongRequest> songs = new ArrayList<>();
        for(TrackResponseDto track : tracks) {
            songs.add(SongRequest.from(track));
        }
        return SongsRequest.of(songs);
    }



    private SongsResponse fetchYoutubeUrls (SongsRequest songsRequest) {
        return youtubeUrlsService.getYoutubeUrls(songsRequest);
    }

    // URL을 DB에 업데이트하는 메서드
    @Transactional
    public void updateSongsWithYoutubeUrls(List<TrackResponseDto> tracks, SongsResponse songsResponse) {
        if (tracks == null || songsResponse == null) {
            return;
        }

        List<SongResponse> songResponses = songsResponse.getResults();

        for (SongResponse songResponse : songResponses) {
            String youtubeUrl = songResponse.getYoutubeUrl();

            // TrackResponseDto에서 해당 노래를 찾아서 업데이트
            tracks.forEach(track -> {
                if (track.getName().equals(songResponse.getName()) &&
                        track.getAlbumName().equals(songResponse.getAlbumName()) &&
                        track.getArtists().containsAll(songResponse.getArtists())) {
//                    updateYoutubeUrl(track.toEntity().getId(), youtubeUrl);
                    System.out.println(track.getId());
                }
            });
        }
    }

    @Transactional
    public void updateYoutubeUrl(Long songId, String youtubeUrl){
        if (songId == null) {
            logger.error("Received null songId in updateYoutubeUrl");
            return;
        }

        Song song = songRepository.findById(songId).orElseThrow(()-> new IllegalArgumentException("Song not found"));
        Song updatedSong = Song.builder()
                .youtubeUrl(youtubeUrl) // 여기서 youtubeUrl을 업데이트
                .build();
        songRepository.save(updatedSong);
    }

}
