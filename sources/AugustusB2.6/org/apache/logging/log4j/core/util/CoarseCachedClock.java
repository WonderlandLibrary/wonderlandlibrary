// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.concurrent.locks.LockSupport;

public final class CoarseCachedClock implements Clock
{
    private static volatile CoarseCachedClock instance;
    private static final Object INSTANCE_LOCK;
    private volatile long millis;
    private final Thread updater;
    
    private CoarseCachedClock() {
        this.millis = System.currentTimeMillis();
        (this.updater = new Log4jThread("CoarseCachedClock Updater Thread") {
            @Override
            public void run() {
                while (true) {
                    CoarseCachedClock.this.millis = System.currentTimeMillis();
                    LockSupport.parkNanos(1000000L);
                }
            }
        }).setDaemon(true);
        this.updater.start();
    }
    
    public static CoarseCachedClock instance() {
        CoarseCachedClock result = CoarseCachedClock.instance;
        if (result == null) {
            synchronized (CoarseCachedClock.INSTANCE_LOCK) {
                result = CoarseCachedClock.instance;
                if (result == null) {
                    result = (CoarseCachedClock.instance = new CoarseCachedClock());
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
