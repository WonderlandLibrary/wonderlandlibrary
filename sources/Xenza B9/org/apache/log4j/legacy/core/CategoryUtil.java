// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.legacy.core;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.log4j.bridge.AppenderAdapter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.core.Filter;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import org.apache.logging.log4j.core.config.LoggerConfig;
import java.util.Collections;
import org.apache.logging.log4j.core.Appender;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.logging.log4j.Logger;

public final class CategoryUtil
{
    private static org.apache.logging.log4j.core.Logger asCore(final Logger logger) {
        return (org.apache.logging.log4j.core.Logger)logger;
    }
    
    private static <T> T get(final Logger logger, final Supplier<T> run, final T defaultValue) {
        return isCore(logger) ? run.get() : defaultValue;
    }
    
    public static Map<String, Appender> getAppenders(final Logger logger) {
        return get(logger, () -> getDirectAppenders(logger), Collections.emptyMap());
    }
    
    private static Map<String, Appender> getDirectAppenders(final Logger logger) {
        return getExactLoggerConfig(logger).map((Function<? super LoggerConfig, ? extends Map<String, Appender>>)LoggerConfig::getAppenders).orElse(Collections.emptyMap());
    }
    
    private static Optional<LoggerConfig> getExactLoggerConfig(final Logger logger) {
        return Optional.of(asCore(logger).get()).filter(lc -> logger.getName().equals(lc.getName()));
    }
    
    public static Iterator<Filter> getFilters(final Logger logger) {
        return get(logger, asCore(logger)::getFilters, null);
    }
    
    public static LoggerContext getLoggerContext(final Logger logger) {
        return get(logger, asCore(logger)::getContext, null);
    }
    
    public static Logger getParent(final Logger logger) {
        return get(logger, asCore(logger)::getParent, null);
    }
    
    public static boolean isAdditive(final Logger logger) {
        return get(logger, asCore(logger)::isAdditive, false);
    }
    
    private static boolean isCore(final Logger logger) {
        return logger instanceof org.apache.logging.log4j.core.Logger;
    }
    
    public static void setAdditivity(final Logger logger, final boolean additive) {
        if (isCore(logger)) {
            asCore(logger).setAdditive(additive);
        }
    }
    
    public static void setLevel(final Logger logger, final Level level) {
        if (isCore(logger)) {
            asCore(logger).setLevel(level);
        }
    }
    
    public static void addAppender(final Logger logger, final Appender appender) {
        if (appender instanceof AppenderAdapter.Adapter) {
            appender.start();
        }
        asCore(logger).addAppender(appender);
    }
    
    public static void log(final Logger logger, final LogEvent event) {
        getExactLoggerConfig(logger).ifPresent(lc -> lc.log(event));
    }
    
    private CategoryUtil() {
    }
}
