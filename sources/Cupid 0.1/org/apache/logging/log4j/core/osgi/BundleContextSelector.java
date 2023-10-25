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

public class BundleContextSelector extends ClassLoaderContextSelector {
  public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
    LoggerContext ctx = null;
    Bundle bundle = null;
    if (currentContext) {
      ctx = ContextAnchor.THREAD_CONTEXT.get();
      ContextAnchor.THREAD_CONTEXT.remove();
    } 
    if (ctx == null && loader instanceof BundleReference) {
      bundle = ((BundleReference)loader).getBundle();
      ctx = getLoggerContext(bundle);
      removeLoggerContext(ctx);
    } 
    if (ctx == null) {
      Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
      if (callerClass != null) {
        bundle = FrameworkUtil.getBundle(callerClass);
        ctx = getLoggerContext(FrameworkUtil.getBundle(callerClass));
        removeLoggerContext(ctx);
      } 
    } 
    if (ctx == null) {
      ctx = ContextAnchor.THREAD_CONTEXT.get();
      ContextAnchor.THREAD_CONTEXT.remove();
    } 
    if (ctx != null)
      ctx.stop(50L, TimeUnit.MILLISECONDS); 
    if (bundle != null && allContexts) {
      Bundle[] bundles = bundle.getBundleContext().getBundles();
      for (Bundle bdl : bundles) {
        ctx = getLoggerContext(bdl);
        if (ctx != null)
          ctx.stop(50L, TimeUnit.MILLISECONDS); 
      } 
    } 
  }
  
  private LoggerContext getLoggerContext(Bundle bundle) {
    String name = ((Bundle)Objects.<Bundle>requireNonNull(bundle, "No Bundle provided")).getSymbolicName();
    AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name);
    if (ref != null && ref.get() != null)
      return ((WeakReference<LoggerContext>)ref.get()).get(); 
    return null;
  }
  
  private void removeLoggerContext(LoggerContext context) {
    CONTEXT_MAP.remove(context.getName());
  }
  
  public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
    if (currentContext && ContextAnchor.THREAD_CONTEXT.get() != null)
      return ((LoggerContext)ContextAnchor.THREAD_CONTEXT.get()).isStarted(); 
    if (loader instanceof BundleReference)
      return hasContext(((BundleReference)loader).getBundle()); 
    Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
    if (callerClass != null)
      return hasContext(FrameworkUtil.getBundle(callerClass)); 
    return (ContextAnchor.THREAD_CONTEXT.get() != null && ((LoggerContext)ContextAnchor.THREAD_CONTEXT.get()).isStarted());
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
    if (currentContext) {
      LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
      if (ctx != null)
        return ctx; 
      return getDefault();
    } 
    if (loader instanceof BundleReference)
      return locateContext(((BundleReference)loader).getBundle(), configLocation); 
    Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
    if (callerClass != null)
      return locateContext(FrameworkUtil.getBundle(callerClass), configLocation); 
    LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
    return (lc == null) ? getDefault() : lc;
  }
  
  private static boolean hasContext(Bundle bundle) {
    String name = ((Bundle)Objects.<Bundle>requireNonNull(bundle, "No Bundle provided")).getSymbolicName();
    AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name);
    return (ref != null && ref.get() != null && ((WeakReference)ref.get()).get() != null && ((LoggerContext)((WeakReference<LoggerContext>)ref.get()).get()).isStarted());
  }
  
  private static LoggerContext locateContext(Bundle bundle, URI configLocation) {
    String name = ((Bundle)Objects.<Bundle>requireNonNull(bundle, "No Bundle provided")).getSymbolicName();
    AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name);
    if (ref == null) {
      LoggerContext context = new LoggerContext(name, bundle, configLocation);
      CONTEXT_MAP.putIfAbsent(name, new AtomicReference(new WeakReference<>(context)));
      return ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name)).get()).get();
    } 
    WeakReference<LoggerContext> r = ref.get();
    LoggerContext ctx = r.get();
    if (ctx == null) {
      LoggerContext context = new LoggerContext(name, bundle, configLocation);
      ref.compareAndSet(r, new WeakReference<>(context));
      return ((WeakReference<LoggerContext>)ref.get()).get();
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\osgi\BundleContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */