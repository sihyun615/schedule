package com.sparta.schedule.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.dto.Response;
import com.sparta.schedule.jwt.JwtUtil;
import com.sparta.schedule.service.ScheduleService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor // final로 선언된 멤버 변수를 파라미터로 사용하여 생성자를 자동으로 생성
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final JwtUtil jwtUtil;

	@PostMapping("/schedule")
	public ResponseEntity<Response> createSchedule(@RequestBody ScheduleRequestDto requestDto, HttpServletRequest req) {
		String username = getUsernameFromRequest(req);
		scheduleService.createSchedule(username, requestDto);

		Response response = new Response(HttpStatus.OK.value(), "일정이 성공적으로 등록되었습니다.");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/schedule/{scheduleId}")
	public ScheduleResponseDto getScheduleById(@PathVariable Long scheduleId) {
		return scheduleService.getScheduleById(scheduleId);
	}

	@GetMapping("/schedules")
	public List<ScheduleResponseDto> getSchedules() {
		return scheduleService.getSchedules();
	}

	@PutMapping("/schedule/{scheduleId}")
	public ResponseEntity<Response> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto requestDto, HttpServletRequest req) {
		String username = getUsernameFromRequest(req);
		scheduleService.updateSchedule(username, scheduleId, requestDto);

		Response response = new Response(HttpStatus.OK.value(), "일정이 성공적으로 수정되었습니다.");
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/schedule/{scheduleId}")
	public ResponseEntity<Response> deleteSchedule(@PathVariable Long scheduleId, HttpServletRequest req) {
		String username = getUsernameFromRequest(req);
		scheduleService.deleteSchedule(username, scheduleId);

		Response response = new Response(HttpStatus.OK.value(), "일정이 성공적으로 삭제되었습니다.");
		return ResponseEntity.ok(response);
	}

	private String getUsernameFromRequest(HttpServletRequest req) {
		// Access Token과 Refresh Token을 각각의 헤더에서 가져옴
		String accessToken = jwtUtil.getAccessTokenFromHeader(req);
		String refreshToken = jwtUtil.getRefreshTokenFromHeader(req);
		String newAccessToken = jwtUtil.checkToken(accessToken, refreshToken);
		Claims claims = jwtUtil.getUserInfoFromToken(newAccessToken);
		return claims.getSubject();
	}
}
