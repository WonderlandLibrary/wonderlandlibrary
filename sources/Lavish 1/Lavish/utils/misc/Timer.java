// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.misc;

public class Timer
{
    public long lastMS;
    private long ms;
    
    public Timer() {
        this.ms = this.getCurrentMS();
    }
    
    private long getCurrentMS() {
        return System.currentTimeMillis();
    }
    
    public final long getElapsedTime() {
        return this.getCurrentMS() - this.ms;
    }
    
    public final boolean elapsed(final long milliseconds) {
        return this.getCurrentMS() - this.ms > milliseconds;
    }
    
    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public boolean hasTimeElapsed(final double d, final boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > d) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }
    
    public void setTime(final long time) {
        this.lastMS = time;
    }
    
    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.lastMS >= milliSec;
    }
    
    public boolean isDelayComplete(final float f) {
        return System.currentTimeMillis() - this.lastMS >= f;
    }
}
