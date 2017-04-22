package org.ftn.msgbase.jam.exceptions;

public class JamValidationException extends JamException {

	private static final long serialVersionUID = 1L;

	public JamValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public JamValidationException(String message) {
		super(message);
	}

	public JamValidationException(Throwable cause) {
		super(cause);
	}

}
