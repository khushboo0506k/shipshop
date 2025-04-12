package com.kns.shipshop.exception;

public class InvalidEmailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidEmailException() {
		super();
	}

	public InvalidEmailException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InvalidEmailException(final String message) {
		super(message);
	}

	public InvalidEmailException(final Throwable cause) {
		super(cause);
	}

}
