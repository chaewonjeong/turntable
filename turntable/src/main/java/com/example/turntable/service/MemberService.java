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
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final NcpService ncpService;
    private final PasswordEncoder passwordEncoder;

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
    public Page<MemberInfoResponseDto> getUsersInfo(int page){
        int pageSize = 9;
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        Page<Member> members = memberRepository.findAll(pageRequest);

        return members.map(member -> {
            return new MemberInfoResponseDto(
                member.getId(),
                member.getName(),
                member.getBackGroundImage()
            );}
        );
    }
    public Long getUserIdByName(String username){
        return memberRepository.findByName(username).map(Member::getId).orElse(null);
    }

    public boolean isUsernameExist(String username) {
        return memberRepository.findByName(username).isEmpty();
    }

    public String getUserBgImg(String username) {
        Optional<Member> member = memberRepository.findByName(username);
        return member.get().getBackGroundImage();
    }
}
