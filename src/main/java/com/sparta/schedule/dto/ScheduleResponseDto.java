package com.sparta.schedule.dto;

import java.time.LocalDateTime;

import com.sparta.schedule.entity.Schedule;

import lombok.Getter;

@Getter
public class ScheduleResponseDto {
	private Long id;
	private String title;
	private String content;
	private String username;
	private LocalDateTime createdAt;

	public ScheduleResponseDto(Schedule schedule) {
		this.id = schedule.getId();
		this.title = schedule.getTitle();
		this.content = schedule.getContent();
		this.username = schedule.getUser().getUsername();
		this.createdAt = schedule.getCreatedAt();
	}
}
