/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.concurrent.locks.LockSupport;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.Log4jThread;

public final class CachedClock
implements Clock {
    private static final int UPDATE_THRESHOLD = 1000;
    private static volatile CachedClock instance;
    private static final Object INSTANCE_LOCK;
    private volatile long millis = System.currentTimeMillis();
    private short count = 0;

    private CachedClock() {
        Log4jThread updater = new Log4jThread(() -> {
            while (true) {
                long time;
                this.millis = time = System.currentTimeMillis();
                LockSupport.parkNanos(1000000L);
            }
        }, "CachedClock Updater Thread");
        updater.setDaemon(true);
        updater.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static CachedClock instance() {
        CachedClock result = instance;
        if (result == null) {
            Object object = INSTANCE_LOCK;
            synchronized (object) {
                result = instance;
                if (result == null) {
                    instance = result = new CachedClock();
                }
            }
        }
        return result;
    }

    @Override
    public long currentTimeMillis() {
        this.count = (short)(this.count + 1);
        if (this.count > 1000) {
            this.millis = System.currentTimeMillis();
            this.count = 0;
        }
        return this.millis;
    }

    static {
        INSTANCE_LOCK = new Object();
    }
}

