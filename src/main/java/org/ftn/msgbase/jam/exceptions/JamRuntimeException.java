package org.ftn.msgbase.jam.exceptions;

import org.ftn.msgbase.exceptions.FtnRuntimeException;

public class JamRuntimeException extends FtnRuntimeException {

	private static final long serialVersionUID = 1L;

	public JamRuntimeException() {
	}

    public JamRuntimeException(String message) {
        super(message);
    }

    public JamRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JamRuntimeException(Throwable cause) {
        super(cause);
    }

}
