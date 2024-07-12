package com.example.turntable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WriteGuestCommentDto {
    private Long commentId;
    private String comment;
    private String date;
    private Long guestId;
}
