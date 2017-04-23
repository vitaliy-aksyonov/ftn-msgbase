package org.ftn.msgbase.tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import org.ftn.msgbase.exceptions.FtnException;

public final class CodingUtils {

	private static final int BUFFER_SIZE = 32 * 1024;

	public static String decodeString(ByteBuffer buffer, Charset charset) {
		CharsetDecoder decoder = charset.newDecoder();
		decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		try {
			CharBuffer cb = decoder.decode(buffer);
			return cb.toString();
		} catch (CharacterCodingException e) {
			return null;
		}
	}

	public static String readText(ReadableByteChannel channel, long textSize, Charset charset)
			throws IOException, FtnException {
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		CharBuffer cb = CharBuffer.allocate(BUFFER_SIZE);
		CharsetDecoder decoder = charset.newDecoder();
		decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		StringBuilder result = new StringBuilder();
		while (textSize != 0) {
			int chunkSize = textSize < BUFFER_SIZE ? Math.toIntExact(textSize) : BUFFER_SIZE;
			textSize -= chunkSize;
			buffer.limit(chunkSize);
			IOUtils.fillBuffer(buffer, channel);
			decoder.decode(buffer, cb, false);
			cb.flip();
			result.append(cb.toString());
			cb.clear();
			buffer.compact();
		}
		buffer.flip();
		decoder.decode(buffer, cb, true);
		cb.flip();
		result.append(cb.toString());
		cb.clear();
		decoder.flush(cb);
		cb.flip();
		result.append(cb.toString());
		return result.toString();
	}
}
