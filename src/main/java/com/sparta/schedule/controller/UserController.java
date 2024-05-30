package com.sparta.schedule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.schedule.dto.LoginRequestDto;
import com.sparta.schedule.dto.SignupRequestDto;
import com.sparta.schedule.entity.Response;
import com.sparta.schedule.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	// 회원가입
	@PostMapping("/user/signup")
	public ResponseEntity<Response> signup(@Valid @RequestBody SignupRequestDto requestDto) {
		return userService.signup(requestDto);
	}

	// 로그인
	@PostMapping("/user/login")
	public ResponseEntity<Response> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
		return userService.login(requestDto, res);
	}
}
