package epsilon.util;

public class Timer {

	public long lastMS = System.currentTimeMillis();
	
	public void reset() {
		lastMS = System.currentTimeMillis();
	}
	
	public boolean hasTimeElapsed(long time, boolean reset) {
		if(System.currentTimeMillis()-lastMS > time) {
			if(reset) 
				reset();
			return true;
		}	
		
		return false;
	}
	public long lastMS2 = System.currentTimeMillis();
	public long lastMS3 = System.currentTimeMillis();
	
	public void reset2() {
		lastMS2 = System.currentTimeMillis();
	}
	public void reset3() {
		lastMS3 = System.currentTimeMillis() +100;
	}
	
	public boolean hasTimeElapsed2(long time, boolean reset2) {
		if(System.currentTimeMillis()-lastMS2 > time) {
			if(reset2) 
				reset2();
			return true;
		}	
		
		return false;
	}

	public boolean hasTimeElapsed3(long time, boolean reset3) {
		if(System.currentTimeMillis()-lastMS3 > time) {
			if(reset3) 
				reset3();
			return true;
		}	
		
		return false;
	}
	
	
}
