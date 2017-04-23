package org.ftn.msgbase.jam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;

import org.ftn.msgbase.exceptions.FtnException;
import org.ftn.msgbase.tools.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class JamAreaHeader {

	private static final Logger log = LoggerFactory.getLogger(JamAreaHeader.class);

	private static final int HEADER_SIZE = 1024;

	private final LocalDateTime creationDate;
	private final long modCounter;
	private final long activeMessages;
	private final long passwordCrc;
	private final long baseMessageNumber;

	JamAreaHeader(ReadableByteChannel byteChannel) throws FtnException, IOException {
		ByteBuffer buffer = IOUtils.allocateBuffer(HEADER_SIZE, Constants.JAM_BYTE_ORDER);
		IOUtils.fillBuffer(buffer, byteChannel);
		JamUtils.validateSignature(buffer);
		creationDate = JamUtils.readDateTime(buffer);
		modCounter = JamUtils.readUnsignedLong(buffer);
		activeMessages = JamUtils.readUnsignedLong(buffer);
		passwordCrc = JamUtils.readUnsignedLong(buffer);
		baseMessageNumber = JamUtils.readUnsignedLong(buffer);

		log.trace("JAM area header info: creation date - {}, modification count - {}, active messages - {}," +
			" base message number - {}", creationDate, modCounter, activeMessages, baseMessageNumber);
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public long getModCounter() {
		return modCounter;
	}

	public long getActiveMessages() {
		return activeMessages;
	}

	public long getPasswordCrc() {
		return passwordCrc;
	}

	public long getBaseMessageNumber() {
		return baseMessageNumber;
	}

}
