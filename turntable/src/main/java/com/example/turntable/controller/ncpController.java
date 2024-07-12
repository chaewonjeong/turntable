package com.example.turntable.controller;

import com.example.turntable.service.MemberService;
import com.example.turntable.service.NcpService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ncpController {

    @Autowired
    private final NcpService ncpService;
    @Autowired
    private final MemberService memberService;


    @GetMapping("image/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        try {
            byte[] imageData = ncpService.getImgObject(imageName);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+imageName+"\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
        }catch (IOException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/imgurl")
    public ResponseEntity<String> getUserImageUrl(@RequestParam Long pageOwnerId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String userBgimg = memberService.getUserBgImg(pageOwnerId);
        return ResponseEntity.ok(userBgimg);
    }
}

