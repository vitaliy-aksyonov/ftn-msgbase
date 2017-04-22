package org.ftn.msgbase.jam;

enum JamSubFieldType {
	UNKNOWN, OADDRESS, DADDRESS, SENDERNAME, RECEIVERNAME, MSGID, REPLYID, SUBJECT, PID, TRACE, ENCLOSEDFILE,
	ENCLOSEDFILEWALIAS, ENCLOSEDFREQ, ENCLOSEDFILEWCARD, ENCLOSEDINDIRECTFILE, EMBINDAT, FTSKLUDGE, SEENBY2D, PATH2D,
	FLAGS, TZUTCINFO;

	static JamSubFieldType from(int value) {
		switch (value) {
		case 0: return OADDRESS;
		case 1: return DADDRESS;
		case 2: return SENDERNAME;
		case 3: return RECEIVERNAME;
		case 4: return MSGID;
		case 5: return REPLYID;
		case 6: return SUBJECT;
		case 7: return PID;
		case 8: return TRACE;
		case 9: return ENCLOSEDFILE;
		case 10: return ENCLOSEDFILEWALIAS;
		case 11: return ENCLOSEDFREQ;
		case 12: return ENCLOSEDFILEWCARD;
		case 13: return ENCLOSEDINDIRECTFILE;
		case 1000: return EMBINDAT;
		case 2000: return FTSKLUDGE;
		case 2001: return SEENBY2D;
		case 2002: return PATH2D;
		case 2003: return FLAGS;
		case 2004: return TZUTCINFO;
		default: return UNKNOWN;
		}
	}
}
