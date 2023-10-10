// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.LogEvent;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.logging.log4j.core.appender.AbstractAppender;

public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager> extends AbstractAppender
{
    public static final int DEFAULT_RECONNECT_INTERVAL_MILLIS = 5000;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;
    private T manager;
    
    @Deprecated
    protected AbstractDatabaseAppender(final String name, final Filter filter, final boolean ignoreExceptions, final T manager) {
        super(name, filter, null, ignoreExceptions, Property.EMPTY_ARRAY);
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.manager = manager;
    }
    
    protected AbstractDatabaseAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final Property[] properties, final T manager) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.manager = manager;
    }
    
    @Deprecated
    protected AbstractDatabaseAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final T manager) {
        super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.manager = manager;
    }
    
    @Override
    public final void append(final LogEvent event) {
        this.readLock.lock();
        try {
            this.getManager().write(event, this.toSerializable(event));
        }
        catch (LoggingException e) {
            AbstractDatabaseAppender.LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(), this.getName(), e);
            throw e;
        }
        catch (Exception e2) {
            AbstractDatabaseAppender.LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(), this.getName(), e2);
            throw new AppenderLoggingException("Unable to write to database in appender: " + e2.getMessage(), e2);
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    @Override
    public final Layout<LogEvent> getLayout() {
        return null;
    }
    
    public final T getManager() {
        return this.manager;
    }
    
    protected final void replaceManager(final T manager) {
        this.writeLock.lock();
        try {
            final T old = this.getManager();
            if (!manager.isRunning()) {
                manager.startup();
            }
            this.manager = manager;
            old.close();
        }
        finally {
            this.writeLock.unlock();
        }
    }
    
    @Override
    public final void start() {
        if (this.getManager() == null) {
            AbstractDatabaseAppender.LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", this.getName());
        }
        super.start();
        if (this.getManager() != null) {
            this.getManager().startup();
        }
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        if (this.getManager() != null) {
            stopped &= this.getManager().stop(timeout, timeUnit);
        }
        this.setStopped();
        return stopped;
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
    {
    }
}
