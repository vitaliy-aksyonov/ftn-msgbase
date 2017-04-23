package org.ftn.msgbase.jam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import org.ftn.msgbase.exceptions.FtnException;
import org.ftn.msgbase.tools.IOUtils;

final class JamIndexRecord {

	static final int RECORD_SIZE = 8;
	static final long EMPTY_MSG_OFFSET = 0xffffffffL;

	private final long recepientCrc;
	private final long headerOffset;

	public JamIndexRecord(ByteChannel channel) throws IOException, FtnException {
		ByteBuffer buffer = IOUtils.allocateBuffer(RECORD_SIZE, Constants.JAM_BYTE_ORDER);
		IOUtils.fillBuffer(buffer, channel);
		recepientCrc = JamUtils.readUnsignedLong(buffer);
		headerOffset = JamUtils.readUnsignedLong(buffer);
	}

	public long getRecepientCrc() {
		return recepientCrc;
	}

	public long getHeaderOffset() {
		return headerOffset;
	}
}
