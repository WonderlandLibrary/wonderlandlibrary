// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

public interface WatchEventService
{
    void subscribe(final WatchManager manager);
    
    void unsubscribe(final WatchManager manager);
}
