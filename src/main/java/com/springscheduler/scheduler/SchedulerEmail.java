package com.springscheduler.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.springscheduler.service.SchedulerService;

@Configuration
@EnableScheduling
public class SchedulerEmail {

	@Autowired
	SchedulerService schedulerService;

	@Scheduled(cron = "${pg.cron.expression.teenminute}")
	public void sendEmailEveryTenMinutes() {
		try {
			System.out.println("Test Scheduller Bro........");
			System.out.println("Test Scheduller Bro1........");
			System.out.println("Test Scheduller Bro2........");

			schedulerService.sendEmailEveryTenMinutes();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
