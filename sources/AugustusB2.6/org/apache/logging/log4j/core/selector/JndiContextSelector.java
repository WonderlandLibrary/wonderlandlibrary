// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.net.JndiManager;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.LoggerContext;

public class JndiContextSelector implements NamedContextSelector
{
    private static final LoggerContext CONTEXT;
    private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP;
    private static final StatusLogger LOGGER;
    
    @Override
    public void shutdown(final String fqcn, final ClassLoader loader, final boolean currentContext, final boolean allContexts) {
        LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        if (ctx == null) {
            final String loggingContextName = this.getContextName();
            if (loggingContextName != null) {
                ctx = JndiContextSelector.CONTEXT_MAP.get(loggingContextName);
            }
        }
        if (ctx != null) {
            ctx.stop(50L, TimeUnit.MILLISECONDS);
        }
    }
    
    @Override
    public boolean hasContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        if (ctx == null) {
            final String loggingContextName = this.getContextName();
            if (loggingContextName == null) {
                return false;
            }
            ctx = JndiContextSelector.CONTEXT_MAP.get(loggingContextName);
        }
        return ctx != null && ctx.isStarted();
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return this.getContext(fqcn, loader, currentContext, null);
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        final LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
        if (lc != null) {
            return lc;
        }
        String loggingContextName = null;
        try (final JndiManager jndiManager = JndiManager.getDefaultManager()) {
            loggingContextName = jndiManager.lookup("java:comp/env/log4j/context-name");
        }
        catch (NamingException ne) {
            JndiContextSelector.LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
        }
        return (loggingContextName == null) ? JndiContextSelector.CONTEXT : this.locateContext(loggingContextName, null, configLocation);
    }
    
    private String getContextName() {
        String loggingContextName = null;
        try (final JndiManager jndiManager = JndiManager.getDefaultManager()) {
            loggingContextName = jndiManager.lookup("java:comp/env/log4j/context-name");
        }
        catch (NamingException ne) {
            JndiContextSelector.LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
        }
        return loggingContextName;
    }
    
    @Override
    public LoggerContext locateContext(final String name, final Object externalContext, final URI configLocation) {
        if (name == null) {
            JndiContextSelector.LOGGER.error("A context name is required to locate a LoggerContext");
            return null;
        }
        if (!JndiContextSelector.CONTEXT_MAP.containsKey(name)) {
            final LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
            JndiContextSelector.CONTEXT_MAP.putIfAbsent(name, ctx);
        }
        return JndiContextSelector.CONTEXT_MAP.get(name);
    }
    
    @Override
    public void removeContext(final LoggerContext context) {
        for (final Map.Entry<String, LoggerContext> entry : JndiContextSelector.CONTEXT_MAP.entrySet()) {
            if (entry.getValue().equals(context)) {
                JndiContextSelector.CONTEXT_MAP.remove(entry.getKey());
            }
        }
    }
    
    @Override
    public boolean isClassLoaderDependent() {
        return false;
    }
    
    @Override
    public LoggerContext removeContext(final String name) {
        return JndiContextSelector.CONTEXT_MAP.remove(name);
    }
    
    @Override
    public List<LoggerContext> getLoggerContexts() {
        return Collections.unmodifiableList((List<? extends LoggerContext>)new ArrayList<LoggerContext>(JndiContextSelector.CONTEXT_MAP.values()));
    }
    
    static {
        CONTEXT = new LoggerContext("Default");
        CONTEXT_MAP = new ConcurrentHashMap<String, LoggerContext>();
        LOGGER = StatusLogger.getLogger();
    }
}
