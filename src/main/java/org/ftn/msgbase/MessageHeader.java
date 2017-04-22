package org.ftn.msgbase;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class MessageHeader {

	private String from;
	private String to;
	private FtnAddress originAddress;
	private FtnAddress destinationAddress;

	private String subject;

	private LocalDateTime written;
	private LocalDateTime received;
	private LocalDateTime processed;

	private long replyTo = 0;
	private long reply1st = 0;
	private long replyNext = 0;

	private EnumSet<Attribute> attributes;

	private Map<String, List<String>> kludges;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public FtnAddress getOriginAddress() {
		return originAddress;
	}

	public void setOriginAddress(FtnAddress originAddress) {
		this.originAddress = originAddress;
	}

	public FtnAddress getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(FtnAddress destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public LocalDateTime getWritten() {
		return written;
	}

	public void setWritten(LocalDateTime written) {
		this.written = written;
	}

	public LocalDateTime getReceived() {
		return received;
	}

	public void setReceived(LocalDateTime received) {
		this.received = received;
	}

	public LocalDateTime getProcessed() {
		return processed;
	}

	public void setProcessed(LocalDateTime processed) {
		this.processed = processed;
	}

	public long getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(long replyTo) {
		this.replyTo = replyTo;
	}

	public long getReply1st() {
		return reply1st;
	}

	public void setReply1st(long reply1st) {
		this.reply1st = reply1st;
	}

	public long getReplyNext() {
		return replyNext;
	}

	public void setReplyNext(long replyNext) {
		this.replyNext = replyNext;
	}

	public EnumSet<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(EnumSet<Attribute> attributes) {
		this.attributes = attributes;
	}

	public Map<String, List<String>> getKludges() {
		return kludges;
	}

	public void setKludges(Map<String, List<String>> kludges) {
		this.kludges = kludges;
	}

}
