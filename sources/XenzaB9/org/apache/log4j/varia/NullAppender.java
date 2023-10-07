// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.varia;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.AppenderSkeleton;

public class NullAppender extends AppenderSkeleton
{
    private static final NullAppender INSTANCE;
    
    public static NullAppender getNullAppender() {
        return NullAppender.INSTANCE;
    }
    
    @Override
    public void activateOptions() {
    }
    
    @Override
    protected void append(final LoggingEvent event) {
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void doAppend(final LoggingEvent event) {
    }
    
    @Deprecated
    public NullAppender getInstance() {
        return NullAppender.INSTANCE;
    }
    
    @Override
    public boolean requiresLayout() {
        return false;
    }
    
    static {
        INSTANCE = new NullAppender();
    }
}
