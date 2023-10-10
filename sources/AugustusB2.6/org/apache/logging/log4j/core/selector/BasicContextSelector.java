// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LoggerContext;

public class BasicContextSelector implements ContextSelector
{
    private static final LoggerContext CONTEXT;
    
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
        return (ctx != null) ? ctx : BasicContextSelector.CONTEXT;
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        final LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        return (ctx != null) ? ctx : BasicContextSelector.CONTEXT;
    }
    
    public LoggerContext locateContext(final String name, final String configLocation) {
        return BasicContextSelector.CONTEXT;
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
        final List<LoggerContext> list = new ArrayList<LoggerContext>();
        list.add(BasicContextSelector.CONTEXT);
        return Collections.unmodifiableList((List<? extends LoggerContext>)list);
    }
    
    static {
        CONTEXT = new LoggerContext("Default");
    }
}
