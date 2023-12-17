// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import ru.tuskevich.util.Utility;

public class TimerUtility implements Utility
{
    public long lastMS;
    
    public TimerUtility() {
        this.lastMS = System.currentTimeMillis();
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
    
    public boolean hasTimeElapsed(final long time) {
        return System.currentTimeMillis() - this.lastMS > time;
    }
    
    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }
    
    public void setTime(final long time) {
        this.lastMS = time;
    }
}
