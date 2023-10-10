// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.core.time.PreciseClock;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.HashMap;
import org.apache.logging.log4j.util.Supplier;
import java.util.Map;
import org.apache.logging.log4j.status.StatusLogger;

public final class ClockFactory
{
    public static final String PROPERTY_NAME = "log4j.Clock";
    private static final StatusLogger LOGGER;
    
    private ClockFactory() {
    }
    
    public static Clock getClock() {
        return createClock();
    }
    
    private static Map<String, Supplier<Clock>> aliases() {
        final Map<String, Supplier<Clock>> result = new HashMap<String, Supplier<Clock>>();
        result.put("SystemClock", (Supplier<Clock>)SystemClock::new);
        result.put("SystemMillisClock", (Supplier<Clock>)SystemMillisClock::new);
        result.put("CachedClock", (Supplier<Clock>)CachedClock::instance);
        result.put("CoarseCachedClock", (Supplier<Clock>)CoarseCachedClock::instance);
        result.put("org.apache.logging.log4j.core.util.CachedClock", (Supplier<Clock>)CachedClock::instance);
        result.put("org.apache.logging.log4j.core.util.CoarseCachedClock", (Supplier<Clock>)CoarseCachedClock::instance);
        return result;
    }
    
    private static Clock createClock() {
        final String userRequest = PropertiesUtil.getProperties().getStringProperty("log4j.Clock");
        if (userRequest == null) {
            ClockFactory.LOGGER.trace("Using default SystemClock for timestamps.");
            return logSupportedPrecision(new SystemClock());
        }
        final Supplier<Clock> specified = aliases().get(userRequest);
        if (specified != null) {
            ClockFactory.LOGGER.trace("Using specified {} for timestamps.", userRequest);
            return logSupportedPrecision(specified.get());
        }
        try {
            final Clock result = Loader.newCheckedInstanceOf(userRequest, Clock.class);
            ClockFactory.LOGGER.trace("Using {} for timestamps.", result.getClass().getName());
            return logSupportedPrecision(result);
        }
        catch (Exception e) {
            final String fmt = "Could not create {}: {}, using default SystemClock for timestamps.";
            ClockFactory.LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps.", userRequest, e);
            return logSupportedPrecision(new SystemClock());
        }
    }
    
    private static Clock logSupportedPrecision(final Clock clock) {
        final String support = (clock instanceof PreciseClock) ? "supports" : "does not support";
        ClockFactory.LOGGER.debug("{} {} precise timestamps.", clock.getClass().getName(), support);
        return clock;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
