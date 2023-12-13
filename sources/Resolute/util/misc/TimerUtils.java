// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

public class TimerUtils
{
    public long lastMS;
    private long time;
    
    public TimerUtils() {
        this.lastMS = System.currentTimeMillis();
        this.time = -1L;
    }
    
    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }
    
    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }
    
    public void setTime(final long time) {
        this.lastMS = time;
    }
    
    public final long getElapsedTime() {
        return this.getCurrentMS() - this.time;
    }
}
