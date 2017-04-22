package org.ftn.msgbase.exceptions;

public class FtnInvalidStateException extends FtnException {

	private static final long serialVersionUID = -2928145334462328143L;

	public FtnInvalidStateException() {
	}

	public FtnInvalidStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FtnInvalidStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtnInvalidStateException(String message) {
		super(message);
	}

	public FtnInvalidStateException(Throwable cause) {
		super(cause);
	}
}
