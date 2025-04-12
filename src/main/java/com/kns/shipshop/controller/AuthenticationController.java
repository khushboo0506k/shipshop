package com.kns.shipshop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kns.shipshop.constants.CommonConstants;
import com.kns.shipshop.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {
	
	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest req, Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("session", req.getSession());
		if(Utility.isAuthenticated(authentication))
			modelAndView.setViewName(CommonConstants.REDIRECT_HOMEPAGE);
		else
			modelAndView.setViewName(CommonConstants.LOGIN);
		return modelAndView;
	}
	
}
