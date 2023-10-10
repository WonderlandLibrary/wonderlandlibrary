// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public class AsyncLoggerContext extends LoggerContext
{
    private final AsyncLoggerDisruptor loggerDisruptor;
    
    public AsyncLoggerContext(final String name) {
        super(name);
        this.loggerDisruptor = new AsyncLoggerDisruptor(name);
    }
    
    public AsyncLoggerContext(final String name, final Object externalContext) {
        super(name, externalContext);
        this.loggerDisruptor = new AsyncLoggerDisruptor(name);
    }
    
    public AsyncLoggerContext(final String name, final Object externalContext, final URI configLocn) {
        super(name, externalContext, configLocn);
        this.loggerDisruptor = new AsyncLoggerDisruptor(name);
    }
    
    public AsyncLoggerContext(final String name, final Object externalContext, final String configLocn) {
        super(name, externalContext, configLocn);
        this.loggerDisruptor = new AsyncLoggerDisruptor(name);
    }
    
    @Override
    protected Logger newInstance(final LoggerContext ctx, final String name, final MessageFactory messageFactory) {
        return new AsyncLogger(ctx, name, messageFactory, this.loggerDisruptor);
    }
    
    @Override
    public void setName(final String name) {
        super.setName("AsyncContext[" + name + "]");
        this.loggerDisruptor.setContextName(name);
    }
    
    @Override
    public void start() {
        this.loggerDisruptor.start();
        super.start();
    }
    
    @Override
    public void start(final Configuration config) {
        this.maybeStartHelper(config);
        super.start(config);
    }
    
    private void maybeStartHelper(final Configuration config) {
        if (config instanceof DefaultConfiguration) {
            StatusLogger.getLogger().debug("[{}] Not starting Disruptor for DefaultConfiguration.", this.getName());
        }
        else {
            this.loggerDisruptor.start();
        }
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        this.loggerDisruptor.stop(timeout, timeUnit);
        super.stop(timeout, timeUnit);
        return true;
    }
    
    public RingBufferAdmin createRingBufferAdmin() {
        return this.loggerDisruptor.createRingBufferAdmin(this.getName());
    }
    
    public void setUseThreadLocals(final boolean useThreadLocals) {
        this.loggerDisruptor.setUseThreadLocals(useThreadLocals);
    }
}
