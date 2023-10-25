/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Level
 *  org.apache.log4j.Logger
 *  org.apache.log4j.Priority
 */
package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger
implements Log,
Serializable {
    private static final long serialVersionUID = 5160705895411730424L;
    private static final String FQCN;
    private volatile transient Logger logger = null;
    private final String name;
    private static final Priority traceLevel;
    static /* synthetic */ Class class$org$apache$commons$logging$impl$Log4JLogger;
    static /* synthetic */ Class class$org$apache$log4j$Level;
    static /* synthetic */ Class class$org$apache$log4j$Priority;

    public Log4JLogger() {
        this.name = null;
    }

    public Log4JLogger(String name) {
        this.name = name;
        this.logger = this.getLogger();
    }

    public Log4JLogger(Logger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
        }
        this.name = logger.getName();
        this.logger = logger;
    }

    public void trace(Object message) {
        this.getLogger().log(FQCN, traceLevel, message, null);
    }

    public void trace(Object message, Throwable t) {
        this.getLogger().log(FQCN, traceLevel, message, t);
    }

    public void debug(Object message) {
        this.getLogger().log(FQCN, (Priority)Level.DEBUG, message, null);
    }

    public void debug(Object message, Throwable t) {
        this.getLogger().log(FQCN, (Priority)Level.DEBUG, message, t);
    }

    public void info(Object message) {
        this.getLogger().log(FQCN, (Priority)Level.INFO, message, null);
    }

    public void info(Object message, Throwable t) {
        this.getLogger().log(FQCN, (Priority)Level.INFO, message, t);
    }

    public void warn(Object message) {
        this.getLogger().log(FQCN, (Priority)Level.WARN, message, null);
    }

    public void warn(Object message, Throwable t) {
        this.getLogger().log(FQCN, (Priority)Level.WARN, message, t);
    }

    public void error(Object message) {
        this.getLogger().log(FQCN, (Priority)Level.ERROR, message, null);
    }

    public void error(Object message, Throwable t) {
        this.getLogger().log(FQCN, (Priority)Level.ERROR, message, t);
    }

    public void fatal(Object message) {
        this.getLogger().log(FQCN, (Priority)Level.FATAL, message, null);
    }

    public void fatal(Object message, Throwable t) {
        this.getLogger().log(FQCN, (Priority)Level.FATAL, message, t);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Logger getLogger() {
        Logger result = this.logger;
        if (result == null) {
            Log4JLogger log4JLogger = this;
            synchronized (log4JLogger) {
                result = this.logger;
                if (result == null) {
                    this.logger = result = Logger.getLogger((String)this.name);
                }
            }
        }
        return result;
    }

    public boolean isDebugEnabled() {
        return this.getLogger().isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return this.getLogger().isEnabledFor((Priority)Level.ERROR);
    }

    public boolean isFatalEnabled() {
        return this.getLogger().isEnabledFor((Priority)Level.FATAL);
    }

    public boolean isInfoEnabled() {
        return this.getLogger().isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return this.getLogger().isEnabledFor(traceLevel);
    }

    public boolean isWarnEnabled() {
        return this.getLogger().isEnabledFor((Priority)Level.WARN);
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Level _traceLevel;
        FQCN = (class$org$apache$commons$logging$impl$Log4JLogger == null ? (class$org$apache$commons$logging$impl$Log4JLogger = Log4JLogger.class$("org.apache.commons.logging.impl.Log4JLogger")) : class$org$apache$commons$logging$impl$Log4JLogger).getName();
        if (!(class$org$apache$log4j$Priority == null ? (class$org$apache$log4j$Priority = Log4JLogger.class$("org.apache.log4j.Priority")) : class$org$apache$log4j$Priority).isAssignableFrom(class$org$apache$log4j$Level == null ? (class$org$apache$log4j$Level = Log4JLogger.class$("org.apache.log4j.Level")) : class$org$apache$log4j$Level)) {
            throw new InstantiationError("Log4J 1.2 not available");
        }
        try {
            _traceLevel = (Priority)(class$org$apache$log4j$Level == null ? (class$org$apache$log4j$Level = Log4JLogger.class$("org.apache.log4j.Level")) : class$org$apache$log4j$Level).getDeclaredField("TRACE").get(null);
        }
        catch (Exception ex) {
            _traceLevel = Level.DEBUG;
        }
        traceLevel = _traceLevel;
    }
}

