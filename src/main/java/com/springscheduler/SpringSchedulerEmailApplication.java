package com.springscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SpringSchedulerEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSchedulerEmailApplication.class, args);
	}
}
