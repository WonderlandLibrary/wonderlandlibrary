/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.core.util.NanoClock;

public final class SystemNanoClock
implements NanoClock {
    @Override
    public long nanoTime() {
        return System.nanoTime();
    }
}

