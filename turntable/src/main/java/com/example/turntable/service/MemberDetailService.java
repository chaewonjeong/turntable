package com.example.turntable.service;

import com.example.turntable.auth.CustomOAuthDetails;
import com.example.turntable.domain.Member;
import com.example.turntable.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberDetailService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("Could not find user: " + username));

        System.out.println("Success find member {}"+member);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));

        return new User(member.getName(),member.getPassword(),grantedAuthorities);
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("id");
        String username = provider + "_" + providerId;

        // 네이버 사용자 정보의 'response' 객체 가져오기
        Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        String userId = (String) response.get("id");
        String nickname = (String) response.get("nickname");

        Optional<Member> member = memberRepository.findByName(userId);
        if(member.isEmpty()){
            Member newMember = new Member();
            newMember.setOAuth2User(userId,nickname,"https://kr.object.ncloudstorage.com/turntable-bgimg/0071b2b4-6cd7-4b5e-a424-faaa22db8be0-%EA%B8%B0%EB%B3%B8%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B45.jpg");
            memberRepository.save(newMember);
            return new CustomOAuthDetails(newMember,oAuth2User.getAttributes());
        }
        return new CustomOAuthDetails(member.get(),oAuth2User.getAttributes());
    }
}
