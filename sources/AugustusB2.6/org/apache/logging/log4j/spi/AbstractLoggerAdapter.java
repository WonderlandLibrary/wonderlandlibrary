// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;

public abstract class AbstractLoggerAdapter<L> implements LoggerAdapter<L>, LoggerContextShutdownAware
{
    protected final Map<LoggerContext, ConcurrentMap<String, L>> registry;
    private final ReadWriteLock lock;
    
    public AbstractLoggerAdapter() {
        this.registry = new ConcurrentHashMap<LoggerContext, ConcurrentMap<String, L>>();
        this.lock = new ReentrantReadWriteLock(true);
    }
    
    @Override
    public L getLogger(final String name) {
        final LoggerContext context = this.getContext();
        final ConcurrentMap<String, L> loggers = this.getLoggersInContext(context);
        final L logger = loggers.get(name);
        if (logger != null) {
            return logger;
        }
        loggers.putIfAbsent(name, this.newLogger(name, context));
        return loggers.get(name);
    }
    
    @Override
    public void contextShutdown(final LoggerContext loggerContext) {
        this.registry.remove(loggerContext);
    }
    
    public ConcurrentMap<String, L> getLoggersInContext(final LoggerContext context) {
        this.lock.readLock().lock();
        ConcurrentMap<String, L> loggers;
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
                    ((LoggerContextShutdownEnabled)context).addShutdownListener(this);
                }
            }
            return loggers;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public Set<LoggerContext> getLoggerContexts() {
        return new HashSet<LoggerContext>(this.registry.keySet());
    }
    
    protected abstract L newLogger(final String name, final LoggerContext context);
    
    protected abstract LoggerContext getContext();
    
    protected LoggerContext getContext(final Class<?> callerClass) {
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
