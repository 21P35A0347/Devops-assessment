package study.querydsl.configuration;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import study.querydsl.configuration.conf.GlobalProperties;

public class GlobalConfig {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ResourceLoader resourceLoader;
		
	private Properties properties; 

	@PostConstruct
	public void init() {
		logger.info(getClass().getName() + " Init");
		// JVM 파라메타에서 넘겨준 값을 가져옴
		String[] activeProfiles = this.context.getEnvironment().getActiveProfiles();
		
		String activeProfile = "local";
		// 넘겨준 값이 있는경우
		if (ObjectUtils.isNotEmpty(activeProfiles)) {
			activeProfile = activeProfiles[0];
		}
		// 환경변수 파일 위치
		String resourcePath = String.format("classpath:globals/globals-%s.properties", activeProfile);
		
		try {
			Resource resource = (Resource) resourceLoader.getResource(resourcePath);
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (Exception e) {
			logger.error(getClass().getName() + "init Exception", e);
		}
	}
	
	/**
	 * globals.properties의 값을 가져오는 클래스
	 *  
	 * @param key
	 * @return
	 */
	public String getProperties(String key) {
		return this.properties.getProperty(key);
	}
	
	public boolean isLocal() {
		if (this.getProperties(GlobalProperties.MODE) == "local") {
			return true;
		} 
		return false;
	}
}
