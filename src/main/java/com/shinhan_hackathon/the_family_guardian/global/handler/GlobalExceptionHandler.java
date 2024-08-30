package com.shinhan_hackathon.the_family_guardian.global.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handler(Exception exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}
}
