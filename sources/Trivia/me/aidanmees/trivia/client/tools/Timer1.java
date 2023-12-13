package me.aidanmees.trivia.client.tools;

public class Timer1 {

	private long lastMS;

	public long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public long getLastMS() {
		return this.lastMS;
	}

	public boolean hasReached(long milliseconds) {
		return getCurrentMS() - this.lastMS >= milliseconds;
	}

	public long getTime() {
		return getCurrentMS() - this.lastMS;
	}

	public void reset() {
		this.lastMS = getCurrentMS();
	}

	public void setLastMS(long currentMS) {
		this.lastMS = currentMS;
	}

	public boolean hasTimeElapsed(long time, boolean reset) {
		if (time() >= time) {
			if (reset) {
				reset();
			}
			return true;
		}
		return false;
	}

	public long time() {
		return System.nanoTime() / 1000000L - this.lastMS;
	}
	
}
