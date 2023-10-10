// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import java.util.Collections;
import java.util.List;
import java.net.URI;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.selector.ContextSelector;

public class BasicAsyncLoggerContextSelector implements ContextSelector
{
    private static final AsyncLoggerContext CONTEXT;
    
    @Override
    public void shutdown(final String fqcn, final ClassLoader loader, final boolean currentContext, final boolean allContexts) {
        final LoggerContext ctx = this.getContext(fqcn, loader, currentContext);
        if (ctx != null && ctx.isStarted()) {
            ctx.stop(50L, TimeUnit.MILLISECONDS);
        }
    }
    
    @Override
    public boolean hasContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        final LoggerContext ctx = this.getContext(fqcn, loader, currentContext);
        return ctx != null && ctx.isStarted();
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        final LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        return (ctx != null) ? ctx : BasicAsyncLoggerContextSelector.CONTEXT;
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        final LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        return (ctx != null) ? ctx : BasicAsyncLoggerContextSelector.CONTEXT;
    }
    
    @Override
    public void removeContext(final LoggerContext context) {
    }
    
    @Override
    public boolean isClassLoaderDependent() {
        return false;
    }
    
    @Override
    public List<LoggerContext> getLoggerContexts() {
        return (List<LoggerContext>)Collections.singletonList(BasicAsyncLoggerContextSelector.CONTEXT);
    }
    
    static {
        CONTEXT = new AsyncLoggerContext("AsyncDefault");
    }
}
