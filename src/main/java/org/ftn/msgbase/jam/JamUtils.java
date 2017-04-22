package org.ftn.msgbase.jam;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import org.ftn.msgbase.jam.exceptions.JamValidationException;

final class JamUtils {

	public static void validateSignature(ByteBuffer buffer) throws JamValidationException {
		byte[] signature = new byte[Constants.JAM_SIGNATURE_SIZE];
		buffer.get(signature);
		if (!Arrays.equals(signature, Constants.JAM_SIGNATURE)) {
			throw new JamValidationException("JAM signature is missing.");
		}
	}

	static public long readUnsignedLong(ByteBuffer buffer) {
		return Integer.toUnsignedLong(buffer.getInt());
	}

	static public int readUnsignedInt(ByteBuffer buffer) {
		return Short.toUnsignedInt(buffer.getShort());
	}

	static public LocalDateTime readDateTime(ByteBuffer buffer) {
		return LocalDateTime.ofEpochSecond(readUnsignedLong(buffer), 0, ZoneOffset.UTC);
	}

}
