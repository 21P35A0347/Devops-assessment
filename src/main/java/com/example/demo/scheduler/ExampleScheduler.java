package com.example.demo.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExampleScheduler {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Scheduled(cron = "#{@scheduleCronExample1}")
	public void scheduler () {
		logger.info("스케줄 동작중");
	}

}
