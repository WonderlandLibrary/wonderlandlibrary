// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

public final class SystemNanoClock implements NanoClock
{
    @Override
    public long nanoTime() {
        return System.nanoTime();
    }
}
