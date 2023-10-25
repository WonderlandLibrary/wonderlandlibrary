package org.apache.logging.log4j.core.async;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.spi.AbstractLogger;

@Plugin(name = "asyncLogger", category = "Core", printObject = true)
public class AsyncLoggerConfig extends LoggerConfig {
  private static final ThreadLocal<Boolean> ASYNC_LOGGER_ENTERED = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() {
        return Boolean.FALSE;
      }
    };
  
  private final AsyncLoggerConfigDelegate delegate;
  
  protected AsyncLoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
    super(name, appenders, filter, level, additive, properties, config, includeLocation);
    this.delegate = config.getAsyncLoggerConfigDelegate();
    this.delegate.setLogEventFactory(getLogEventFactory());
  }
  
  protected void log(LogEvent event, LoggerConfig.LoggerConfigPredicate predicate) {
    if (predicate == LoggerConfig.LoggerConfigPredicate.ALL && ASYNC_LOGGER_ENTERED
      .get() == Boolean.FALSE && 
      
      hasAppenders()) {
      ASYNC_LOGGER_ENTERED.set(Boolean.TRUE);
      try {
        super.log(event, LoggerConfig.LoggerConfigPredicate.SYNCHRONOUS_ONLY);
        logToAsyncDelegate(event);
      } finally {
        ASYNC_LOGGER_ENTERED.set(Boolean.FALSE);
      } 
    } else {
      super.log(event, predicate);
    } 
  }
  
  protected void callAppenders(LogEvent event) {
    super.callAppenders(event);
  }
  
  private void logToAsyncDelegate(LogEvent event) {
    if (!isFiltered(event)) {
      populateLazilyInitializedFields(event);
      if (!this.delegate.tryEnqueue(event, this))
        handleQueueFull(event); 
    } 
  }
  
  private void handleQueueFull(LogEvent event) {
    if (AbstractLogger.getRecursionDepth() > 1) {
      AsyncQueueFullMessageUtil.logWarningToStatusLogger();
      logToAsyncLoggerConfigsOnCurrentThread(event);
    } else {
      EventRoute eventRoute = this.delegate.getEventRoute(event.getLevel());
      eventRoute.logMessage(this, event);
    } 
  }
  
  private void populateLazilyInitializedFields(LogEvent event) {
    event.getSource();
    event.getThreadName();
  }
  
  void logInBackgroundThread(LogEvent event) {
    this.delegate.enqueueEvent(event, this);
  }
  
  void logToAsyncLoggerConfigsOnCurrentThread(LogEvent event) {
    log(event, LoggerConfig.LoggerConfigPredicate.ASYNCHRONOUS_ONLY);
  }
  
  private String displayName() {
    return "".equals(getName()) ? "root" : getName();
  }
  
  public void start() {
    LOGGER.trace("AsyncLoggerConfig[{}] starting...", displayName());
    super.start();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    stop(timeout, timeUnit, false);
    LOGGER.trace("AsyncLoggerConfig[{}] stopping...", displayName());
    setStopped();
    return true;
  }
  
  public RingBufferAdmin createRingBufferAdmin(String contextName) {
    return this.delegate.createRingBufferAdmin(contextName, getName());
  }
  
  @Deprecated
  public static LoggerConfig createLogger(String additivity, String levelName, String loggerName, String includeLocation, AppenderRef[] refs, Property[] properties, Configuration config, Filter filter) {
    Level level;
    if (loggerName == null) {
      LOGGER.error("Loggers cannot be configured without a name");
      return null;
    } 
    List<AppenderRef> appenderRefs = Arrays.asList(refs);
    try {
      level = Level.toLevel(levelName, Level.ERROR);
    } catch (Exception ex) {
      LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
      level = Level.ERROR;
    } 
    String name = loggerName.equals("root") ? "" : loggerName;
    boolean additive = Booleans.parseBoolean(additivity, true);
    return new AsyncLoggerConfig(name, appenderRefs, filter, level, additive, properties, config, 
        includeLocation(includeLocation));
  }
  
  @PluginFactory
  public static LoggerConfig createLogger(@PluginAttribute(value = "additivity", defaultBoolean = true) boolean additivity, @PluginAttribute("level") Level level, @Required(message = "Loggers cannot be configured without a name") @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
    String name = loggerName.equals("root") ? "" : loggerName;
    return new AsyncLoggerConfig(name, Arrays.asList(refs), filter, level, additivity, properties, config, 
        includeLocation(includeLocation));
  }
  
  protected static boolean includeLocation(String includeLocationConfigValue) {
    return Boolean.parseBoolean(includeLocationConfigValue);
  }
  
  @Plugin(name = "asyncRoot", category = "Core", printObject = true)
  public static class RootLogger extends LoggerConfig {
    @Deprecated
    public static LoggerConfig createLogger(String additivity, String levelName, String includeLocation, AppenderRef[] refs, Property[] properties, Configuration config, Filter filter) {
      List<AppenderRef> appenderRefs = Arrays.asList(refs);
      Level level = null;
      try {
        level = Level.toLevel(levelName, Level.ERROR);
      } catch (Exception ex) {
        LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
        level = Level.ERROR;
      } 
      boolean additive = Booleans.parseBoolean(additivity, true);
      return new AsyncLoggerConfig("", appenderRefs, filter, level, additive, properties, config, 
          
          AsyncLoggerConfig.includeLocation(includeLocation));
    }
    
    @PluginFactory
    public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") Level level, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
      List<AppenderRef> appenderRefs = Arrays.asList(refs);
      Level actualLevel = (level == null) ? Level.ERROR : level;
      boolean additive = Booleans.parseBoolean(additivity, true);
      return new AsyncLoggerConfig("", appenderRefs, filter, actualLevel, additive, properties, config, 
          AsyncLoggerConfig.includeLocation(includeLocation));
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */