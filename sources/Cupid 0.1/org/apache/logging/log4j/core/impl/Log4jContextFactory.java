package org.apache.logging.log4j.core.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.core.util.Cancellable;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.util.DefaultShutdownCallbackRegistry;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class Log4jContextFactory implements LoggerContextFactory, ShutdownCallbackRegistry {
  private static final StatusLogger LOGGER = StatusLogger.getLogger();
  
  private static final boolean SHUTDOWN_HOOK_ENABLED = (PropertiesUtil.getProperties().getBooleanProperty("log4j.shutdownHookEnabled", true) && !Constants.IS_WEB_APP);
  
  private final ContextSelector selector;
  
  private final ShutdownCallbackRegistry shutdownCallbackRegistry;
  
  public Log4jContextFactory() {
    this(createContextSelector(), createShutdownCallbackRegistry());
  }
  
  public Log4jContextFactory(ContextSelector selector) {
    this(selector, createShutdownCallbackRegistry());
  }
  
  public Log4jContextFactory(ShutdownCallbackRegistry shutdownCallbackRegistry) {
    this(createContextSelector(), shutdownCallbackRegistry);
  }
  
  public Log4jContextFactory(ContextSelector selector, ShutdownCallbackRegistry shutdownCallbackRegistry) {
    this.selector = Objects.<ContextSelector>requireNonNull(selector, "No ContextSelector provided");
    this.shutdownCallbackRegistry = Objects.<ShutdownCallbackRegistry>requireNonNull(shutdownCallbackRegistry, "No ShutdownCallbackRegistry provided");
    LOGGER.debug("Using ShutdownCallbackRegistry {}", shutdownCallbackRegistry.getClass());
    initializeShutdownCallbackRegistry();
  }
  
  private static ContextSelector createContextSelector() {
    try {
      ContextSelector selector = (ContextSelector)Loader.newCheckedInstanceOfProperty("Log4jContextSelector", ContextSelector.class);
      if (selector != null)
        return selector; 
    } catch (Exception e) {
      LOGGER.error("Unable to create custom ContextSelector. Falling back to default.", e);
    } 
    return (ContextSelector)new ClassLoaderContextSelector();
  }
  
  private static ShutdownCallbackRegistry createShutdownCallbackRegistry() {
    try {
      ShutdownCallbackRegistry registry = (ShutdownCallbackRegistry)Loader.newCheckedInstanceOfProperty("log4j.shutdownCallbackRegistry", ShutdownCallbackRegistry.class);
      if (registry != null)
        return registry; 
    } catch (Exception e) {
      LOGGER.error("Unable to create custom ShutdownCallbackRegistry. Falling back to default.", e);
    } 
    return (ShutdownCallbackRegistry)new DefaultShutdownCallbackRegistry();
  }
  
  private void initializeShutdownCallbackRegistry() {
    if (isShutdownHookEnabled() && this.shutdownCallbackRegistry instanceof LifeCycle)
      try {
        ((LifeCycle)this.shutdownCallbackRegistry).start();
      } catch (IllegalStateException e) {
        LOGGER.error("Cannot start ShutdownCallbackRegistry, already shutting down.");
        throw e;
      } catch (RuntimeException e) {
        LOGGER.error("There was an error starting the ShutdownCallbackRegistry.", e);
      }  
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
    LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext);
    if (externalContext != null && ctx.getExternalContext() == null)
      ctx.setExternalContext(externalContext); 
    if (ctx.getState() == LifeCycle.State.INITIALIZED)
      ctx.start(); 
    return ctx;
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, ConfigurationSource source) {
    LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
    if (externalContext != null && ctx.getExternalContext() == null)
      ctx.setExternalContext(externalContext); 
    if (ctx.getState() == LifeCycle.State.INITIALIZED)
      if (source != null) {
        ContextAnchor.THREAD_CONTEXT.set(ctx);
        Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, source);
        LOGGER.debug("Starting LoggerContext[name={}] from configuration {}", ctx.getName(), source);
        ctx.start(config);
        ContextAnchor.THREAD_CONTEXT.remove();
      } else {
        ctx.start();
      }  
    return ctx;
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, Configuration configuration) {
    LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
    if (externalContext != null && ctx.getExternalContext() == null)
      ctx.setExternalContext(externalContext); 
    if (ctx.getState() == LifeCycle.State.INITIALIZED) {
      ContextAnchor.THREAD_CONTEXT.set(ctx);
      try {
        ctx.start(configuration);
      } finally {
        ContextAnchor.THREAD_CONTEXT.remove();
      } 
    } 
    return ctx;
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, URI configLocation, String name) {
    LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, configLocation);
    if (externalContext != null && ctx.getExternalContext() == null)
      ctx.setExternalContext(externalContext); 
    if (name != null)
      ctx.setName(name); 
    if (ctx.getState() == LifeCycle.State.INITIALIZED)
      if (configLocation != null || name != null) {
        ContextAnchor.THREAD_CONTEXT.set(ctx);
        Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
        LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", ctx.getName(), configLocation);
        ctx.start(config);
        ContextAnchor.THREAD_CONTEXT.remove();
      } else {
        ctx.start();
      }  
    return ctx;
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext, URI configLocation, String name) {
    LoggerContext ctx = this.selector.getContext(fqcn, loader, entry, currentContext, configLocation);
    if (name != null)
      ctx.setName(name); 
    if (ctx.getState() == LifeCycle.State.INITIALIZED)
      if (configLocation != null || name != null) {
        ContextAnchor.THREAD_CONTEXT.set(ctx);
        Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
        LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", ctx.getName(), configLocation);
        ctx.start(config);
        ContextAnchor.THREAD_CONTEXT.remove();
      } else {
        ctx.start();
      }  
    return ctx;
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, List<URI> configLocations, String name) {
    LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
    if (externalContext != null && ctx.getExternalContext() == null)
      ctx.setExternalContext(externalContext); 
    if (name != null)
      ctx.setName(name); 
    if (ctx.getState() == LifeCycle.State.INITIALIZED)
      if (configLocations != null && !configLocations.isEmpty()) {
        ContextAnchor.THREAD_CONTEXT.set(ctx);
        List<AbstractConfiguration> configurations = new ArrayList<>(configLocations.size());
        for (URI configLocation : configLocations) {
          Configuration currentReadConfiguration = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
          if (currentReadConfiguration != null) {
            if (currentReadConfiguration instanceof org.apache.logging.log4j.core.config.DefaultConfiguration) {
              LOGGER.warn("Unable to locate configuration {}, ignoring", configLocation.toString());
              continue;
            } 
            if (currentReadConfiguration instanceof AbstractConfiguration) {
              configurations.add((AbstractConfiguration)currentReadConfiguration);
              continue;
            } 
            LOGGER.error("Found configuration {}, which is not an AbstractConfiguration and can't be handled by CompositeConfiguration", configLocation);
            continue;
          } 
          LOGGER.info("Unable to access configuration {}, ignoring", configLocation.toString());
        } 
        if (configurations.isEmpty()) {
          LOGGER.error("No configurations could be created for {}", configLocations.toString());
        } else if (configurations.size() == 1) {
          AbstractConfiguration config = configurations.get(0);
          LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", ctx.getName(), config
              .getConfigurationSource().getLocation());
          ctx.start((Configuration)config);
        } else {
          CompositeConfiguration compositeConfiguration = new CompositeConfiguration(configurations);
          LOGGER.debug("Starting LoggerContext[name={}] from configurations at {}", ctx.getName(), configLocations);
          ctx.start((Configuration)compositeConfiguration);
        } 
        ContextAnchor.THREAD_CONTEXT.remove();
      } else {
        ctx.start();
      }  
    return ctx;
  }
  
  public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
    if (this.selector.hasContext(fqcn, loader, currentContext))
      this.selector.shutdown(fqcn, loader, currentContext, allContexts); 
  }
  
  public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
    return this.selector.hasContext(fqcn, loader, currentContext);
  }
  
  public ContextSelector getSelector() {
    return this.selector;
  }
  
  public ShutdownCallbackRegistry getShutdownCallbackRegistry() {
    return this.shutdownCallbackRegistry;
  }
  
  public void removeContext(LoggerContext context) {
    if (context instanceof LoggerContext)
      this.selector.removeContext((LoggerContext)context); 
  }
  
  public boolean isClassLoaderDependent() {
    return this.selector.isClassLoaderDependent();
  }
  
  public Cancellable addShutdownCallback(Runnable callback) {
    return isShutdownHookEnabled() ? this.shutdownCallbackRegistry.addShutdownCallback(callback) : null;
  }
  
  public boolean isShutdownHookEnabled() {
    return SHUTDOWN_HOOK_ENABLED;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\Log4jContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */