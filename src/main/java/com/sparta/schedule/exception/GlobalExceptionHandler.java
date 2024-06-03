package com.sparta.schedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sparta.schedule.dto.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Response> handleNotFoundException(NotFoundException ex) {
		Response response = new Response(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Response> handleInvalidPasswordException(InvalidPasswordException ex) {
		Response response = new Response(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Response> handleInvalidTokenException(InvalidTokenException ex) {
		Response response = new Response(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
}