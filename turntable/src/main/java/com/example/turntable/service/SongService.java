package com.example.turntable.service;

import com.example.turntable.domain.Song;
import com.example.turntable.event.TrackSavedEvent;
import com.example.turntable.repository.SongRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final ApplicationEventPublisher eventPublisher;

    public String getYoutubeUrl(Long songId){
        Song song = songRepository.findById(songId).orElse(null);
        return song.getYoutubeUrl();
    }

    public void updateYoutubeUrl(Long songId){
        Song song = songRepository.findById(songId).orElse(null);
        List<Song> savedSongs = new ArrayList<>();
        savedSongs.add(song);
        // tracks를 요청하면 안됨 -> DB에 저장된
        eventPublisher.publishEvent(new TrackSavedEvent(savedSongs));
    }

}
