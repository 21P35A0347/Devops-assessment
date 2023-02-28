package study.querydsl.configuration.http;

import lombok.Data;

/**
 * 공통 응답 클래스
 * @author arsurei
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> {
	private BaseResponseCode code;
	private String message ;
	private T data;
	
	public BaseResponse(T data) {
		this.code = BaseResponseCode.SUCCESS;
		this.data = data;
	}
	public BaseResponse(BaseResponseCode code, String message) {
		this.code = code;
		this.message = message;
	}
}
