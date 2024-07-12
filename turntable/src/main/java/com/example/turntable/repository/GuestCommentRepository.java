package com.example.turntable.repository;

import com.example.turntable.domain.GuestComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestCommentRepository extends JpaRepository<GuestComment, Long> {
    int deleteByDailyComment_Id(Long dailyCommentId);
    List<GuestComment> findByDailyCommentId(Long commentId);
    Page<GuestComment> findAllByDailyCommentId(Pageable pageable,Long commentId);
}
