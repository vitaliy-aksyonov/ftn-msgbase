package org.ftn.msgbase.exceptions;

public class FtnRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -1582914939371854370L;

	public FtnRuntimeException() {
	}

	public FtnRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FtnRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtnRuntimeException(String message) {
		super(message);
	}

	public FtnRuntimeException(Throwable cause) {
		super(cause);
	}

}
