package com.example.turntable.repository;

import com.example.turntable.domain.Song;
import com.example.turntable.domain.SongArtist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongArtistRepository extends JpaRepository<SongArtist, Long> {
    @Query("SELECT new com.example.turntable.repository.SongNameInfo(SA.song.id, S.name) "
        + "FROM SongArtist SA "
        + "JOIN SA.song S "
        + "JOIN SA.artist A "
        + "WHERE S.name LIKE :title "
        + "AND A.name IN :artistNames G"
        + "ROUP BY SA.song.id, S.name "
        + "HAVING COUNT(DISTINCT A.id) = :artistCount")
    List<SongNameInfo> findBySongTitleAndArtistNamesIn(@Param("title") String title, @Param("artistNames") List<String> artistNames, @Param("artistCount") int artistCount);
    List<SongArtist> findBySongId(Long songId);

    List<SongArtist> findAllBySong(Song song);
}
