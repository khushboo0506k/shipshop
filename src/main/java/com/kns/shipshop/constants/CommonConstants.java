package com.kns.shipshop.constants;

public class CommonConstants {

	public static final String USERNAME_REGEX = "\\w{4,21}";
	public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,16}$";
	
	public static final String REDIRECT_HOMEPAGE = "redirect:/";
	public static final String LOGIN = "login";
	
	public static final String REDIRECT_USER_DASHBOARD = "redirect:/user/dashboard";
}
