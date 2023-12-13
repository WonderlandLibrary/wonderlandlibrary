// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.logging;

import io.netty.util.internal.logging.InternalLogLevel;

public enum LogLevel
{
    TRACE(InternalLogLevel.TRACE), 
    DEBUG(InternalLogLevel.DEBUG), 
    INFO(InternalLogLevel.INFO), 
    WARN(InternalLogLevel.WARN), 
    ERROR(InternalLogLevel.ERROR);
    
    private final InternalLogLevel internalLevel;
    
    private LogLevel(final InternalLogLevel internalLevel) {
        this.internalLevel = internalLevel;
    }
    
    InternalLogLevel toInternalLevel() {
        return this.internalLevel;
    }
}
