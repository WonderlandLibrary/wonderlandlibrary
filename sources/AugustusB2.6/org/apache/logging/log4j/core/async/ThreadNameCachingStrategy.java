// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;

public enum ThreadNameCachingStrategy
{
    CACHED {
        public String getThreadName() {
            String result = ThreadNameCachingStrategy.THREADLOCAL_NAME.get();
            if (result == null) {
                result = Thread.currentThread().getName();
                ThreadNameCachingStrategy.THREADLOCAL_NAME.set(result);
            }
            return result;
        }
    }, 
    UNCACHED {
        public String getThreadName() {
            return Thread.currentThread().getName();
        }
    };
    
    private static final StatusLogger LOGGER;
    private static final ThreadLocal<String> THREADLOCAL_NAME;
    static final ThreadNameCachingStrategy DEFAULT_STRATEGY;
    
    abstract String getThreadName();
    
    public static ThreadNameCachingStrategy create() {
        final String name = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ThreadNameStrategy");
        try {
            final ThreadNameCachingStrategy result = (name != null) ? valueOf(name) : ThreadNameCachingStrategy.DEFAULT_STRATEGY;
            ThreadNameCachingStrategy.LOGGER.debug("AsyncLogger.ThreadNameStrategy={} (user specified {}, default is {})", result.name(), name, ThreadNameCachingStrategy.DEFAULT_STRATEGY.name());
            return result;
        }
        catch (Exception ex) {
            ThreadNameCachingStrategy.LOGGER.debug("Using AsyncLogger.ThreadNameStrategy.{}: '{}' not valid: {}", ThreadNameCachingStrategy.DEFAULT_STRATEGY.name(), name, ex.toString());
            return ThreadNameCachingStrategy.DEFAULT_STRATEGY;
        }
    }
    
    static boolean isAllocatingThreadGetName() {
        if (Constants.JAVA_MAJOR_VERSION == 8) {
            try {
                final Pattern javaVersionPattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)_(\\d+)");
                final Matcher m = javaVersionPattern.matcher(System.getProperty("java.version"));
                return !m.matches() || (Integer.parseInt(m.group(3)) == 0 && Integer.parseInt(m.group(4)) < 102);
            }
            catch (Exception e) {
                return true;
            }
        }
        return Constants.JAVA_MAJOR_VERSION < 8;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        THREADLOCAL_NAME = new ThreadLocal<String>();
        DEFAULT_STRATEGY = (isAllocatingThreadGetName() ? ThreadNameCachingStrategy.CACHED : ThreadNameCachingStrategy.UNCACHED);
    }
}
