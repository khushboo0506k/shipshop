package com.kns.shipshop.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
@EnableAsync
public class CustomMailSender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomMailSender.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SimpleMailMessage simpleMailMessage;
	
	@Autowired
	private MailProperties mailProperties;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Async
	public void sendEmail(String toEmail, String subject, String body) {
		LOGGER.debug("------ START OF sendEmail :: toEmail + subject + body -------");
		LOGGER.debug("to: " + toEmail);
		simpleMailMessage.setTo(toEmail);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(body);
		LOGGER.debug("simpleMailMessage.getTo(): " + simpleMailMessage.getTo());
        javaMailSender.send(simpleMailMessage);
        LOGGER.debug("------ END OF sendEmail -------");
	}
	
	@Async
	public void sendVerificationMail(String toAddress, String subject, String content) {
		LOGGER.debug("------ START OF sendVerificationMail -------");
		LOGGER.debug("Content ---- > {}", content);
		simpleMailMessage.setTo(toAddress);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		LOGGER.debug("simpleMailMessage.getTo(): " + simpleMailMessage.getTo());
        javaMailSender.send(simpleMailMessage);
        LOGGER.debug("------ END OF sendVerificationMail -------");
	}
	
	@Async
	public void sendVerificationMailHtml(String toAddress, String subject, String content) throws MessagingException {
		LOGGER.debug("------ START OF sendVerificationMail -------");
		LOGGER.debug("Content ---- > " + content);
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		LOGGER.debug("{}", mailProperties.getProperties());
		helper.setFrom(fromEmail);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(content, true);
		javaMailSender.send(mail);

		LOGGER.debug("------ END OF sendVerificationMail -------");
	}
	
}
