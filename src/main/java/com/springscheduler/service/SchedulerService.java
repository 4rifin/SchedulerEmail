package com.springscheduler.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springscheduler.constant.UserStatus;
import com.springscheduler.dao.UserDao;
import com.springscheduler.model.User;

@Service
@Transactional
public class SchedulerService {
	@Value("${REST.SERVICE.URI}")
    public String REST_SERVICE_URI;
	
	@Autowired
	UserDao userDao;
	@Autowired
	UserService userService;
	@Autowired
	EmailService emailService;
	
	public void sendEmailEveryTenMinutes(){
		List<User> user = userService.findAllUserActive(String.valueOf(UserStatus.MEMBER_ACTIVE.getCode()));
		if(!user.isEmpty()){
			for (User objectUser : user) {
				//EmailService.setupEmailUser(objectUser.getEmail(),REST_SERVICE_URI,objectUser.getUserName());
				EmailService.setupEmailUserWitMailGun(objectUser.getEmail(),REST_SERVICE_URI,objectUser.getUserName());
				//EmailService.setupEmailUserWithAPI(objectUser.getEmail(),REST_SERVICE_URI,objectUser.getUserName());
			}
		}
	}
}
