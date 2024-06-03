package com.sparta.schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.schedule.dto.CommentRequestDto;
import com.sparta.schedule.dto.CommentResponseDto;
import com.sparta.schedule.dto.Response;
import com.sparta.schedule.jwt.JwtUtil;
import com.sparta.schedule.service.CommentService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

	private final CommentService commentService;
	private final JwtUtil jwtUtil;

	// 댓글 작성
	@PostMapping("/schedule/{scheduleId}/comment")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long scheduleId, @Valid @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
		String username = getUsernameFromRequest(req);
		CommentResponseDto responseDto = commentService.createComment(scheduleId, username, requestDto);
		return ResponseEntity.ok(responseDto);
	}

	// 댓글 수정
	@PutMapping("/schedule/{scheduleId}/comment/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long scheduleId, @PathVariable Long commentId, @Valid @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
		String username = getUsernameFromRequest(req);
		CommentResponseDto responseDto = commentService.updateComment(scheduleId, commentId, username, requestDto);
		return ResponseEntity.ok(responseDto);
	}

	// 댓글 삭제
	@DeleteMapping("/schedule/{scheduleId}/comment/{commentId}")
	public ResponseEntity<Response> deleteComment(@PathVariable Long scheduleId, @PathVariable Long commentId, HttpServletRequest req) {
		String username = getUsernameFromRequest(req);
		commentService.deleteComment(scheduleId, commentId, username);

		Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 삭제되었습니다.");
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
