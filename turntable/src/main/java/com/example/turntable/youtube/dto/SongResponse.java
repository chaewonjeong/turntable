package com.example.turntable.youtube.dto;


import java.util.List;


public record SongResponse(String name, List<String> artists, String albumName, String youtubeUrl) {
}
