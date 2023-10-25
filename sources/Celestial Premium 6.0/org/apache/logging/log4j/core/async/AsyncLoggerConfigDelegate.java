/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.async.EventRoute;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;

public interface AsyncLoggerConfigDelegate {
    public RingBufferAdmin createRingBufferAdmin(String var1, String var2);

    public EventRoute getEventRoute(Level var1);

    public void enqueueEvent(LogEvent var1, AsyncLoggerConfig var2);

    public boolean tryEnqueue(LogEvent var1, AsyncLoggerConfig var2);

    public void setLogEventFactory(LogEventFactory var1);
}

