package com.example.turntable.repository;

import com.example.turntable.domain.DailyComment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyCommentRepository  extends JpaRepository<DailyComment, Long> {
    List <DailyComment> findByMember_Id(Long memberId);
    int deleteByMember_Id(Long memberId);
    DailyComment findFirstByMember_IdOrderByCreatedAtDesc(Long memberId);
    Page<DailyComment> findAllByMemberId(Pageable pageable, Long memberId);
}
