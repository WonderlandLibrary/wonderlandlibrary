/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.selector.ContextSelector;

public class BasicContextSelector
implements ContextSelector {
    private static final LoggerContext CONTEXT = new LoggerContext("Default");

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

    public LoggerContext locateContext(String name, String configLocation) {
        return CONTEXT;
    }

    @Override
    public void removeContext(LoggerContext context) {
    }

    @Override
    public List<LoggerContext> getLoggerContexts() {
        ArrayList<LoggerContext> list = new ArrayList<LoggerContext>();
        list.add(CONTEXT);
        return Collections.unmodifiableList(list);
    }
}

