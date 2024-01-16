// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.misc;

import ru.fluger.client.helpers.Helper;

public class TimerHelper implements Helper
{
    private long ms;
    
    public TimerHelper() {
        this.ms = this.getCurrentMS();
    }
    
    private long getCurrentMS() {
        return System.currentTimeMillis();
    }
    
    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.ms > milliseconds;
    }
    
    public void reset() {
        this.ms = this.getCurrentMS();
    }
    
    public long getTime() {
        return this.getCurrentMS() - this.ms;
    }
}
