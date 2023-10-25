/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.time.Instant;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.time.PreciseClock;
import org.apache.logging.log4j.core.util.Clock;

public final class SystemClock
implements Clock,
PreciseClock {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public void init(MutableInstant mutableInstant) {
        Instant instant = java.time.Clock.systemUTC().instant();
        mutableInstant.initFromEpochSecond(instant.getEpochSecond(), instant.getNano());
    }
}

