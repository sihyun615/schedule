package com.sparta.schedule.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

	private Long scheduleId;
	private String content;
	private String userId;
}
