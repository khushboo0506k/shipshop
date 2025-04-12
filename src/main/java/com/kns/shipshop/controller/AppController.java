package com.kns.shipshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kns.shipshop.constants.CommonConstants;
import com.kns.shipshop.service.ProductService;
import com.kns.shipshop.utility.Utility;

@RestController
public class AppController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping(value = {"/", "/home"})
	public ModelAndView getHomePage(Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("newProducts", productService.getNewProducts());
		modelAndView.addObject("topSellingProducts", productService.getNewProducts());
		return modelAndView;
	}
	
	@GetMapping("/register")
	public ModelAndView getRegistrationPage(Authentication authentication) {
		if(Utility.isAuthenticated(authentication))
			return new ModelAndView(CommonConstants.REDIRECT_HOMEPAGE);
		
		return new ModelAndView("register");
	}
	
	@GetMapping("/register/seller")
	public ModelAndView getSellerRegistrationPage(Authentication authentication) {
		if(Utility.isAuthenticated(authentication))
			return new ModelAndView(CommonConstants.REDIRECT_HOMEPAGE);
		
		return new ModelAndView("seller/register");
	}
	
	@GetMapping("/register_success")
	public ModelAndView showRegisterSuccessPage(Authentication authentication) {
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken))
			return new ModelAndView(CommonConstants.REDIRECT_USER_DASHBOARD);
		
		return new ModelAndView("register_success");
	}

}
