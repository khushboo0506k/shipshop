package com.kns.shipshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Bean("template")
	public RestTemplate template() {
		return new RestTemplate();
	}
	
	@Bean("simpleMailMessage")
	public SimpleMailMessage simpleMailMessage() {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(fromEmail);
		return msg;
	}

}
