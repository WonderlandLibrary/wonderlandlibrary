package com.enjoytheban.api;

/**
 * @author Purity
 * Handles EventTypes
 */

public class Type {
	/**
	 * Usually call this one before a method begins its processes
	 */
	public static final byte PRE = 0;
	/**
	 * Usually call this one after a method begins its processes
	 */
	public static final byte POST = 1;
	/**
	 * Possible usage for outgoing packets in a connection, Can be used in EventPacketSend
	 */
	public static final byte OUTGOING = 2;
	/**
	 * Possible usage for incoming packets from a connection, Can be used in EventPacketSend
	 */
	public static final byte INCOMING = 3;
}
