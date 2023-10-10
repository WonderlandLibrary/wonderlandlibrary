// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import java.io.Serializable;

public interface Appender extends LifeCycle
{
    public static final String ELEMENT_TYPE = "appender";
    
    void append(final LogEvent event);
    
    String getName();
    
    Layout<? extends Serializable> getLayout();
    
    boolean ignoreExceptions();
    
    ErrorHandler getHandler();
    
    void setHandler(final ErrorHandler handler);
}
