package com.kns.shipshop.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AppErrorController implements ErrorController {
	
	@RequestMapping("/error")
    public ModelAndView handleError() {
		return new ModelAndView("error");
    }
	
	@RequestMapping("/accessDenied")
	public ModelAndView accessDeniedPage() {
		return new ModelAndView("access-denied");
	}
	
}
