// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

public interface RingBufferAdminMBean
{
    public static final String PATTERN_ASYNC_LOGGER = "org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer";
    public static final String PATTERN_ASYNC_LOGGER_CONFIG = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer";
    
    long getBufferSize();
    
    long getRemainingCapacity();
}
