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
import com.sparta.schedule.entity.Response;
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
	@PostMapping("/comment")
	public ResponseEntity<Response> createComment(@Valid @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
		// Access Token과 Refresh Token을 각각의 헤더에서 가져옴
		String accessToken = jwtUtil.getAccessTokenFromHeader(req);
		String refreshToken = jwtUtil.getRefreshTokenFromHeader(req);

		String newAccessToken = jwtUtil.checkToken(accessToken, refreshToken);

		Claims claims = jwtUtil.getUserInfoFromToken(newAccessToken);
		String username = claims.getSubject();

		// 댓글 생성 또는 수정
		commentService.createComment(username, requestDto);

		// 응답 반환
		Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 등록되었습니다.");
		return ResponseEntity.ok(response);
	}

	// 댓글 수정
	@PutMapping("/comment/{commentId}")
	public ResponseEntity<Response> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
		// Access Token과 Refresh Token을 각각의 헤더에서 가져옴
		String accessToken = jwtUtil.getAccessTokenFromHeader(req);
		String refreshToken = jwtUtil.getRefreshTokenFromHeader(req);

		String newAccessToken = jwtUtil.checkToken(accessToken, refreshToken);

		Claims claims = jwtUtil.getUserInfoFromToken(newAccessToken);
		String username = claims.getSubject();

		// 댓글 생성 또는 수정
		commentService.updateComment(commentId, username, requestDto);

		// 응답 반환
		Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 수정되었습니다.");
		return ResponseEntity.ok(response);
	}

	// 댓글 삭제
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<Response> deleteComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
		// Access Token과 Refresh Token을 각각의 헤더에서 가져옴
		String accessToken = jwtUtil.getAccessTokenFromHeader(req);
		String refreshToken = jwtUtil.getRefreshTokenFromHeader(req);

		String newAccessToken = jwtUtil.checkToken(accessToken, refreshToken);

		Claims claims = jwtUtil.getUserInfoFromToken(newAccessToken);
		String username = claims.getSubject();

		// 댓글 삭제
		commentService.deleteComment(commentId, username, requestDto);

		// 응답 반환
		Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 삭제되었습니다.");
		return ResponseEntity.ok(response);
	}
}
