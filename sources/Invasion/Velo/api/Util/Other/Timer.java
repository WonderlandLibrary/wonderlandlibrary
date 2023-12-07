package Velo.api.Util.Other;

import Velo.impl.Modules.combat.Killaura;

public class Timer {
	
	public long lastMS = System.currentTimeMillis();
    private long ms;
    
    public Timer() {
        ms = getCurrentMS();
    }
    
    
    private long time = -1L;

    public boolean hasTimePassed(final long ms) {
        return System.currentTimeMillis() >= time + ms;
    }

    public long hasTimeLeft(final long ms) {
        return (ms + time) - System.currentTimeMillis();
    }
    
    
    
    private long prevMS = 0L;

    public boolean delay(float milliSec) {
       return (float)Killaura.getIncremental((double)(this.getTime() - this.prevMS), 50.0D) >= milliSec;
    }

    public void reset2() {
       this.prevMS = this.getTime();
    }

    public long getTime() {
       return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
       return this.getTime() - this.prevMS;
    }

    public void setDifference(long difference) {
       this.prevMS = this.getTime() - difference;
    }
    
    
    
	public void reset() {
		lastMS = System.currentTimeMillis();
	    time = System.currentTimeMillis();
	}
	
	
 
    
    public final long getElapsedTime1() {
        return this.getCurrentMS() - this.time;
    }
    
    
	public boolean hasTimedElapsed(long time, boolean reset) {
		if(System.currentTimeMillis()-lastMS > time) {
			if(reset)
				reset();
			
			return true;
		}
		
		return false;
	}
	
	public final void reset1() {
        ms = getCurrentMS();
    }
	
	  private long getCurrentMS() {
	        return System.currentTimeMillis();
	    }
	  
	   public final boolean elapsed(final long milliseconds) {
	        return (getCurrentMS() - ms) > milliseconds;
	    }
	   
	    public final long getElapsedTime() {
	        return getCurrentMS() - ms;
	    }
	    
	    
}
