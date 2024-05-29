package com.sparta.schedule.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sparta.schedule.dto.LoginRequestDto;
import com.sparta.schedule.dto.SignupRequestDto;
import com.sparta.schedule.entity.Response;
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


	// ADMIN_TOKEN
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

	public ResponseEntity<Response> signup(SignupRequestDto requestDto) {
		String username = requestDto.getUsername();
		String password = requestDto.getPassword();

		// 회원 중복 확인
		Optional<User> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		// 별명 중복확인
		String nickname = requestDto.getNickname();
		Optional<User> checkNickname = userRepository.findByNickname(nickname);
		if (checkNickname.isPresent()) {
			throw new IllegalArgumentException("중복된 별명입니다.");
		}

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
		userRepository.save(user);

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

		String token = jwtUtil.createToken(user.getUsername(), user.getRole());

		// JWT를 헤더에 추가
		res.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);


		Response response = new Response(HttpStatus.OK.value(), "로그인이 성공적으로 완료되었습니다.");
		return ResponseEntity.ok(response);
	}
}

