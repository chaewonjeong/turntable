package com.example.turntable.repository;

import com.example.turntable.domain.DailyComment;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyCommentRepository  extends JpaRepository<DailyComment, Long> {
    Page<DailyComment> findAllByMemberId(Pageable pageable, Long memberId);
}
