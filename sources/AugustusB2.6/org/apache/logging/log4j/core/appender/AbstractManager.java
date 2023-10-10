// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import java.util.HashMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.ConfigurationException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.locks.Lock;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public abstract class AbstractManager implements AutoCloseable
{
    protected static final Logger LOGGER;
    private static final Map<String, AbstractManager> MAP;
    private static final Lock LOCK;
    protected int count;
    private final String name;
    private final LoggerContext loggerContext;
    
    protected AbstractManager(final LoggerContext loggerContext, final String name) {
        this.loggerContext = loggerContext;
        this.name = name;
        AbstractManager.LOGGER.debug("Starting {} {}", this.getClass().getSimpleName(), name);
    }
    
    @Override
    public void close() {
        this.stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
    }
    
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        boolean stopped = true;
        AbstractManager.LOCK.lock();
        try {
            --this.count;
            if (this.count <= 0) {
                AbstractManager.MAP.remove(this.name);
                AbstractManager.LOGGER.debug("Shutting down {} {}", this.getClass().getSimpleName(), this.getName());
                stopped = this.releaseSub(timeout, timeUnit);
                AbstractManager.LOGGER.debug("Shut down {} {}, all resources released: {}", this.getClass().getSimpleName(), this.getName(), stopped);
            }
        }
        finally {
            AbstractManager.LOCK.unlock();
        }
        return stopped;
    }
    
    public static <M extends AbstractManager, T> M getManager(final String name, final ManagerFactory<M, T> factory, final T data) {
        AbstractManager.LOCK.lock();
        try {
            M manager = (M)AbstractManager.MAP.get(name);
            if (manager == null) {
                manager = factory.createManager(name, data);
                if (manager == null) {
                    throw new IllegalStateException("ManagerFactory [" + factory + "] unable to create manager for [" + name + "] with data [" + data + "]");
                }
                AbstractManager.MAP.put(name, manager);
            }
            else {
                manager.updateData(data);
            }
            final AbstractManager abstractManager = manager;
            ++abstractManager.count;
            return manager;
        }
        finally {
            AbstractManager.LOCK.unlock();
        }
    }
    
    public void updateData(final Object data) {
    }
    
    public static boolean hasManager(final String name) {
        AbstractManager.LOCK.lock();
        try {
            return AbstractManager.MAP.containsKey(name);
        }
        finally {
            AbstractManager.LOCK.unlock();
        }
    }
    
    protected static <M extends AbstractManager> M narrow(final Class<M> narrowClass, final AbstractManager manager) {
        if (narrowClass.isAssignableFrom(manager.getClass())) {
            return (M)manager;
        }
        throw new ConfigurationException("Configuration has multiple incompatible Appenders pointing to the same resource '" + manager.getName() + "'");
    }
    
    protected static StatusLogger logger() {
        return StatusLogger.getLogger();
    }
    
    protected boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        return true;
    }
    
    protected int getCount() {
        return this.count;
    }
    
    public LoggerContext getLoggerContext() {
        return this.loggerContext;
    }
    
    @Deprecated
    public void release() {
        this.close();
    }
    
    public String getName() {
        return this.name;
    }
    
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
    
    protected void log(final Level level, final String message, final Throwable throwable) {
        final Message m = AbstractManager.LOGGER.getMessageFactory().newMessage("{} {} {}: {}", this.getClass().getSimpleName(), this.getName(), message, throwable);
        AbstractManager.LOGGER.log(level, m, throwable);
    }
    
    protected void logDebug(final String message, final Throwable throwable) {
        this.log(Level.DEBUG, message, throwable);
    }
    
    protected void logError(final String message, final Throwable throwable) {
        this.log(Level.ERROR, message, throwable);
    }
    
    protected void logWarn(final String message, final Throwable throwable) {
        this.log(Level.WARN, message, throwable);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        MAP = new HashMap<String, AbstractManager>();
        LOCK = new ReentrantLock();
    }
}
