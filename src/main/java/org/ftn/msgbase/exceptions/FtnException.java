package org.ftn.msgbase.exceptions;

public class FtnException extends Exception {

	private static final long serialVersionUID = 8175771363852957353L;

	public FtnException() {
	}

	public FtnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FtnException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtnException(String message) {
		super(message);
	}

	public FtnException(Throwable cause) {
		super(cause);
	}

}
