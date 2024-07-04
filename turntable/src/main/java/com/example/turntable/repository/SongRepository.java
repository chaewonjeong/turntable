package com.example.turntable.repository;

import com.example.turntable.domain.Song;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByTitleAndArtist(String title,String artist);

}
