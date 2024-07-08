package com.example.turntable.repository;

import com.example.turntable.domain.PlayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayListRepository extends JpaRepository<PlayList, Long> {
    Optional<PlayList> findByMember_Id(Long memberId);
    int countByMember_Id(Long memberId);
}
