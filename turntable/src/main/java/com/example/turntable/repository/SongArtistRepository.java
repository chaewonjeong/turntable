package com.example.turntable.repository;

import com.example.turntable.domain.SongArtist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongArtistRepository extends JpaRepository<SongArtist, Long> {
    @Query("SELECT sa FROM SongArtist sa WHERE sa.song.name = :title AND sa.artist.name IN :artistNames GROUP BY sa.song.Id HAVING count(*)=:artistCount")
    Optional<SongArtist> findBySongTitleAndArtistNamesIn(@Param("title") String title, @Param("artistNames") List<String> artistNames,@Param("artistCount") int artistCount);

}
