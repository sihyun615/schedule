package com.sparta.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
	@NotBlank(message = "일정의 이름이 비어있습니다.")
	private String title;
	private String content;
}
