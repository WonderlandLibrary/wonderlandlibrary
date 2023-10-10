// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import java.util.List;
import java.net.URI;
import java.util.Map;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.TimeUnit;

public interface ContextSelector
{
    public static final long DEFAULT_STOP_TIMEOUT = 50L;
    
    default void shutdown(final String fqcn, final ClassLoader loader, final boolean currentContext, final boolean allContexts) {
        if (this.hasContext(fqcn, loader, currentContext)) {
            this.getContext(fqcn, loader, currentContext).stop(50L, TimeUnit.MILLISECONDS);
        }
    }
    
    default boolean hasContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return false;
    }
    
    LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext);
    
    default LoggerContext getContext(final String fqcn, final ClassLoader loader, final Map.Entry<String, Object> entry, final boolean currentContext) {
        final LoggerContext lc = this.getContext(fqcn, loader, currentContext);
        if (lc != null) {
            lc.putObject(entry.getKey(), entry.getValue());
        }
        return lc;
    }
    
    LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation);
    
    default LoggerContext getContext(final String fqcn, final ClassLoader loader, final Map.Entry<String, Object> entry, final boolean currentContext, final URI configLocation) {
        final LoggerContext lc = this.getContext(fqcn, loader, currentContext, configLocation);
        if (lc != null) {
            lc.putObject(entry.getKey(), entry.getValue());
        }
        return lc;
    }
    
    List<LoggerContext> getLoggerContexts();
    
    void removeContext(final LoggerContext context);
    
    default boolean isClassLoaderDependent() {
        return true;
    }
}
