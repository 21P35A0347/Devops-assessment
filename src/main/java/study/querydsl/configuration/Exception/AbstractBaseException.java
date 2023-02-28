package study.querydsl.configuration.Exception;

import study.querydsl.configuration.http.BaseResponseCode;


public abstract class AbstractBaseException extends RuntimeException {

	private static final long serialVersionUID = -6099994421611056790L;
	
	protected BaseResponseCode responseCode;
	  protected Object[] args;
	  
	  public AbstractBaseException() {
	  }
	  
	  public AbstractBaseException(BaseResponseCode responseCode) {
	  	this.responseCode = responseCode;
	  }
	  
	  public BaseResponseCode getResponseCode() {
	  	return responseCode;
	  }
	  
	  public Object[] getArgs() {
	  	return args;
	  }
}
