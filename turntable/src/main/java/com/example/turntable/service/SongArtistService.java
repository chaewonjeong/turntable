package com.example.turntable.service;

import com.example.turntable.domain.Artist;
import com.example.turntable.domain.Song;
import com.example.turntable.domain.SongArtist;
import com.example.turntable.event.TrackSavedEvent;
import com.example.turntable.repository.ArtistRepository;
import com.example.turntable.repository.SongArtistRepository;
import com.example.turntable.repository.SongNameInfo;
import com.example.turntable.repository.SongRepository;
import com.example.turntable.spotify.dto.TrackResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class SongArtistService {
    private final SongArtistRepository songArtistRepository;
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ConversionService conversionService;

    @TransactionalEventListener
    public boolean saveTrackInfo(List<TrackResponseDto> tracks) {
        // 추가
        List<Song> needYoutubeUrlSongs = new ArrayList<>();

        tracks.forEach(track -> {
            if (!isSongExist(track.getName(),track.getArtists())) {
                Song song = track.toEntity();
                needYoutubeUrlSongs.add(saveTrackNeedToSave(song));
                List<Artist> artists = artistsNeedToSave(track.getArtists());
                saveAllArtistsNeedToSave(artists);
                List<SongArtist> songArtists = songArtistsNeedToSave(song,track.getArtists());
                saveAllSongArtistsNeedToSave(songArtists);
            } else if (track.toEntity().getYoutubeUrl()==null) {
                List<SongNameInfo> songNameInfos = songArtistRepository.findBySongTitleAndArtistNamesIn(track.getName(),track.getArtists(),track.getArtists().size());
                Optional<Song> song = songRepository.findById(songNameInfos.get(0).getSongId());
                needYoutubeUrlSongs.add(song.get());
            }
        });

        youtubePublishEvent(needYoutubeUrlSongs);
        return true;
    }

    public void youtubePublishEvent(List<Song> songs){
        eventPublisher.publishEvent(new TrackSavedEvent(songs));
    }

    @Transactional
    public Song saveTrackNeedToSave(Song song) {
        return songRepository.save(song);
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

    public List<SongArtist> songArtistsNeedToSave(Song song, List<String> artists){
        List<SongArtist> songArtists = new ArrayList<>();
        artists.forEach(artist -> {
            SongArtist songArtist = SongArtist.builder()
                .song(song)
                .artist(artistRepository.findByName(artist).orElse(null))
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
