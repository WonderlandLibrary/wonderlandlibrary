package org.apache.logging.log4j.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.ThreadContextDataInjector;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.core.util.Cancellable;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.LoggerContextShutdownAware;
import org.apache.logging.log4j.spi.LoggerContextShutdownEnabled;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.apache.logging.log4j.spi.Terminable;
import org.apache.logging.log4j.spi.ThreadContextMapFactory;
import org.apache.logging.log4j.util.PropertiesUtil;

public class LoggerContext extends AbstractLifeCycle implements LoggerContext, AutoCloseable, Terminable, ConfigurationListener, LoggerContextShutdownEnabled {
  public static final String PROPERTY_CONFIG = "config";
  
  static {
    try {
      Loader.loadClass(ExecutorServices.class.getName());
    } catch (Exception e) {
      LOGGER.error("Failed to preload ExecutorServices class.", e);
    } 
  }
  
  private static final Configuration NULL_CONFIGURATION = (Configuration)new NullConfiguration();
  
  private final LoggerRegistry<Logger> loggerRegistry = new LoggerRegistry();
  
  private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<>();
  
  private volatile List<LoggerContextShutdownAware> listeners;
  
  private volatile Configuration configuration = (Configuration)new DefaultConfiguration();
  
  private static final String EXTERNAL_CONTEXT_KEY = "__EXTERNAL_CONTEXT_KEY__";
  
  private ConcurrentMap<String, Object> externalMap = new ConcurrentHashMap<>();
  
  private String contextName;
  
  private volatile URI configLocation;
  
  private Cancellable shutdownCallback;
  
  private final Lock configLock = new ReentrantLock();
  
  public LoggerContext(String name) {
    this(name, (Object)null, (URI)null);
  }
  
  public LoggerContext(String name, Object externalContext) {
    this(name, externalContext, (URI)null);
  }
  
  public LoggerContext(String name, Object externalContext, URI configLocn) {
    this.contextName = name;
    if (externalContext == null) {
      this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
    } else {
      this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", externalContext);
    } 
    this.configLocation = configLocn;
    Thread runner = new Thread(new ThreadContextDataTask(), "Thread Context Data Task");
    runner.setDaemon(true);
    runner.start();
  }
  
  public LoggerContext(String name, Object externalContext, String configLocn) {
    this.contextName = name;
    if (externalContext == null) {
      this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
    } else {
      this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", externalContext);
    } 
    if (configLocn != null) {
      URI uri;
      try {
        uri = (new File(configLocn)).toURI();
      } catch (Exception ex) {
        uri = null;
      } 
      this.configLocation = uri;
    } else {
      this.configLocation = null;
    } 
    Thread runner = new Thread(new ThreadContextDataTask(), "Thread Context Data Task");
    runner.setDaemon(true);
    runner.start();
  }
  
  public void addShutdownListener(LoggerContextShutdownAware listener) {
    if (this.listeners == null)
      synchronized (this) {
        if (this.listeners == null)
          this.listeners = new CopyOnWriteArrayList<>(); 
      }  
    this.listeners.add(listener);
  }
  
  public List<LoggerContextShutdownAware> getListeners() {
    return this.listeners;
  }
  
  public static LoggerContext getContext() {
    return (LoggerContext)LogManager.getContext();
  }
  
  public static LoggerContext getContext(boolean currentContext) {
    return (LoggerContext)LogManager.getContext(currentContext);
  }
  
  public static LoggerContext getContext(ClassLoader loader, boolean currentContext, URI configLocation) {
    return (LoggerContext)LogManager.getContext(loader, currentContext, configLocation);
  }
  
  public void start() {
    LOGGER.debug("Starting LoggerContext[name={}, {}]...", getName(), this);
    if (PropertiesUtil.getProperties().getBooleanProperty("log4j.LoggerContext.stacktrace.on.start", false))
      LOGGER.debug("Stack trace to locate invoker", new Exception("Not a real error, showing stack trace to locate invoker")); 
    if (this.configLock.tryLock())
      try {
        if (isInitialized() || isStopped()) {
          setStarting();
          reconfigure();
          if (this.configuration.isShutdownHookEnabled())
            setUpShutdownHook(); 
          setStarted();
        } 
      } finally {
        this.configLock.unlock();
      }  
    LOGGER.debug("LoggerContext[name={}, {}] started OK.", getName(), this);
  }
  
  public void start(Configuration config) {
    LOGGER.debug("Starting LoggerContext[name={}, {}] with configuration {}...", getName(), this, config);
    if (this.configLock.tryLock())
      try {
        if (isInitialized() || isStopped()) {
          if (this.configuration.isShutdownHookEnabled())
            setUpShutdownHook(); 
          setStarted();
        } 
      } finally {
        this.configLock.unlock();
      }  
    setConfiguration(config);
    LOGGER.debug("LoggerContext[name={}, {}] started OK with configuration {}.", getName(), this, config);
  }
  
  private void setUpShutdownHook() {
    if (this.shutdownCallback == null) {
      LoggerContextFactory factory = LogManager.getFactory();
      if (factory instanceof ShutdownCallbackRegistry) {
        LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Shutdown hook enabled. Registering a new one.");
        try {
          final long shutdownTimeoutMillis = this.configuration.getShutdownTimeoutMillis();
          this.shutdownCallback = ((ShutdownCallbackRegistry)factory).addShutdownCallback(new Runnable() {
                public void run() {
                  LoggerContext context = LoggerContext.this;
                  AbstractLifeCycle.LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Stopping LoggerContext[name={}, {}]", context
                      .getName(), context);
                  context.stop(shutdownTimeoutMillis, TimeUnit.MILLISECONDS);
                }
                
                public String toString() {
                  return "Shutdown callback for LoggerContext[name=" + LoggerContext.this.getName() + ']';
                }
              });
        } catch (IllegalStateException e) {
          throw new IllegalStateException("Unable to register Log4j shutdown hook because JVM is shutting down.", e);
        } catch (SecurityException e) {
          LOGGER.error(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Unable to register shutdown hook due to security restrictions", e);
        } 
      } 
    } 
  }
  
  public void close() {
    stop();
  }
  
  public void terminate() {
    stop();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    LOGGER.debug("Stopping LoggerContext[name={}, {}]...", getName(), this);
    this.configLock.lock();
    try {
      if (isStopped())
        return true; 
      setStopping();
      try {
        Server.unregisterLoggerContext(getName());
      } catch (LinkageError|Exception e) {
        LOGGER.error("Unable to unregister MBeans", e);
      } 
      if (this.shutdownCallback != null) {
        this.shutdownCallback.cancel();
        this.shutdownCallback = null;
      } 
      Configuration prev = this.configuration;
      this.configuration = NULL_CONFIGURATION;
      updateLoggers();
      if (prev instanceof LifeCycle2) {
        ((LifeCycle2)prev).stop(timeout, timeUnit);
      } else {
        prev.stop();
      } 
      this.externalMap.clear();
      LogManager.getFactory().removeContext(this);
    } finally {
      this.configLock.unlock();
      setStopped();
    } 
    if (this.listeners != null)
      for (LoggerContextShutdownAware listener : this.listeners) {
        try {
          listener.contextShutdown(this);
        } catch (Exception exception) {}
      }  
    LOGGER.debug("Stopped LoggerContext[name={}, {}] with status {}", getName(), this, Boolean.valueOf(true));
    return true;
  }
  
  public String getName() {
    return this.contextName;
  }
  
  public Logger getRootLogger() {
    return getLogger("");
  }
  
  public void setName(String name) {
    this.contextName = Objects.<String>requireNonNull(name);
  }
  
  public Object getObject(String key) {
    return this.externalMap.get(key);
  }
  
  public Object putObject(String key, Object value) {
    return this.externalMap.put(key, value);
  }
  
  public Object putObjectIfAbsent(String key, Object value) {
    return this.externalMap.putIfAbsent(key, value);
  }
  
  public Object removeObject(String key) {
    return this.externalMap.remove(key);
  }
  
  public boolean removeObject(String key, Object value) {
    return this.externalMap.remove(key, value);
  }
  
  public void setExternalContext(Object context) {
    if (context != null) {
      this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", context);
    } else {
      this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
    } 
  }
  
  public Object getExternalContext() {
    return this.externalMap.get("__EXTERNAL_CONTEXT_KEY__");
  }
  
  public Logger getLogger(String name) {
    return getLogger(name, (MessageFactory)null);
  }
  
  public Collection<Logger> getLoggers() {
    return this.loggerRegistry.getLoggers();
  }
  
  public Logger getLogger(String name, MessageFactory messageFactory) {
    Logger logger = (Logger)this.loggerRegistry.getLogger(name, messageFactory);
    if (logger != null) {
      AbstractLogger.checkMessageFactory((ExtendedLogger)logger, messageFactory);
      return logger;
    } 
    logger = newInstance(this, name, messageFactory);
    this.loggerRegistry.putIfAbsent(name, messageFactory, (ExtendedLogger)logger);
    return (Logger)this.loggerRegistry.getLogger(name, messageFactory);
  }
  
  public boolean hasLogger(String name) {
    return this.loggerRegistry.hasLogger(name);
  }
  
  public boolean hasLogger(String name, MessageFactory messageFactory) {
    return this.loggerRegistry.hasLogger(name, messageFactory);
  }
  
  public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
    return this.loggerRegistry.hasLogger(name, messageFactoryClass);
  }
  
  public Configuration getConfiguration() {
    return this.configuration;
  }
  
  public void addFilter(Filter filter) {
    this.configuration.addFilter(filter);
  }
  
  public void removeFilter(Filter filter) {
    this.configuration.removeFilter(filter);
  }
  
  public Configuration setConfiguration(Configuration config) {
    if (config == null) {
      LOGGER.error("No configuration found for context '{}'.", this.contextName);
      return this.configuration;
    } 
    this.configLock.lock();
    try {
      Configuration prev = this.configuration;
      config.addListener(this);
      ConcurrentMap<String, String> map = (ConcurrentMap<String, String>)config.getComponent("ContextProperties");
      try {
        map.computeIfAbsent("hostName", s -> NetUtils.getLocalHostname());
      } catch (Exception ex) {
        LOGGER.debug("Ignoring {}, setting hostName to 'unknown'", ex.toString());
        map.putIfAbsent("hostName", "unknown");
      } 
      map.putIfAbsent("contextName", this.contextName);
      config.start();
      this.configuration = config;
      updateLoggers();
      if (prev != null) {
        prev.removeListener(this);
        prev.stop();
      } 
      firePropertyChangeEvent(new PropertyChangeEvent(this, "config", prev, config));
      try {
        Server.reregisterMBeansAfterReconfigure();
      } catch (LinkageError|Exception e) {
        LOGGER.error("Could not reconfigure JMX", e);
      } 
      Log4jLogEvent.setNanoClock(this.configuration.getNanoClock());
      return prev;
    } finally {
      this.configLock.unlock();
    } 
  }
  
  private void firePropertyChangeEvent(PropertyChangeEvent event) {
    for (PropertyChangeListener listener : this.propertyChangeListeners)
      listener.propertyChange(event); 
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    this.propertyChangeListeners.add(Objects.requireNonNull(listener, "listener"));
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.propertyChangeListeners.remove(listener);
  }
  
  public URI getConfigLocation() {
    return this.configLocation;
  }
  
  public void setConfigLocation(URI configLocation) {
    this.configLocation = configLocation;
    reconfigure(configLocation);
  }
  
  private void reconfigure(URI configURI) {
    Object externalContext = this.externalMap.get("__EXTERNAL_CONTEXT_KEY__");
    ClassLoader cl = ClassLoader.class.isInstance(externalContext) ? (ClassLoader)externalContext : null;
    LOGGER.debug("Reconfiguration started for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, configURI, this, cl);
    Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this, this.contextName, configURI, cl);
    if (instance == null) {
      LOGGER.error("Reconfiguration failed: No configuration found for '{}' at '{}' in '{}'", this.contextName, configURI, cl);
    } else {
      setConfiguration(instance);
      String location = (this.configuration == null) ? "?" : String.valueOf(this.configuration.getConfigurationSource());
      LOGGER.debug("Reconfiguration complete for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, location, this, cl);
    } 
  }
  
  public void reconfigure() {
    reconfigure(this.configLocation);
  }
  
  public void reconfigure(Configuration configuration) {
    setConfiguration(configuration);
    ConfigurationSource source = configuration.getConfigurationSource();
    if (source != null) {
      URI uri = source.getURI();
      if (uri != null)
        this.configLocation = uri; 
    } 
  }
  
  public void updateLoggers() {
    updateLoggers(this.configuration);
  }
  
  public void updateLoggers(Configuration config) {
    Configuration old = this.configuration;
    for (Logger logger : this.loggerRegistry.getLoggers())
      logger.updateConfiguration(config); 
    firePropertyChangeEvent(new PropertyChangeEvent(this, "config", old, config));
  }
  
  public synchronized void onChange(Reconfigurable reconfigurable) {
    long startMillis = System.currentTimeMillis();
    LOGGER.debug("Reconfiguration started for context {} ({})", this.contextName, this);
    initApiModule();
    Configuration newConfig = reconfigurable.reconfigure();
    if (newConfig != null) {
      setConfiguration(newConfig);
      LOGGER.debug("Reconfiguration completed for {} ({}) in {} milliseconds.", this.contextName, this, 
          Long.valueOf(System.currentTimeMillis() - startMillis));
    } else {
      LOGGER.debug("Reconfiguration failed for {} ({}) in {} milliseconds.", this.contextName, this, 
          Long.valueOf(System.currentTimeMillis() - startMillis));
    } 
  }
  
  private void initApiModule() {
    ThreadContextMapFactory.init();
  }
  
  protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory) {
    return new Logger(ctx, name, messageFactory);
  }
  
  private class ThreadContextDataTask implements Runnable {
    private ThreadContextDataTask() {}
    
    public void run() {
      AbstractLifeCycle.LOGGER.debug("Initializing Thread Context Data Service Providers");
      ThreadContextDataInjector.initServiceProviders();
      AbstractLifeCycle.LOGGER.debug("Thread Context Data Service Provider initialization complete");
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\LoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */