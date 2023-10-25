/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractManager {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final Map<String, AbstractManager> MAP = new HashMap<String, AbstractManager>();
    private static final Lock LOCK = new ReentrantLock();
    protected int count;
    private final String name;

    protected AbstractManager(String name) {
        this.name = name;
        LOGGER.debug("Starting {} {}", this.getClass().getSimpleName(), name);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static <M extends AbstractManager, T> M getManager(String name, ManagerFactory<M, T> factory, T data) {
        LOCK.lock();
        try {
            AbstractManager manager = MAP.get(name);
            if (manager == null) {
                manager = (AbstractManager)factory.createManager(name, data);
                if (manager == null) {
                    throw new IllegalStateException("Unable to create a manager");
                }
                MAP.put(name, manager);
            }
            ++manager.count;
            AbstractManager abstractManager = manager;
            return (M)abstractManager;
        }
        finally {
            LOCK.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasManager(String name) {
        LOCK.lock();
        try {
            boolean bl = MAP.containsKey(name);
            return bl;
        }
        finally {
            LOCK.unlock();
        }
    }

    protected void releaseSub() {
    }

    protected int getCount() {
        return this.count;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void release() {
        LOCK.lock();
        try {
            --this.count;
            if (this.count <= 0) {
                MAP.remove(this.name);
                LOGGER.debug("Shutting down {} {}", this.getClass().getSimpleName(), this.getName());
                this.releaseSub();
            }
        }
        finally {
            LOCK.unlock();
        }
    }

    public String getName() {
        return this.name;
    }

    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
}

