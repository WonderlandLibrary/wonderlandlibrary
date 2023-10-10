// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.net.URI;

public interface LoggerContextFactory
{
    default void shutdown(final String fqcn, final ClassLoader loader, final boolean currentContext, final boolean allContexts) {
        if (this.hasContext(fqcn, loader, currentContext)) {
            final LoggerContext ctx = this.getContext(fqcn, loader, null, currentContext);
            if (ctx instanceof Terminable) {
                ((Terminable)ctx).terminate();
            }
        }
    }
    
    default boolean hasContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return false;
    }
    
    LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext);
    
    LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext, final URI configLocation, final String name);
    
    void removeContext(final LoggerContext context);
    
    default boolean isClassLoaderDependent() {
        return true;
    }
}
