package wtf.dawn.utils;

public class Timer {

public long lastMS = System.currentTimeMillis();
	
	public void resetTimer() {
		lastMS = System.currentTimeMillis();
	}
	
	public boolean timeElapsed(long time, boolean resetTimer) {
		if(System.currentTimeMillis()-lastMS > time) {
			if(resetTimer)
				resetTimer();
			return true;
		}
		return false;
	}
}
