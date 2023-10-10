// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

public final class SystemMillisClock implements Clock
{
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
