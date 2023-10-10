// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.Executors;
import javax.annotation.CheckReturnValue;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.concurrent.ThreadFactory;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;

@CanIgnoreReturnValue
@GwtIncompatible
public final class ThreadFactoryBuilder
{
    private String nameFormat;
    private Boolean daemon;
    private Integer priority;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private ThreadFactory backingThreadFactory;
    
    public ThreadFactoryBuilder() {
        this.nameFormat = null;
        this.daemon = null;
        this.priority = null;
        this.uncaughtExceptionHandler = null;
        this.backingThreadFactory = null;
    }
    
    public ThreadFactoryBuilder setNameFormat(final String nameFormat) {
        final String unused = format(nameFormat, 0);
        this.nameFormat = nameFormat;
        return this;
    }
    
    public ThreadFactoryBuilder setDaemon(final boolean daemon) {
        this.daemon = daemon;
        return this;
    }
    
    public ThreadFactoryBuilder setPriority(final int priority) {
        Preconditions.checkArgument(priority >= 1, "Thread priority (%s) must be >= %s", priority, 1);
        Preconditions.checkArgument(priority <= 10, "Thread priority (%s) must be <= %s", priority, 10);
        this.priority = priority;
        return this;
    }
    
    public ThreadFactoryBuilder setUncaughtExceptionHandler(final Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = Preconditions.checkNotNull(uncaughtExceptionHandler);
        return this;
    }
    
    public ThreadFactoryBuilder setThreadFactory(final ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = Preconditions.checkNotNull(backingThreadFactory);
        return this;
    }
    
    @CheckReturnValue
    public ThreadFactory build() {
        return build(this);
    }
    
    private static ThreadFactory build(final ThreadFactoryBuilder builder) {
        final String nameFormat = builder.nameFormat;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        final ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory : Executors.defaultThreadFactory();
        final AtomicLong count = (nameFormat != null) ? new AtomicLong(0L) : null;
        return new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable runnable) {
                final Thread thread = backingThreadFactory.newThread(runnable);
                if (nameFormat != null) {
                    thread.setName(format(nameFormat, new Object[] { count.getAndIncrement() }));
                }
                if (daemon != null) {
                    thread.setDaemon(daemon);
                }
                if (priority != null) {
                    thread.setPriority(priority);
                }
                if (uncaughtExceptionHandler != null) {
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }
                return thread;
            }
        };
    }
    
    private static String format(final String format, final Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}
