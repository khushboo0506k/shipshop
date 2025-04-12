package com.kns.shipshop.auth.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
			throws IOException, ServletException {

		if (auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch((a) -> a.toUpperCase().contains("ADMIN"))) {
			LOGGER.debug("ADMIN");
			res.sendRedirect("/admin/home");
		} else if (auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch((a) -> a.toUpperCase().contains("SELLER"))) {
			LOGGER.debug("SELLER");
			res.sendRedirect("/seller/dashboard");
		} else {
			LOGGER.debug("USER");
			res.sendRedirect("/home");
		}
	}

}
