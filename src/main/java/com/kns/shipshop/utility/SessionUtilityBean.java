package com.kns.shipshop.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionUtilityBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtilityBean.class);

	public void removeAttributeFromSession(String attribute) {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			HttpSession session = request.getSession();
			session.removeAttribute(attribute);
		} catch (RuntimeException ex) {
			LOGGER.error("No Request: ", ex);
		}
	}

}
