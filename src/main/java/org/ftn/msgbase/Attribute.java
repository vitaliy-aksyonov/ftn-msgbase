package org.ftn.msgbase;

public enum Attribute {
	MSGPRIVATE, // Private message
	MSGCRASH,   // Crash priority message
	MSGREAD,    // Read by addressee
	MSGSENT,    // Sent okay
	MSGFILE,    // file attached
	MSGFWD,     // being forwarded
	MSGORPHAN,  // Unknown destination
	MSGKILL,    // Kill after mailing
	MSGLOCAL,   // True if message entered here
	MSGHOLD,    // true if hold for pickup
	MSGX2,      // reserved
	MSGFREQ,    // Requesting a file
	MSGRREQ,    // Return Receipt requested
	MSGRRCT,    // Return Receipt
	MSGAREQ,    // Request audit trail
	MSGUREQ;    // Requesting a file update
}
