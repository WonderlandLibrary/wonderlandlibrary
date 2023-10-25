/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Level
 *  org.apache.log4j.Logger
 *  org.apache.log4j.Priority
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.AbstractInternalLogger;
import io.netty.util.internal.logging.FormattingTuple;
import io.netty.util.internal.logging.MessageFormatter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

class Log4JLogger
extends AbstractInternalLogger {
    static final String FQCN = Log4JLogger.class.getName();
    final transient Logger logger;
    private static final long serialVersionUID = 2851357342488183058L;
    final boolean traceCapable;

    @Override
    public void warn(String string, Object object) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.log(FQCN, (Priority)Level.WARN, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void error(String string, Object ... objectArray) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.log(FQCN, (Priority)Level.ERROR, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void error(String string, Object object) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.log(FQCN, (Priority)Level.ERROR, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void error(String string) {
        this.logger.log(FQCN, (Priority)Level.ERROR, (Object)string, null);
    }

    @Override
    public void info(String string, Object object) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.log(FQCN, (Priority)Level.INFO, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void info(String string, Throwable throwable) {
        this.logger.log(FQCN, (Priority)Level.INFO, (Object)string, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    private boolean isTraceCapable() {
        try {
            this.logger.isTraceEnabled();
            return true;
        }
        catch (NoSuchMethodError noSuchMethodError) {
            return false;
        }
    }

    @Override
    public void error(String string, Throwable throwable) {
        this.logger.log(FQCN, (Priority)Level.ERROR, (Object)string, throwable);
    }

    @Override
    public void info(String string, Object object, Object object2) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.log(FQCN, (Priority)Level.INFO, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Throwable throwable) {
        this.logger.log(FQCN, (Priority)Level.DEBUG, (Object)string, throwable);
    }

    @Override
    public void warn(String string, Throwable throwable) {
        this.logger.log(FQCN, (Priority)Level.WARN, (Object)string, throwable);
    }

    @Override
    public void trace(String string) {
        this.logger.log(FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)string, null);
    }

    @Override
    public void debug(String string, Object object) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.log(FQCN, (Priority)Level.DEBUG, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void warn(String string, Object object, Object object2) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.log(FQCN, (Priority)Level.WARN, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public void info(String string) {
        this.logger.log(FQCN, (Priority)Level.INFO, (Object)string, null);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isEnabledFor((Priority)Level.WARN);
    }

    @Override
    public void warn(String string) {
        this.logger.log(FQCN, (Priority)Level.WARN, (Object)string, null);
    }

    @Override
    public void info(String string, Object ... objectArray) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.log(FQCN, (Priority)Level.INFO, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    Log4JLogger(Logger logger) {
        super(logger.getName());
        this.logger = logger;
        this.traceCapable = this.isTraceCapable();
    }

    @Override
    public void trace(String string, Object ... objectArray) {
        if (this.isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.log(FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void trace(String string, Object object) {
        if (this.isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.log(FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Object object, Object object2) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.log(FQCN, (Priority)Level.DEBUG, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isEnabledFor((Priority)Level.ERROR);
    }

    @Override
    public void warn(String string, Object ... objectArray) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.log(FQCN, (Priority)Level.WARN, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void trace(String string, Throwable throwable) {
        this.logger.log(FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)string, throwable);
    }

    @Override
    public boolean isTraceEnabled() {
        if (this.traceCapable) {
            return this.logger.isTraceEnabled();
        }
        return this.logger.isDebugEnabled();
    }

    @Override
    public void error(String string, Object object, Object object2) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.log(FQCN, (Priority)Level.ERROR, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void trace(String string, Object object, Object object2) {
        if (this.isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.log(FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Object ... objectArray) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.log(FQCN, (Priority)Level.DEBUG, (Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string) {
        this.logger.log(FQCN, (Priority)Level.DEBUG, (Object)string, null);
    }
}

