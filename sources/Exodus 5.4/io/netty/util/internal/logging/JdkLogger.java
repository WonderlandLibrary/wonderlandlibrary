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
    final transient Logger logger;
    static final String SELF = JdkLogger.class.getName();
    static final String SUPER = AbstractInternalLogger.class.getName();
    private static final long serialVersionUID = -1767272577989225979L;

    JdkLogger(Logger logger) {
        super(logger.getName());
        this.logger = logger;
    }

    @Override
    public void trace(String string, Object object, Object object2) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.log(SELF, Level.FINEST, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void error(String string, Throwable throwable) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.log(SELF, Level.SEVERE, string, throwable);
        }
    }

    @Override
    public void trace(String string) {
        if (this.logger.isLoggable(Level.FINEST)) {
            this.log(SELF, Level.FINEST, string, null);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(String string, Object ... objectArray) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.log(SELF, Level.SEVERE, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void info(String string, Object object) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.log(SELF, Level.INFO, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Object object, Object object2) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.log(SELF, Level.FINE, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void warn(String string, Object object) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.log(SELF, Level.WARNING, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.log(SELF, Level.FINE, string, null);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isLoggable(Level.FINE);
    }

    private void log(String string, Level level, String string2, Throwable throwable) {
        LogRecord logRecord = new LogRecord(level, string2);
        logRecord.setLoggerName(this.name());
        logRecord.setThrown(throwable);
        JdkLogger.fillCallerData(string, logRecord);
        this.logger.log(logRecord);
    }

    @Override
    public void info(String string, Object ... objectArray) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.log(SELF, Level.INFO, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void trace(String string, Object object) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.log(SELF, Level.FINEST, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void warn(String string, Throwable throwable) {
        if (this.logger.isLoggable(Level.WARNING)) {
            this.log(SELF, Level.WARNING, string, throwable);
        }
    }

    @Override
    public void trace(String string, Object ... objectArray) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.log(SELF, Level.FINEST, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isLoggable(Level.INFO);
    }

    @Override
    public void error(String string) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.log(SELF, Level.SEVERE, string, null);
        }
    }

    @Override
    public void debug(String string, Object ... objectArray) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.log(SELF, Level.FINE, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void info(String string, Throwable throwable) {
        if (this.logger.isLoggable(Level.INFO)) {
            this.log(SELF, Level.INFO, string, throwable);
        }
    }

    private static void fillCallerData(String string, LogRecord logRecord) {
        int n;
        StackTraceElement[] stackTraceElementArray = new Throwable().getStackTrace();
        int n2 = -1;
        for (n = 0; n < stackTraceElementArray.length; ++n) {
            String string2 = stackTraceElementArray[n].getClassName();
            if (!string2.equals(string) && !string2.equals(SUPER)) continue;
            n2 = n;
            break;
        }
        n = -1;
        for (int i = n2 + 1; i < stackTraceElementArray.length; ++i) {
            String string3 = stackTraceElementArray[i].getClassName();
            if (string3.equals(string) || string3.equals(SUPER)) continue;
            n = i;
            break;
        }
        if (n != -1) {
            StackTraceElement stackTraceElement = stackTraceElementArray[n];
            logRecord.setSourceClassName(stackTraceElement.getClassName());
            logRecord.setSourceMethodName(stackTraceElement.getMethodName());
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isLoggable(Level.WARNING);
    }

    @Override
    public void info(String string, Object object, Object object2) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.log(SELF, Level.INFO, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void warn(String string, Object ... objectArray) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(string, objectArray);
            this.log(SELF, Level.WARNING, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Throwable throwable) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.log(SELF, Level.FINE, string, throwable);
        }
    }

    @Override
    public void info(String string) {
        if (this.logger.isLoggable(Level.INFO)) {
            this.log(SELF, Level.INFO, string, null);
        }
    }

    @Override
    public void warn(String string, Object object, Object object2) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.log(SELF, Level.WARNING, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void debug(String string, Object object) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.log(SELF, Level.FINE, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void error(String string, Object object) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object);
            this.log(SELF, Level.SEVERE, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isLoggable(Level.FINEST);
    }

    @Override
    public void warn(String string) {
        if (this.logger.isLoggable(Level.WARNING)) {
            this.log(SELF, Level.WARNING, string, null);
        }
    }

    @Override
    public void error(String string, Object object, Object object2) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple formattingTuple = MessageFormatter.format(string, object, object2);
            this.log(SELF, Level.SEVERE, formattingTuple.getMessage(), formattingTuple.getThrowable());
        }
    }

    @Override
    public void trace(String string, Throwable throwable) {
        if (this.logger.isLoggable(Level.FINEST)) {
            this.log(SELF, Level.FINEST, string, throwable);
        }
    }
}

