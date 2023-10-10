// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

public final class DummyNanoClock implements NanoClock
{
    private final long fixedNanoTime;
    
    public DummyNanoClock() {
        this(0L);
    }
    
    public DummyNanoClock(final long fixedNanoTime) {
        this.fixedNanoTime = fixedNanoTime;
    }
    
    @Override
    public long nanoTime() {
        return this.fixedNanoTime;
    }
}
