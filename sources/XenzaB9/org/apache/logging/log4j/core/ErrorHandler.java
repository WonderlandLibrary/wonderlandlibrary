// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core;

public interface ErrorHandler
{
    void error(final String msg);
    
    void error(final String msg, final Throwable t);
    
    void error(final String msg, final LogEvent event, final Throwable t);
}
