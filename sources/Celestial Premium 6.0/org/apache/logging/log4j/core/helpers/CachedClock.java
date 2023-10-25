/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import java.util.concurrent.locks.LockSupport;
import org.apache.logging.log4j.core.helpers.Clock;

public final class CachedClock
implements Clock {
    private static final int UPDATE_THRESHOLD = 1023;
    private static CachedClock instance = new CachedClock();
    private volatile long millis = System.currentTimeMillis();
    private volatile short count = 0;
    private final Thread updater = new Thread("Clock Updater Thread"){

        @Override
        public void run() {
            while (true) {
                long time = System.currentTimeMillis();
                CachedClock.this.millis = time;
                LockSupport.parkNanos(1000000L);
            }
        }
    };

    private CachedClock() {
        this.updater.setDaemon(true);
        this.updater.start();
    }

    public static CachedClock instance() {
        return instance;
    }

    @Override
    public long currentTimeMillis() {
        this.count = (short)(this.count + 1);
        if ((this.count & 0x3FF) == 1023) {
            this.millis = System.currentTimeMillis();
        }
        return this.millis;
    }
}

