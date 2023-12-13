// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import org.apache.logging.log4j.status.StatusLogger;

public class LogLog
{
    private static final StatusLogger LOGGER;
    public static final String DEBUG_KEY = "log4j.debug";
    @Deprecated
    public static final String CONFIG_DEBUG_KEY = "log4j.configDebug";
    protected static boolean debugEnabled;
    private static boolean quietMode;
    
    public static void debug(final String message) {
        if (LogLog.debugEnabled && !LogLog.quietMode) {
            LogLog.LOGGER.debug(message);
        }
    }
    
    public static void debug(final String message, final Throwable throwable) {
        if (LogLog.debugEnabled && !LogLog.quietMode) {
            LogLog.LOGGER.debug(message, throwable);
        }
    }
    
    public static void error(final String message) {
        if (!LogLog.quietMode) {
            LogLog.LOGGER.error(message);
        }
    }
    
    public static void error(final String message, final Throwable throwable) {
        if (!LogLog.quietMode) {
            LogLog.LOGGER.error(message, throwable);
        }
    }
    
    public static void setInternalDebugging(final boolean enabled) {
        LogLog.debugEnabled = enabled;
    }
    
    public static void setQuietMode(final boolean quietMode) {
        LogLog.quietMode = quietMode;
    }
    
    public static void warn(final String message) {
        if (!LogLog.quietMode) {
            LogLog.LOGGER.warn(message);
        }
    }
    
    public static void warn(final String message, final Throwable throwable) {
        if (!LogLog.quietMode) {
            LogLog.LOGGER.warn(message, throwable);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        LogLog.debugEnabled = false;
        LogLog.quietMode = false;
        String key = OptionConverter.getSystemProperty("log4j.debug", null);
        if (key == null) {
            key = OptionConverter.getSystemProperty("log4j.configDebug", null);
        }
        if (key != null) {
            LogLog.debugEnabled = OptionConverter.toBoolean(key, true);
        }
    }
}
