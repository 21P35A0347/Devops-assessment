package com.example.demo.framework.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.demo.framework.domain.MySqlPageRequest;

public class MySQLPageRequestHandleMethodArgumentResolver implements HandlerMethodArgumentResolver {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String DEFAULT_PARAMETER_PAGE = "page";
	private static final String DEFAULT_PARAMETER_SIZE = "size";
	private static final int DEFAULT_SIZE = 20;
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// TODO Auto-generated method stub
		return MySqlPageRequest.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		String pageStr = webRequest.getParameter(DEFAULT_PARAMETER_PAGE);
		// 현재 페이지
		int page = NumberUtils.toInt(request.getParameter(DEFAULT_PARAMETER_PAGE), 1);
		// 리스트 개수
		int offset = NumberUtils.toInt(request.getParameter(DEFAULT_PARAMETER_SIZE), DEFAULT_SIZE);
		// 호출 페이지의 시작번호
		int limit = (offset * page)-offset;
		logger.info("page : {}", page);
		logger.info("offset : {}, limit : {}", offset, limit);
		return new MySqlPageRequest(page, page, limit, offset);
	}

}
