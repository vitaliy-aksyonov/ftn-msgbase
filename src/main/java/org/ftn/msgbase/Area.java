package org.ftn.msgbase;

import java.util.Optional;

import org.ftn.msgbase.exceptions.FtnInvalidStateException;

public interface Area extends AutoCloseable {
	long size() throws FtnInvalidStateException;
	Optional<MessageHeader> getMessageHeader(long idx) throws FtnInvalidStateException;
	Optional<Message> getMessage(long idx) throws FtnInvalidStateException;
}
