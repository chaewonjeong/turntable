package com.example.turntable.service;

import com.example.turntable.domain.DailyComment;
import com.example.turntable.domain.Member;
import com.example.turntable.dto.CommentResponseDto;
import com.example.turntable.dto.WriteDailyCommentDto;
import com.example.turntable.repository.DailyCommentRepository;
import com.example.turntable.repository.GuestCommentRepository;
import com.example.turntable.repository.MemberRepository;
import com.example.turntable.spotify.SpotifyService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final DailyCommentRepository dailycommentRepository;
    private final GuestCommentRepository guestCommentRepository;
    private final MemberRepository memberRepository;
    private final SpotifyService spotifyService;

    @Transactional
    public void create(WriteDailyCommentDto writeDailyCommentDto, Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(writeDailyCommentDto.getDate(), formatter);

        final DailyComment dailyComment = DailyComment.builder()
            .comment(writeDailyCommentDto.getComment())
            .createdAt(date)
            //.spotifySongId(writeDailyCommentDto.getSpotifySongId())
            .member(memberOptional.get())
            .build();
        dailycommentRepository.save(dailyComment);
    }

    /*public Page<CommentResponseDto> getCommentsByPage(int page,Long memberId){
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        Page<DailyComment> comments = dailycommentRepository.findAll(pageRequest);

        return comments.map(comment -> {
            int count = guestCommentRepository.findByDailyCommentId(comment.getId()).size();
            //Map<String,String> trackInfo = spotifyService.getTrackInfo(comment.getSpotifySongId());
            return new CommentResponseDto(
                comment.getId(),
                comment.getComment(),
                comment.getCreatedAt(),
                trackInfo.get("title"),
                trackInfo.get("artist"),
                count
            );
        });
    }

    public CommentResponseDto getLatestComment(Long memberId){
        Optional<DailyComment> comment = dailycommentRepository.findFirstByMember_IdOrderByCreatedAtDesc(memberId);
        Map<String,String> trackInfo = spotifyService.getTrackInfo(comment.get().getSpotifySongId());
        return new CommentResponseDto(
            comment.get().getId(),
            comment.get().getComment(),
            comment.get().getCreatedAt(),
            trackInfo.get("title"),
            trackInfo.get("artist"),
            guestCommentRepository.findByDailyCommentId(comment.get().getId()).size()
        );
    }*/
}
