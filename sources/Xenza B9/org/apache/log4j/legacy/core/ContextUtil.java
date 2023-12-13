// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.legacy.core;

import org.apache.logging.log4j.spi.LoggerContext;

public final class ContextUtil
{
    public static void reconfigure(final LoggerContext loggerContext) {
        if (loggerContext instanceof org.apache.logging.log4j.core.LoggerContext) {
            ((org.apache.logging.log4j.core.LoggerContext)loggerContext).reconfigure();
        }
    }
    
    public static void shutdown(final LoggerContext loggerContext) {
        if (loggerContext instanceof org.apache.logging.log4j.core.LoggerContext) {
            ((org.apache.logging.log4j.core.LoggerContext)loggerContext).close();
        }
    }
    
    private ContextUtil() {
    }
}
