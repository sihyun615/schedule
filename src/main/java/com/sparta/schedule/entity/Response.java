package com.sparta.schedule.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	private int status;
	private String message;

	public Response(int status, String message) {
		this.status = status;
		this.message = message;
	}
}

