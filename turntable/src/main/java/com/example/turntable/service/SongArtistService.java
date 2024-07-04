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

    public boolean saveTrackInfo(List<TrackResponseDto> tracks) {
        List<Song> trackSongs = new ArrayList<>();
        List<Artist> trackArtists = new ArrayList<>();
        List<SongArtist> trackSongArtists = new ArrayList<>();

        tracks.forEach(track -> {
            if (!isSongExist(track.getName(),track.getArtists())) {
                Song song = track.toEntity();
                List<Artist> artists = artistsNeedToSave(track.getArtists());
                List<SongArtist> songArtists = songArtistsNeedToSave(song,artists);

                trackSongs.add(song);
                trackSongArtists.addAll(songArtists);
                trackArtists.addAll(artists);
            }
        });

       saveAllTracksNeedToSave(trackSongs,trackArtists,trackSongArtists);
       songArtistRepository.saveAll(trackSongArtists);
       return true;
    }

    @Transactional
    public boolean saveAllTracksNeedToSave(List<Song> songs, List<Artist> artists,List<SongArtist> songArtists) {
        songRepository.saveAll(songs);
        artistRepository.saveAll(artists);
        return true;
    }

    public boolean isSongExist(String title, List<String> artists) {
        Optional<SongArtist> existingSong = songArtistRepository.findBySongTitleAndArtistNamesIn(title,artists,artists.size());
        if(existingSong.isPresent()){
            return true;
        }
        return false;
    }

    public List<Artist> artistsNeedToSave(List<String> trackArtists){
        List<Artist> artists = new ArrayList<>();
        trackArtists.forEach(trackartist -> {
            if(!isArtistExist(trackartist)){
                artists.add(Artist.builder().name(trackartist).build());
            }
        });

        return artists;
    }

    public boolean isArtistExist(String artist) {
        Optional<Artist> existingArtist = artistRepository.findByName(artist);
        if(existingArtist.isPresent()){
            return true;
        }
        return false;
    }

    public List<SongArtist> songArtistsNeedToSave(Song song, List<Artist> artists){
        List<SongArtist> songArtists = new ArrayList<>();
        artists.forEach(artist -> {
            SongArtist songArtist = SongArtist.builder()
                .song(song)
                .artist(artist)
                .build();
            songArtists.add(songArtist);
        });
        return songArtists;
    }

}
