package com.initial.utils.player;

public class Timer
{
    public long lastMS;
    private long lastTime;
    
    public Timer() {
        this.lastMS = System.currentTimeMillis();
        this.lastTime = this.getCurrentTime();
        this.reset1();
    }
    
    public void reset1() {
        this.lastMS = System.currentTimeMillis();
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
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset1();
            }
            return true;
        }
        return false;
    }
}
