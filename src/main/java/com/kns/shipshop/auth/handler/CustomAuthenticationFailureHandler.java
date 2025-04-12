package com.kns.shipshop.auth.handler;

import java.io.IOException;

import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exp)
			throws IOException, ServletException {
		LOGGER.info("inside failure handler");
		String errMsg = "";
		if (exp.getMessage().equalsIgnoreCase("blocked")) {
			errMsg = "Login Blocked. Contact Admin";
		} else if (exp.getClass().isAssignableFrom(BadCredentialsException.class)) {
			errMsg = "Invalid username or password.";
		} else if (exp.getClass().isAssignableFrom(JDBCConnectionException.class)) {
			errMsg = "Some error occurred. Try again or contact admin";
		} else if (exp.getClass().isAssignableFrom(DisabledException.class)) {
			errMsg = "Account is not active. Check your mail for verification.";
		} else {
			errMsg = "Unknown error occurred - Contact Admin";
		}

		req.getSession().setAttribute("message", errMsg);
//		req.getSession().setAttribute("logoutSuccess", true);
		LOGGER.debug("{}", req.getSession().getAttribute("message"));
		res.sendRedirect("/login");
	}
}