package com.sparta.schedule.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
	@NotNull(message = "일정의 ID가 입력되지 않았습니다.")
	private Long scheduleId;
	@NotBlank(message = "댓글의 내용이 비어있습니다.")
	private String content;
	@NotBlank(message = "작성자 ID가 입력되지 않았습니다.")
	private String userId;
}
