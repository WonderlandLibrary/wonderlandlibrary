// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;

public interface AsyncQueueFullPolicy
{
    EventRoute getRoute(final long backgroundThreadId, final Level level);
}
