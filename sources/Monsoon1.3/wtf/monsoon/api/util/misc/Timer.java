package wtf.monsoon.api.util.misc;

public class Timer {

	//skidded util, but i dont care tbh.

	public long lastMS = System.currentTimeMillis();
	
	public void reset() {
		lastMS = System.currentTimeMillis();	
		
	}
	
	public void setTime(long time) {
        lastMS = time;
    }
	
	public long getTime() {
	        return System.currentTimeMillis() - lastMS;
    }
	
	public boolean hasTimeElapsed(long time, boolean reset) {
		if(System.currentTimeMillis()-lastMS > time) {
			
			if(reset)
				reset();
				
			return true;
			
		}
		
		return false;
	}
	
	private long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}
	
	public boolean hasReached(final double milliseconds) {
		return this.getCurrentMS() - this.lastMS >= milliseconds;
	}
	
}
