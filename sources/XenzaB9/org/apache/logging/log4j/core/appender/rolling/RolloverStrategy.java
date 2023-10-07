// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

public interface RolloverStrategy
{
    RolloverDescription rollover(final RollingFileManager manager) throws SecurityException;
}
