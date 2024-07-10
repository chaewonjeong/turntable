package com.example.turntable.repository;

import com.example.turntable.domain.PlayList;
import com.example.turntable.domain.PlayListSong;
import com.example.turntable.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListSongRepository extends JpaRepository<PlayListSong, Long> {
    List<PlayListSong> findAllByPlayList(PlayList playList);
    void deleteByPlayList_Id(Long playListId);
}
