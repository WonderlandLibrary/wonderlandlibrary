/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.slf4j.LoggerFactory
 *  org.slf4j.Marker
 *  org.slf4j.spi.LocationAwareLogger
 */
package org.apache.logging.slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.slf4j.Log4jLoggerFactory;
import org.apache.logging.slf4j.Log4jMarker;
import org.apache.logging.slf4j.Log4jMarkerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

public class Log4jLogger
implements LocationAwareLogger,
Serializable {
    public static final String FQCN = Log4jLogger.class.getName();
    private static final long serialVersionUID = 7869000638091304316L;
    private transient ExtendedLogger logger;
    private final String name;
    private transient Log4jMarkerFactory markerFactory;

    public Log4jLogger(Log4jMarkerFactory markerFactory, ExtendedLogger logger, String name) {
        this.markerFactory = markerFactory;
        this.logger = logger;
        this.name = name;
    }

    public void trace(String format) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, null, format);
    }

    public void trace(String format, Object o) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, (org.apache.logging.log4j.Marker)null, format, o);
    }

    public void trace(String format, Object arg1, Object arg2) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, (org.apache.logging.log4j.Marker)null, format, arg1, arg2);
    }

    public void trace(String format, Object ... args) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, (org.apache.logging.log4j.Marker)null, format, args);
    }

    public void trace(String format, Throwable t) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, (org.apache.logging.log4j.Marker)null, format, t);
    }

    public boolean isTraceEnabled() {
        return this.logger.isEnabled(Level.TRACE, null, null);
    }

    public boolean isTraceEnabled(Marker marker) {
        return this.logger.isEnabled(Level.TRACE, this.getMarker(marker), null);
    }

    public void trace(Marker marker, String s) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, this.getMarker(marker), s);
    }

    public void trace(Marker marker, String s, Object o) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, this.getMarker(marker), s, o);
    }

    public void trace(Marker marker, String s, Object o, Object o1) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, this.getMarker(marker), s, o, o1);
    }

    public void trace(Marker marker, String s, Object ... objects) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, this.getMarker(marker), s, objects);
    }

    public void trace(Marker marker, String s, Throwable throwable) {
        this.logger.logIfEnabled(FQCN, Level.TRACE, this.getMarker(marker), s, throwable);
    }

    public void debug(String format) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, null, format);
    }

    public void debug(String format, Object o) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, (org.apache.logging.log4j.Marker)null, format, o);
    }

    public void debug(String format, Object arg1, Object arg2) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, (org.apache.logging.log4j.Marker)null, format, arg1, arg2);
    }

    public void debug(String format, Object ... args) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, (org.apache.logging.log4j.Marker)null, format, args);
    }

    public void debug(String format, Throwable t) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, (org.apache.logging.log4j.Marker)null, format, t);
    }

    public boolean isDebugEnabled() {
        return this.logger.isEnabled(Level.DEBUG, null, null);
    }

    public boolean isDebugEnabled(Marker marker) {
        return this.logger.isEnabled(Level.DEBUG, this.getMarker(marker), null);
    }

    public void debug(Marker marker, String s) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, this.getMarker(marker), s);
    }

    public void debug(Marker marker, String s, Object o) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, this.getMarker(marker), s, o);
    }

    public void debug(Marker marker, String s, Object o, Object o1) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, this.getMarker(marker), s, o, o1);
    }

    public void debug(Marker marker, String s, Object ... objects) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, this.getMarker(marker), s, objects);
    }

    public void debug(Marker marker, String s, Throwable throwable) {
        this.logger.logIfEnabled(FQCN, Level.DEBUG, this.getMarker(marker), s, throwable);
    }

    public void info(String format) {
        this.logger.logIfEnabled(FQCN, Level.INFO, null, format);
    }

    public void info(String format, Object o) {
        this.logger.logIfEnabled(FQCN, Level.INFO, (org.apache.logging.log4j.Marker)null, format, o);
    }

    public void info(String format, Object arg1, Object arg2) {
        this.logger.logIfEnabled(FQCN, Level.INFO, (org.apache.logging.log4j.Marker)null, format, arg1, arg2);
    }

    public void info(String format, Object ... args) {
        this.logger.logIfEnabled(FQCN, Level.INFO, (org.apache.logging.log4j.Marker)null, format, args);
    }

    public void info(String format, Throwable t) {
        this.logger.logIfEnabled(FQCN, Level.INFO, (org.apache.logging.log4j.Marker)null, format, t);
    }

    public boolean isInfoEnabled() {
        return this.logger.isEnabled(Level.INFO, null, null);
    }

    public boolean isInfoEnabled(Marker marker) {
        return this.logger.isEnabled(Level.INFO, this.getMarker(marker), null);
    }

    public void info(Marker marker, String s) {
        this.logger.logIfEnabled(FQCN, Level.INFO, this.getMarker(marker), s);
    }

    public void info(Marker marker, String s, Object o) {
        this.logger.logIfEnabled(FQCN, Level.INFO, this.getMarker(marker), s, o);
    }

    public void info(Marker marker, String s, Object o, Object o1) {
        this.logger.logIfEnabled(FQCN, Level.INFO, this.getMarker(marker), s, o, o1);
    }

    public void info(Marker marker, String s, Object ... objects) {
        this.logger.logIfEnabled(FQCN, Level.INFO, this.getMarker(marker), s, objects);
    }

    public void info(Marker marker, String s, Throwable throwable) {
        this.logger.logIfEnabled(FQCN, Level.INFO, this.getMarker(marker), s, throwable);
    }

    public void warn(String format) {
        this.logger.logIfEnabled(FQCN, Level.WARN, null, format);
    }

    public void warn(String format, Object o) {
        this.logger.logIfEnabled(FQCN, Level.WARN, (org.apache.logging.log4j.Marker)null, format, o);
    }

    public void warn(String format, Object arg1, Object arg2) {
        this.logger.logIfEnabled(FQCN, Level.WARN, (org.apache.logging.log4j.Marker)null, format, arg1, arg2);
    }

    public void warn(String format, Object ... args) {
        this.logger.logIfEnabled(FQCN, Level.WARN, (org.apache.logging.log4j.Marker)null, format, args);
    }

    public void warn(String format, Throwable t) {
        this.logger.logIfEnabled(FQCN, Level.WARN, (org.apache.logging.log4j.Marker)null, format, t);
    }

    public boolean isWarnEnabled() {
        return this.logger.isEnabled(Level.WARN, null, null);
    }

    public boolean isWarnEnabled(Marker marker) {
        return this.logger.isEnabled(Level.WARN, this.getMarker(marker), null);
    }

    public void warn(Marker marker, String s) {
        this.logger.logIfEnabled(FQCN, Level.WARN, this.getMarker(marker), s);
    }

    public void warn(Marker marker, String s, Object o) {
        this.logger.logIfEnabled(FQCN, Level.WARN, this.getMarker(marker), s, o);
    }

    public void warn(Marker marker, String s, Object o, Object o1) {
        this.logger.logIfEnabled(FQCN, Level.WARN, this.getMarker(marker), s, o, o1);
    }

    public void warn(Marker marker, String s, Object ... objects) {
        this.logger.logIfEnabled(FQCN, Level.WARN, this.getMarker(marker), s, objects);
    }

    public void warn(Marker marker, String s, Throwable throwable) {
        this.logger.logIfEnabled(FQCN, Level.WARN, this.getMarker(marker), s, throwable);
    }

    public void error(String format) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, null, format);
    }

    public void error(String format, Object o) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, (org.apache.logging.log4j.Marker)null, format, o);
    }

    public void error(String format, Object arg1, Object arg2) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, (org.apache.logging.log4j.Marker)null, format, arg1, arg2);
    }

    public void error(String format, Object ... args) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, (org.apache.logging.log4j.Marker)null, format, args);
    }

    public void error(String format, Throwable t) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, (org.apache.logging.log4j.Marker)null, format, t);
    }

    public boolean isErrorEnabled() {
        return this.logger.isEnabled(Level.ERROR, null, null);
    }

    public boolean isErrorEnabled(Marker marker) {
        return this.logger.isEnabled(Level.ERROR, this.getMarker(marker), null);
    }

    public void error(Marker marker, String s) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, this.getMarker(marker), s);
    }

    public void error(Marker marker, String s, Object o) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, this.getMarker(marker), s, o);
    }

    public void error(Marker marker, String s, Object o, Object o1) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, this.getMarker(marker), s, o, o1);
    }

    public void error(Marker marker, String s, Object ... objects) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, this.getMarker(marker), s, objects);
    }

    public void error(Marker marker, String s, Throwable throwable) {
        this.logger.logIfEnabled(FQCN, Level.ERROR, this.getMarker(marker), s, throwable);
    }

    public void log(Marker marker, String fqcn, int level, String message, Object[] params, Throwable throwable) {
        Message msg;
        org.apache.logging.log4j.Marker log4jMarker;
        Level log4jLevel = Log4jLogger.getLevel(level);
        if (!this.logger.isEnabled(log4jLevel, log4jMarker = this.getMarker(marker), message, params)) {
            return;
        }
        if (params == null) {
            msg = new SimpleMessage(message);
        } else {
            msg = new ParameterizedMessage(message, params, throwable);
            if (throwable != null) {
                throwable = msg.getThrowable();
            }
        }
        this.logger.logMessage(fqcn, log4jLevel, log4jMarker, msg, throwable);
    }

    private org.apache.logging.log4j.Marker getMarker(Marker marker) {
        if (marker == null) {
            return null;
        }
        if (marker instanceof Log4jMarker) {
            return ((Log4jMarker)marker).getLog4jMarker();
        }
        return ((Log4jMarker)this.markerFactory.getMarker(marker)).getLog4jMarker();
    }

    public String getName() {
        return this.name;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
        aInputStream.defaultReadObject();
        this.logger = LogManager.getContext().getLogger(this.name);
        this.markerFactory = ((Log4jLoggerFactory)LoggerFactory.getILoggerFactory()).getMarkerFactory();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
        aOutputStream.defaultWriteObject();
    }

    private static Level getLevel(int i) {
        switch (i) {
            case 0: {
                return Level.TRACE;
            }
            case 10: {
                return Level.DEBUG;
            }
            case 20: {
                return Level.INFO;
            }
            case 30: {
                return Level.WARN;
            }
            case 40: {
                return Level.ERROR;
            }
        }
        return Level.ERROR;
    }
}

