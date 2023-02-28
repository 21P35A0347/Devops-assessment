package study.querydsl.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * MySQL 페이지 요청정보 및 계산된 값
 * @author arsurei
 *
 */
@Data
public class MySqlPageRequest {
	private int page;
	private int size;
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private int limit;
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private int offset;
	
	public MySqlPageRequest(int page, int size, int limit, int offset) {
		this.page = page;
		this.size = size;
		this.limit = limit;
		this.offset = offset;
	}
}
