package com.sparta.schedule.dto;

import java.time.LocalDateTime;

import com.sparta.schedule.entity.Comment;

import lombok.Getter;

@Getter
public class CommentResponseDto {

	private Long id;
	private String content;
	private String username;
	private Long scheduleId;
	private LocalDateTime createdAt;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.username = comment.getUser().getUsername();
		this.scheduleId = comment.getSchedule().getId();
		this.createdAt = comment.getCreatedAt();
	}
}
