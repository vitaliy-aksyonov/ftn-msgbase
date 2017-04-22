package org.ftn.msgbase.exceptions;

public class FtnValidationException extends FtnException {

	private static final long serialVersionUID = 479403381233467603L;

	public FtnValidationException() {
	}

	public FtnValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FtnValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtnValidationException(String message) {
		super(message);
	}

	public FtnValidationException(Throwable cause) {
		super(cause);
	}

}
