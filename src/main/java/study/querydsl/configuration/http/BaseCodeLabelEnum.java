package study.querydsl.configuration.http;

/**
 * 기본 CodeLabelEnum
 * @author arsurei
 *
 */
public interface BaseCodeLabelEnum {
	/**
	 * 코드를 리턴
	 * @return
	 */
	String code();
	
	/**
	 * 라벨을 리턴
	 * @return
	 */
	String label();
}
