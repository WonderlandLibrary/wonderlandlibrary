// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;

public interface TriggeringPolicy
{
    void initialize(final RollingFileManager manager);
    
    boolean isTriggeringEvent(final LogEvent logEvent);
}
