package com.enjoytheban.utils;

/**
 * A util that will be handling time related things
 * 
 * @author Purity
 */

public class TimerUtil {
	// variable to hold out lastms
	private long lastMS;

	public TimerUtil() {
	}

	// returns currentms
	private long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	// boolean to check if x has reached the set time
	public boolean hasReached(double milliseconds) {
		return getCurrentMS() - lastMS >= milliseconds;
	}

	// method to reset lastMS
	public void reset() {
		lastMS = getCurrentMS();
	}

	public boolean delay(float milliSec) {
		return (float) (getTime() - this.lastMS) >= milliSec;
	}
	
	  public long getTime()
	  {
	    return System.nanoTime() / 1000000L;
	}
}