package com.example.turntable.service;

import com.example.turntable.domain.*;
import com.example.turntable.dto.PlayListCreateDto;
import com.example.turntable.dto.PlayListDetailDto;
import com.example.turntable.dto.PlayListDto;
import com.example.turntable.dto.SongDto;
import com.example.turntable.repository.PlayListRepository;
import com.example.turntable.repository.MemberRepository;
import com.example.turntable.repository.PlayListSongRepository;
import java.util.Optional;

import com.example.turntable.repository.SongArtistRepository;
import com.example.turntable.repository.SongArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayListService {
    private final PlayListRepository playListRepository;
    private final MemberRepository memberRepository;
    private final PlayListSongRepository playListSongRepository;
    private final SongArtistRepository songArtistRepository;
    private final SongArtistService songArtistService;

    @Transactional
    public PlayList savePlayList(Long userId, PlayListCreateDto playListCreateDto, PlayListStatus playListStatus) {
        // 추천 받은 시점에서 이미 db에 song은 저장됨 플레이리스트를 만들고 곡들과 매핑해줘야함 추가 저장 x
        Member member = getMemberById(userId);
        PlayList playList = PlayList.of(member, playListCreateDto.getName(), playListStatus);
        playList = playListRepository.save(playList);

        for (SongDto songDto : playListCreateDto.getTracks()) {
            Optional<Song> song = songArtistService.findSongByTitleAndArtist(songDto.getName(), songDto.getArtists());
            if (song.isPresent()) {
                PlayListSong playListSong = PlayListSong.of(playList, song.get());
                playListSongRepository.save(playListSong);
            } else {
                throw new RuntimeException("Song not found");
            }
        }

        return playList;
    }


    @Transactional(readOnly = true)
    public List<PlayListDto> getPlayListsByStatus(Long userId, PlayListStatus state) {
        Member member = getMemberById(userId);
        List<PlayList> playLists = playListRepository.findAllByMemberAndState(member, state);
        return playLists.stream().map(PlayListDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlayListDetailDto getPlayListDetails(Long playListId) {
        PlayList playList = playListRepository.findById(playListId).orElseThrow(() -> new IllegalArgumentException("Invalid playlist id: " + playListId));
        List<PlayListSong> playListSongs = playListSongRepository.findAllByPlayList(playList);
        List<SongDto> songs = playListSongs.stream()
                .map(playListSong -> {
                    Song song = playListSong.getSong();
                    List<SongArtist> songArtists = songArtistRepository.findAllBySong(song);
                    List<String> artists = songArtists.stream()
                            .map(songArtist -> songArtist.getArtist().getName())
                            .collect(Collectors.toList());
                    return SongDto.from(song, artists);
                })
                .collect(Collectors.toList());
        return PlayListDetailDto.from(playList, songs);
    }


    private Member getMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
    }




    public int getPlaylistCount(Long memberId){
        return playListRepository.countByMember_Id(memberId);
    }

}
