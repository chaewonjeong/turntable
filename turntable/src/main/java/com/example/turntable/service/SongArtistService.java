package com.example.turntable.service;

import com.example.turntable.domain.Artist;
import com.example.turntable.domain.Song;
import com.example.turntable.domain.SongArtist;
import com.example.turntable.dto.SongDto;
import com.example.turntable.repository.ArtistRepository;
import com.example.turntable.repository.SongArtistRepository;
import com.example.turntable.repository.SongNameInfo;
import com.example.turntable.repository.SongRepository;
import com.example.turntable.spotify.dto.TrackResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongArtistService {
    private final SongArtistRepository songArtistRepository;
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;

    public List<SongDto> saveTrackInfo(List<TrackResponseDto> tracks) {
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

       List<Song> savedSongs = saveAllTracksNeedToSave(trackSongs);
       List<Artist> savedArtists = saveAllArtistsNeedToSave(trackArtists);
       List<SongArtist> savedsongArtists = saveAllSongArtistsNeedToSave(trackSongArtists);

       return savedSongs.stream()
           .map(this::convertToSongDto)
           .collect(Collectors.toList());
    }

    private SongDto convertToSongDto(Song song) {
        List<SongArtist> songArtists = songArtistRepository.findBySongId(song.getId());
        List<String> artistNames = songArtists.stream()
            .map(songArtist -> artistRepository.findById(songArtist.getArtist().getId())
                .orElseThrow(() -> new RuntimeException("Artist not found"))
                .getName())
            .collect(Collectors.toList());
        return SongDto.from(song, artistNames);
    }

    @Transactional
    public List<Song> saveAllTracksNeedToSave(List<Song> songs) {
        return songRepository.saveAll(songs);
    }
    @Transactional
    public List<Artist> saveAllArtistsNeedToSave(List<Artist> artists) {
        return artistRepository.saveAll(artists);
    }
    @Transactional
    public List<SongArtist> saveAllSongArtistsNeedToSave(List<SongArtist> songArtists) {
        return songArtistRepository.saveAll(songArtists);
    }

    public boolean isSongExist(String title, List<String> artists) {
        List<SongNameInfo> existingSong = songArtistRepository.findBySongTitleAndArtistNamesIn(title,artists,artists.size());
        if(existingSong.isEmpty()){
            return false;
        }
        return true;
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

    public List<Artist> findArtistsBySong(Long songId){
        List<SongArtist> songArtists = songArtistRepository.findBySongId(songId);
        List<Artist> artists = new ArrayList<>();
        songArtists.forEach(songArtist -> {
            artists.add(songArtist.getArtist());
        });
        return artists;
    }

    public Optional<Song> findSongByTitleAndArtist(String title,List<String> artists){
        List<SongNameInfo> existingSong = songArtistRepository.findBySongTitleAndArtistNamesIn(title,artists,artists.size());
        return songRepository.findById(existingSong.get(0).getSongId());
    }
}
