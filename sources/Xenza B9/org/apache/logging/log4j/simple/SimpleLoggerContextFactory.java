// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.simple;

import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class SimpleLoggerContextFactory implements LoggerContextFactory
{
    public static final SimpleLoggerContextFactory INSTANCE;
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext) {
        return SimpleLoggerContext.INSTANCE;
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext, final URI configLocation, final String name) {
        return SimpleLoggerContext.INSTANCE;
    }
    
    @Override
    public void removeContext(final LoggerContext removeContext) {
    }
    
    @Override
    public boolean isClassLoaderDependent() {
        return false;
    }
    
    static {
        INSTANCE = new SimpleLoggerContextFactory();
    }
}
