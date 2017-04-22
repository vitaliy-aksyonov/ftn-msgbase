package org.ftn.msgbase.tools;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.ftn.msgbase.exceptions.FtnException;

public class IOUtils {

	public static List<IOException> closeAll(Closeable... closeables) {
		List<IOException> supressed = new ArrayList<>();
		for (Closeable closeable : closeables) {
			try {
				if (closeable != null)
					closeable.close();
			} catch (IOException e) {
				supressed.add(e);
			}
		}
		return supressed;
	}

	public static ByteBuffer allocateBuffer(int capacity, ByteOrder byteOrder) {
		ByteBuffer buffer = ByteBuffer.allocate(capacity);
		buffer.order(byteOrder);
		return buffer;
	}

	public static void fillBuffer(ByteBuffer buffer, ReadableByteChannel channel) throws IOException, FtnException {
		int remaining = 0;
		int bytesRead = 0;
		do {
			remaining = buffer.remaining();
			bytesRead = channel.read(buffer);
			if (bytesRead == -1) {
				throw new FtnException("File is shorter than expected.");
			}
		} while (bytesRead != remaining);
		buffer.flip();
	}
}
