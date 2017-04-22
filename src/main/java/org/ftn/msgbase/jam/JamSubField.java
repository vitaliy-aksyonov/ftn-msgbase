package org.ftn.msgbase.jam;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ftn.msgbase.tools.CollectionUtils;

final class JamSubField {

	private final byte[] data;

	JamSubField(byte[] data) {
		this.data = data;
	}

	static Map<JamSubFieldType, List<JamSubField>> readSubFields(ByteBuffer buffer) {
		Map<JamSubFieldType, List<JamSubField>> result = new LinkedHashMap<>();
		while(buffer.hasRemaining()) {
			JamSubFieldType subFieldType = JamSubFieldType.from(JamUtils.readUnsignedInt(buffer));
			// Skip two reserved bytes
			buffer.position(buffer.position() + Constants.USHORT_SIZE);
			final long dataSize = JamUtils.readUnsignedLong(buffer);
			byte[] data = new byte[Math.toIntExact(dataSize)];
			buffer.get(data);
			CollectionUtils.putInMultimap(result, subFieldType, new JamSubField(data));
		}
		return result;
	}

	public byte[] getData() {
		return data;
	}
}
