package com.sparta.schedule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtConfiguration {

	@Value("${jwt.secret.key}") // application.properties에서 설정한 비밀키 값
	private String secretKey;

	@Bean
	public Key jwtKey() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}
}

