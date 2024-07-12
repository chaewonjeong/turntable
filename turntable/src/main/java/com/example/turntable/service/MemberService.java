package com.example.turntable.service;

import com.example.turntable.domain.Member;
import com.example.turntable.dto.MemberInfoResponseDto;
import com.example.turntable.dto.SignupRequestDto;
import com.example.turntable.repository.MemberRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final NcpService ncpService;
    private final PasswordEncoder passwordEncoder;
    private final PlayListService playListService;
    private final CommentService commentService;

    @Transactional
    public boolean create(SignupRequestDto signupRequestDto) throws IOException {
        Optional<Member> existingUser = memberRepository.findByName(signupRequestDto.getName());
        if (existingUser.isPresent()) {
            return false;
        }

        final Member member = Member.builder()
            .name(signupRequestDto.getName())
            .nickname(signupRequestDto.getNickname())
            .password(passwordEncoder.encode(signupRequestDto.getPassword()))
            .backGroundImage(ncpService.uploadFile(signupRequestDto.getBgImg()))
            .build();
        memberRepository.save(member);
        return true;
    }

    public boolean deleteUserInfo(Long userId) throws Exception {
        playListService.deleteAllPlaylistByMemberId(userId);
        commentService.deleteDailyCommentByMemberId(userId);
        deleteUser(userId);
        return true;
    }

    @Transactional
    protected boolean deleteUser(Long userId){
        Optional<Member> member = memberRepository.findById(userId);
        memberRepository.delete(member.get());
        return true;
    }

    public Page<MemberInfoResponseDto> getAllUsersInfo(int page){
        int pageSize = 9;
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        Page<Member> members = memberRepository.findAll(pageRequest);

        return members.map(member -> {
            int playlistCount = playListService.getPlaylistCount(member.getId());
            return MemberInfoResponseDto.of(member,playlistCount);
        }
        );
    }
    public Page<MemberInfoResponseDto> getAllUsersInfoByName(int page, String name){
        int pageSize = 9;
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        Page<Member> members = memberRepository.findByNameContaining(name,pageRequest);

        return members.map(member -> {
            int playlistCount = playListService.getPlaylistCount(member.getId());
            return MemberInfoResponseDto.of(member,playlistCount);
        });
    }

    public MemberInfoResponseDto getUserById(Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
           Member member = optionalMember.get();
           int playlistCount = playListService.getPlaylistCount(member.getId());
            return MemberInfoResponseDto.of(member,playlistCount);
        }
        else{
            return null;
        }
    }
    public Long getUserIdByName(String username){
        return memberRepository.findByName(username).map(Member::getId).orElse(null);
    }

    public boolean isUsernameExist(String username) {
        return memberRepository.findByName(username).isEmpty();
    }

    public String getUserBgImg(Long username) {
        Optional<Member> member = memberRepository.findById(username);
        return member.get().getBackGroundImage();
    }

    @Transactional
    public String changeUserName(Long userId, String newMemberName) {
        Member member = memberRepository.findById(userId).orElse(null);
        member.changeNickname(newMemberName);
        return newMemberName;
    }

    @Transactional
    public String changeBgImg(Long userId, MultipartFile newBgImg) throws IOException {
        Member member = memberRepository.findById(userId).orElse(null);
        String bgImgUrl = ncpService.uploadFile(newBgImg);
        member.changeBackGroundImage(bgImgUrl);
        return bgImgUrl;
    }
}
