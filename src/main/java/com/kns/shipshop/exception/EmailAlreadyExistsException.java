package com.kns.shipshop.exception;

public class EmailAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
		super();
	}

	public EmailAlreadyExistsException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public EmailAlreadyExistsException(final String message) {
		super(message);
	}

	public EmailAlreadyExistsException(final Throwable cause) {
		super(cause);
	}

}
