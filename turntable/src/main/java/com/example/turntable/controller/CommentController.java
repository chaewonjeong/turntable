package com.example.turntable.controller;

import com.example.turntable.dto.*;
import com.example.turntable.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public String writeComment(@RequestBody WriteDailyCommentDto writeDailyCommentDto, HttpSession session) {
        Long memberId = (Long) session.getAttribute("userId");
        commentService.createDailyComment(writeDailyCommentDto,memberId);
        return "redirect:/comment?pageOwnerId="+memberId;
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

    @PostMapping("/comment/guest")
    @ResponseBody
    public String writeGuestComment(@RequestBody WriteGuestCommentDto writeGuestCommentDto, HttpSession session) {
        Long memberId = (Long) session.getAttribute("userId");
        commentService.createGuestComment(writeGuestCommentDto);
        return "redirect:/comment?pageOwnerId="+memberId;
    }

    @GetMapping("/comments/guest")
    @ResponseBody
    public Page<GuestCommentResponseDto> getGuestComments(@RequestParam int page, Long commentId, HttpSession session){
        Long currentUserId = (Long) session.getAttribute("userId");
        return commentService.getGuestCommentsByPage(page, commentId, currentUserId);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseBody
    public void deleteGuestComment(@PathVariable Long commentId) {
        commentService.deleteGuestCommentById(commentId);
    }

    @PutMapping("/comment/guest")
    @ResponseBody
    public void updateGuestComment(@RequestBody UpdateGuestCommentDto updateGuestCommentDto) {
        commentService.updateGuestComment(updateGuestCommentDto);
    }
}
