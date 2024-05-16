package com.sparta.schedule.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
	private String title;
	private String content;
	private String manager;
	private String password;
}
