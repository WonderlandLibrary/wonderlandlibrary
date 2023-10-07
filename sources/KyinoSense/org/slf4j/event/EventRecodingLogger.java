/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.event;

import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.SubstituteLogger;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EventRecodingLogger
implements Logger {
    String name;
    SubstituteLogger logger;
    Queue<SubstituteLoggingEvent> eventQueue;

    public EventRecodingLogger(SubstituteLogger logger, Queue<SubstituteLoggingEvent> eventQueue) {
        this.logger = logger;
        this.name = logger.getName();
        this.eventQueue = eventQueue;
    }

    @Override
    public String getName() {
        return this.name;
    }

    private void recordEvent(Level level, String msg, Object[] args2, Throwable throwable) {
        this.recordEvent(level, null, msg, args2, throwable);
    }

    private void recordEvent(Level level, Marker marker, String msg, Object[] args2, Throwable throwable) {
        SubstituteLoggingEvent loggingEvent = new SubstituteLoggingEvent();
        loggingEvent.setTimeStamp(System.currentTimeMillis());
        loggingEvent.setLevel(level);
        loggingEvent.setLogger(this.logger);
        loggingEvent.setLoggerName(this.name);
        loggingEvent.setMarker(marker);
        loggingEvent.setMessage(msg);
        loggingEvent.setArgumentArray(args2);
        loggingEvent.setThrowable(throwable);
        loggingEvent.setThreadName(Thread.currentThread().getName());
        this.eventQueue.add(loggingEvent);
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void trace(String msg) {
        this.recordEvent(Level.TRACE, msg, null, null);
    }

    @Override
    public void trace(String format, Object arg) {
        this.recordEvent(Level.TRACE, format, new Object[]{arg}, null);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        this.recordEvent(Level.TRACE, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void trace(String format, Object ... arguments) {
        this.recordEvent(Level.TRACE, format, arguments, null);
    }

    @Override
    public void trace(String msg, Throwable t) {
        this.recordEvent(Level.TRACE, msg, null, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return true;
    }

    @Override
    public void trace(Marker marker, String msg) {
        this.recordEvent(Level.TRACE, marker, msg, null, null);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        this.recordEvent(Level.TRACE, marker, format, new Object[]{arg}, null);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.recordEvent(Level.TRACE, marker, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void trace(Marker marker, String format, Object ... argArray) {
        this.recordEvent(Level.TRACE, marker, format, argArray, null);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        this.recordEvent(Level.TRACE, marker, msg, null, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(String msg) {
        this.recordEvent(Level.TRACE, msg, null, null);
    }

    @Override
    public void debug(String format, Object arg) {
        this.recordEvent(Level.DEBUG, format, new Object[]{arg}, null);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        this.recordEvent(Level.DEBUG, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void debug(String format, Object ... arguments) {
        this.recordEvent(Level.DEBUG, format, arguments, null);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.recordEvent(Level.DEBUG, msg, null, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return true;
    }

    @Override
    public void debug(Marker marker, String msg) {
        this.recordEvent(Level.DEBUG, marker, msg, null, null);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        this.recordEvent(Level.DEBUG, marker, format, new Object[]{arg}, null);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        this.recordEvent(Level.DEBUG, marker, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void debug(Marker marker, String format, Object ... arguments) {
        this.recordEvent(Level.DEBUG, marker, format, arguments, null);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        this.recordEvent(Level.DEBUG, marker, msg, null, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(String msg) {
        this.recordEvent(Level.INFO, msg, null, null);
    }

    @Override
    public void info(String format, Object arg) {
        this.recordEvent(Level.INFO, format, new Object[]{arg}, null);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        this.recordEvent(Level.INFO, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void info(String format, Object ... arguments) {
        this.recordEvent(Level.INFO, format, arguments, null);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.recordEvent(Level.INFO, msg, null, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    @Override
    public void info(Marker marker, String msg) {
        this.recordEvent(Level.INFO, marker, msg, null, null);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        this.recordEvent(Level.INFO, marker, format, new Object[]{arg}, null);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.recordEvent(Level.INFO, marker, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void info(Marker marker, String format, Object ... arguments) {
        this.recordEvent(Level.INFO, marker, format, arguments, null);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.recordEvent(Level.INFO, marker, msg, null, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String msg) {
        this.recordEvent(Level.WARN, msg, null, null);
    }

    @Override
    public void warn(String format, Object arg) {
        this.recordEvent(Level.WARN, format, new Object[]{arg}, null);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        this.recordEvent(Level.WARN, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void warn(String format, Object ... arguments) {
        this.recordEvent(Level.WARN, format, arguments, null);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.recordEvent(Level.WARN, msg, null, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public void warn(Marker marker, String msg) {
        this.recordEvent(Level.WARN, msg, null, null);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        this.recordEvent(Level.WARN, format, new Object[]{arg}, null);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.recordEvent(Level.WARN, marker, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void warn(Marker marker, String format, Object ... arguments) {
        this.recordEvent(Level.WARN, marker, format, arguments, null);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.recordEvent(Level.WARN, marker, msg, null, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(String msg) {
        this.recordEvent(Level.ERROR, msg, null, null);
    }

    @Override
    public void error(String format, Object arg) {
        this.recordEvent(Level.ERROR, format, new Object[]{arg}, null);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        this.recordEvent(Level.ERROR, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void error(String format, Object ... arguments) {
        this.recordEvent(Level.ERROR, format, arguments, null);
    }

    @Override
    public void error(String msg, Throwable t) {
        this.recordEvent(Level.ERROR, msg, null, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    @Override
    public void error(Marker marker, String msg) {
        this.recordEvent(Level.ERROR, marker, msg, null, null);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        this.recordEvent(Level.ERROR, marker, format, new Object[]{arg}, null);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.recordEvent(Level.ERROR, marker, format, new Object[]{arg1, arg2}, null);
    }

    @Override
    public void error(Marker marker, String format, Object ... arguments) {
        this.recordEvent(Level.ERROR, marker, format, arguments, null);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        this.recordEvent(Level.ERROR, marker, msg, null, t);
    }
}

