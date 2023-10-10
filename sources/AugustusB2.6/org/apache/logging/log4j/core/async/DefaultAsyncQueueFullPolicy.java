// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.Level;

public class DefaultAsyncQueueFullPolicy implements AsyncQueueFullPolicy
{
    @Override
    public EventRoute getRoute(final long backgroundThreadId, final Level level) {
        final Thread currentThread = Thread.currentThread();
        if (currentThread.getId() == backgroundThreadId || currentThread instanceof Log4jThread) {
            return EventRoute.SYNCHRONOUS;
        }
        return EventRoute.ENQUEUE;
    }
}
