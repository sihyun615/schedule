package com.sparta.schedule.controller;

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
import com.sparta.schedule.entity.Response;
import com.sparta.schedule.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

	private final CommentService commentService;

	// 댓글 작성
	@PostMapping("/comment")
	public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto) {
		return commentService.createComment(requestDto);
	}

	// 댓글 수정
	@PutMapping("/comment/{commentId}")
	public CommentResponseDto updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
		return commentService.updateComment(commentId, requestDto);
	}

	// 댓글 삭제
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<Response> deleteComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
		return commentService.deleteComment(commentId, requestDto);
	}
}
