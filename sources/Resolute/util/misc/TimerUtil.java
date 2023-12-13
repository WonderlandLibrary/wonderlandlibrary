// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

public class TimerUtil
{
    private long currentMs;
    
    public TimerUtil() {
        this.reset();
    }
    
    public long lastReset() {
        return this.currentMs;
    }
    
    public boolean hasElapsed(final long milliseconds) {
        return this.elapsed() > milliseconds;
    }
    
    public long elapsed() {
        return System.currentTimeMillis() - this.currentMs;
    }
    
    public void reset() {
        this.currentMs = System.currentTimeMillis();
    }
}
