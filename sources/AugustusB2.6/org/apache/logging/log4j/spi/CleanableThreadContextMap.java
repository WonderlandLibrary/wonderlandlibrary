// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

public interface CleanableThreadContextMap extends ThreadContextMap2
{
    void removeAll(final Iterable<String> keys);
}
