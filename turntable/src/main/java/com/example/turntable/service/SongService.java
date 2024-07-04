package com.example.turntable.service;

import com.example.turntable.domain.Song;
import com.example.turntable.domain.SongArtist;
import com.example.turntable.repository.SongArtistRepository;
import com.example.turntable.spotify.dto.TrackResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongArtistRepository songArtistRepository;

    @Transactional
    public boolean saveSongs(List<TrackResponseDto> tracks){
        List<Song> songs = new ArrayList<>();
        tracks.forEach(track -> {
            if (!isSongExist(track.getName(), track.getArtists())) {
                Song song = song.of()


            }
        });
    }

    public boolean isSongExist(String title, List<String> artists) {
        Optional<SongArtist> existingSong = songArtistRepository.findBySongTitleAndArtistNamesIn(title,artists,artists.size());
        if(existingSong.isPresent()){
            return true;
        }
        return false;
    }

}
