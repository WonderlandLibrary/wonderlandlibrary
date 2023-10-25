/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.concurrent.locks.LockSupport;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.Log4jThread;

public final class CoarseCachedClock
implements Clock {
    private static volatile CoarseCachedClock instance;
    private static final Object INSTANCE_LOCK;
    private volatile long millis = System.currentTimeMillis();
    private final Thread updater = new Log4jThread("CoarseCachedClock Updater Thread"){

        @Override
        public void run() {
            while (true) {
                CoarseCachedClock.this.millis = System.currentTimeMillis();
                LockSupport.parkNanos(1000000L);
            }
        }
    };

    private CoarseCachedClock() {
        this.updater.setDaemon(true);
        this.updater.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static CoarseCachedClock instance() {
        CoarseCachedClock result = instance;
        if (result == null) {
            Object object = INSTANCE_LOCK;
            synchronized (object) {
                result = instance;
                if (result == null) {
                    instance = result = new CoarseCachedClock();
                }
            }
        }
        return result;
    }

    @Override
    public long currentTimeMillis() {
        return this.millis;
    }

    static {
        INSTANCE_LOCK = new Object();
    }
}

