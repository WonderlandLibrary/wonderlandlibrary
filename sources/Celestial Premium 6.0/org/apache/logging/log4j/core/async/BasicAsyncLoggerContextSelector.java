/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.async;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.AsyncLoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.selector.ContextSelector;

public class BasicAsyncLoggerContextSelector
implements ContextSelector {
    private static final AsyncLoggerContext CONTEXT = new AsyncLoggerContext("AsyncDefault");

    public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
        LoggerContext ctx = this.getContext(fqcn, loader, currentContext);
        if (ctx != null && ctx.isStarted()) {
            ctx.stop(50L, TimeUnit.MILLISECONDS);
        }
    }

    public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
        LoggerContext ctx = this.getContext(fqcn, loader, currentContext);
        return ctx != null && ctx.isStarted();
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
        LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        return ctx != null ? ctx : CONTEXT;
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        return ctx != null ? ctx : CONTEXT;
    }

    @Override
    public void removeContext(LoggerContext context) {
    }

    public boolean isClassLoaderDependent() {
        return false;
    }

    @Override
    public List<LoggerContext> getLoggerContexts() {
        return Collections.singletonList(CONTEXT);
    }
}

