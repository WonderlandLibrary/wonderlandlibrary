package com.initial.utils.player;

public class TimeHelper
{
    private long lastTime;
    
    public TimeHelper() {
        this.lastTime = this.getCurrentTime();
        this.reset();
    }
    
    public long getCurrentTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getLastTime() {
        return this.lastTime;
    }
    
    public long getDifference() {
        return this.getCurrentTime() - this.lastTime;
    }
    
    public void reset() {
        this.lastTime = this.getCurrentTime();
    }
    
    public boolean hasReached(final long milliseconds) {
        return this.getDifference() >= milliseconds;
    }
}
