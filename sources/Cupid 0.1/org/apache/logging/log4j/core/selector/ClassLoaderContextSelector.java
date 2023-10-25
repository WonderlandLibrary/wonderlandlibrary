package org.apache.logging.log4j.core.selector;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextShutdownAware;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.StackLocatorUtil;

public class ClassLoaderContextSelector implements ContextSelector, LoggerContextShutdownAware {
  private static final AtomicReference<LoggerContext> DEFAULT_CONTEXT = new AtomicReference<>();
  
  protected static final StatusLogger LOGGER = StatusLogger.getLogger();
  
  protected static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP = new ConcurrentHashMap<>();
  
  public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
    LoggerContext ctx = null;
    if (currentContext) {
      ctx = ContextAnchor.THREAD_CONTEXT.get();
    } else if (loader != null) {
      ctx = findContext(loader);
    } else {
      Class<?> clazz = StackLocatorUtil.getCallerClass(fqcn);
      if (clazz != null)
        ctx = findContext(clazz.getClassLoader()); 
      if (ctx == null)
        ctx = ContextAnchor.THREAD_CONTEXT.get(); 
    } 
    if (ctx != null)
      ctx.stop(50L, TimeUnit.MILLISECONDS); 
  }
  
  public void contextShutdown(LoggerContext loggerContext) {
    if (loggerContext instanceof LoggerContext)
      removeContext((LoggerContext)loggerContext); 
  }
  
  public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
    LoggerContext ctx;
    if (currentContext) {
      ctx = ContextAnchor.THREAD_CONTEXT.get();
    } else if (loader != null) {
      ctx = findContext(loader);
    } else {
      Class<?> clazz = StackLocatorUtil.getCallerClass(fqcn);
      if (clazz != null) {
        ctx = findContext(clazz.getClassLoader());
      } else {
        ctx = ContextAnchor.THREAD_CONTEXT.get();
      } 
    } 
    return (ctx != null && ctx.isStarted());
  }
  
  private LoggerContext findContext(ClassLoader loaderOrNull) {
    ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
    String name = toContextMapKey(loader);
    AtomicReference<WeakReference<LoggerContext>> ref = CONTEXT_MAP.get(name);
    if (ref != null) {
      WeakReference<LoggerContext> weakRef = ref.get();
      return weakRef.get();
    } 
    return null;
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
    return getContext(fqcn, loader, currentContext, null);
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
    return getContext(fqcn, loader, null, currentContext, configLocation);
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext, URI configLocation) {
    if (currentContext) {
      LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
      if (ctx != null)
        return ctx; 
      return getDefault();
    } 
    if (loader != null)
      return locateContext(loader, entry, configLocation); 
    Class<?> clazz = StackLocatorUtil.getCallerClass(fqcn);
    if (clazz != null)
      return locateContext(clazz.getClassLoader(), entry, configLocation); 
    LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
    if (lc != null)
      return lc; 
    return getDefault();
  }
  
  public void removeContext(LoggerContext context) {
    for (Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : CONTEXT_MAP.entrySet()) {
      LoggerContext ctx = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)entry.getValue()).get()).get();
      if (ctx == context)
        CONTEXT_MAP.remove(entry.getKey()); 
    } 
  }
  
  public boolean isClassLoaderDependent() {
    return true;
  }
  
  public List<LoggerContext> getLoggerContexts() {
    List<LoggerContext> list = new ArrayList<>();
    Collection<AtomicReference<WeakReference<LoggerContext>>> coll = CONTEXT_MAP.values();
    for (AtomicReference<WeakReference<LoggerContext>> ref : coll) {
      LoggerContext ctx = ((WeakReference<LoggerContext>)ref.get()).get();
      if (ctx != null)
        list.add(ctx); 
    } 
    return Collections.unmodifiableList(list);
  }
  
  private LoggerContext locateContext(ClassLoader loaderOrNull, Map.Entry<String, Object> entry, URI configLocation) {
    ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
    String name = toContextMapKey(loader);
    AtomicReference<WeakReference<LoggerContext>> ref = CONTEXT_MAP.get(name);
    if (ref == null) {
      if (configLocation == null) {
        ClassLoader parent = loader.getParent();
        while (parent != null) {
          ref = CONTEXT_MAP.get(toContextMapKey(parent));
          if (ref != null) {
            WeakReference<LoggerContext> r = ref.get();
            LoggerContext loggerContext = r.get();
            if (loggerContext != null)
              return loggerContext; 
          } 
          parent = parent.getParent();
        } 
      } 
      LoggerContext loggerContext1 = createContext(name, configLocation);
      if (entry != null)
        loggerContext1.putObject(entry.getKey(), entry.getValue()); 
      LoggerContext newContext = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.computeIfAbsent(name, k -> new AtomicReference(new WeakReference<>(ctx)))).get()).get();
      if (newContext == loggerContext1)
        loggerContext1.addShutdownListener(this); 
      return newContext;
    } 
    WeakReference<LoggerContext> weakRef = ref.get();
    LoggerContext ctx = weakRef.get();
    if (ctx != null) {
      if (entry != null && ctx.getObject(entry.getKey()) == null)
        ctx.putObject(entry.getKey(), entry.getValue()); 
      if (ctx.getConfigLocation() == null && configLocation != null) {
        LOGGER.debug("Setting configuration to {}", configLocation);
        ctx.setConfigLocation(configLocation);
      } else if (ctx.getConfigLocation() != null && configLocation != null && 
        !ctx.getConfigLocation().equals(configLocation)) {
        LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", configLocation, ctx
            .getConfigLocation());
      } 
      return ctx;
    } 
    ctx = createContext(name, configLocation);
    if (entry != null)
      ctx.putObject(entry.getKey(), entry.getValue()); 
    ref.compareAndSet(weakRef, new WeakReference<>(ctx));
    return ctx;
  }
  
  protected LoggerContext createContext(String name, URI configLocation) {
    return new LoggerContext(name, null, configLocation);
  }
  
  protected String toContextMapKey(ClassLoader loader) {
    return Integer.toHexString(System.identityHashCode(loader));
  }
  
  protected LoggerContext getDefault() {
    LoggerContext ctx = DEFAULT_CONTEXT.get();
    if (ctx != null)
      return ctx; 
    DEFAULT_CONTEXT.compareAndSet(null, createContext(defaultContextName(), null));
    return DEFAULT_CONTEXT.get();
  }
  
  protected String defaultContextName() {
    return "Default";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\selector\ClassLoaderContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */