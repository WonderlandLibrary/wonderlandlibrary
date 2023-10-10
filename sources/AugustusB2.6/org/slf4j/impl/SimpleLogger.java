// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import java.util.Date;
import java.io.PrintStream;
import org.slf4j.helpers.MarkerIgnoringBase;

public class SimpleLogger extends MarkerIgnoringBase
{
    private static final long serialVersionUID = -632788891211436180L;
    private static long START_TIME;
    protected static final int LOG_LEVEL_TRACE = 0;
    protected static final int LOG_LEVEL_DEBUG = 10;
    protected static final int LOG_LEVEL_INFO = 20;
    protected static final int LOG_LEVEL_WARN = 30;
    protected static final int LOG_LEVEL_ERROR = 40;
    protected static final int LOG_LEVEL_OFF = 50;
    private static boolean INITIALIZED;
    static SimpleLoggerConfiguration CONFIG_PARAMS;
    protected int currentLogLevel;
    private transient String shortLogName;
    public static final String SYSTEM_PREFIX = "org.slf4j.simpleLogger.";
    public static final String LOG_KEY_PREFIX = "org.slf4j.simpleLogger.log.";
    public static final String CACHE_OUTPUT_STREAM_STRING_KEY = "org.slf4j.simpleLogger.cacheOutputStream";
    public static final String WARN_LEVEL_STRING_KEY = "org.slf4j.simpleLogger.warnLevelString";
    public static final String LEVEL_IN_BRACKETS_KEY = "org.slf4j.simpleLogger.levelInBrackets";
    public static final String LOG_FILE_KEY = "org.slf4j.simpleLogger.logFile";
    public static final String SHOW_SHORT_LOG_NAME_KEY = "org.slf4j.simpleLogger.showShortLogName";
    public static final String SHOW_LOG_NAME_KEY = "org.slf4j.simpleLogger.showLogName";
    public static final String SHOW_THREAD_NAME_KEY = "org.slf4j.simpleLogger.showThreadName";
    public static final String DATE_TIME_FORMAT_KEY = "org.slf4j.simpleLogger.dateTimeFormat";
    public static final String SHOW_DATE_TIME_KEY = "org.slf4j.simpleLogger.showDateTime";
    public static final String DEFAULT_LOG_LEVEL_KEY = "org.slf4j.simpleLogger.defaultLogLevel";
    
    static void lazyInit() {
        if (SimpleLogger.INITIALIZED) {
            return;
        }
        SimpleLogger.INITIALIZED = true;
        init();
    }
    
    static void init() {
        (SimpleLogger.CONFIG_PARAMS = new SimpleLoggerConfiguration()).init();
    }
    
    SimpleLogger(final String name) {
        this.currentLogLevel = 20;
        this.shortLogName = null;
        this.name = name;
        final String levelString = this.recursivelyComputeLevelString();
        if (levelString != null) {
            this.currentLogLevel = SimpleLoggerConfiguration.stringToLevel(levelString);
        }
        else {
            this.currentLogLevel = SimpleLogger.CONFIG_PARAMS.defaultLogLevel;
        }
    }
    
    String recursivelyComputeLevelString() {
        String tempName = this.name;
        String levelString = null;
        for (int indexOfLastDot = tempName.length(); levelString == null && indexOfLastDot > -1; levelString = SimpleLogger.CONFIG_PARAMS.getStringProperty("org.slf4j.simpleLogger.log." + tempName, null), indexOfLastDot = String.valueOf(tempName).lastIndexOf(".")) {
            tempName = tempName.substring(0, indexOfLastDot);
        }
        return levelString;
    }
    
    private void log(final int level, final String message, final Throwable t) {
        if (!this.isLevelEnabled(level)) {
            return;
        }
        final StringBuilder buf = new StringBuilder(32);
        if (SimpleLogger.CONFIG_PARAMS.showDateTime) {
            if (SimpleLogger.CONFIG_PARAMS.dateFormatter != null) {
                buf.append(this.getFormattedDate());
                buf.append(' ');
            }
            else {
                buf.append(System.currentTimeMillis() - SimpleLogger.START_TIME);
                buf.append(' ');
            }
        }
        if (SimpleLogger.CONFIG_PARAMS.showThreadName) {
            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
        }
        if (SimpleLogger.CONFIG_PARAMS.levelInBrackets) {
            buf.append('[');
        }
        final String levelStr = this.renderLevel(level);
        buf.append(levelStr);
        if (SimpleLogger.CONFIG_PARAMS.levelInBrackets) {
            buf.append(']');
        }
        buf.append(' ');
        if (SimpleLogger.CONFIG_PARAMS.showShortLogName) {
            if (this.shortLogName == null) {
                this.shortLogName = this.computeShortName();
            }
            buf.append(String.valueOf(this.shortLogName)).append(" - ");
        }
        else if (SimpleLogger.CONFIG_PARAMS.showLogName) {
            buf.append(String.valueOf(this.name)).append(" - ");
        }
        buf.append(message);
        this.write(buf, t);
    }
    
    protected String renderLevel(final int level) {
        switch (level) {
            case 0: {
                return "TRACE";
            }
            case 10: {
                return "DEBUG";
            }
            case 20: {
                return "INFO";
            }
            case 30: {
                return SimpleLogger.CONFIG_PARAMS.warnLevelString;
            }
            case 40: {
                return "ERROR";
            }
            default: {
                throw new IllegalStateException("Unrecognized level [" + level + "]");
            }
        }
    }
    
    void write(final StringBuilder buf, final Throwable t) {
        final PrintStream targetStream = SimpleLogger.CONFIG_PARAMS.outputChoice.getTargetPrintStream();
        targetStream.println(buf.toString());
        this.writeThrowable(t, targetStream);
        targetStream.flush();
    }
    
    protected void writeThrowable(final Throwable t, final PrintStream targetStream) {
        if (t != null) {
            t.printStackTrace(targetStream);
        }
    }
    
    private String getFormattedDate() {
        final Date now = new Date();
        final String dateText;
        synchronized (SimpleLogger.CONFIG_PARAMS.dateFormatter) {
            dateText = SimpleLogger.CONFIG_PARAMS.dateFormatter.format(now);
        }
        return dateText;
    }
    
    private String computeShortName() {
        return this.name.substring(this.name.lastIndexOf(".") + 1);
    }
    
    private void formatAndLog(final int level, final String format, final Object arg1, final Object arg2) {
        if (!this.isLevelEnabled(level)) {
            return;
        }
        final FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        this.log(level, tp.getMessage(), tp.getThrowable());
    }
    
    private void formatAndLog(final int level, final String format, final Object... arguments) {
        if (!this.isLevelEnabled(level)) {
            return;
        }
        final FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        this.log(level, tp.getMessage(), tp.getThrowable());
    }
    
    protected boolean isLevelEnabled(final int logLevel) {
        return logLevel >= this.currentLogLevel;
    }
    
    public boolean isTraceEnabled() {
        return this.isLevelEnabled(0);
    }
    
    public void trace(final String msg) {
        this.log(0, msg, null);
    }
    
    public void trace(final String format, final Object param1) {
        this.formatAndLog(0, format, param1, null);
    }
    
    public void trace(final String format, final Object param1, final Object param2) {
        this.formatAndLog(0, format, param1, param2);
    }
    
    public void trace(final String format, final Object... argArray) {
        this.formatAndLog(0, format, argArray);
    }
    
    public void trace(final String msg, final Throwable t) {
        this.log(0, msg, t);
    }
    
    public boolean isDebugEnabled() {
        return this.isLevelEnabled(10);
    }
    
    public void debug(final String msg) {
        this.log(10, msg, null);
    }
    
    public void debug(final String format, final Object param1) {
        this.formatAndLog(10, format, param1, null);
    }
    
    public void debug(final String format, final Object param1, final Object param2) {
        this.formatAndLog(10, format, param1, param2);
    }
    
    public void debug(final String format, final Object... argArray) {
        this.formatAndLog(10, format, argArray);
    }
    
    public void debug(final String msg, final Throwable t) {
        this.log(10, msg, t);
    }
    
    public boolean isInfoEnabled() {
        return this.isLevelEnabled(20);
    }
    
    public void info(final String msg) {
        this.log(20, msg, null);
    }
    
    public void info(final String format, final Object arg) {
        this.formatAndLog(20, format, arg, null);
    }
    
    public void info(final String format, final Object arg1, final Object arg2) {
        this.formatAndLog(20, format, arg1, arg2);
    }
    
    public void info(final String format, final Object... argArray) {
        this.formatAndLog(20, format, argArray);
    }
    
    public void info(final String msg, final Throwable t) {
        this.log(20, msg, t);
    }
    
    public boolean isWarnEnabled() {
        return this.isLevelEnabled(30);
    }
    
    public void warn(final String msg) {
        this.log(30, msg, null);
    }
    
    public void warn(final String format, final Object arg) {
        this.formatAndLog(30, format, arg, null);
    }
    
    public void warn(final String format, final Object arg1, final Object arg2) {
        this.formatAndLog(30, format, arg1, arg2);
    }
    
    public void warn(final String format, final Object... argArray) {
        this.formatAndLog(30, format, argArray);
    }
    
    public void warn(final String msg, final Throwable t) {
        this.log(30, msg, t);
    }
    
    public boolean isErrorEnabled() {
        return this.isLevelEnabled(40);
    }
    
    public void error(final String msg) {
        this.log(40, msg, null);
    }
    
    public void error(final String format, final Object arg) {
        this.formatAndLog(40, format, arg, null);
    }
    
    public void error(final String format, final Object arg1, final Object arg2) {
        this.formatAndLog(40, format, arg1, arg2);
    }
    
    public void error(final String format, final Object... argArray) {
        this.formatAndLog(40, format, argArray);
    }
    
    public void error(final String msg, final Throwable t) {
        this.log(40, msg, t);
    }
    
    public void log(final LoggingEvent event) {
        final int levelInt = event.getLevel().toInt();
        if (!this.isLevelEnabled(levelInt)) {
            return;
        }
        final FormattingTuple tp = MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray(), event.getThrowable());
        this.log(levelInt, tp.getMessage(), event.getThrowable());
    }
    
    static {
        SimpleLogger.START_TIME = System.currentTimeMillis();
        SimpleLogger.INITIALIZED = false;
        SimpleLogger.CONFIG_PARAMS = null;
    }
}
