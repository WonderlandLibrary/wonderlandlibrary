// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public class Log4jThreadFactory implements ThreadFactory
{
    private static final String PREFIX = "TF-";
    private static final AtomicInteger FACTORY_NUMBER;
    private static final AtomicInteger THREAD_NUMBER;
    private final boolean daemon;
    private final ThreadGroup group;
    private final int priority;
    private final String threadNamePrefix;
    
    public static Log4jThreadFactory createDaemonThreadFactory(final String threadFactoryName) {
        return new Log4jThreadFactory(threadFactoryName, true, 5);
    }
    
    public static Log4jThreadFactory createThreadFactory(final String threadFactoryName) {
        return new Log4jThreadFactory(threadFactoryName, false, 5);
    }
    
    public Log4jThreadFactory(final String threadFactoryName, final boolean daemon, final int priority) {
        this.threadNamePrefix = "TF-" + Log4jThreadFactory.FACTORY_NUMBER.getAndIncrement() + "-" + threadFactoryName + "-";
        this.daemon = daemon;
        this.priority = priority;
        final SecurityManager securityManager = System.getSecurityManager();
        this.group = ((securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup());
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = new Log4jThread(this.group, runnable, this.threadNamePrefix + Log4jThreadFactory.THREAD_NUMBER.getAndIncrement(), 0L);
        if (thread.isDaemon() != this.daemon) {
            thread.setDaemon(this.daemon);
        }
        if (thread.getPriority() != this.priority) {
            thread.setPriority(this.priority);
        }
        return thread;
    }
    
    static {
        FACTORY_NUMBER = new AtomicInteger(1);
        THREAD_NUMBER = new AtomicInteger(1);
    }
}
