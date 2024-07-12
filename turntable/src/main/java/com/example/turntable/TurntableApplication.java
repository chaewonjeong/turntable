package com.example.turntable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class TurntableApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurntableApplication.class, args);
	}

	@GetMapping("/todayplaylist")
	public String todayplaylist() {
		return "todayPlaylist";
	}

	@GetMapping("/main")
	public String main() {
		return "main";
	}

	@GetMapping("/login")
	public String loginForm() {
		System.out.println("loginForm");
		return "loginForm";
	}

	@GetMapping("/signup")
	public String signupForm() {
		return "signup";
	}

	@GetMapping("/comment")
	public String commentForm() {
		return "comment";
	}

	@GetMapping("/users")
	public String getUsers() {
		return "users";
	}

	@GetMapping("/mypage")
	public String myPageFrom() {
		return "mypage";
	}

	@GetMapping("/")
	public String index() {
		return "start";
	}
}
