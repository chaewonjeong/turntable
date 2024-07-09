package com.example.turntable.service;

import com.example.turntable.domain.*;
import com.example.turntable.dto.PlayListDto;
import com.example.turntable.dto.SongDto;
import com.example.turntable.repository.PlayListRepository;
import com.example.turntable.repository.MemberRepository;
import com.example.turntable.repository.PlayListSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayListService {
    private final PlayListRepository playListRepository;
    private final MemberRepository memberRepository;
    private final PlayListSongRepository playListSongRepository;

    @Transactional
    public PlayList savePlayList(Long userId, PlayListDto playListDto, PlayListStatus playListStatus) {
        Member member = getMemberById(userId);
        PlayList playList = PlayList.of(member, playListDto.getName(), playListStatus);
        List<PlayListSong> playListSongs = createSongs(playList, playListDto);

        savePlayListAndSongs(playList, playListSongs);

        return playList;
    }

    private Member getMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
    }


    private List<PlayListSong> createSongs(PlayList playList, PlayListDto playListDto) {
        return playListDto.getTracks().stream()
                .map(trackDto -> {
                    Song song = SongDto.toSong(trackDto);
                return PlayListSong.of(playList, song);
                })
                .collect(Collectors.toList());
    }

    private void savePlayListAndSongs(PlayList playList, List<PlayListSong> playListSongs) {
        playListRepository.save(playList);
        playListSongRepository.saveAll(playListSongs);
    }

    public int getPlaylistCount(Long memberId){
        return playListRepository.countByMember_Id(memberId);
    }

}
