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
import com.sparta.schedule.exception.InvalidTokenException;
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
		String token = jwtUtil.getJwtFromHeader(req);
		if (token != null && jwtUtil.validateToken(token)) {
			Claims claims = jwtUtil.getUserInfoFromToken(token);
			String username = claims.getSubject();
			commentService.createComment(username, requestDto);

			Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 작성되었습니다.");
			return ResponseEntity.ok(response);  // 성공 메시지와 상태 코드 반환
		} else {
			throw new InvalidTokenException("토큰이 유효하지 않습니다.");
		}
	}

	// 댓글 수정
	@PutMapping("/comment/{commentId}")
	public ResponseEntity<Response> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
		String token = jwtUtil.getJwtFromHeader(req);
		if (token != null && jwtUtil.validateToken(token)) {
			Claims claims = jwtUtil.getUserInfoFromToken(token);
			String username = claims.getSubject();
			commentService.updateComment(commentId, username, requestDto);

			Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 수정되었습니다.");
			return ResponseEntity.ok(response);  // 성공 메시지와 상태 코드 반환
		} else {
			throw new InvalidTokenException("토큰이 유효하지 않습니다.");
		}
	}

	// 댓글 삭제
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<Response> deleteComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
		String token = jwtUtil.getJwtFromHeader(req);
		if (token != null && jwtUtil.validateToken(token)) {
			Claims claims = jwtUtil.getUserInfoFromToken(token);
			String username = claims.getSubject();
			commentService.deleteComment(commentId, username, requestDto);

			Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 삭제되었습니다.");
			return ResponseEntity.ok(response);  // 성공 메시지와 상태 코드 반환
		} else {
			throw new InvalidTokenException("토큰이 유효하지 않습니다.");
		}
	}
}
