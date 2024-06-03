package com.sparta.schedule.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sparta.schedule.dto.LoginRequestDto;
import com.sparta.schedule.dto.SignupRequestDto;
import com.sparta.schedule.dto.Response;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserRoleEnum;
import com.sparta.schedule.exception.InvalidPasswordException;
import com.sparta.schedule.exception.NotFoundException;
import com.sparta.schedule.jwt.JwtUtil;
import com.sparta.schedule.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	// application.properties에서 ADMIN_TOKEN 값 가져오기
	@Value("${admin.token}")
	private String ADMIN_TOKEN;

	public ResponseEntity<Response> signup(SignupRequestDto requestDto) {
		String username = requestDto.getUsername();
		String nickname = requestDto.getNickname();
		String password = requestDto.getPassword();

		// 사용자 ROLE 확인
		UserRoleEnum role = UserRoleEnum.USER;
		if (requestDto.isAdmin()) {
			if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
				throw new InvalidPasswordException("관리자 암호가 틀려 등록이 불가능합니다.");
			}
			role = UserRoleEnum.ADMIN;
		}

		// 사용자 등록
		User user = new User(username, nickname, password, role);
		try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("constraint [uk_username]")) {
				// 사용자 이름이 중복될 경우
				throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
			} else if (e.getMessage().contains("constraint [uk_nickname]")) {
				// 별명이 중복될 경우
				throw new IllegalArgumentException("중복된 별명입니다.");
			} else {
				// 기타 데이터베이스 제약 조건 위반
				throw e;
			}
		}

		Response response = new Response(HttpStatus.OK.value(), "회원가입이 성공적으로 완료되었습니다.");
		return ResponseEntity.ok(response);  // 성공 메시지와 상태 코드 반환
	}

	public ResponseEntity<Response> login(LoginRequestDto requestDto, HttpServletResponse res) {
		String username = requestDto.getUsername();
		String password = requestDto.getPassword();

		// 사용자 확인
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException("등록된 사용자가 없습니다.")
		);

		// 비밀번호 확인
		if (!password.equals(user.getPassword())) {
			throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
		}

		String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
		String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

		res.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		res.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);

		Response response = new Response(HttpStatus.OK.value(), "로그인이 성공적으로 완료되었습니다.");
		return ResponseEntity.ok(response);
	}
}

