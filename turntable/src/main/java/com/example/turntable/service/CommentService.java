package com.example.turntable.service;

import com.example.turntable.domain.DailyComment;
import com.example.turntable.domain.GuestComment;
import com.example.turntable.domain.Member;
import com.example.turntable.domain.PlayList;
import com.example.turntable.domain.Song;
import com.example.turntable.dto.CommentResponseDto;
import com.example.turntable.dto.GuestCommentResponseDto;
import com.example.turntable.dto.WriteDailyCommentDto;
import com.example.turntable.dto.WriteGuestCommentDto;
import com.example.turntable.repository.DailyCommentRepository;
import com.example.turntable.repository.GuestCommentRepository;
import com.example.turntable.repository.MemberRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final DailyCommentRepository dailycommentRepository;
    private final GuestCommentRepository guestCommentRepository;
    private final MemberRepository memberRepository;
    private final SongArtistService songArtistService;

    @Transactional
    public void create(WriteDailyCommentDto writeDailyCommentDto, Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(writeDailyCommentDto.getDate(), formatter);
        Optional<Song> song = songArtistService.findSongByTitleAndArtist(writeDailyCommentDto.getTitle(),writeDailyCommentDto.getArtists());

        final DailyComment dailyComment = DailyComment.builder()
            .comment(writeDailyCommentDto.getComment())
            .createdAt(date)
            .song(song.get())
            .member(memberOptional.get())
            .build();
        dailycommentRepository.save(dailyComment);
    }

    @Transactional
    public void createGuestComment(WriteGuestCommentDto writeGuestCommentDto) {
        Optional<Member> guest = memberRepository.findById(writeGuestCommentDto.getGuestId());
        Optional<DailyComment> dailyComment = dailycommentRepository.findById(writeGuestCommentDto.getCommentId());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(writeGuestCommentDto.getDate(),formatter);

        final GuestComment guestComment = GuestComment.builder()
            .comment(writeGuestCommentDto.getComment())
            .createdAt(date)
            .dailyComment(dailyComment.get())
            .visitorMember(guest.get())
            .build();
        guestCommentRepository.save(guestComment);
    }

    public Page<CommentResponseDto> getCommentsByPage(int page,Long memberId){
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(page,pageSize,Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DailyComment> comments = dailycommentRepository.findAllByMemberId(pageRequest,memberId);

        return comments.map(comment -> {
            return new CommentResponseDto(
                comment.getId(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getSong().getName(),
                songArtistService.findArtistsBySong(comment.getSong().getId()).stream()
                    .map(artist ->{
                        return artist.getName();
                    }).collect(Collectors.toList()),
                guestCommentRepository.findByDailyCommentId(comment.getId()).size()
            );
        });
    }

    public CommentResponseDto getLatestComment(Long memberId){
        DailyComment comment = dailycommentRepository.findFirstByMember_IdOrderByCreatedAtDesc(memberId);

        return new CommentResponseDto(
            comment.getId(),
            comment.getComment(),
            comment.getCreatedAt(),
            comment.getSong().getName(),
            songArtistService.findArtistsBySong(comment.getSong().getId()).stream()
                .map(artist ->{
                    return artist.getName();
                }).collect(Collectors.toList()),
            guestCommentRepository.findByDailyCommentId(comment.getId()).size()
        );
    }

    public Page<GuestCommentResponseDto> getGuestCommentsByPage(int page,Long dailyCommentId){
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(page,pageSize,Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GuestComment> comments = guestCommentRepository.findAllByDailyCommentId(pageRequest,dailyCommentId);

        return comments.map(comment -> {
            return GuestCommentResponseDto.from(comment);
        });
    }

    @Transactional
    public boolean deleteGuestCommentByCommnetId(Long commentId){
        return guestCommentRepository.deleteByDailyComment_Id(commentId);
    }

    @Transactional
    public boolean deleteDailyCommentByMemberId(Long memberId) {
        List<DailyComment> dailyCommentOptional = dailycommentRepository.findByMember_Id(memberId);
        dailyCommentOptional.stream()
            .map(DailyComment::getId)
            .forEach(dailyCommentId -> {
                deleteGuestCommentByCommnetId(dailyCommentId);
            });
        dailycommentRepository.deleteByMember_Id(memberId);
        return true;
    }
}
