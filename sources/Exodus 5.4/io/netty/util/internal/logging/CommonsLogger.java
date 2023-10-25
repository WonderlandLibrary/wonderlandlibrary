/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.logging.Log
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.AbstractInternalLogger;
import io.netty.util.internal.logging.FormattingTuple;
import io.netty.util.internal.logging.MessageFormatter;
import org.apache.commons.logging.Log;

class CommonsLogger
extends AbstractInternalLogger {
    private static final long serialVersionUID = 8647838678388394885L;
    private final transient Log logger;

    @Override
    public void debug(String string, Object object, Object object2) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.debug((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override
    public void error(String string, Throwable throwable) {
        this.logger.error((Object)string, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override
    public void debug(String string, Object ... objectArray) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.debug((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void warn(String string, Object object, Object object2) {
        if (this.logger.isWarnEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.warn((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override
    public void trace(String string, Object object) {
        if (this.logger.isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.trace((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void error(String string, Object object, Object object2) {
        if (this.logger.isErrorEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.error((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void trace(String string, Throwable throwable) {
        this.logger.trace((Object)string, throwable);
    }

    @Override
    public void info(String string, Object object, Object object2) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.info((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public void trace(String string) {
        this.logger.trace((Object)string);
    }

    @Override
    public void warn(String string, Object ... objectArray) {
        if (this.logger.isWarnEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.warn((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public void warn(String string) {
        this.logger.warn((Object)string);
    }

    @Override
    public void trace(String string, Object object, Object object2) {
        if (this.logger.isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.logger.trace((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void info(String string, Object object) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.info((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void trace(String string, Object ... objectArray) {
        if (this.logger.isTraceEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.trace((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void info(String string, Throwable throwable) {
        this.logger.info((Object)string, throwable);
    }

    CommonsLogger(Log log, String string) {
        super(string);
        if (log == null) {
            throw new NullPointerException("logger");
        }
        this.logger = log;
    }

    @Override
    public void error(String string, Object object) {
        if (this.logger.isErrorEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.error((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void error(String string) {
        this.logger.error((Object)string);
    }

    @Override
    public void error(String string, Object ... objectArray) {
        if (this.logger.isErrorEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.error((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void info(String string, Object ... objectArray) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.logger.info((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Object object) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.debug((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Throwable throwable) {
        this.logger.debug((Object)string, throwable);
    }

    @Override
    public void info(String string) {
        this.logger.info((Object)string);
    }

    @Override
    public void debug(String string) {
        this.logger.debug((Object)string);
    }

    @Override
    public void warn(String string, Throwable throwable) {
        this.logger.warn((Object)string, throwable);
    }

    @Override
    public void warn(String string, Object object) {
        if (this.logger.isWarnEnabled()) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.logger.warn((Object)formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }
}

