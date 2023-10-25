/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.LoggerAdapter;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextShutdownAware;
import org.apache.logging.log4j.spi.LoggerContextShutdownEnabled;
import org.apache.logging.log4j.util.LoaderUtil;

public abstract class AbstractLoggerAdapter<L>
implements LoggerAdapter<L>,
LoggerContextShutdownAware {
    protected final Map<LoggerContext, ConcurrentMap<String, L>> registry = new ConcurrentHashMap<LoggerContext, ConcurrentMap<String, L>>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Override
    public L getLogger(String name) {
        LoggerContext context = this.getContext();
        ConcurrentMap<String, L> loggers = this.getLoggersInContext(context);
        Object logger = loggers.get(name);
        if (logger != null) {
            return (L)logger;
        }
        loggers.putIfAbsent(name, this.newLogger(name, context));
        return (L)loggers.get(name);
    }

    @Override
    public void contextShutdown(LoggerContext loggerContext) {
        this.registry.remove(loggerContext);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ConcurrentMap<String, L> getLoggersInContext(LoggerContext context) {
        ConcurrentMap<String, L> loggers;
        this.lock.readLock().lock();
        try {
            loggers = this.registry.get(context);
        }
        finally {
            this.lock.readLock().unlock();
        }
        if (loggers != null) {
            return loggers;
        }
        this.lock.writeLock().lock();
        try {
            loggers = this.registry.get(context);
            if (loggers == null) {
                loggers = new ConcurrentHashMap<String, L>();
                this.registry.put(context, loggers);
                if (context instanceof LoggerContextShutdownEnabled) {
                    ((LoggerContextShutdownEnabled)((Object)context)).addShutdownListener(this);
                }
            }
            ConcurrentMap<String, L> concurrentMap = loggers;
            return concurrentMap;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }

    public Set<LoggerContext> getLoggerContexts() {
        return new HashSet<LoggerContext>(this.registry.keySet());
    }

    protected abstract L newLogger(String var1, LoggerContext var2);

    protected abstract LoggerContext getContext();

    protected LoggerContext getContext(Class<?> callerClass) {
        ClassLoader cl = null;
        if (callerClass != null) {
            cl = callerClass.getClassLoader();
        }
        if (cl == null) {
            cl = LoaderUtil.getThreadContextClassLoader();
        }
        return LogManager.getContext(cl, false);
    }

    @Override
    public void close() {
        this.lock.writeLock().lock();
        try {
            this.registry.clear();
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
}

