// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

public final class TimeUtil
{
    public long lastMS;
    
    public TimeUtil() {
        this.lastMS = 0L;
    }
    
    public int convertToMS(final int d) {
        return 1000 / d;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.lastMS;
    }
    
    public boolean hasReached(final long milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }
    
    public long getDelay() {
        return System.currentTimeMillis() - this.lastMS;
    }
    
    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
    
    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public void setLastMS(final long lastMS) {
        this.lastMS = lastMS;
    }
}
