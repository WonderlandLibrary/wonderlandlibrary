/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.AbstractInternalLogger;
import io.netty.util.internal.logging.FormattingTuple;
import io.netty.util.internal.logging.MessageFormatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class JdkLogger
extends AbstractInternalLogger {
    private static final long serialVersionUID = -1767272577989225979L;
    final transient Logger logger;
    static final String SELF = JdkLogger.class.getName();
    static final String SUPER = AbstractInternalLogger.class.getName();

    JdkLogger(Logger logger) {
        super(logger.getName());
        this.logger = logger;
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isLoggable(Level.FINEST);
    }

    @Override
    public void trace(String msg) {
        if (this.logger.isLoggable(Level.FINEST)) {
            this.log(SELF, Level.FINEST, msg, null);
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void trace(String format, Object argA, Object argB) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void trace(String format, Object ... argArray) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void trace(String msg, Throwable t2) {
        if (this.logger.isLoggable(Level.FINEST)) {
            this.log(SELF, Level.FINEST, msg, t2);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isLoggable(Level.FINE);
    }

    @Override
    public void debug(String msg) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.log(SELF, Level.FINE, msg, null);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void debug(String format, Object argA, Object argB) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void debug(String format, Object ... argArray) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void debug(String msg, Throwable t2) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.log(SELF, Level.FINE, msg, t2);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isLoggable(Level.INFO);
    }

    @Override
    public void info(String msg) {
        if (this.logger.isLoggable(Level.INFO)) {
            this.log(SELF, Level.INFO, msg, null);
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void info(String format, Object argA, Object argB) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void info(String format, Object ... argArray) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void info(String msg, Throwable t2) {
        if (this.logger.isLoggable(Level.INFO)) {
            this.log(SELF, Level.INFO, msg, t2);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(String msg) {
        if (this.logger.isLoggable(Level.WARNING)) {
            this.log(SELF, Level.WARNING, msg, null);
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void warn(String format, Object argA, Object argB) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void warn(String format, Object ... argArray) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void warn(String msg, Throwable t2) {
        if (this.logger.isLoggable(Level.WARNING)) {
            this.log(SELF, Level.WARNING, msg, t2);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(String msg) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.log(SELF, Level.SEVERE, msg, null);
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void error(String format, Object argA, Object argB) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void error(String format, Object ... arguments) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void error(String msg, Throwable t2) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.log(SELF, Level.SEVERE, msg, t2);
        }
    }

    private void log(String callerFQCN, Level level, String msg, Throwable t2) {
        LogRecord record = new LogRecord(level, msg);
        record.setLoggerName(this.name());
        record.setThrown(t2);
        JdkLogger.fillCallerData(callerFQCN, record);
        this.logger.log(record);
    }

    private static void fillCallerData(String callerFQCN, LogRecord record) {
        StackTraceElement[] steArray = new Throwable().getStackTrace();
        int selfIndex = -1;
        for (int i2 = 0; i2 < steArray.length; ++i2) {
            String className = steArray[i2].getClassName();
            if (!className.equals(callerFQCN) && !className.equals(SUPER)) continue;
            selfIndex = i2;
            break;
        }
        int found = -1;
        for (int i3 = selfIndex + 1; i3 < steArray.length; ++i3) {
            String className = steArray[i3].getClassName();
            if (className.equals(callerFQCN) || className.equals(SUPER)) continue;
            found = i3;
            break;
        }
        if (found != -1) {
            StackTraceElement ste = steArray[found];
            record.setSourceClassName(ste.getClassName());
            record.setSourceMethodName(ste.getMethodName());
        }
    }
}

