package org.ftn.msgbase.jam.exceptions;

import org.ftn.msgbase.exceptions.FtnException;

public class JamException extends FtnException {

	private static final long serialVersionUID = 1L;

    public JamException(String message) {
        super(message);
    }

    public JamException(String message, Throwable cause) {
        super(message, cause);
    }

    public JamException(Throwable cause) {
        super(cause);
    }

}
