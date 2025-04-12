package com.kns.shipshop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;
import com.kns.shipshop.service.UserService;
import com.kns.shipshop.user.UserDetailsImpl;

@RestController
@RequestMapping("/seller")
public class SellerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SellerController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/account")
	public ModelAndView getAccountHomePage(Authentication authentication) {
		
		ModelAndView modelAndView = new ModelAndView();
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
		}
		modelAndView.setViewName("/seller/account-home");
		return modelAndView;
	}
	
	@GetMapping("/dashboard")
	public ModelAndView getDashboardPage(Authentication authentication) {
		
		ModelAndView modelAndView = new ModelAndView();
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
		}
		modelAndView.setViewName("/seller/dashboard");
		return modelAndView;
	}
	
	@GetMapping("/profile")
	public ModelAndView getProfilePage(Authentication authentication) {
		
		ModelAndView modelAndView = new ModelAndView();
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
		}
		modelAndView.setViewName("/seller/my_profile");
		return modelAndView;
	}
	
	@GetMapping("/products")
	public ModelAndView getProductsPage(Authentication authentication) {
		
		ModelAndView modelAndView = new ModelAndView();
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
			List<Product> products = userService.getProducts(user);
			modelAndView.addObject("products", products);
			modelAndView.addObject("count", products.size());
		}
		modelAndView.setViewName("/seller/my_products");
		return modelAndView;
	}
	
	@GetMapping("/addProduct")
	public ModelAndView addProductPage(Authentication authentication) {
		
		ModelAndView modelAndView = new ModelAndView();
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
			modelAndView.addObject("user", user);
		}
		modelAndView.setViewName("/seller/add_product");
		return modelAndView;
	}
	
}
