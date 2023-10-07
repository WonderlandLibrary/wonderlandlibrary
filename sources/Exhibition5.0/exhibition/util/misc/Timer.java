// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util.misc;

public class Timer
{
    private long previousTime;
    
    public Timer() {
        this.previousTime = -1L;
    }
    
    public boolean check(final float milliseconds) {
        return this.getTime() >= milliseconds;
    }
    
    public long getTime() {
        return this.getCurrentTime() - this.previousTime;
    }
    
    public void reset() {
        this.previousTime = this.getCurrentTime();
    }
    
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
