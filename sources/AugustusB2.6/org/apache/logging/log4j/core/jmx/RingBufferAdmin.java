// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import javax.management.ObjectName;
import com.lmax.disruptor.RingBuffer;

public class RingBufferAdmin implements RingBufferAdminMBean
{
    private final RingBuffer<?> ringBuffer;
    private final ObjectName objectName;
    
    public static RingBufferAdmin forAsyncLogger(final RingBuffer<?> ringBuffer, final String contextName) {
        final String ctxName = Server.escape(contextName);
        final String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", ctxName);
        return new RingBufferAdmin(ringBuffer, name);
    }
    
    public static RingBufferAdmin forAsyncLoggerConfig(final RingBuffer<?> ringBuffer, final String contextName, final String configName) {
        final String ctxName = Server.escape(contextName);
        final String cfgName = Server.escape(configName);
        final String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", ctxName, cfgName);
        return new RingBufferAdmin(ringBuffer, name);
    }
    
    protected RingBufferAdmin(final RingBuffer<?> ringBuffer, final String mbeanName) {
        this.ringBuffer = ringBuffer;
        try {
            this.objectName = new ObjectName(mbeanName);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public long getBufferSize() {
        return (this.ringBuffer == null) ? 0L : this.ringBuffer.getBufferSize();
    }
    
    @Override
    public long getRemainingCapacity() {
        return (this.ringBuffer == null) ? 0L : this.ringBuffer.remainingCapacity();
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
}
