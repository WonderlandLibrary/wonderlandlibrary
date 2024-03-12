// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.time;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

public class TimerUtil
{
    public long lastMS;
    
    public TimerUtil() {
        this.lastMS = System.currentTimeMillis();
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean hasTimeElapsed(final long time) {
        return System.currentTimeMillis() - this.lastMS > time;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean hasTimeElapsed(final double time) {
        return this.hasTimeElapsed((long)time);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }
    
    public void setTime(final long time) {
        this.lastMS = time;
    }
}
