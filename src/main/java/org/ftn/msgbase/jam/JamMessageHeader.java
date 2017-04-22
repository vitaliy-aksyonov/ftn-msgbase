package org.ftn.msgbase.jam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.ftn.msgbase.FtnAddress;
import org.ftn.msgbase.MessageHeader;
import org.ftn.msgbase.exceptions.FtnException;
import org.ftn.msgbase.exceptions.FtnValidationException;
import org.ftn.msgbase.jam.exceptions.JamValidationException;
import org.ftn.msgbase.tools.CodingUtils;
import org.ftn.msgbase.tools.CollectionUtils;
import org.ftn.msgbase.tools.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class JamMessageHeader {

	private static final Logger log = LoggerFactory.getLogger(JamMessageHeader.class);
	private static final int MESSAGE_HEADER_SIZE = 76;
	private static final int SUPPORTED_HEADER_REVISION = 1;

	private static final Pattern CHRS_REGEX = Pattern.compile("CHRS:\\s+(\\S+).*");
	private static final Pattern KLUDGE_REGEX = Pattern.compile("([^:\\s]+):?\\s*(.*)");

	private static final Map<JamSubFieldType, String> SUBFIELDS_TO_KLUDGES = new HashMap<>();
	static {
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.MSGID, "MSGID");
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.REPLYID, "REPLY");
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.PID, "PID");
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.TRACE, "Via");
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.SEENBY2D, "SEEN-BY");
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.PATH2D, "PATH");
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.FLAGS, "FLAGS");
		SUBFIELDS_TO_KLUDGES.put(JamSubFieldType.TZUTCINFO, "TZUTC");
	}

	private long timesRead;
	private long msgIdCrc;
	private long replyCrc;
	private long replyTo;
	private long reply1st;
	private long replyNext;
	LocalDateTime dateWritten;
	LocalDateTime dateReceived;
	LocalDateTime dateProcessed;
	private long messageNumber;
	EnumSet<JamMessageAttribute> messageAttributes;
	private long offset;
	private long txtLen;
	private long passwordCrc;
	private long cost;

	private Map<JamSubFieldType, List<JamSubField>> subFields;

	private Optional<Charset> messageCharset;

	JamMessageHeader(ByteChannel byteChannel, long baseNum) throws FtnException, IOException {
		readHeader(byteChannel, baseNum);
	}

	public long getTimesRead() {
		return timesRead;
	}

	public long getMsgIdCrc() {
		return msgIdCrc;
	}

	public long getReplyCrc() {
		return replyCrc;
	}

	public long getReplyTo() {
		return replyTo;
	}

	public long getReply1st() {
		return reply1st;
	}

	public long getReplyNext() {
		return replyNext;
	}

	public LocalDateTime getDateWritten() {
		return dateWritten;
	}

	public LocalDateTime getDateReceived() {
		return dateReceived;
	}

	public LocalDateTime getDateProcessed() {
		return dateProcessed;
	}

	public long getMessageNumber() {
		return messageNumber;
	}

	public EnumSet<JamMessageAttribute> getMessageAttributes() {
		return messageAttributes;
	}

	public long getOffset() {
		return offset;
	}

	public long getTxtLen() {
		return txtLen;
	}

	public long getPasswordCrc() {
		return passwordCrc;
	}

	public long getCost() {
		return cost;
	}

	public Map<JamSubFieldType, List<JamSubField>> getSubFields() {
		return subFields;
	}

	public Optional<Charset> getMessageCharset() {
		return messageCharset;
	}

	public MessageHeader toMessageHeader(Charset charset) {
		Charset effectiveCharset = messageCharset.orElse(charset);
		MessageHeader result = new MessageHeader();
		result.setFrom(getStringFromSubfield(JamSubFieldType.SENDERNAME, effectiveCharset));
		result.setTo(getStringFromSubfield(JamSubFieldType.RECEIVERNAME, effectiveCharset));
		result.setOriginAddress(getFtnAddressFromSubfield(JamSubFieldType.OADDRESS, effectiveCharset));
		result.setDestinationAddress(getFtnAddressFromSubfield(JamSubFieldType.DADDRESS, effectiveCharset));
		result.setSubject(getStringFromSubfield(JamSubFieldType.SUBJECT, effectiveCharset));
		result.setWritten(dateWritten);
		result.setReceived(dateReceived);
		result.setProcessed(dateProcessed);
		result.setReplyTo(replyTo);
		result.setReply1st(reply1st);
		result.setReplyNext(replyNext);
		result.setAttributes(JamMessageAttribute.toApiAttributes(messageAttributes));
		result.setKludges(convertKludges(effectiveCharset));
		return result;
	}

	private void readHeader(ByteChannel byteChannel, long baseNum) throws FtnException, IOException {
		log.trace("Retrieving message header.");
		ByteBuffer buffer = IOUtils.allocateBuffer(MESSAGE_HEADER_SIZE, ByteOrder.LITTLE_ENDIAN);
		IOUtils.fillBuffer(buffer, byteChannel);
		JamUtils.validateSignature(buffer);
		int revision = JamUtils.readUnsignedInt(buffer);
		if (revision != SUPPORTED_HEADER_REVISION) {
			throw new JamValidationException(String.format("Unsupported revision: %d", revision));
		}
		// Skipping reserved word
		buffer.position(buffer.position() + Constants.USHORT_SIZE);
		long subfieldLength = JamUtils.readUnsignedLong(buffer);
		if (subfieldLength > Integer.MAX_VALUE) {
			throw new JamValidationException("Too long subfields length");
		}
		timesRead = JamUtils.readUnsignedLong(buffer);
		msgIdCrc = JamUtils.readUnsignedLong(buffer);
		replyCrc = JamUtils.readUnsignedLong(buffer);
		replyTo = JamUtils.readUnsignedLong(buffer) - baseNum;
		reply1st = JamUtils.readUnsignedLong(buffer) - baseNum;
		replyNext = JamUtils.readUnsignedLong(buffer) - baseNum;
		dateWritten = JamUtils.readDateTime(buffer);
		dateReceived = JamUtils.readDateTime(buffer);
		dateProcessed = JamUtils.readDateTime(buffer);
		messageNumber = JamUtils.readUnsignedLong(buffer) - baseNum;
		messageAttributes = JamMessageAttribute.enumSetFrom(buffer.getLong());
		offset = JamUtils.readUnsignedLong(buffer);
		txtLen = JamUtils.readUnsignedLong(buffer);
		passwordCrc = JamUtils.readUnsignedLong(buffer);
		cost = JamUtils.readUnsignedLong(buffer);

		log.trace("Retrieving JAM message subfields data length: {}.", subfieldLength);
		int intSubfieldLength = Math.toIntExact(subfieldLength);
		if (buffer.capacity() < subfieldLength) {
			buffer = IOUtils.allocateBuffer(intSubfieldLength, ByteOrder.LITTLE_ENDIAN);
		}

		buffer.clear();
		buffer.limit(intSubfieldLength);
		IOUtils.fillBuffer(buffer, byteChannel);
		subFields = JamSubField.readSubFields(buffer);
		messageCharset = obtainCharset();
		if (messageCharset.isPresent()) {
			log.trace("Detected charset: {}.", messageCharset.get());
		}
	}

	private Optional<Charset> obtainCharset() {
		String charsetName = subFields.getOrDefault(JamSubFieldType.FTSKLUDGE, Collections.emptyList())
		.stream()
		.map(s -> {
			String data = CodingUtils.decodeString(ByteBuffer.wrap(s.getData()), StandardCharsets.US_ASCII);
			return CHRS_REGEX.matcher(data != null ? data : "");
		})
		.filter(m -> m.matches())
		.map(m -> m.group(1))
		.findFirst().orElse(null);

		if (charsetName != null) {
			try {
				return Optional.of(Charset.forName(charsetName));
			} catch (UnsupportedCharsetException | IllegalCharsetNameException ex) {
				log.warn("Failed to obtain message charset.", ex);
			}
		}
		return Optional.empty();
	}

	private JamSubField getSubfield(JamSubFieldType type) {
		List<JamSubField> subfieldsForType = subFields.get(type);
		return subfieldsForType == null || subfieldsForType.isEmpty() ? null : subfieldsForType.get(0);
	}

	private String getStringFromSubfield(JamSubFieldType type, Charset charset) {
		JamSubField fromSubField = getSubfield(type);
		if (fromSubField != null) {
			return CodingUtils.decodeString(ByteBuffer.wrap(fromSubField.getData()), charset);
		}
		return null;
	}

	private FtnAddress getFtnAddressFromSubfield(JamSubFieldType type, Charset charset) {
		String strAddress = getStringFromSubfield(type, charset);
		if (strAddress != null) {
			try {
				return new FtnAddress(strAddress);
			} catch (FtnValidationException e) {
				log.warn("FTN address is invalid.", e);
			}
		}
		return null;
	}

	private Map<String, List<String>> convertKludges(Charset charset) {
		Map<String, List<String>> result = new LinkedHashMap<>();
		for (Entry<JamSubFieldType, List<JamSubField>> entry : subFields.entrySet()) {
			if (entry.getKey() == JamSubFieldType.FTSKLUDGE) {
				for (JamSubField subfield : entry.getValue()) {
					String ftsKludge = CodingUtils.decodeString(ByteBuffer.wrap(subfield.getData()), charset);
					Matcher m = KLUDGE_REGEX.matcher(ftsKludge);
					if (m.matches()) {
						CollectionUtils.putInMultimap(result, m.group(1), m.group(2));
					} else {
						log.warn("Can't extract kludge name. Kludge '{}' will be skipped.", ftsKludge);
					}
				}
			} else {
				String mapping = SUBFIELDS_TO_KLUDGES.get(entry.getKey());
				if (mapping != null) {
					CollectionUtils.putAllInMultimap(result, mapping, entry.getValue().stream()
							.map(e -> CodingUtils.decodeString(ByteBuffer.wrap(e.getData()), charset))
							.collect(Collectors.toList()));
				}
			}
		}
		return result;
	}
}
