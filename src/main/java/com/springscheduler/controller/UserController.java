package com.springscheduler.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springscheduler.bean.UserBean;
import com.springscheduler.model.User;
import com.springscheduler.service.EmailService;
import com.springscheduler.service.SchedulerService;
import com.springscheduler.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	EmailService emailService;
	@Autowired
	SchedulerService schedulerService;

	
	public static final String path = "user";

	private static final String PARAM_ID = "id";
	
	@RequestMapping("/")
	public String ShowHomePageLogin(HttpServletRequest httpServletRequest,Model model){
		List<User>listUser = userService.findAll();
		model.addAttribute("listUser",listUser);
		schedulerService.sendEmailEveryTenMinutes();
		return path + "/" +"index";
	}

	@RequestMapping(value="/addUser", method=RequestMethod.GET)
	public String ShowRegister(HttpServletRequest httpServletRequest,Model model){
		return path + "/" +"new-user";
	}	
	
	@RequestMapping(value="/editUser/{id}", method=RequestMethod.GET)
	public String ShowEditUser(HttpServletRequest httpServletRequest,Model model,final @PathVariable(PARAM_ID) long id){
		User user = userService.findId(id);
		model.addAttribute("user",user);
		return path + "/" +"edit-user";
	}
	
	@RequestMapping(value="/editUser/", method=RequestMethod.POST)
	public String EditCustomer(HttpServletRequest httpServletRequest,Model model,@Valid UserBean params,RedirectAttributes redirectAttrs){
		
		
		User user = userService.updateUser(params.getId(),params); 
		redirectAttrs.addFlashAttribute("message" , "Reset Password success");
		redirectAttrs.addFlashAttribute("user" , user);
		return "redirect:" + "/";
	}
	
	@RequestMapping(value="/addUser/new", method=RequestMethod.POST)
	public String AddNew(HttpServletRequest httpServletRequest,Model model,final @Valid UserBean params,RedirectAttributes redirectAttrs)throws Exception{
		redirectAttrs.addFlashAttribute("register" , params);
		
		boolean isUserNameExists = userService.isUserNameExists(params.getUserName());
		if(isUserNameExists){
			redirectAttrs.addFlashAttribute("messageType" , "Danger");
			redirectAttrs.addFlashAttribute("message" , "username Exists");
			return "redirect:" + "/addUser";
		}
		
		boolean isEmailUserExists = userService.isEmailExists(params.getEmail());
		if(isEmailUserExists){
			redirectAttrs.addFlashAttribute("messageType" , "Danger");
			redirectAttrs.addFlashAttribute("message" , "email Exists");
			return "redirect:" + "/addUser";
		}
		
		boolean isEmailUser = userService.isEmailUser(params.getEmail());
		if(isEmailUser){
			redirectAttrs.addFlashAttribute("messageType" , "Danger");
			redirectAttrs.addFlashAttribute("message" , "email user is not correct");
			return "redirect:" + "/addUser";
		}
		
		if(isUserNameExists){
			redirectAttrs.addFlashAttribute("messageType" , "Danger");
			redirectAttrs.addFlashAttribute("message" , "username Exists");
			return "redirect:" + "/addUser";
		}
		
		
		if (userService.isRecordFull()){
			redirectAttrs.addFlashAttribute("messageType" , "Danger");
			redirectAttrs.addFlashAttribute("message" , "record User full");
			return "redirect:" + "/addUser";
		}
		redirectAttrs.addFlashAttribute("messageType" , "Info");
		redirectAttrs.addFlashAttribute("message" , "Success Register");
		userService.saveUser(params);
		
		return "redirect:" + "/addUser";
	}
	
	@RequestMapping(value="/login/submit", method=RequestMethod.POST)
	public String LoginSubmit(HttpServletRequest httpServletRequest,Model model,final @Valid UserBean params,RedirectAttributes redirectAttrs)throws Exception{
			        
		User user = userService.findUserNameOrEmail(params.getUserName());
		
		if(user == null){
			redirectAttrs.addFlashAttribute("messageType" , "Danger");
			redirectAttrs.addFlashAttribute("message" , "username and password is not correct");
			return "redirect:" + "/login";
		}
		
		
		redirectAttrs.addFlashAttribute("messageType" , "Info");
		redirectAttrs.addFlashAttribute("message" , "Success Login");
		redirectAttrs.addFlashAttribute("user" , user);
		return "redirect:" + "/successLogin";
	}
	
	@RequestMapping(value="/deleteUser/{id}", method=RequestMethod.POST)
	public String DeleteCustomer(HttpServletRequest httpServletRequest,Model model,final @PathVariable(PARAM_ID) long id,RedirectAttributes redirectAttrs){
		userService.deleteById(id); 
		redirectAttrs.addFlashAttribute("messageType" , "Info");
		redirectAttrs.addFlashAttribute("message" , "delete record success");
		return "redirect:" + "/"; 
	}
}
