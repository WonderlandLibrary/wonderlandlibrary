/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.selector;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.ReflectiveCallerClassUtility;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.status.StatusLogger;

public class ClassLoaderContextSelector
implements ContextSelector {
    private static final AtomicReference<LoggerContext> CONTEXT = new AtomicReference();
    private static final PrivateSecurityManager SECURITY_MANAGER;
    private static final StatusLogger LOGGER;
    private static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP;

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return this.getContext(fqcn, loader, currentContext, null);
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        LoggerContext lc;
        boolean next;
        Class clazz;
        if (currentContext) {
            LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
            if (ctx != null) {
                return ctx;
            }
            return this.getDefault();
        }
        if (loader != null) {
            return this.locateContext(loader, configLocation);
        }
        if (ReflectiveCallerClassUtility.isSupported()) {
            try {
                clazz = Class.class;
                next = false;
                int index = 2;
                while (clazz != null && (clazz = ReflectiveCallerClassUtility.getCaller(index)) != null) {
                    if (clazz.getName().equals(fqcn)) {
                        next = true;
                    } else if (next) break;
                    ++index;
                }
                if (clazz != null) {
                    return this.locateContext(clazz.getClassLoader(), configLocation);
                }
            }
            catch (Exception ex) {
                // empty catch block
            }
        }
        if (SECURITY_MANAGER != null && (clazz = SECURITY_MANAGER.getCaller(fqcn)) != null) {
            ClassLoader ldr = clazz.getClassLoader() != null ? clazz.getClassLoader() : ClassLoader.getSystemClassLoader();
            return this.locateContext(ldr, configLocation);
        }
        Throwable t = new Throwable();
        next = false;
        String name = null;
        for (StackTraceElement element : t.getStackTrace()) {
            if (element.getClassName().equals(fqcn)) {
                next = true;
                continue;
            }
            if (!next) continue;
            name = element.getClassName();
            break;
        }
        if (name != null) {
            try {
                return this.locateContext(Loader.loadClass(name).getClassLoader(), configLocation);
            }
            catch (ClassNotFoundException ignore) {
                // empty catch block
            }
        }
        if ((lc = ContextAnchor.THREAD_CONTEXT.get()) != null) {
            return lc;
        }
        return this.getDefault();
    }

    @Override
    public void removeContext(LoggerContext context) {
        for (Map.Entry entry : CONTEXT_MAP.entrySet()) {
            LoggerContext ctx = (LoggerContext)((WeakReference)((AtomicReference)entry.getValue()).get()).get();
            if (ctx != context) continue;
            CONTEXT_MAP.remove(entry.getKey());
        }
    }

    @Override
    public List<LoggerContext> getLoggerContexts() {
        ArrayList<LoggerContext> list = new ArrayList<LoggerContext>();
        Collection coll = CONTEXT_MAP.values();
        for (AtomicReference ref : coll) {
            LoggerContext ctx = (LoggerContext)((WeakReference)ref.get()).get();
            if (ctx == null) continue;
            list.add(ctx);
        }
        return Collections.unmodifiableList(list);
    }

    private LoggerContext locateContext(ClassLoader loader, URI configLocation) {
        String name = loader.toString();
        AtomicReference ref = (AtomicReference)CONTEXT_MAP.get(name);
        if (ref == null) {
            Object r;
            if (configLocation == null) {
                for (ClassLoader parent = loader.getParent(); parent != null; parent = parent.getParent()) {
                    LoggerContext ctx;
                    ref = (AtomicReference)CONTEXT_MAP.get(parent.toString());
                    if (ref == null || (ctx = (LoggerContext)((Reference)(r = (WeakReference)ref.get())).get()) == null) continue;
                    return ctx;
                }
            }
            LoggerContext ctx = new LoggerContext(name, null, configLocation);
            r = new AtomicReference<WeakReference<LoggerContext>>();
            ((AtomicReference)r).set(new WeakReference<LoggerContext>(ctx));
            CONTEXT_MAP.putIfAbsent(loader.toString(), (AtomicReference<WeakReference<LoggerContext>>)r);
            ctx = (LoggerContext)((WeakReference)((AtomicReference)CONTEXT_MAP.get(name)).get()).get();
            return ctx;
        }
        WeakReference r = (WeakReference)ref.get();
        LoggerContext ctx = (LoggerContext)r.get();
        if (ctx != null) {
            if (ctx.getConfigLocation() == null && configLocation != null) {
                LOGGER.debug("Setting configuration to {}", configLocation);
                ctx.setConfigLocation(configLocation);
            } else if (ctx.getConfigLocation() != null && configLocation != null && !ctx.getConfigLocation().equals(configLocation)) {
                LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", configLocation, ctx.getConfigLocation());
            }
            return ctx;
        }
        ctx = new LoggerContext(name, null, configLocation);
        ref.compareAndSet(r, new WeakReference<LoggerContext>(ctx));
        return ctx;
    }

    private LoggerContext getDefault() {
        LoggerContext ctx = CONTEXT.get();
        if (ctx != null) {
            return ctx;
        }
        CONTEXT.compareAndSet(null, new LoggerContext("Default"));
        return CONTEXT.get();
    }

    static {
        LOGGER = StatusLogger.getLogger();
        CONTEXT_MAP = new ConcurrentHashMap<String, AtomicReference<WeakReference<LoggerContext>>>();
        if (ReflectiveCallerClassUtility.isSupported()) {
            SECURITY_MANAGER = null;
        } else {
            PrivateSecurityManager securityManager;
            try {
                securityManager = new PrivateSecurityManager();
                if (securityManager.getCaller(ClassLoaderContextSelector.class.getName()) == null) {
                    securityManager = null;
                    LOGGER.error("Unable to obtain call stack from security manager.");
                }
            }
            catch (Exception e) {
                securityManager = null;
                LOGGER.debug("Unable to install security manager", (Throwable)e);
            }
            SECURITY_MANAGER = securityManager;
        }
    }

    private static class PrivateSecurityManager
    extends SecurityManager {
        private PrivateSecurityManager() {
        }

        public Class<?> getCaller(String fqcn) {
            Class<?>[] classes = this.getClassContext();
            boolean next = false;
            for (Class<?> clazz : classes) {
                if (clazz.getName().equals(fqcn)) {
                    next = true;
                    continue;
                }
                if (!next) continue;
                return clazz;
            }
            return null;
        }
    }
}

