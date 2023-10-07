// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

public interface RolloverListener
{
    void rolloverTriggered(final String fileName);
    
    void rolloverComplete(final String fileName);
}
