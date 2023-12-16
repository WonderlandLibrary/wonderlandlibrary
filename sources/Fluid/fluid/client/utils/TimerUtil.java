// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.utils;

public class TimerUtil
{
    public long start;
    
    public TimerUtil() {
        this.start = System.currentTimeMillis();
        this.start = System.currentTimeMillis();
    }
    
    public void reset() {
        this.start = System.currentTimeMillis();
    }
    
    public long getTimePassed() {
        return System.currentTimeMillis() - this.start;
    }
    
    public boolean isTimePassed(final long time) {
        return System.currentTimeMillis() - this.start > time;
    }
}
