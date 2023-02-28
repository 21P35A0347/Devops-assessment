package study.querydsl.configuration;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import study.querydsl.configuration.http.BaseCodeLabelEnum;
import study.querydsl.configuration.conf.GlobalProperties;
import study.querydsl.configuration.servlet.handler.BaseHandlerInterceptor;
import study.querydsl.framework.web.MySQLPageRequestHandleMethodArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Web관련 설정 메세지 다국어
 * 
 * @author arsurei
 *
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {

		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasename("classpath:/messages/message");
		source.setDefaultEncoding("UTF-8");
		source.setCacheSeconds(60);
		source.setDefaultLocale(Locale.KOREAN);
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule simplieModule = new SimpleModule();
		simplieModule.addSerializer(BaseCodeLabelEnum.class, new BaseCodeLabelEnumJsonSerializer());
		objectMapper.registerModule(simplieModule);
		return objectMapper;
	}
	
	@Bean
	public MappingJackson2JsonView mappingJackson2JsonView() {
		MappingJackson2JsonView jsonView  = new MappingJackson2JsonView();
		jsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);
		jsonView.setObjectMapper(objectMapper());
		return jsonView;
	}
	
	@Bean
	public BaseHandlerInterceptor baseHandlerInterceptor() {
		return new BaseHandlerInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(baseHandlerInterceptor());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new MySQLPageRequestHandleMethodArgumentResolver());
	}
	
	@Bean
	public GlobalConfig config() {
		return new GlobalConfig();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		GlobalConfig config = this.config();
		
		// 업로드 파일  static resource접근 검토
		String resourcePattern = config.getProperties(GlobalProperties.UPLOADFILE_RESOURCE_PATH)+"**";
		// 업로드 패스
		String uploadFile = config.getProperties(GlobalProperties.UPLOAD_PROPERTIES);
		// 윈도우 환경
		if (config.isLocal()) {
			registry.addResourceHandler(resourcePattern).addResourceLocations("file:///"+uploadFile);
		} else {
			// 리눅스/유닉스 환경
			registry.addResourceHandler(resourcePattern).addResourceLocations("file:"+uploadFile);
		}
	}

}
