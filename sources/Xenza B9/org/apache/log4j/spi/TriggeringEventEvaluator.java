// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

public interface TriggeringEventEvaluator
{
    boolean isTriggeringEvent(final LoggingEvent event);
}
