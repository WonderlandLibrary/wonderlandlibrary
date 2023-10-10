// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.osgi;

import java.net.URI;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Objects;
import org.osgi.framework.Bundle;
import java.util.concurrent.TimeUnit;
import org.osgi.framework.FrameworkUtil;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.osgi.framework.BundleReference;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;

public class BundleContextSelector extends ClassLoaderContextSelector
{
    @Override
    public void shutdown(final String fqcn, final ClassLoader loader, final boolean currentContext, final boolean allContexts) {
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
        if (ctx == null) {
            final Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
            if (callerClass != null) {
                bundle = FrameworkUtil.getBundle((Class)callerClass);
                ctx = this.getLoggerContext(FrameworkUtil.getBundle((Class)callerClass));
                this.removeLoggerContext(ctx);
            }
        }
        if (ctx == null) {
            ctx = ContextAnchor.THREAD_CONTEXT.get();
            ContextAnchor.THREAD_CONTEXT.remove();
        }
        if (ctx != null) {
            ctx.stop(50L, TimeUnit.MILLISECONDS);
        }
        if (bundle != null && allContexts) {
            final Bundle[] bundles2;
            final Bundle[] bundles = bundles2 = bundle.getBundleContext().getBundles();
            for (final Bundle bdl : bundles2) {
                ctx = this.getLoggerContext(bdl);
                if (ctx != null) {
                    ctx.stop(50L, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
    
    private LoggerContext getLoggerContext(final Bundle bundle) {
        final String name = Objects.requireNonNull(bundle, "No Bundle provided").getSymbolicName();
        final AtomicReference<WeakReference<LoggerContext>> ref = BundleContextSelector.CONTEXT_MAP.get(name);
        if (ref != null && ref.get() != null) {
            return ref.get().get();
        }
        return null;
    }
    
    private void removeLoggerContext(final LoggerContext context) {
        BundleContextSelector.CONTEXT_MAP.remove(context.getName());
    }
    
    @Override
    public boolean hasContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        if (currentContext && ContextAnchor.THREAD_CONTEXT.get() != null) {
            return ContextAnchor.THREAD_CONTEXT.get().isStarted();
        }
        if (loader instanceof BundleReference) {
            return hasContext(((BundleReference)loader).getBundle());
        }
        final Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
        if (callerClass != null) {
            return hasContext(FrameworkUtil.getBundle((Class)callerClass));
        }
        return ContextAnchor.THREAD_CONTEXT.get() != null && ContextAnchor.THREAD_CONTEXT.get().isStarted();
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        if (currentContext) {
            final LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
            if (ctx != null) {
                return ctx;
            }
            return this.getDefault();
        }
        else {
            if (loader instanceof BundleReference) {
                return locateContext(((BundleReference)loader).getBundle(), configLocation);
            }
            final Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
            if (callerClass != null) {
                return locateContext(FrameworkUtil.getBundle((Class)callerClass), configLocation);
            }
            final LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
            return (lc == null) ? this.getDefault() : lc;
        }
    }
    
    private static boolean hasContext(final Bundle bundle) {
        final String name = Objects.requireNonNull(bundle, "No Bundle provided").getSymbolicName();
        final AtomicReference<WeakReference<LoggerContext>> ref = BundleContextSelector.CONTEXT_MAP.get(name);
        return ref != null && ref.get() != null && ref.get().get() != null && ref.get().get().isStarted();
    }
    
    private static LoggerContext locateContext(final Bundle bundle, final URI configLocation) {
        final String name = Objects.requireNonNull(bundle, "No Bundle provided").getSymbolicName();
        final AtomicReference<WeakReference<LoggerContext>> ref = BundleContextSelector.CONTEXT_MAP.get(name);
        if (ref == null) {
            final LoggerContext context = new LoggerContext(name, bundle, configLocation);
            BundleContextSelector.CONTEXT_MAP.putIfAbsent(name, new AtomicReference<WeakReference<LoggerContext>>(new WeakReference<LoggerContext>(context)));
            return BundleContextSelector.CONTEXT_MAP.get(name).get().get();
        }
        final WeakReference<LoggerContext> r = ref.get();
        final LoggerContext ctx = r.get();
        if (ctx == null) {
            final LoggerContext context2 = new LoggerContext(name, bundle, configLocation);
            ref.compareAndSet(r, new WeakReference<LoggerContext>(context2));
            return ref.get().get();
        }
        final URI oldConfigLocation = ctx.getConfigLocation();
        if (oldConfigLocation == null && configLocation != null) {
            BundleContextSelector.LOGGER.debug("Setting bundle ({}) configuration to {}", name, configLocation);
            ctx.setConfigLocation(configLocation);
        }
        else if (oldConfigLocation != null && configLocation != null && !configLocation.equals(oldConfigLocation)) {
            BundleContextSelector.LOGGER.warn("locateContext called with URI [{}], but existing LoggerContext has URI [{}]", configLocation, oldConfigLocation);
        }
        return ctx;
    }
}
