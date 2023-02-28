package study.querydsl.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import study.querydsl.configuration.conf.GlobalProperties;

@Configuration
public class ScheduleConfiguration {

	@Autowired
	private GlobalConfig config ;
	
	@Bean
	public String scheduleCronExample1() {
		return config.getProperties(GlobalProperties.SCHEDULE_CRON_EXAMPLE1);
	}
}
