// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

public class TimeHelper
{
    private long time;
    
    public TimeHelper() {
        this.time = System.currentTimeMillis();
    }
    
    public boolean reached(final long currentTime) {
        return Math.max(0L, System.currentTimeMillis() - this.time) >= currentTime;
    }
    
    public boolean reached(final long lastTime, final long currentTime) {
        return Math.max(0L, System.currentTimeMillis() - this.time + lastTime) >= currentTime;
    }
    
    public void reset() {
        this.time = System.currentTimeMillis();
    }
    
    public long getTime() {
        return Math.max(0L, System.currentTimeMillis() - this.time);
    }
}
