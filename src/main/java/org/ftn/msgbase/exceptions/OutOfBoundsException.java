package org.ftn.msgbase.exceptions;

public class OutOfBoundsException extends FtnRuntimeException {

	private static final long serialVersionUID = 648851595359381261L;

	public OutOfBoundsException() {
	}

	public OutOfBoundsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OutOfBoundsException(String message, Throwable cause) {
		super(message, cause);
	}

	public OutOfBoundsException(String message) {
		super(message);
	}

	public OutOfBoundsException(Throwable cause) {
		super(cause);
	}
}
