package com.sparta.schedule.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserRoleEnum;
import com.sparta.schedule.exception.InvalidTokenException;
import com.sparta.schedule.exception.NotFoundException;
import com.sparta.schedule.repository.UserRepository;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

	//JWT 데이터
	// Header KEY 값
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESH_HEADER = "Refresh-Token";
	// 사용자 권한 값의 KEY
	public static final String AUTHORIZATION_KEY = "auth";
	// Token 식별자
	public static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
	private final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

	private final Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	// 로그 설정
	public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

	private final UserRepository userRepository;


	//JWT 생성 (토큰 생성)
	public String createAccessToken(String username, UserRoleEnum role) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(username) // 사용자 식별자값(ID)
				.claim(AUTHORIZATION_KEY, role) // 사용자 권한
				.setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
				.setIssuedAt(date) // 발급일
				.signWith(key, signatureAlgorithm) // 암호화 알고리즘
				.compact();
	}

	public String createRefreshToken(String username) {
		Date date = new Date();
		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	public String generateNewAccessToken(String refreshToken) {
		Claims claims = getUserInfoFromToken(refreshToken);
		String username = claims.getSubject();
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException("등록된 사용자가 없습니다.")
		);
		return createAccessToken(username, user.getRole());
	}

	public String checkToken(String accessToken, String refreshToken) {
		if (!validateToken(refreshToken)) {
			// Refresh Token 만료로 인한 인증 실패 처리 및 재로그인 유도
			throw new InvalidTokenException("Refresh Token이 만료되었거나 유효하지 않습니다. 다시 로그인하세요.");
		}

		if (!validateToken(accessToken)) {
			// Refresh Token을 사용하여 새로운 Access Token 생성
			return generateNewAccessToken(refreshToken);
		} else {
			// Refresh Token과 Access Token이 유효한 경우
			return accessToken;
		}
	}

	// 토큰 헤더에서 Access Token을 가져오는 메서드
	public String getAccessTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	// 토큰 헤더에서 Refresh Token을 가져오는 메서드
	public String getRefreshTokenFromHeader(HttpServletRequest request) {
		String refreshToken = request.getHeader(REFRESH_HEADER);
		if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(BEARER_PREFIX)) {
			return refreshToken.substring(7);
		}
		return null;
	}

	//JWT 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}


	//JWT에서 사용자 정보 가져오기
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

}
