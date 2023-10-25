/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.core.util.Clock;

public final class SystemMillisClock
implements Clock {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}

