package com.sparta.schedule.dto;

import java.time.LocalDateTime;

import com.sparta.schedule.entity.Schedule;

import lombok.Getter;

@Getter
public class ScheduleResponseDto {
	private Long id;
	private String title;
	private String content;
	private String manager;
	private LocalDateTime createdAt;

	public ScheduleResponseDto(Schedule schedule) {
		this.id = schedule.getId();
		this.title = schedule.getTitle();
		this.content = schedule.getContent();
		this.manager = schedule.getManager();
		this.createdAt = schedule.getCreatedAt();
	}
}
