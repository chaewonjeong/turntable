package com.example.turntable.controller;

import com.example.turntable.dto.CommentResponseDto;
import com.example.turntable.dto.WriteDailyCommentDto;
import com.example.turntable.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public String writeComment(@RequestBody WriteDailyCommentDto writeDailyCommentDto, HttpSession session) {
        Long memberId = (Long) session.getAttribute("userId");
        commentService.create(writeDailyCommentDto,memberId);
        return "redirect:/comment";
    }

    @GetMapping("/comments")
    @ResponseBody
    public Page<CommentResponseDto> getDailyComments(@RequestParam int page, Long memberId){
        return commentService.getCommentsByPage(page,memberId);
    }

    @GetMapping("/comment/latest")
    @ResponseBody
    public CommentResponseDto getLatestComment(@RequestParam Long memberId){
        return commentService.getLatestComment(memberId);
    }
}
