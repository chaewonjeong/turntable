package com.example.turntable.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GuestCommentDto {
    private Long commentId;
    private String comment;
    private LocalDateTime date;
    private Long guestId;
    private int commentCount;
}
