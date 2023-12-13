/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util.concurrent;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory
implements ThreadFactory {
    private static final AtomicInteger poolId = new AtomicInteger();
    private final AtomicInteger nextId = new AtomicInteger();
    private final String prefix;
    private final boolean daemon;
    private final int priority;

    public DefaultThreadFactory(Class<?> poolType) {
        this(poolType, false, 5);
    }

    public DefaultThreadFactory(String poolName) {
        this(poolName, false, 5);
    }

    public DefaultThreadFactory(Class<?> poolType, boolean daemon) {
        this(poolType, daemon, 5);
    }

    public DefaultThreadFactory(String poolName, boolean daemon) {
        this(poolName, daemon, 5);
    }

    public DefaultThreadFactory(Class<?> poolType, int priority) {
        this(poolType, false, priority);
    }

    public DefaultThreadFactory(String poolName, int priority) {
        this(poolName, false, priority);
    }

    public DefaultThreadFactory(Class<?> poolType, boolean daemon, int priority) {
        this(DefaultThreadFactory.toPoolName(poolType), daemon, priority);
    }

    private static String toPoolName(Class<?> poolType) {
        if (poolType == null) {
            throw new NullPointerException("poolType");
        }
        Package pkg = poolType.getPackage();
        String poolName = pkg != null ? poolType.getName().substring(pkg.getName().length() + 1) : poolType.getName();
        switch (poolName.length()) {
            case 0: {
                return "unknown";
            }
            case 1: {
                return poolName.toLowerCase(Locale.US);
            }
        }
        if (Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1))) {
            return Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1);
        }
        return poolName;
    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority) {
        if (poolName == null) {
            throw new NullPointerException("poolName");
        }
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }
        this.prefix = poolName + '-' + poolId.incrementAndGet() + '-';
        this.daemon = daemon;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.prefix + this.nextId.incrementAndGet());
        try {
            if (t.isDaemon()) {
                if (!this.daemon) {
                    t.setDaemon(false);
                }
            } else if (this.daemon) {
                t.setDaemon(true);
            }
            if (t.getPriority() != this.priority) {
                t.setPriority(this.priority);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return t;
    }
}

