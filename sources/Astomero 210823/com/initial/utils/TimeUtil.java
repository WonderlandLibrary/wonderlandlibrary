package com.initial.utils;

public final class TimeUtil
{
    private long time;
    
    public TimeUtil() {
        this.time = -1L;
    }
    
    public boolean hasTimePassed(final long ms) {
        return System.currentTimeMillis() >= this.time + ms;
    }
    
    public long hasTimeLeft(final long ms) {
        return ms + this.time - System.currentTimeMillis();
    }
    
    public long timePassed() {
        return System.currentTimeMillis() - this.time;
    }
    
    public void reset() {
        this.time = System.currentTimeMillis();
    }
}
