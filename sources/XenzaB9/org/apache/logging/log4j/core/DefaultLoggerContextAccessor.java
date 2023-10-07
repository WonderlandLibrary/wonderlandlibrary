// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core;

public class DefaultLoggerContextAccessor implements LoggerContextAccessor
{
    public static DefaultLoggerContextAccessor INSTANCE;
    
    @Override
    public LoggerContext getLoggerContext() {
        return LoggerContext.getContext();
    }
    
    static {
        DefaultLoggerContextAccessor.INSTANCE = new DefaultLoggerContextAccessor();
    }
}
