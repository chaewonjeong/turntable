package com.example.turntable.service;

import com.example.turntable.domain.Artist;
import com.example.turntable.domain.Song;
import com.example.turntable.domain.SongArtist;
import com.example.turntable.repository.ArtistRepository;
import com.example.turntable.repository.SongArtistRepository;
import com.example.turntable.repository.SongRepository;
import com.example.turntable.spotify.dto.TrackResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongArtistService {
    private final SongArtistRepository songArtistRepository;
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;

    @Transactional
    public boolean saveSongs(List<TrackResponseDto> tracks){
        List<Song> songs = new ArrayList<>();
        tracks.forEach(track -> {
            if (!isSongExist(track.getName(), track.getArtists())) {
                songs.add(track.toEntity());
            }
        });
        songRepository.saveAll(songs);
        return true;
    }

    public boolean isSongExist(String title, List<String> artists) {
        Optional<SongArtist> existingSong = songArtistRepository.findBySongTitleAndArtistNamesIn(title,artists,artists.size());
        if(existingSong.isPresent()){
            return true;
        }
        return false;
    }

    @Transactional
    public boolean saveArtistsInTrack(List<String> trackArtists){
        List<Artist> artists = new ArrayList<>();
        trackArtists.forEach(trackartist -> {
            if(!isArtistExist(trackartist)){
                artists.add(Artist.builder().name(trackartist).build());
            }
        });
        artistRepository.saveAll(artists);
        return true;
    }

}
