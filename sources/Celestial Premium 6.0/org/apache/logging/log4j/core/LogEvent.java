/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;

public interface LogEvent
extends Serializable {
    public Level getLevel();

    public String getLoggerName();

    public StackTraceElement getSource();

    public Message getMessage();

    public Marker getMarker();

    public String getThreadName();

    public long getMillis();

    public Throwable getThrown();

    public Map<String, String> getContextMap();

    public ThreadContext.ContextStack getContextStack();

    public String getFQCN();

    public boolean isIncludeLocation();

    public void setIncludeLocation(boolean var1);

    public boolean isEndOfBatch();

    public void setEndOfBatch(boolean var1);
}

