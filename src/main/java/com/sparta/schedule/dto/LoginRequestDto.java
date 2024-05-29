package com.sparta.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
	@NotBlank(message = "사용자명이 입력되지 않았습니다.")
	private String username;
	@NotBlank(message = "비밀번호가 입력되지 않았습니다.")
	private String password;
}