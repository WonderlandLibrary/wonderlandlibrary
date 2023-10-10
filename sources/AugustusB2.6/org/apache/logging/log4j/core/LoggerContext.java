// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.impl.ThreadContextDataInjector;
import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.apache.logging.log4j.spi.ThreadContextMapFactory;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.beans.PropertyChangeEvent;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.AbstractLogger;
import java.util.Collection;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.Objects;
import java.util.Iterator;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import java.util.concurrent.locks.Lock;
import org.apache.logging.log4j.core.util.Cancellable;
import java.net.URI;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.spi.LoggerContextShutdownAware;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.spi.LoggerContextShutdownEnabled;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.spi.Terminable;

public class LoggerContext extends AbstractLifeCycle implements org.apache.logging.log4j.spi.LoggerContext, AutoCloseable, Terminable, ConfigurationListener, LoggerContextShutdownEnabled
{
    public static final String PROPERTY_CONFIG = "config";
    private static final Configuration NULL_CONFIGURATION;
    private final LoggerRegistry<Logger> loggerRegistry;
    private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners;
    private volatile List<LoggerContextShutdownAware> listeners;
    private volatile Configuration configuration;
    private static final String EXTERNAL_CONTEXT_KEY = "__EXTERNAL_CONTEXT_KEY__";
    private ConcurrentMap<String, Object> externalMap;
    private String contextName;
    private volatile URI configLocation;
    private Cancellable shutdownCallback;
    private final Lock configLock;
    
    public LoggerContext(final String name) {
        this(name, null, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext) {
        this(name, externalContext, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext, final URI configLocn) {
        this.loggerRegistry = new LoggerRegistry<Logger>();
        this.propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        this.configuration = new DefaultConfiguration();
        this.externalMap = new ConcurrentHashMap<String, Object>();
        this.configLock = new ReentrantLock();
        this.contextName = name;
        if (externalContext == null) {
            this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
        }
        else {
            this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", externalContext);
        }
        this.configLocation = configLocn;
        final Thread runner = new Thread(new ThreadContextDataTask(), "Thread Context Data Task");
        runner.setDaemon(true);
        runner.start();
    }
    
    public LoggerContext(final String name, final Object externalContext, final String configLocn) {
        this.loggerRegistry = new LoggerRegistry<Logger>();
        this.propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        this.configuration = new DefaultConfiguration();
        this.externalMap = new ConcurrentHashMap<String, Object>();
        this.configLock = new ReentrantLock();
        this.contextName = name;
        if (externalContext == null) {
            this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
        }
        else {
            this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", externalContext);
        }
        if (configLocn != null) {
            URI uri;
            try {
                uri = new File(configLocn).toURI();
            }
            catch (Exception ex) {
                uri = null;
            }
            this.configLocation = uri;
        }
        else {
            this.configLocation = null;
        }
        final Thread runner = new Thread(new ThreadContextDataTask(), "Thread Context Data Task");
        runner.setDaemon(true);
        runner.start();
    }
    
    @Override
    public void addShutdownListener(final LoggerContextShutdownAware listener) {
        if (this.listeners == null) {
            synchronized (this) {
                if (this.listeners == null) {
                    this.listeners = new CopyOnWriteArrayList<LoggerContextShutdownAware>();
                }
            }
        }
        this.listeners.add(listener);
    }
    
    @Override
    public List<LoggerContextShutdownAware> getListeners() {
        return this.listeners;
    }
    
    public static LoggerContext getContext() {
        return (LoggerContext)LogManager.getContext();
    }
    
    public static LoggerContext getContext(final boolean currentContext) {
        return (LoggerContext)LogManager.getContext(currentContext);
    }
    
    public static LoggerContext getContext(final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        return (LoggerContext)LogManager.getContext(loader, currentContext, configLocation);
    }
    
    @Override
    public void start() {
        LoggerContext.LOGGER.debug("Starting LoggerContext[name={}, {}]...", this.getName(), this);
        if (PropertiesUtil.getProperties().getBooleanProperty("log4j.LoggerContext.stacktrace.on.start", false)) {
            LoggerContext.LOGGER.debug("Stack trace to locate invoker", new Exception("Not a real error, showing stack trace to locate invoker"));
        }
        if (this.configLock.tryLock()) {
            try {
                if (this.isInitialized() || this.isStopped()) {
                    this.setStarting();
                    this.reconfigure();
                    if (this.configuration.isShutdownHookEnabled()) {
                        this.setUpShutdownHook();
                    }
                    this.setStarted();
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
        LoggerContext.LOGGER.debug("LoggerContext[name={}, {}] started OK.", this.getName(), this);
    }
    
    public void start(final Configuration config) {
        LoggerContext.LOGGER.debug("Starting LoggerContext[name={}, {}] with configuration {}...", this.getName(), this, config);
        if (this.configLock.tryLock()) {
            try {
                if (this.isInitialized() || this.isStopped()) {
                    if (this.configuration.isShutdownHookEnabled()) {
                        this.setUpShutdownHook();
                    }
                    this.setStarted();
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
        this.setConfiguration(config);
        LoggerContext.LOGGER.debug("LoggerContext[name={}, {}] started OK with configuration {}.", this.getName(), this, config);
    }
    
    private void setUpShutdownHook() {
        if (this.shutdownCallback == null) {
            final LoggerContextFactory factory = LogManager.getFactory();
            if (factory instanceof ShutdownCallbackRegistry) {
                LoggerContext.LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Shutdown hook enabled. Registering a new one.");
                try {
                    final long shutdownTimeoutMillis = this.configuration.getShutdownTimeoutMillis();
                    this.shutdownCallback = ((ShutdownCallbackRegistry)factory).addShutdownCallback(new Runnable() {
                        @Override
                        public void run() {
                            final LoggerContext context = LoggerContext.this;
                            AbstractLifeCycle.LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Stopping LoggerContext[name={}, {}]", context.getName(), context);
                            context.stop(shutdownTimeoutMillis, TimeUnit.MILLISECONDS);
                        }
                        
                        @Override
                        public String toString() {
                            return "Shutdown callback for LoggerContext[name=" + LoggerContext.this.getName() + ']';
                        }
                    });
                }
                catch (IllegalStateException e) {
                    throw new IllegalStateException("Unable to register Log4j shutdown hook because JVM is shutting down.", e);
                }
                catch (SecurityException e2) {
                    LoggerContext.LOGGER.error(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Unable to register shutdown hook due to security restrictions", e2);
                }
            }
        }
    }
    
    @Override
    public void close() {
        this.stop();
    }
    
    @Override
    public void terminate() {
        this.stop();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        LoggerContext.LOGGER.debug("Stopping LoggerContext[name={}, {}]...", this.getName(), this);
        this.configLock.lock();
        try {
            if (this.isStopped()) {
                return true;
            }
            this.setStopping();
            try {
                Server.unregisterLoggerContext(this.getName());
            }
            catch (LinkageError | Exception linkageError) {
                final Throwable t;
                final Throwable e = t;
                LoggerContext.LOGGER.error("Unable to unregister MBeans", e);
            }
            if (this.shutdownCallback != null) {
                this.shutdownCallback.cancel();
                this.shutdownCallback = null;
            }
            final Configuration prev = this.configuration;
            this.configuration = LoggerContext.NULL_CONFIGURATION;
            this.updateLoggers();
            if (prev instanceof LifeCycle2) {
                ((LifeCycle2)prev).stop(timeout, timeUnit);
            }
            else {
                prev.stop();
            }
            this.externalMap.clear();
            LogManager.getFactory().removeContext(this);
        }
        finally {
            this.configLock.unlock();
            this.setStopped();
        }
        if (this.listeners != null) {
            for (final LoggerContextShutdownAware listener : this.listeners) {
                try {
                    listener.contextShutdown(this);
                }
                catch (Exception ex) {}
            }
        }
        LoggerContext.LOGGER.debug("Stopped LoggerContext[name={}, {}] with status {}", this.getName(), this, true);
        return true;
    }
    
    public String getName() {
        return this.contextName;
    }
    
    public Logger getRootLogger() {
        return this.getLogger("");
    }
    
    public void setName(final String name) {
        this.contextName = Objects.requireNonNull(name);
    }
    
    @Override
    public Object getObject(final String key) {
        return this.externalMap.get(key);
    }
    
    @Override
    public Object putObject(final String key, final Object value) {
        return this.externalMap.put(key, value);
    }
    
    @Override
    public Object putObjectIfAbsent(final String key, final Object value) {
        return this.externalMap.putIfAbsent(key, value);
    }
    
    @Override
    public Object removeObject(final String key) {
        return this.externalMap.remove(key);
    }
    
    @Override
    public boolean removeObject(final String key, final Object value) {
        return this.externalMap.remove(key, value);
    }
    
    public void setExternalContext(final Object context) {
        if (context != null) {
            this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", context);
        }
        else {
            this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
        }
    }
    
    @Override
    public Object getExternalContext() {
        return this.externalMap.get("__EXTERNAL_CONTEXT_KEY__");
    }
    
    @Override
    public Logger getLogger(final String name) {
        return this.getLogger(name, null);
    }
    
    public Collection<Logger> getLoggers() {
        return this.loggerRegistry.getLoggers();
    }
    
    @Override
    public Logger getLogger(final String name, final MessageFactory messageFactory) {
        Logger logger = this.loggerRegistry.getLogger(name, messageFactory);
        if (logger != null) {
            AbstractLogger.checkMessageFactory(logger, messageFactory);
            return logger;
        }
        logger = this.newInstance(this, name, messageFactory);
        this.loggerRegistry.putIfAbsent(name, messageFactory, logger);
        return this.loggerRegistry.getLogger(name, messageFactory);
    }
    
    @Override
    public boolean hasLogger(final String name) {
        return this.loggerRegistry.hasLogger(name);
    }
    
    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return this.loggerRegistry.hasLogger(name, messageFactory);
    }
    
    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return this.loggerRegistry.hasLogger(name, messageFactoryClass);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public void addFilter(final Filter filter) {
        this.configuration.addFilter(filter);
    }
    
    public void removeFilter(final Filter filter) {
        this.configuration.removeFilter(filter);
    }
    
    public Configuration setConfiguration(final Configuration config) {
        if (config == null) {
            LoggerContext.LOGGER.error("No configuration found for context '{}'.", this.contextName);
            return this.configuration;
        }
        this.configLock.lock();
        try {
            final Configuration prev = this.configuration;
            config.addListener(this);
            final ConcurrentMap<String, String> map = config.getComponent("ContextProperties");
            try {
                map.computeIfAbsent("hostName", s -> NetUtils.getLocalHostname());
            }
            catch (Exception ex) {
                LoggerContext.LOGGER.debug("Ignoring {}, setting hostName to 'unknown'", ex.toString());
                map.putIfAbsent("hostName", "unknown");
            }
            map.putIfAbsent("contextName", this.contextName);
            config.start();
            this.configuration = config;
            this.updateLoggers();
            if (prev != null) {
                prev.removeListener(this);
                prev.stop();
            }
            this.firePropertyChangeEvent(new PropertyChangeEvent(this, "config", prev, config));
            try {
                Server.reregisterMBeansAfterReconfigure();
            }
            catch (LinkageError | Exception linkageError) {
                final Throwable t;
                final Throwable e = t;
                LoggerContext.LOGGER.error("Could not reconfigure JMX", e);
            }
            Log4jLogEvent.setNanoClock(this.configuration.getNanoClock());
            return prev;
        }
        finally {
            this.configLock.unlock();
        }
    }
    
    private void firePropertyChangeEvent(final PropertyChangeEvent event) {
        for (final PropertyChangeListener listener : this.propertyChangeListeners) {
            listener.propertyChange(event);
        }
    }
    
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.add(Objects.requireNonNull(listener, "listener"));
    }
    
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.remove(listener);
    }
    
    public URI getConfigLocation() {
        return this.configLocation;
    }
    
    public void setConfigLocation(final URI configLocation) {
        this.reconfigure(this.configLocation = configLocation);
    }
    
    private void reconfigure(final URI configURI) {
        final Object externalContext = this.externalMap.get("__EXTERNAL_CONTEXT_KEY__");
        final ClassLoader cl = ClassLoader.class.isInstance(externalContext) ? ((ClassLoader)externalContext) : null;
        LoggerContext.LOGGER.debug("Reconfiguration started for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, configURI, this, cl);
        final Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this, this.contextName, configURI, cl);
        if (instance == null) {
            LoggerContext.LOGGER.error("Reconfiguration failed: No configuration found for '{}' at '{}' in '{}'", this.contextName, configURI, cl);
        }
        else {
            this.setConfiguration(instance);
            final String location = (this.configuration == null) ? "?" : String.valueOf(this.configuration.getConfigurationSource());
            LoggerContext.LOGGER.debug("Reconfiguration complete for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, location, this, cl);
        }
    }
    
    public void reconfigure() {
        this.reconfigure(this.configLocation);
    }
    
    public void reconfigure(final Configuration configuration) {
        this.setConfiguration(configuration);
        final ConfigurationSource source = configuration.getConfigurationSource();
        if (source != null) {
            final URI uri = source.getURI();
            if (uri != null) {
                this.configLocation = uri;
            }
        }
    }
    
    public void updateLoggers() {
        this.updateLoggers(this.configuration);
    }
    
    public void updateLoggers(final Configuration config) {
        final Configuration old = this.configuration;
        for (final Logger logger : this.loggerRegistry.getLoggers()) {
            logger.updateConfiguration(config);
        }
        this.firePropertyChangeEvent(new PropertyChangeEvent(this, "config", old, config));
    }
    
    @Override
    public synchronized void onChange(final Reconfigurable reconfigurable) {
        final long startMillis = System.currentTimeMillis();
        LoggerContext.LOGGER.debug("Reconfiguration started for context {} ({})", this.contextName, this);
        this.initApiModule();
        final Configuration newConfig = reconfigurable.reconfigure();
        if (newConfig != null) {
            this.setConfiguration(newConfig);
            LoggerContext.LOGGER.debug("Reconfiguration completed for {} ({}) in {} milliseconds.", this.contextName, this, System.currentTimeMillis() - startMillis);
        }
        else {
            LoggerContext.LOGGER.debug("Reconfiguration failed for {} ({}) in {} milliseconds.", this.contextName, this, System.currentTimeMillis() - startMillis);
        }
    }
    
    private void initApiModule() {
        ThreadContextMapFactory.init();
    }
    
    protected Logger newInstance(final LoggerContext ctx, final String name, final MessageFactory messageFactory) {
        return new Logger(ctx, name, messageFactory);
    }
    
    static {
        try {
            Loader.loadClass(ExecutorServices.class.getName());
        }
        catch (Exception e) {
            LoggerContext.LOGGER.error("Failed to preload ExecutorServices class.", e);
        }
        NULL_CONFIGURATION = new NullConfiguration();
    }
    
    private class ThreadContextDataTask implements Runnable
    {
        @Override
        public void run() {
            AbstractLifeCycle.LOGGER.debug("Initializing Thread Context Data Service Providers");
            ThreadContextDataInjector.initServiceProviders();
            AbstractLifeCycle.LOGGER.debug("Thread Context Data Service Provider initialization complete");
        }
    }
}
