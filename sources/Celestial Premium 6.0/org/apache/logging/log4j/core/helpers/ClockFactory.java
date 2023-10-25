/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.core.helpers.CachedClock;
import org.apache.logging.log4j.core.helpers.Clock;
import org.apache.logging.log4j.core.helpers.CoarseCachedClock;
import org.apache.logging.log4j.core.helpers.SystemClock;
import org.apache.logging.log4j.status.StatusLogger;

public final class ClockFactory {
    public static final String PROPERTY_NAME = "log4j.Clock";
    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    private ClockFactory() {
    }

    public static Clock getClock() {
        return ClockFactory.createClock();
    }

    private static Clock createClock() {
        String userRequest = System.getProperty(PROPERTY_NAME);
        if (userRequest == null || "SystemClock".equals(userRequest)) {
            LOGGER.debug("Using default SystemClock for timestamps");
            return new SystemClock();
        }
        if (CachedClock.class.getName().equals(userRequest) || "CachedClock".equals(userRequest)) {
            LOGGER.debug("Using specified CachedClock for timestamps");
            return CachedClock.instance();
        }
        if (CoarseCachedClock.class.getName().equals(userRequest) || "CoarseCachedClock".equals(userRequest)) {
            LOGGER.debug("Using specified CoarseCachedClock for timestamps");
            return CoarseCachedClock.instance();
        }
        try {
            Clock result = (Clock)Class.forName(userRequest).newInstance();
            LOGGER.debug("Using {} for timestamps", userRequest);
            return result;
        }
        catch (Exception e) {
            String fmt = "Could not create {}: {}, using default SystemClock for timestamps";
            LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps", userRequest, e);
            return new SystemClock();
        }
    }
}

