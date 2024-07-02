package com.example.turntable.repository;

import com.example.turntable.domain.GuestComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestCommentRepository extends JpaRepository<GuestComment, Long> {
    List<GuestComment> findByDailyCommentId(Long commnetId);
}
