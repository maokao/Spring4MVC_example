package org.iii.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	@Resource(name = "LoginService")
	org.iii.service.LoginService loginService;
	
	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView defaultPage() {
		
		ModelAndView model = new ModelAndView();

		model.setViewName("index");
		return model;

	}
	
	/*
	@RequestMapping(value = { "/login", "/" }, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error) throws IOException{ 
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}
		model.setViewName("login"); 
		return model;
	}
	*/
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}

		model.setViewName("login");
		return model;

	}
	
	@RequestMapping(value = { "/logout" }, method = RequestMethod.GET)
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/login";
	}
	
	//登入處理
	@RequestMapping(value = "/login_page", method = RequestMethod.GET)
	public ModelAndView login_page(HttpServletRequest request)
			throws UnsupportedEncodingException, ParseException {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Login Form - Database Authentication");
		model.addObject("message", "This is default page!!!");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			for (GrantedAuthority authority : userDetail.getAuthorities()) {
	            if (authority.getAuthority().equals("ROLE_ADMIN"))
	            {
	    			List alluserinfo = loginService.getallUserinfo();
	    			model.addObject("alluserinfo", alluserinfo);
	            }
	            else
	            {
	            	String username = userDetail.getUsername();
	    			String useremail = loginService.getUserEmail(username);
	    			model.addObject("useremail", useremail);
	            }
	        }
		}

		model.setViewName("hello");
		return model;

	}
}
