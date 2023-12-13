package me.robbanrobbin.jigsaw.client;

public final class TickTimer {
	
	public int ticks;
	
	private int maxTicks = -1;
	
	public TickTimer() {
		
	}
	
	public TickTimer(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
	public void tick() {
		if(maxTicks == -1 || (maxTicks != -1 && ticks != maxTicks)) {
			ticks++;
		}
	}
	
	public int getTicks() {
		return ticks;
	}
	
	public boolean hasTicksPassed(int numberOfTicks, boolean reset) {
		if(ticks >= numberOfTicks) {
			if(reset) {
				reset();
			}
			return true;
		}
		return false;
	}
	
	public void reset() {
		ticks = 0;
	}
	
	public int getMaxTicks() {
		return maxTicks;
	}
	
	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
}