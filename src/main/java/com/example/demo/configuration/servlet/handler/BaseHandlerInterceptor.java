package com.example.demo.configuration.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.example.demo.configuration.Exception.BaseException;
import com.example.demo.configuration.http.BaseResponseCode;
import com.example.demo.framework.web.annotation.RequestConfig;

@SuppressWarnings("deprecation")
public class BaseHandlerInterceptor extends HandlerInterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("preHandle requestURI : {}", request.getRequestURI());
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			logger.info("handlerMethod : {}", handlerMethod);
			RequestConfig requestConfig = handlerMethod.getMethodAnnotation(RequestConfig.class);
			if (requestConfig != null) {
				if (requestConfig.loginCheck()) {
					throw new BaseException(BaseResponseCode.LOGIN_REQUIRED);
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("postHandle requestURI : {}", request.getRequestURI());
	}

}
