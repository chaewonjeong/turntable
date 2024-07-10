package com.example.turntable.controller;

import com.example.turntable.dto.MemberInfoResponseDto;
import com.example.turntable.dto.SignupRequestDto;
import com.example.turntable.service.MemberService;
import com.example.turntable.service.PlayListService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;
    private final PlayListService playListService;

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequestDto signupRequestDto, RedirectAttributes redirectAttributes) throws IOException {
        if(memberService.create(signupRequestDto)){
            return "redirect:/login";
        }
        else{
            return "회원가입 실패";
        }
    }

    @GetMapping("/check-username")
    @ResponseBody
    public ResponseEntity<Map<String,Boolean>> checkUsername(@RequestParam("name") String username){
        boolean available = memberService.isUsernameExist(username);
        Map<String,Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login-success")
    public String loginSuccess(HttpSession session) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = memberService.getUserIdByName(username);
        session.setAttribute("username", username);
        session.setAttribute("userId",userId);
        return "redirect:/main?pageOwnerId="+userId;
    }

    @GetMapping("/username")
    @ResponseBody
    public MemberInfoResponseDto findUserById(@RequestParam Long memberId){
        return memberService.getUserById(memberId);
    }

    @GetMapping("/users/all")
    @ResponseBody
    public Page<MemberInfoResponseDto> findAllUsers(@RequestParam int page){
        return memberService.getAllUsersInfo(page);
    }

    @GetMapping("/users/nickname")
    @ResponseBody
    public Page<MemberInfoResponseDto> findAllUsersByName(@RequestParam int page, @RequestParam String name){
        return memberService.getAllUsersInfoByName(page, name);
    }

    @GetMapping("/user/playlist-count")
    @ResponseBody
    public ResponseEntity<Integer> findAllUsersByName(@RequestParam Long userId){
        int playlistCount = playListService.getPlaylistCount(userId);
        return ResponseEntity.ok(playlistCount);
    }

    @PostMapping("user/change-username")
    public String changeUsername(@RequestBody String newUsername, HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        memberService.changeUserName(userId,newUsername);
        session.setAttribute("username", newUsername);
        return "redirect:/main?pageOwnerId="+userId;
    }

    @PostMapping("user/change-bgimg")
    public String changeBgImg(@RequestBody MultipartFile newBgImg, HttpSession session)
        throws IOException {
        Long userId = (Long) session.getAttribute("userId");
        memberService.changeBgImg(userId,newBgImg);
        return "redirect:/main?pageOwnerId="+userId;
    }

    @PostMapping("/withdraw")
    public String deleteUser(HttpSession session) throws Exception {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // 세션에 사용자 ID가 없는 경우 로그인 페이지로 리다이렉트
        }

        boolean res = memberService.deleteUserInfo(userId);

        if (res) {
            session.invalidate(); // 탈퇴 성공 시 세션 무효화
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        } else {
            return "redirect:/error"; // 탈퇴 실패 시 에러 페이지로 리다이렉트
        }
    }
}
