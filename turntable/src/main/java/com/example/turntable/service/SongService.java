package com.example.turntable.service;

import com.example.turntable.domain.Song;
import com.example.turntable.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;

    public String getYoutubeUrl(Long songId){
        Song song = songRepository.findById(songId).orElse(null);
        return song.getYoutubeUrl();
    }

}
