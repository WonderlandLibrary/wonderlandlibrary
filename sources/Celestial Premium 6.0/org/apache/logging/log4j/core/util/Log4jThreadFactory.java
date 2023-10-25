/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.core.util.Log4jThread;

public class Log4jThreadFactory
implements ThreadFactory {
    private static final String PREFIX = "TF-";
    private static final AtomicInteger FACTORY_NUMBER = new AtomicInteger(1);
    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
    private final boolean daemon;
    private final ThreadGroup group;
    private final int priority;
    private final String threadNamePrefix;

    public static Log4jThreadFactory createDaemonThreadFactory(String threadFactoryName) {
        return new Log4jThreadFactory(threadFactoryName, true, 5);
    }

    public static Log4jThreadFactory createThreadFactory(String threadFactoryName) {
        return new Log4jThreadFactory(threadFactoryName, false, 5);
    }

    public Log4jThreadFactory(String threadFactoryName, boolean daemon, int priority) {
        this.threadNamePrefix = PREFIX + FACTORY_NUMBER.getAndIncrement() + "-" + threadFactoryName + "-";
        this.daemon = daemon;
        this.priority = priority;
        SecurityManager securityManager = System.getSecurityManager();
        this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Log4jThread thread = new Log4jThread(this.group, runnable, this.threadNamePrefix + THREAD_NUMBER.getAndIncrement(), 0L);
        if (thread.isDaemon() != this.daemon) {
            thread.setDaemon(this.daemon);
        }
        if (thread.getPriority() != this.priority) {
            thread.setPriority(this.priority);
        }
        return thread;
    }
}

