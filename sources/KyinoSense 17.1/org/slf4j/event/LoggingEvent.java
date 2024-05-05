/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.event;

import org.slf4j.Marker;
import org.slf4j.event.Level;

public interface LoggingEvent {
    public Level getLevel();

    public Marker getMarker();

    public String getLoggerName();

    public String getMessage();

    public String getThreadName();

    public Object[] getArgumentArray();

    public long getTimeStamp();

    public Throwable getThrowable();
}

