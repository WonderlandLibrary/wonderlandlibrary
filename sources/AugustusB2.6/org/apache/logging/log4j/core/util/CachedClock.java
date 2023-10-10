// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.concurrent.locks.LockSupport;

public final class CachedClock implements Clock
{
    private static final int UPDATE_THRESHOLD = 1000;
    private static volatile CachedClock instance;
    private static final Object INSTANCE_LOCK;
    private volatile long millis;
    private short count;
    
    private CachedClock() {
        this.millis = System.currentTimeMillis();
        this.count = 0;
        long time;
        final Thread updater = new Log4jThread(() -> {
            while (true) {
                time = System.currentTimeMillis();
                this.millis = time;
                LockSupport.parkNanos(1000000L);
            }
        }, "CachedClock Updater Thread");
        updater.setDaemon(true);
        updater.start();
    }
    
    public static CachedClock instance() {
        CachedClock result = CachedClock.instance;
        if (result == null) {
            synchronized (CachedClock.INSTANCE_LOCK) {
                result = CachedClock.instance;
                if (result == null) {
                    result = (CachedClock.instance = new CachedClock());
                }
            }
        }
        return result;
    }
    
    @Override
    public long currentTimeMillis() {
        final short count = (short)(this.count + 1);
        this.count = count;
        if (count > 1000) {
            this.millis = System.currentTimeMillis();
            this.count = 0;
        }
        return this.millis;
    }
    
    static {
        INSTANCE_LOCK = new Object();
    }
}
