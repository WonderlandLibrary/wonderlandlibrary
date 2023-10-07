// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db;

import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import java.util.ArrayList;
import java.io.Flushable;
import org.apache.logging.log4j.core.appender.AbstractManager;

public abstract class AbstractDatabaseManager extends AbstractManager implements Flushable
{
    private final ArrayList<LogEvent> buffer;
    private final int bufferSize;
    private final Layout<? extends Serializable> layout;
    private boolean running;
    
    protected static <M extends AbstractDatabaseManager, T extends AbstractFactoryData> M getManager(final String name, final T data, final ManagerFactory<M, T> factory) {
        return AbstractManager.getManager(name, factory, data);
    }
    
    @Deprecated
    protected AbstractDatabaseManager(final String name, final int bufferSize) {
        this(name, bufferSize, null);
    }
    
    @Deprecated
    protected AbstractDatabaseManager(final String name, final int bufferSize, final Layout<? extends Serializable> layout) {
        this(name, bufferSize, layout, null);
    }
    
    protected AbstractDatabaseManager(final String name, final int bufferSize, final Layout<? extends Serializable> layout, final Configuration configuration) {
        super((configuration != null) ? configuration.getLoggerContext() : null, name);
        this.bufferSize = bufferSize;
        this.buffer = new ArrayList<LogEvent>(bufferSize + 1);
        this.layout = layout;
    }
    
    protected void buffer(final LogEvent event) {
        this.buffer.add(event.toImmutable());
        if (this.buffer.size() >= this.bufferSize || event.isEndOfBatch()) {
            this.flush();
        }
    }
    
    protected abstract boolean commitAndClose();
    
    protected abstract void connectAndStart();
    
    @Override
    public final synchronized void flush() {
        if (this.isRunning() && this.isBuffered()) {
            this.connectAndStart();
            try {
                for (final LogEvent event : this.buffer) {
                    this.writeInternal(event, (this.layout != null) ? this.layout.toSerializable(event) : null);
                }
            }
            finally {
                this.commitAndClose();
                this.buffer.clear();
            }
        }
    }
    
    protected boolean isBuffered() {
        return this.bufferSize > 0;
    }
    
    public final boolean isRunning() {
        return this.running;
    }
    
    public final boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        return this.shutdown();
    }
    
    public final synchronized boolean shutdown() {
        boolean closed = true;
        this.flush();
        if (this.isRunning()) {
            try {
                closed &= this.shutdownInternal();
            }
            catch (final Exception e) {
                this.logWarn("Caught exception while performing database shutdown operations", e);
                closed = false;
            }
            finally {
                this.running = false;
            }
        }
        return closed;
    }
    
    protected abstract boolean shutdownInternal() throws Exception;
    
    public final synchronized void startup() {
        if (!this.isRunning()) {
            try {
                this.startupInternal();
                this.running = true;
            }
            catch (final Exception e) {
                this.logError("Could not perform database startup operations", e);
            }
        }
    }
    
    protected abstract void startupInternal() throws Exception;
    
    @Override
    public final String toString() {
        return this.getName();
    }
    
    @Deprecated
    public final synchronized void write(final LogEvent event) {
        this.write(event, null);
    }
    
    public final synchronized void write(final LogEvent event, final Serializable serializable) {
        if (this.isBuffered()) {
            this.buffer(event);
        }
        else {
            this.writeThrough(event, serializable);
        }
    }
    
    @Deprecated
    protected void writeInternal(final LogEvent event) {
        this.writeInternal(event, null);
    }
    
    protected abstract void writeInternal(final LogEvent event, final Serializable serializable);
    
    protected void writeThrough(final LogEvent event, final Serializable serializable) {
        this.connectAndStart();
        try {
            this.writeInternal(event, serializable);
        }
        finally {
            this.commitAndClose();
        }
    }
    
    protected abstract static class AbstractFactoryData extends AbstractManager.AbstractFactoryData
    {
        private final int bufferSize;
        private final Layout<? extends Serializable> layout;
        
        @Deprecated
        protected AbstractFactoryData(final int bufferSize, final Layout<? extends Serializable> layout) {
            this(null, bufferSize, layout);
        }
        
        protected AbstractFactoryData(final Configuration configuration, final int bufferSize, final Layout<? extends Serializable> layout) {
            super(configuration);
            this.bufferSize = bufferSize;
            this.layout = layout;
        }
        
        public int getBufferSize() {
            return this.bufferSize;
        }
        
        public Layout<? extends Serializable> getLayout() {
            return this.layout;
        }
    }
}
