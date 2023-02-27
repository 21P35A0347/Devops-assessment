package com.example.demo.configuration.http;

/**
 * 응답 코드 종류
 * @author arsur
 *
 */
public enum BaseResponseCode {
	SUCCESS, // 성공
	ERROR, // 에러
	LOGIN_REQUIRED, // 로그인 필수
	DATA_IS_NULL,  // NULL
	VALIDATE_REQUIRED, // 필수 체크
	;
}
