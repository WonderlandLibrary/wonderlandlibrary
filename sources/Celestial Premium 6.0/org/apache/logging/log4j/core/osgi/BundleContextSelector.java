/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.osgi.framework.Bundle
 *  org.osgi.framework.BundleReference
 *  org.osgi.framework.FrameworkUtil
 */
package org.apache.logging.log4j.core.osgi;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.framework.FrameworkUtil;

public class BundleContextSelector
extends ClassLoaderContextSelector {
    public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
        Class<?> callerClass;
        LoggerContext ctx = null;
        Bundle bundle = null;
        if (currentContext) {
            ctx = ContextAnchor.THREAD_CONTEXT.get();
            ContextAnchor.THREAD_CONTEXT.remove();
        }
        if (ctx == null && loader instanceof BundleReference) {
            bundle = ((BundleReference)loader).getBundle();
            ctx = this.getLoggerContext(bundle);
            this.removeLoggerContext(ctx);
        }
        if (ctx == null && (callerClass = StackLocatorUtil.getCallerClass(fqcn)) != null) {
            bundle = FrameworkUtil.getBundle(callerClass);
            ctx = this.getLoggerContext(FrameworkUtil.getBundle(callerClass));
            this.removeLoggerContext(ctx);
        }
        if (ctx == null) {
            ctx = ContextAnchor.THREAD_CONTEXT.get();
            ContextAnchor.THREAD_CONTEXT.remove();
        }
        if (ctx != null) {
            ctx.stop(50L, TimeUnit.MILLISECONDS);
        }
        if (bundle != null && allContexts) {
            Bundle[] bundles;
            for (Bundle bdl : bundles = bundle.getBundleContext().getBundles()) {
                ctx = this.getLoggerContext(bdl);
                if (ctx == null) continue;
                ctx.stop(50L, TimeUnit.MILLISECONDS);
            }
        }
    }

    private LoggerContext getLoggerContext(Bundle bundle) {
        String name = Objects.requireNonNull(bundle, "No Bundle provided").getSymbolicName();
        AtomicReference ref = (AtomicReference)CONTEXT_MAP.get(name);
        if (ref != null && ref.get() != null) {
            return (LoggerContext)((WeakReference)ref.get()).get();
        }
        return null;
    }

    private void removeLoggerContext(LoggerContext context) {
        CONTEXT_MAP.remove(context.getName());
    }

    public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
        if (currentContext && ContextAnchor.THREAD_CONTEXT.get() != null) {
            return ContextAnchor.THREAD_CONTEXT.get().isStarted();
        }
        if (loader instanceof BundleReference) {
            return BundleContextSelector.hasContext(((BundleReference)loader).getBundle());
        }
        Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
        if (callerClass != null) {
            return BundleContextSelector.hasContext(FrameworkUtil.getBundle(callerClass));
        }
        return ContextAnchor.THREAD_CONTEXT.get() != null && ContextAnchor.THREAD_CONTEXT.get().isStarted();
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        if (currentContext) {
            LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
            if (ctx != null) {
                return ctx;
            }
            return this.getDefault();
        }
        if (loader instanceof BundleReference) {
            return BundleContextSelector.locateContext(((BundleReference)loader).getBundle(), configLocation);
        }
        Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
        if (callerClass != null) {
            return BundleContextSelector.locateContext(FrameworkUtil.getBundle(callerClass), configLocation);
        }
        LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
        return lc == null ? this.getDefault() : lc;
    }

    private static boolean hasContext(Bundle bundle) {
        String name = Objects.requireNonNull(bundle, "No Bundle provided").getSymbolicName();
        AtomicReference ref = (AtomicReference)CONTEXT_MAP.get(name);
        return ref != null && ref.get() != null && ((WeakReference)ref.get()).get() != null && ((LoggerContext)((WeakReference)ref.get()).get()).isStarted();
    }

    private static LoggerContext locateContext(Bundle bundle, URI configLocation) {
        String name = Objects.requireNonNull(bundle, "No Bundle provided").getSymbolicName();
        AtomicReference ref = (AtomicReference)CONTEXT_MAP.get(name);
        if (ref == null) {
            LoggerContext context = new LoggerContext(name, (Object)bundle, configLocation);
            CONTEXT_MAP.putIfAbsent(name, new AtomicReference<WeakReference<LoggerContext>>(new WeakReference<LoggerContext>(context)));
            return (LoggerContext)((WeakReference)((AtomicReference)CONTEXT_MAP.get(name)).get()).get();
        }
        WeakReference r = (WeakReference)ref.get();
        LoggerContext ctx = (LoggerContext)r.get();
        if (ctx == null) {
            LoggerContext context = new LoggerContext(name, (Object)bundle, configLocation);
            ref.compareAndSet(r, new WeakReference<LoggerContext>(context));
            return (LoggerContext)((WeakReference)ref.get()).get();
        }
        URI oldConfigLocation = ctx.getConfigLocation();
        if (oldConfigLocation == null && configLocation != null) {
            LOGGER.debug("Setting bundle ({}) configuration to {}", name, configLocation);
            ctx.setConfigLocation(configLocation);
        } else if (oldConfigLocation != null && configLocation != null && !configLocation.equals(oldConfigLocation)) {
            LOGGER.warn("locateContext called with URI [{}], but existing LoggerContext has URI [{}]", configLocation, oldConfigLocation);
        }
        return ctx;
    }
}

