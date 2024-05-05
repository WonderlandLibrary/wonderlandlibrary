/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.NamedLoggerBase;

public abstract class MarkerIgnoringBase
extends NamedLoggerBase
implements Logger {
    private static final long serialVersionUID = 9044267456635152283L;

    public boolean isTraceEnabled(Marker marker) {
        return this.isTraceEnabled();
    }

    public void trace(Marker marker, String msg) {
        this.trace(msg);
    }

    public void trace(Marker marker, String format, Object arg) {
        this.trace(format, arg);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.trace(format, arg1, arg2);
    }

    public void trace(Marker marker, String format, Object ... arguments) {
        this.trace(format, arguments);
    }

    public void trace(Marker marker, String msg, Throwable t) {
        this.trace(msg, t);
    }

    public boolean isDebugEnabled(Marker marker) {
        return this.isDebugEnabled();
    }

    public void debug(Marker marker, String msg) {
        this.debug(msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        this.debug(format, arg);
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        this.debug(format, arg1, arg2);
    }

    public void debug(Marker marker, String format, Object ... arguments) {
        this.debug(format, arguments);
    }

    public void debug(Marker marker, String msg, Throwable t) {
        this.debug(msg, t);
    }

    public boolean isInfoEnabled(Marker marker) {
        return this.isInfoEnabled();
    }

    public void info(Marker marker, String msg) {
        this.info(msg);
    }

    public void info(Marker marker, String format, Object arg) {
        this.info(format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.info(format, arg1, arg2);
    }

    public void info(Marker marker, String format, Object ... arguments) {
        this.info(format, arguments);
    }

    public void info(Marker marker, String msg, Throwable t) {
        this.info(msg, t);
    }

    public boolean isWarnEnabled(Marker marker) {
        return this.isWarnEnabled();
    }

    public void warn(Marker marker, String msg) {
        this.warn(msg);
    }

    public void warn(Marker marker, String format, Object arg) {
        this.warn(format, arg);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.warn(format, arg1, arg2);
    }

    public void warn(Marker marker, String format, Object ... arguments) {
        this.warn(format, arguments);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        this.warn(msg, t);
    }

    public boolean isErrorEnabled(Marker marker) {
        return this.isErrorEnabled();
    }

    public void error(Marker marker, String msg) {
        this.error(msg);
    }

    public void error(Marker marker, String format, Object arg) {
        this.error(format, arg);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.error(format, arg1, arg2);
    }

    public void error(Marker marker, String format, Object ... arguments) {
        this.error(format, arguments);
    }

    public void error(Marker marker, String msg, Throwable t) {
        this.error(msg, t);
    }

    public String toString() {
        return this.getClass().getName() + "(" + this.getName() + ")";
    }
}

