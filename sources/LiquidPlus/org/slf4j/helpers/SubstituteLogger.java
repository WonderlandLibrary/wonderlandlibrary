/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.EventRecodingLogger;
import org.slf4j.event.LoggingEvent;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.NOPLogger;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SubstituteLogger
implements Logger {
    private final String name;
    private volatile Logger _delegate;
    private Boolean delegateEventAware;
    private Method logMethodCache;
    private EventRecodingLogger eventRecodingLogger;
    private Queue<SubstituteLoggingEvent> eventQueue;
    private final boolean createdPostInitialization;

    public SubstituteLogger(String name, Queue<SubstituteLoggingEvent> eventQueue, boolean createdPostInitialization) {
        this.name = name;
        this.eventQueue = eventQueue;
        this.createdPostInitialization = createdPostInitialization;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isTraceEnabled() {
        return this.delegate().isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        this.delegate().trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        this.delegate().trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        this.delegate().trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object ... arguments) {
        this.delegate().trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        this.delegate().trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.delegate().isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        this.delegate().trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        this.delegate().trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().trace(marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object ... arguments) {
        this.delegate().trace(marker, format, arguments);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        this.delegate().trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.delegate().isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        this.delegate().debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        this.delegate().debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        this.delegate().debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object ... arguments) {
        this.delegate().debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.delegate().debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.delegate().isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        this.delegate().debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        this.delegate().debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().debug(marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object ... arguments) {
        this.delegate().debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        this.delegate().debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.delegate().isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        this.delegate().info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        this.delegate().info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        this.delegate().info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object ... arguments) {
        this.delegate().info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.delegate().info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.delegate().isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        this.delegate().info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        this.delegate().info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().info(marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object ... arguments) {
        this.delegate().info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.delegate().info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.delegate().isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        this.delegate().warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        this.delegate().warn(format, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        this.delegate().warn(format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object ... arguments) {
        this.delegate().warn(format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.delegate().warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.delegate().isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        this.delegate().warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        this.delegate().warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().warn(marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object ... arguments) {
        this.delegate().warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.delegate().warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.delegate().isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        this.delegate().error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        this.delegate().error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        this.delegate().error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object ... arguments) {
        this.delegate().error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        this.delegate().error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.delegate().isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        this.delegate().error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        this.delegate().error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().error(marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object ... arguments) {
        this.delegate().error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        this.delegate().error(marker, msg, t);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SubstituteLogger that = (SubstituteLogger)o;
        return this.name.equals(that.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    Logger delegate() {
        if (this._delegate != null) {
            return this._delegate;
        }
        if (this.createdPostInitialization) {
            return NOPLogger.NOP_LOGGER;
        }
        return this.getEventRecordingLogger();
    }

    private Logger getEventRecordingLogger() {
        if (this.eventRecodingLogger == null) {
            this.eventRecodingLogger = new EventRecodingLogger(this, this.eventQueue);
        }
        return this.eventRecodingLogger;
    }

    public void setDelegate(Logger delegate) {
        this._delegate = delegate;
    }

    public boolean isDelegateEventAware() {
        if (this.delegateEventAware != null) {
            return this.delegateEventAware;
        }
        try {
            this.logMethodCache = this._delegate.getClass().getMethod("log", LoggingEvent.class);
            this.delegateEventAware = Boolean.TRUE;
        }
        catch (NoSuchMethodException e) {
            this.delegateEventAware = Boolean.FALSE;
        }
        return this.delegateEventAware;
    }

    public void log(LoggingEvent event) {
        if (this.isDelegateEventAware()) {
            try {
                this.logMethodCache.invoke(this._delegate, event);
            }
            catch (IllegalAccessException e) {
            }
            catch (IllegalArgumentException e) {
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
        }
    }

    public boolean isDelegateNull() {
        return this._delegate == null;
    }

    public boolean isDelegateNOP() {
        return this._delegate instanceof NOPLogger;
    }
}

