// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.LogEvent;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;

public abstract class AbstractOutputStreamAppender<M extends OutputStreamManager> extends AbstractAppender
{
    private final boolean immediateFlush;
    private final M manager;
    
    @Deprecated
    protected AbstractOutputStreamAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean immediateFlush, final M manager) {
        super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        this.manager = manager;
        this.immediateFlush = immediateFlush;
    }
    
    protected AbstractOutputStreamAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean immediateFlush, final Property[] properties, final M manager) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.manager = manager;
        this.immediateFlush = immediateFlush;
    }
    
    public boolean getImmediateFlush() {
        return this.immediateFlush;
    }
    
    public M getManager() {
        return this.manager;
    }
    
    @Override
    public void start() {
        if (this.getLayout() == null) {
            AbstractOutputStreamAppender.LOGGER.error("No layout set for the appender named [" + this.getName() + "].");
        }
        if (this.manager == null) {
            AbstractOutputStreamAppender.LOGGER.error("No OutputStreamManager set for the appender named [" + this.getName() + "].");
        }
        super.start();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        return this.stop(timeout, timeUnit, true);
    }
    
    @Override
    protected boolean stop(final long timeout, final TimeUnit timeUnit, final boolean changeLifeCycleState) {
        boolean stopped = super.stop(timeout, timeUnit, changeLifeCycleState);
        stopped &= this.manager.stop(timeout, timeUnit);
        if (changeLifeCycleState) {
            this.setStopped();
        }
        AbstractOutputStreamAppender.LOGGER.debug("Appender {} stopped with status {}", this.getName(), stopped);
        return stopped;
    }
    
    @Override
    public void append(final LogEvent event) {
        try {
            this.tryAppend(event);
        }
        catch (AppenderLoggingException ex) {
            this.error("Unable to write to stream " + this.manager.getName() + " for appender " + this.getName(), event, ex);
            throw ex;
        }
    }
    
    private void tryAppend(final LogEvent event) {
        if (Constants.ENABLE_DIRECT_ENCODERS) {
            this.directEncodeEvent(event);
        }
        else {
            this.writeByteArrayToManager(event);
        }
    }
    
    protected void directEncodeEvent(final LogEvent event) {
        this.getLayout().encode(event, this.manager);
        if (this.immediateFlush || event.isEndOfBatch()) {
            this.manager.flush();
        }
    }
    
    protected void writeByteArrayToManager(final LogEvent event) {
        final byte[] bytes = this.getLayout().toByteArray(event);
        if (bytes != null && bytes.length > 0) {
            this.manager.write(bytes, this.immediateFlush || event.isEndOfBatch());
        }
    }
    
    public abstract static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
    {
        @PluginBuilderAttribute
        private boolean bufferedIo;
        @PluginBuilderAttribute
        private int bufferSize;
        @PluginBuilderAttribute
        private boolean immediateFlush;
        
        public Builder() {
            this.bufferedIo = true;
            this.bufferSize = Constants.ENCODER_BYTE_BUFFER_SIZE;
            this.immediateFlush = true;
        }
        
        public int getBufferSize() {
            return this.bufferSize;
        }
        
        public boolean isBufferedIo() {
            return this.bufferedIo;
        }
        
        public boolean isImmediateFlush() {
            return this.immediateFlush;
        }
        
        public B withImmediateFlush(final boolean immediateFlush) {
            this.immediateFlush = immediateFlush;
            return this.asBuilder();
        }
        
        public B withBufferedIo(final boolean bufferedIo) {
            this.bufferedIo = bufferedIo;
            return this.asBuilder();
        }
        
        public B withBufferSize(final int bufferSize) {
            this.bufferSize = bufferSize;
            return this.asBuilder();
        }
    }
}
