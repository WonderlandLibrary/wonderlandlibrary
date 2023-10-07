package org.lwjglx.input;

import java.nio.ByteBuffer;

/**
 * Internal utility class to keep track of event positions in an array.
 * When the array is full the position will wrap to the beginning.
 */
class EventQueue {

	private static final int QUEUE_SIZE = 200;
	private int maxEvents = 32;
	private int eventCount = 0;
	private int currentEventPos = -1;
	private int nextEventPos = 0;

	private final ByteBuffer queue;
	
	EventQueue(int maxEvents) {
		this.maxEvents = maxEvents;
		this.queue = ByteBuffer.allocate(QUEUE_SIZE*maxEvents);
	}

	public synchronized boolean putEvent(ByteBuffer event) {
		if (event.remaining() != maxEvents)
			throw new IllegalArgumentException("Internal error: event size " + maxEvents + " does not equal the given event size " + event.remaining());
		if (queue.remaining() >= event.remaining()) {
			queue.put(event);
			return true;
		} else
			return false;
	}

	/**
	 * add event to the queue
	 */
	void add() {
		eventCount++; // increment event count
		if (eventCount > maxEvents) eventCount = maxEvents; // cap max events
		
		nextEventPos++; // increment next event position
		if (nextEventPos == maxEvents) nextEventPos = 0; // wrap next event position
		
		if (nextEventPos == currentEventPos) currentEventPos++; // skip oldest event is queue full
		if (currentEventPos == maxEvents) currentEventPos = 0; // wrap current event position
	}

	public synchronized void copyEvents(ByteBuffer dest) {
		queue.flip();
		int old_limit = queue.limit();
		if (dest.remaining() < queue.remaining())
			queue.limit(dest.remaining() + queue.position());
		dest.put(queue);
		queue.limit(old_limit);
		queue.compact();
	}
	
	/**
	 * Increment the event queue
	 * @return - true if there is an event available
	 */
	boolean next() {
		if (eventCount == 0) return false;
		
		eventCount--; // decrement event count
		currentEventPos++; // increment current event position
		if (currentEventPos == maxEvents) currentEventPos = 0; // wrap current event position
		
		return true;
	}
	
	int getMaxEvents() {
		return maxEvents;
	}
	
	int getCurrentPos() {
		return currentEventPos;
	}
	
	int getNextPos() {
		return nextEventPos;
	}
}
