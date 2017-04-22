package org.ftn.msgbase.jam;

import java.util.EnumSet;

import org.ftn.msgbase.Attribute;

enum JamMessageAttribute {

	MSG_LOCAL, MSG_INTRANSIT, MSG_PRIVATE, MSG_READ, MSG_SENT, MSG_KILLSENT, MSG_ARCHIVESENT, MSG_HOLD, MSG_CRASH,
	MSG_IMMEDIATE, MSG_DIRECT, MSG_GATE, MSG_FILEREQUEST, MSG_FILEATTACH, MSG_TRUNCFILE, MSG_KILLFILE, MSG_RECEIPTREQ,
	MSG_CONFIRMREQ, MSG_ORPHAN, MSG_ENCRYPT, MSG_COMPRESS, MSG_ESCAPED, MSG_FPU, MSG_TYPELOCAL, MSG_TYPEECHO,
	MSG_TYPENET, MSG_NODISP, MSG_LOCKED, MSG_DELETED;

	static EnumSet<JamMessageAttribute> enumSetFrom(long value) {
		EnumSet<JamMessageAttribute> result = EnumSet.noneOf(JamMessageAttribute.class);

		if ((value & 0x00000001L) != 0)
			result.add(MSG_LOCAL);
		if ((value & 0x00000002L) != 0)
			result.add(MSG_INTRANSIT);
		if ((value & 0x00000004L) != 0)
			result.add(MSG_PRIVATE);
		if ((value & 0x00000008L) != 0)
			result.add(MSG_READ);
		if ((value & 0x00000010L) != 0)
			result.add(MSG_SENT);
		if ((value & 0x00000020L) != 0)
			result.add(MSG_KILLSENT);
		if ((value & 0x00000040L) != 0)
			result.add(MSG_ARCHIVESENT);
		if ((value & 0x00000080L) != 0)
			result.add(MSG_HOLD);
		if ((value & 0x00000100L) != 0)
			result.add(MSG_CRASH);
		if ((value & 0x00000200L) != 0)
			result.add(MSG_IMMEDIATE);
		if ((value & 0x00000400L) != 0)
			result.add(MSG_DIRECT);
		if ((value & 0x00000800L) != 0)
			result.add(MSG_GATE);
		if ((value & 0x00001000L) != 0)
			result.add(MSG_FILEREQUEST);
		if ((value & 0x00002000L) != 0)
			result.add(MSG_FILEATTACH);
		if ((value & 0x00004000L) != 0)
			result.add(MSG_TRUNCFILE);
		if ((value & 0x00008000L) != 0)
			result.add(MSG_KILLFILE);
		if ((value & 0x00010000L) != 0)
			result.add(MSG_RECEIPTREQ);
		if ((value & 0x00020000L) != 0)
			result.add(MSG_CONFIRMREQ);
		if ((value & 0x00040000L) != 0)
			result.add(MSG_ORPHAN);
		if ((value & 0x00080000L) != 0)
			result.add(MSG_ENCRYPT);
		if ((value & 0x00100000L) != 0)
			result.add(MSG_COMPRESS);
		if ((value & 0x00200000L) != 0)
			result.add(MSG_ESCAPED);
		if ((value & 0x00400000L) != 0)
			result.add(MSG_FPU);
		if ((value & 0x00800000L) != 0)
			result.add(MSG_TYPELOCAL);
		if ((value & 0x01000000L) != 0)
			result.add(MSG_TYPEECHO);
		if ((value & 0x02000000L) != 0)
			result.add(MSG_TYPENET);
		if ((value & 0x20000000L) != 0)
			result.add(MSG_NODISP);
		if ((value & 0x40000000L) != 0)
			result.add(MSG_LOCKED);
		if ((value & 0x80000000L) != 0)
			result.add(MSG_DELETED);

		return result;
	}

	static EnumSet<Attribute> toApiAttributes(EnumSet<JamMessageAttribute> attributes) {
		EnumSet<Attribute> result = EnumSet.noneOf(Attribute.class);
		for (JamMessageAttribute attribute : attributes) {
			switch (attribute) {
			case MSG_CONFIRMREQ:
				result.add(Attribute.MSGRRCT);
				break;

			case MSG_CRASH:
				result.add(Attribute.MSGCRASH);
				break;

			case MSG_FILEATTACH:
				result.add(Attribute.MSGFILE);
				break;

			case MSG_FILEREQUEST:
				result.add(Attribute.MSGFREQ);
				break;

			case MSG_HOLD:
				result.add(Attribute.MSGHOLD);
				break;

			case MSG_INTRANSIT:
				result.add(Attribute.MSGFWD);
				break;

			case MSG_KILLSENT:
				result.add(Attribute.MSGKILL);
				break;

			case MSG_LOCAL:
				result.add(Attribute.MSGLOCAL);
				break;

			case MSG_ORPHAN:
				result.add(Attribute.MSGORPHAN);
				break;

			case MSG_PRIVATE:
				result.add(Attribute.MSGPRIVATE);
				break;

			case MSG_READ:
				result.add(Attribute.MSGREAD);
				break;

			case MSG_RECEIPTREQ:
				result.add(Attribute.MSGRREQ);
				break;

			case MSG_SENT:
				result.add(Attribute.MSGSENT);
				break;

			default:
				break;
			}
		}
		return result;
	}
}
