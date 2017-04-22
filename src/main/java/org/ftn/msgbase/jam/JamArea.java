package org.ftn.msgbase.jam;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.ftn.msgbase.Area;
import org.ftn.msgbase.Message;
import org.ftn.msgbase.MessageHeader;
import org.ftn.msgbase.exceptions.FtnException;
import org.ftn.msgbase.exceptions.OutOfBoundsException;
import org.ftn.msgbase.jam.exceptions.JamException;
import org.ftn.msgbase.jam.exceptions.JamInvalidStateException;
import org.ftn.msgbase.tools.CodingUtils;
import org.ftn.msgbase.tools.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JamArea implements Area, AutoCloseable {

	private static final Logger log = LoggerFactory.getLogger(JamArea.class);

	private static final String HEADER_PREFIX = ".jhr";
	private static final String TEXT_PREFIX = ".jdt";
	private static final String INDEX_PREFIX = ".jdx";
	private static final String LASTREAD_PREFIX = ".jlr";

	private final Charset defaultCharset;
	private final String areaName;

	private SeekableByteChannel headerChannel;
	private SeekableByteChannel textChannel;
	private SeekableByteChannel indexChannel;
	private SeekableByteChannel lastReadChannel;

	private boolean hasError = false;

	private JamAreaHeader header;

	public JamArea(Path basePath, String areaName, Charset defaultCharset) throws FtnException {
		this.defaultCharset = defaultCharset != null ? defaultCharset : Charset.defaultCharset();
		this.areaName = areaName;
		log.info("Openig JAM area. Path: {}. Area name: {}. Default charset: {}.", basePath, areaName,
			this.defaultCharset);
		try {
			headerChannel = Files.newByteChannel(basePath.resolve(areaName + HEADER_PREFIX));
			textChannel = Files.newByteChannel(basePath.resolve(areaName + TEXT_PREFIX));
			indexChannel = Files.newByteChannel(basePath.resolve(areaName + INDEX_PREFIX));
			lastReadChannel = Files.newByteChannel(basePath.resolve(areaName + LASTREAD_PREFIX));
			header = new JamAreaHeader(headerChannel);
		} catch (IOException e) {
			JamException jamException = new JamException(e);
			for (IOException ex : IOUtils.closeAll(lastReadChannel, indexChannel, textChannel, headerChannel)) {
				jamException.addSuppressed(ex);
			}
			log.error("Failed to open JAM area {}.", areaName, jamException);
			throw jamException;
		}
	}

	@Override
	public long size() throws JamInvalidStateException {
		if (hasError) {
			throw new JamInvalidStateException();
		}

		try {
			return indexChannel.size() / JamIndexRecord.RECORD_SIZE;
		} catch (IOException e) {
			hasError = true;
			log.error("Unrecoverable error during message reading.", e);
			throw new JamInvalidStateException(e);
		}
	}

	@Override
	public Optional<MessageHeader> getMessageHeader (long idx) throws JamInvalidStateException {
		if (hasError) {
			throw new JamInvalidStateException();
		}
		log.info("JAM area {}. Reading message header #{}.", areaName, idx);
		if (idx < 0 || (idx + 1) > size()) {
			throw new OutOfBoundsException();
		}
		return getJamMessageHeader(idx).map(h -> h.toMessageHeader(h.getMessageCharset().orElse(defaultCharset)));
	}

	@Override
	public Optional<Message> getMessage(long idx) throws JamInvalidStateException {
		if (hasError) {
			throw new JamInvalidStateException();
		}
		log.info("JAM area {}. Reading message #{}.", areaName, idx);
		if (idx < 0 || (idx + 1) > size()) {
			throw new OutOfBoundsException();
		}
		Optional<JamMessageHeader> optionalJamHeader = getJamMessageHeader(idx);
		if (!optionalJamHeader.isPresent()) {
			return Optional.empty();
		}

		JamMessageHeader jamHeader = optionalJamHeader.get();
		Charset effectiveCharset = jamHeader.getMessageCharset().orElse(defaultCharset);
		Message result = new Message();
		result.setMessageHeader(jamHeader.toMessageHeader(effectiveCharset));
		try {
			result.setText(getMessageText(jamHeader.getOffset(), jamHeader.getTxtLen(),
					effectiveCharset));
		} catch (FtnException e) {
			log.error("Failed to read message text.", e);
			return Optional.empty();
		}
		catch (IOException e) {
			hasError = true;
			log.error("Unrecoverable error during message reading.", e);
			throw new JamInvalidStateException(e);
		}
		return Optional.of(result);
	}

	@Override
	public void close() throws Exception {
		log.info("Closing JAM area {}.", areaName);
		List<IOException> supressed = IOUtils.closeAll(lastReadChannel, indexChannel, textChannel, headerChannel);
		if (!supressed.isEmpty()) {
			JamException ex = new JamException("Failed to close one or more files.");
			Iterator<IOException> it = supressed.iterator();
			ex.initCause(it.next());
			while (it.hasNext()) {
				ex.addSuppressed(it.next());
			}
			throw ex;
		}
	}

	private Optional<JamMessageHeader> getJamMessageHeader(long idx) throws JamInvalidStateException {
		log.trace("Reading JAM message header.");
		try {
			long indexOffset = idx * JamIndexRecord.RECORD_SIZE;
			log.trace("Retrieving index recod at offset {}.", indexOffset);
			indexChannel.position(indexOffset);
			JamIndexRecord jamIndex = new JamIndexRecord(indexChannel);
			if (jamIndex.getHeaderOffset() == JamIndexRecord.EMPTY_MSG_OFFSET) {
				log.debug("Message is marked as deleted.");
				return Optional.empty();
			}
			log.trace("Retrieving header record at offset {}.", jamIndex.getHeaderOffset());
			headerChannel.position(jamIndex.getHeaderOffset());
			return Optional.of(new JamMessageHeader(headerChannel, header.getBaseMessageNumber()));
		} catch (FtnException e) {
			log.error("Error during JAM message header reading.", e);
			return Optional.empty();
		} catch (IOException e) {
			hasError = true;
			log.error("Unrecoverable error during message reading.", e);
			throw new JamInvalidStateException(e);
		}
	}

	private String getMessageText(long textOffset, long textSize, Charset charset) throws IOException, FtnException {
		log.trace("Retrieving message text of size {} at offset {}.", textSize, textOffset);
		return CodingUtils.readText(textChannel.position(textOffset), textSize, charset);
	}
}
