package org.ftn.msgbase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ftn.msgbase.exceptions.FtnValidationException;

public class FtnAddress {

	private static final Pattern ADDR_PATTERN = Pattern.compile("^(\\d+):(\\d+)\\/(\\d+)(?:\\.(\\d+)(?:@(\\S+))?)?$");
	private static final String NODE_ADDR_FORMAT = "%d:%d/%d";
	private static final String FULL_ADDR_FORMAT = "%d:%d/%d.%d";

	private final int zone;
	private final int net;
	private final int node;
	private final int point;

	public FtnAddress(String address) throws FtnValidationException {
		Matcher m = ADDR_PATTERN.matcher(address);
		if (!m.matches()) {
			throw new FtnValidationException("Invalid FTN address: " + address);
		}
		try {
			zone = Integer.parseInt(m.group(1));
			net = Integer.parseInt(m.group(2));
			node = Integer.parseInt(m.group(3));
			if (m.group(4) != null) {
				point = Integer.parseInt(m.group(4));
			} else {
				point = 0;
			}
		} catch (NumberFormatException e) {
			throw new FtnValidationException("Invalid FTN address: " + address, e);
		}
	}

	public FtnAddress(int zone, int net, int node) {
		this.zone = zone;
		this.net = net;
		this.node = node;
		point = 0;
	}

	public FtnAddress(int zone, int net, int node, int point) {
		this.zone = zone;
		this.net = net;
		this.node = node;
		this.point = point;
	}

	public int getZone() {
		return zone;
	}

	public int getNet() {
		return net;
	}

	public int getNode() {
		return node;
	}

	public int getPoint() {
		return point;
	}

	@Override
	public String toString() {
		return String.format(point == 0 ? NODE_ADDR_FORMAT : FULL_ADDR_FORMAT, zone, net, node, point);
	}

}
