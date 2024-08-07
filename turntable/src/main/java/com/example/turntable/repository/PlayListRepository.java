package com.example.turntable.repository;

import com.example.turntable.domain.Member;
import com.example.turntable.domain.PlayList;
import com.example.turntable.domain.PlayListStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlayListRepository extends JpaRepository<PlayList, Long> {
    List<PlayList> findByMember_Id(Long memberId);
    int countByMember_Id(Long memberId);
    List<PlayList> findAllByMemberAndState(Member member, PlayListStatus status);
}
