package com.sparta.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
	@NotBlank(message = "사용자명이 입력되지 않았습니다.")
	private String username;
	@NotBlank(message = "별명이 입력되지 않았습니다.")
	private String nickname;
	@NotBlank(message = "비밀번호가 입력되지 않았습니다.")
	private String password;

	private boolean admin = false;
	private String adminToken = "";
}
