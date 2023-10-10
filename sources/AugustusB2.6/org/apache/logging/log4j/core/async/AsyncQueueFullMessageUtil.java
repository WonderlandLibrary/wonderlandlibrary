// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.status.StatusLogger;

public final class AsyncQueueFullMessageUtil
{
    private AsyncQueueFullMessageUtil() {
    }
    
    public static void logWarningToStatusLogger() {
        StatusLogger.getLogger().warn("LOG4J2-2031: Log4j2 logged an event out of order to prevent deadlock caused by domain objects logging from their toString method when the async queue is full");
    }
}
