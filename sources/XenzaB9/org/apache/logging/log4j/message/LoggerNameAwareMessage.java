// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

public interface LoggerNameAwareMessage
{
    void setLoggerName(final String name);
    
    String getLoggerName();
}
