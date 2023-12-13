// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.LoggerContext;

public final class ContextAnchor
{
    public static final ThreadLocal<LoggerContext> THREAD_CONTEXT;
    
    private ContextAnchor() {
    }
    
    static {
        THREAD_CONTEXT = new ThreadLocal<LoggerContext>();
    }
}
