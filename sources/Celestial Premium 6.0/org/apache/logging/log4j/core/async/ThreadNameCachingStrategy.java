/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.async;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.PropertiesUtil;

public enum ThreadNameCachingStrategy {
    CACHED{

        @Override
        public String getThreadName() {
            String result = (String)THREADLOCAL_NAME.get();
            if (result == null) {
                result = Thread.currentThread().getName();
                THREADLOCAL_NAME.set(result);
            }
            return result;
        }
    }
    ,
    UNCACHED{

        @Override
        public String getThreadName() {
            return Thread.currentThread().getName();
        }
    };

    private static final StatusLogger LOGGER;
    private static final ThreadLocal<String> THREADLOCAL_NAME;
    static final ThreadNameCachingStrategy DEFAULT_STRATEGY;

    abstract String getThreadName();

    public static ThreadNameCachingStrategy create() {
        String name = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ThreadNameStrategy");
        try {
            ThreadNameCachingStrategy result = name != null ? ThreadNameCachingStrategy.valueOf(name) : DEFAULT_STRATEGY;
            LOGGER.debug("AsyncLogger.ThreadNameStrategy={} (user specified {}, default is {})", result.name(), name, DEFAULT_STRATEGY.name());
            return result;
        }
        catch (Exception ex) {
            LOGGER.debug("Using AsyncLogger.ThreadNameStrategy.{}: '{}' not valid: {}", DEFAULT_STRATEGY.name(), name, ex.toString());
            return DEFAULT_STRATEGY;
        }
    }

    static boolean isAllocatingThreadGetName() {
        if (Constants.JAVA_MAJOR_VERSION == 8) {
            try {
                Pattern javaVersionPattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)_(\\d+)");
                Matcher m = javaVersionPattern.matcher(System.getProperty("java.version"));
                if (m.matches()) {
                    return Integer.parseInt(m.group(3)) == 0 && Integer.parseInt(m.group(4)) < 102;
                }
                return true;
            }
            catch (Exception e) {
                return true;
            }
        }
        return Constants.JAVA_MAJOR_VERSION < 8;
    }

    static {
        LOGGER = StatusLogger.getLogger();
        THREADLOCAL_NAME = new ThreadLocal();
        DEFAULT_STRATEGY = ThreadNameCachingStrategy.isAllocatingThreadGetName() ? CACHED : UNCACHED;
    }
}

