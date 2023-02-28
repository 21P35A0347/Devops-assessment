package study.querydsl.framework.domain;

import lombok.Data;

/**
 * 페이지 요청정보와 파라메터 정보.
 * @author arsurei
 * @param <T>
 */
@Data
public class PageRequestParameter<T> {

	private MySqlPageRequest pageRequest;
	private T parameter;
	
	public PageRequestParameter(MySqlPageRequest pageRequest, T parameter) {
		this.pageRequest = pageRequest;
		this.parameter = parameter;
	}
}