package com.example.turntable.spotify;

import com.example.turntable.service.SongArtistService;
import com.example.turntable.spotify.dto.ArtistResponseDto;
import com.example.turntable.spotify.dto.GenreResponsDto;
import com.example.turntable.spotify.dto.RecommendRequestDto;
import com.example.turntable.spotify.dto.TrackResponseDto;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SpotifyService {

    private final SpotifyApi spotifyApi;
    private final SongArtistService songArtistService;

    public List<TrackResponseDto> searchTracks(String keyword){
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(keyword).build();

        try {
            Paging<Track> trackPaging = searchTracksRequest.execute();
            Track[] tracks = trackPaging.getItems();

            return Arrays.stream(tracks).map(track -> {
                String trackId = track.getId();
                String trackName = track.getName();
                List<String> trackArtists = Arrays.stream(track.getArtists()).map(artist->{
                    return artist.getName();
                }).collect(Collectors.toList());
                String trackAlbumName = track.getAlbum().getName();
                String trackAlbumImgUrl = track.getAlbum().getImages()[0].getUrl();

                TrackResponseDto trackResponseDto = TrackResponseDto.of(trackId, trackName, trackArtists, trackAlbumName, trackAlbumImgUrl);
                return trackResponseDto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search tracks", e);
        }
    }

    public List<ArtistResponseDto>searchArtist(String keyword){
        SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(keyword).build();
        try{
             Paging<Artist> artistPaging = searchArtistsRequest.execute();
             Artist[] artists = artistPaging.getItems();

             return Arrays.stream(artists).map(artist -> {
                 ArtistResponseDto artistResponseDto = new ArtistResponseDto();
                 artistResponseDto.setId(artist.getId());
                 artistResponseDto.setName(artist.getName());
                 return artistResponseDto;
             }).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to search artist", e);
        }
    }

    public List<GenreResponsDto> searchGenre(){
        GetAvailableGenreSeedsRequest getAvailableGenreSeedsRequest = spotifyApi.getAvailableGenreSeeds().build();
        try{
            String[] genres = getAvailableGenreSeedsRequest.execute();
            return Arrays.stream(genres).map(genre -> {
                GenreResponsDto genreResponsDto = new GenreResponsDto();
                genreResponsDto.setName(genre);
                return genreResponsDto;
            }).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to search genres", e);
        }
    }

    public List<TrackResponseDto> getRecommends(RecommendRequestDto recommendRequestDto){
        if (recommendRequestDto.getSeedArtists() == null || recommendRequestDto.getSeedGenres() == null || recommendRequestDto.getSeedTracks() == null) {
            throw new IllegalArgumentException("Seed artists, genres, and tracks must not be null");
        }

        String seedArtistsStr = String.join(",", recommendRequestDto.getSeedArtists());
        String seedGenresStr = String.join(",", recommendRequestDto.getSeedGenres());
        String seedTracksStr = String.join(",", recommendRequestDto.getSeedTracks());

        GetRecommendationsRequest getRecommendationsRequest = spotifyApi.getRecommendations()
            .seed_artists(seedArtistsStr)
            .seed_genres(seedGenresStr)
            .seed_tracks(seedTracksStr)
            .limit(10)
            .build();

        System.out.println("Seed Artists: " + seedArtistsStr);
        System.out.println("Seed Tracks: " + seedTracksStr);
        System.out.println("Seed Genres: " + seedGenresStr);

        try{
            Recommendations recommendations = getRecommendationsRequest.execute();
            TrackSimplified[] tracks = recommendations.getTracks();
            return getTrackList(tracks);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to search recommendations", e);
        }

    }

    public List<TrackResponseDto> getTrackList(TrackSimplified[] trackSimplifieds){
        return List.of(trackSimplifieds).stream().map(trackSimplified -> {
            try {
                GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackSimplified.getId()).build();
                Track track = getTrackRequest.execute();

                String trackId = track.getId();
                String trackName = track.getName();
                List<String> trackArtists = Arrays.stream(track.getArtists()).map(artist->{
                    return artist.getName();
                }).collect(Collectors.toList());
                String trackAlbumName = track.getAlbum().getName();
                String trackAlbumImgUrl = track.getAlbum().getImages()[0].getUrl();

                TrackResponseDto trackResponseDto = TrackResponseDto.of(trackId, trackName, trackArtists, trackAlbumName, trackAlbumImgUrl);
                return trackResponseDto;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    public Map<String, String> getTrackInfo(String trackId) {
        GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();

        try {
            Track track = getTrackRequest.execute();

            String title = track.getName();
            String artist = String.join(", ", track.getArtists()[0].getName());

            Map<String, String> trackInfo = new HashMap<>();
            trackInfo.put("title", title);
            trackInfo.put("artist", artist);

            return trackInfo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }
    }
}
