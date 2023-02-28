package com.example.demo.configuration.Exception;

import com.example.demo.configuration.http.BaseResponseCode;

/**
 * 예외 처리 클래스
 * @author arsurei
 *
 */
public class BaseException extends AbstractBaseException {

	private static final long serialVersionUID = -4225335689196023191L;
	
	public BaseException() {
		
	}
	
	public BaseException(BaseResponseCode responseCode) {
		this.responseCode = responseCode;
	}
	
	public BaseException(BaseResponseCode responseCode, String args[]) {
		this.responseCode = responseCode;
		this.args = args;
	}

}
