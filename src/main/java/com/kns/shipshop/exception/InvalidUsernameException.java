package com.kns.shipshop.exception;

public class InvalidUsernameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUsernameException() {
		super();
	}

	public InvalidUsernameException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InvalidUsernameException(final String message) {
		super(message);
	}

	public InvalidUsernameException(final Throwable cause) {
		super(cause);
	}

}
