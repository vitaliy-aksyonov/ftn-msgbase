package org.ftn.msgbase.jam.exceptions;

import org.ftn.msgbase.exceptions.FtnInvalidStateException;

public class JamInvalidStateException extends FtnInvalidStateException {

	private static final long serialVersionUID = 6533979657528413791L;

	public JamInvalidStateException() {
	}

	public JamInvalidStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public JamInvalidStateException(String message) {
		super(message);
	}

	public JamInvalidStateException(Throwable cause) {
		super(cause);
	}

}
