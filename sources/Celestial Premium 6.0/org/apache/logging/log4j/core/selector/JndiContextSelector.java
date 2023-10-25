/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.selector.NamedContextSelector;
import org.apache.logging.log4j.status.StatusLogger;

public class JndiContextSelector
implements NamedContextSelector {
    private static final LoggerContext CONTEXT = new LoggerContext("Default");
    private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP = new ConcurrentHashMap<String, LoggerContext>();
    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
        String loggingContextName;
        LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        if (ctx == null && (loggingContextName = this.getContextName()) != null) {
            ctx = (LoggerContext)CONTEXT_MAP.get(loggingContextName);
        }
        if (ctx != null) {
            ctx.stop(50L, TimeUnit.MILLISECONDS);
        }
    }

    public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
        LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
        if (ctx == null) {
            String loggingContextName = this.getContextName();
            if (loggingContextName == null) {
                return false;
            }
            ctx = (LoggerContext)CONTEXT_MAP.get(loggingContextName);
        }
        return ctx != null && ctx.isStarted();
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return this.getContext(fqcn, loader, currentContext, null);
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
        if (lc != null) {
            return lc;
        }
        String loggingContextName = null;
        try (JndiManager jndiManager = JndiManager.getDefaultManager();){
            loggingContextName = (String)jndiManager.lookup("java:comp/env/log4j/context-name");
        }
        catch (NamingException ne) {
            LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
        }
        return loggingContextName == null ? CONTEXT : this.locateContext(loggingContextName, null, configLocation);
    }

    private String getContextName() {
        String loggingContextName = null;
        try (JndiManager jndiManager = JndiManager.getDefaultManager();){
            loggingContextName = (String)jndiManager.lookup("java:comp/env/log4j/context-name");
        }
        catch (NamingException ne) {
            LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
        }
        return loggingContextName;
    }

    @Override
    public LoggerContext locateContext(String name, Object externalContext, URI configLocation) {
        if (name == null) {
            LOGGER.error("A context name is required to locate a LoggerContext");
            return null;
        }
        if (!CONTEXT_MAP.containsKey(name)) {
            LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
            CONTEXT_MAP.putIfAbsent(name, ctx);
        }
        return (LoggerContext)CONTEXT_MAP.get(name);
    }

    @Override
    public void removeContext(LoggerContext context) {
        for (Map.Entry entry : CONTEXT_MAP.entrySet()) {
            if (!((LoggerContext)entry.getValue()).equals(context)) continue;
            CONTEXT_MAP.remove(entry.getKey());
        }
    }

    @Override
    public LoggerContext removeContext(String name) {
        return (LoggerContext)CONTEXT_MAP.remove(name);
    }

    @Override
    public List<LoggerContext> getLoggerContexts() {
        return Collections.unmodifiableList(new ArrayList(CONTEXT_MAP.values()));
    }
}

