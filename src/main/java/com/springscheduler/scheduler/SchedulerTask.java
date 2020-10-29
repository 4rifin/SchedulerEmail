package com.springscheduler.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.springscheduler.service.SchedulerService;

@Service
@Component
public class SchedulerTask {
	@Autowired
	SchedulerService schedulerService;

	@Scheduled(cron = "${pg.cron.expression.teenminute}")
	public void sendEmailEveryTenMinutes() {
		try {
			System.out.println("Test Scheduller Bro........ ");
			schedulerService.sendEmailEveryTenMinutes();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
