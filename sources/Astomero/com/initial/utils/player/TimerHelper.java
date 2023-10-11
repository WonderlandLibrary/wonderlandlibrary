package com.initial.utils.player;

public class TimerHelper
{
    public long lastMs;
    
    public TimerHelper() {
        this.lastMs = System.currentTimeMillis();
    }
    
    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }
    
    public void resetWithOffset(final long offset) {
        this.lastMs = System.currentTimeMillis() + offset;
    }
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (System.currentTimeMillis() - this.lastMs > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public boolean timeElapsed(final long time) {
        return System.currentTimeMillis() - this.lastMs > time;
    }
}
