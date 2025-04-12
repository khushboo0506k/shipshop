package com.kns.shipshop.utility;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.kns.shipshop.constants.CommonConstants;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Utility {

	public static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUMBERS = "0123456789";
	public static final String ALLOWED_SYMBOLS = "!@#$_";

	private static Map<String, String> serviceMap;

	private static Predicate<Character> validCharacter = c -> {
		String validChars = LOWERCASE_LETTERS + UPPERCASE_LETTERS + NUMBERS + ALLOWED_SYMBOLS;
		return validChars.chars().mapToObj(vc -> (char) vc).anyMatch(vc -> vc.equals(c));
	};

	/**************** Constructor *********************/

	private Utility() {
	}

	public static Map<String, String> mapFromFile() {
		return serviceMap;
	}

	public static boolean isNotEmpty(String st) {

		return st != null && st.trim().length() > 0;
	}

	// TODO: make password strong
	public static boolean isValidPwd(String pwd) {

		return pwd != null && pwd.length() >= 6 && pwd.length() <= 20;
	}

	public static String getMaxVarChar(String value) {
		return (value.length() > 255) ? value.substring(0, 254) : value;
	}

	public static String checkValidPassword(String password) {
		if (!StringUtils.hasText(password))
			return "Password can not be blank";

		if (!password.matches(CommonConstants.PASSWORD_REGEX))
			return "Invalid Password";

		if (password.length() < 6 && password.length() > 16)
			return "Password must be 6-16 characters long";

		if (!checkValidCharacters(password))
			return "Password contains invalid character";

		return "VALID";
	}

	public static boolean checkValidCharacters(String text) {
		return text.chars().mapToObj(c -> (char) c).allMatch(validCharacter);
	}

	public static String getHostName(HttpServletRequest req) {
		return req.getHeader("host");
	}

	public static boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch((a) -> a.toUpperCase().contains("ADMIN"));
	}
	
	public static boolean isAuthenticated(Authentication authentication) {
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
	}
	
	public static Set<String> listFilesUsingJavaIO(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toSet());
	}

}
