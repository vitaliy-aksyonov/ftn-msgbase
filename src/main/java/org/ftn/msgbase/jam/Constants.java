package org.ftn.msgbase.jam;

import java.nio.ByteOrder;

final class Constants {

	static final byte[] JAM_SIGNATURE = {'J', 'A', 'M', 0};
	static final int JAM_SIGNATURE_SIZE = JAM_SIGNATURE.length;

	static final int UCHAR_SIZE = 1;
	static final int USHORT_SIZE = 2;
	static final int ULONG_SIZE = 4;

	static final ByteOrder JAM_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
}
