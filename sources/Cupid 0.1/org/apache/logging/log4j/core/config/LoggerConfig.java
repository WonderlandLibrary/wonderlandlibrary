package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "logger", category = "Core", printObject = true)
public class LoggerConfig extends AbstractFilterable implements LocationAware {
  public static final String ROOT = "root";
  
  private static LogEventFactory LOG_EVENT_FACTORY = null;
  
  private List<AppenderRef> appenderRefs = new ArrayList<>();
  
  private final AppenderControlArraySet appenders = new AppenderControlArraySet();
  
  private final String name;
  
  private LogEventFactory logEventFactory;
  
  private Level level;
  
  private boolean additive = true;
  
  private boolean includeLocation = true;
  
  private LoggerConfig parent;
  
  private Map<Property, Boolean> propertiesMap;
  
  private final List<Property> properties;
  
  private final boolean propertiesRequireLookup;
  
  private final Configuration config;
  
  private final ReliabilityStrategy reliabilityStrategy;
  
  static {
    String factory = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
    if (factory != null)
      try {
        Class<?> clazz = Loader.loadClass(factory);
        if (clazz != null && LogEventFactory.class.isAssignableFrom(clazz))
          LOG_EVENT_FACTORY = (LogEventFactory)clazz.newInstance(); 
      } catch (Exception ex) {
        LOGGER.error("Unable to create LogEventFactory {}", factory, ex);
      }  
    if (LOG_EVENT_FACTORY == null)
      LOG_EVENT_FACTORY = Constants.ENABLE_THREADLOCALS ? (LogEventFactory)new ReusableLogEventFactory() : (LogEventFactory)new DefaultLogEventFactory(); 
  }
  
  public LoggerConfig() {
    this.logEventFactory = LOG_EVENT_FACTORY;
    this.level = Level.ERROR;
    this.name = "";
    this.properties = null;
    this.propertiesRequireLookup = false;
    this.config = null;
    this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
  }
  
  public LoggerConfig(String name, Level level, boolean additive) {
    this.logEventFactory = LOG_EVENT_FACTORY;
    this.name = name;
    this.level = level;
    this.additive = additive;
    this.properties = null;
    this.propertiesRequireLookup = false;
    this.config = null;
    this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
  }
  
  protected LoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
    super(filter);
    this.logEventFactory = LOG_EVENT_FACTORY;
    this.name = name;
    this.appenderRefs = appenders;
    this.level = level;
    this.additive = additive;
    this.includeLocation = includeLocation;
    this.config = config;
    if (properties != null && properties.length > 0) {
      this.properties = Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(properties, properties.length)));
    } else {
      this.properties = null;
    } 
    this.propertiesRequireLookup = containsPropertyRequiringLookup(properties);
    this.reliabilityStrategy = config.getReliabilityStrategy(this);
  }
  
  private static boolean containsPropertyRequiringLookup(Property[] properties) {
    if (properties == null)
      return false; 
    for (int i = 0; i < properties.length; i++) {
      if (properties[i].isValueNeedsLookup())
        return true; 
    } 
    return false;
  }
  
  public Filter getFilter() {
    return super.getFilter();
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setParent(LoggerConfig parent) {
    this.parent = parent;
  }
  
  public LoggerConfig getParent() {
    return this.parent;
  }
  
  public void addAppender(Appender appender, Level level, Filter filter) {
    this.appenders.add(new AppenderControl(appender, level, filter));
  }
  
  public void removeAppender(String name) {
    AppenderControl removed = null;
    while ((removed = this.appenders.remove(name)) != null)
      cleanupFilter(removed); 
  }
  
  public Map<String, Appender> getAppenders() {
    return this.appenders.asMap();
  }
  
  protected void clearAppenders() {
    do {
      AppenderControl[] original = this.appenders.clear();
      for (AppenderControl ctl : original)
        cleanupFilter(ctl); 
    } while (!this.appenders.isEmpty());
  }
  
  private void cleanupFilter(AppenderControl ctl) {
    Filter filter = ctl.getFilter();
    if (filter != null) {
      ctl.removeFilter(filter);
      filter.stop();
    } 
  }
  
  public List<AppenderRef> getAppenderRefs() {
    return this.appenderRefs;
  }
  
  public void setLevel(Level level) {
    this.level = level;
  }
  
  public Level getLevel() {
    return (this.level == null) ? ((this.parent == null) ? Level.ERROR : this.parent.getLevel()) : this.level;
  }
  
  public LogEventFactory getLogEventFactory() {
    return this.logEventFactory;
  }
  
  public void setLogEventFactory(LogEventFactory logEventFactory) {
    this.logEventFactory = logEventFactory;
  }
  
  public boolean isAdditive() {
    return this.additive;
  }
  
  public void setAdditive(boolean additive) {
    this.additive = additive;
  }
  
  public boolean isIncludeLocation() {
    return this.includeLocation;
  }
  
  @Deprecated
  public Map<Property, Boolean> getProperties() {
    if (this.properties == null)
      return null; 
    if (this.propertiesMap == null) {
      Map<Property, Boolean> result = new HashMap<>(this.properties.size() * 2);
      for (int i = 0; i < this.properties.size(); i++)
        result.put(this.properties.get(i), Boolean.valueOf(((Property)this.properties.get(i)).isValueNeedsLookup())); 
      this.propertiesMap = Collections.unmodifiableMap(result);
    } 
    return this.propertiesMap;
  }
  
  public List<Property> getPropertyList() {
    return this.properties;
  }
  
  public boolean isPropertiesRequireLookup() {
    return this.propertiesRequireLookup;
  }
  
  @PerformanceSensitive({"allocation"})
  public void log(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
    List<Property> props = getProperties(loggerName, fqcn, marker, level, data, t);
    LogEvent logEvent = this.logEventFactory.createEvent(loggerName, marker, fqcn, 
        location(fqcn), level, data, props, t);
    try {
      log(logEvent, LoggerConfigPredicate.ALL);
    } finally {
      ReusableLogEventFactory.release(logEvent);
    } 
  }
  
  private StackTraceElement location(String fqcn) {
    return requiresLocation() ? 
      StackLocatorUtil.calcLocation(fqcn) : null;
  }
  
  @PerformanceSensitive({"allocation"})
  public void log(String loggerName, String fqcn, StackTraceElement location, Marker marker, Level level, Message data, Throwable t) {
    List<Property> props = getProperties(loggerName, fqcn, marker, level, data, t);
    LogEvent logEvent = this.logEventFactory.createEvent(loggerName, marker, fqcn, location, level, data, props, t);
    try {
      log(logEvent, LoggerConfigPredicate.ALL);
    } finally {
      ReusableLogEventFactory.release(logEvent);
    } 
  }
  
  private List<Property> getProperties(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
    List<Property> snapshot = this.properties;
    if (snapshot == null || !this.propertiesRequireLookup)
      return snapshot; 
    return getPropertiesWithLookups(loggerName, fqcn, marker, level, data, t, snapshot);
  }
  
  private List<Property> getPropertiesWithLookups(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t, List<Property> props) {
    List<Property> results = new ArrayList<>(props.size());
    Log4jLogEvent log4jLogEvent = Log4jLogEvent.newBuilder().setMessage(data).setMarker(marker).setLevel(level).setLoggerName(loggerName).setLoggerFqcn(fqcn).setThrown(t).build();
    for (int i = 0; i < props.size(); i++) {
      Property prop = props.get(i);
      String value = prop.isValueNeedsLookup() ? this.config.getStrSubstitutor().replace((LogEvent)log4jLogEvent, prop.getValue()) : prop.getValue();
      results.add(Property.createProperty(prop.getName(), value));
    } 
    return results;
  }
  
  public void log(LogEvent event) {
    log(event, LoggerConfigPredicate.ALL);
  }
  
  protected void log(LogEvent event, LoggerConfigPredicate predicate) {
    if (!isFiltered(event))
      processLogEvent(event, predicate); 
  }
  
  public ReliabilityStrategy getReliabilityStrategy() {
    return this.reliabilityStrategy;
  }
  
  private void processLogEvent(LogEvent event, LoggerConfigPredicate predicate) {
    event.setIncludeLocation(isIncludeLocation());
    if (predicate.allow(this))
      callAppenders(event); 
    logParent(event, predicate);
  }
  
  public boolean requiresLocation() {
    if (!this.includeLocation)
      return false; 
    AppenderControl[] controls = this.appenders.get();
    LoggerConfig loggerConfig = this;
    while (loggerConfig != null) {
      for (AppenderControl control : controls) {
        Appender appender = control.getAppender();
        if (appender instanceof LocationAware && ((LocationAware)appender).requiresLocation())
          return true; 
      } 
      if (loggerConfig.additive) {
        loggerConfig = loggerConfig.parent;
        if (loggerConfig != null)
          controls = loggerConfig.appenders.get(); 
      } 
    } 
    return false;
  }
  
  private void logParent(LogEvent event, LoggerConfigPredicate predicate) {
    if (this.additive && this.parent != null)
      this.parent.log(event, predicate); 
  }
  
  @PerformanceSensitive({"allocation"})
  protected void callAppenders(LogEvent event) {
    AppenderControl[] controls = this.appenders.get();
    for (int i = 0; i < controls.length; i++)
      controls[i].callAppender(event); 
  }
  
  public String toString() {
    return Strings.isEmpty(this.name) ? "root" : this.name;
  }
  
  @Deprecated
  public static LoggerConfig createLogger(String additivity, Level level, @PluginAttribute("name") String loggerName, String includeLocation, AppenderRef[] refs, Property[] properties, @PluginConfiguration Configuration config, Filter filter) {
    if (loggerName == null) {
      LOGGER.error("Loggers cannot be configured without a name");
      return null;
    } 
    List<AppenderRef> appenderRefs = Arrays.asList(refs);
    String name = loggerName.equals("root") ? "" : loggerName;
    boolean additive = Booleans.parseBoolean(additivity, true);
    return new LoggerConfig(name, appenderRefs, filter, level, additive, properties, config, 
        includeLocation(includeLocation, config));
  }
  
  @PluginFactory
  public static LoggerConfig createLogger(@PluginAttribute(value = "additivity", defaultBoolean = true) boolean additivity, @PluginAttribute("level") Level level, @Required(message = "Loggers cannot be configured without a name") @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
    String name = loggerName.equals("root") ? "" : loggerName;
    return new LoggerConfig(name, Arrays.asList(refs), filter, level, additivity, properties, config, 
        includeLocation(includeLocation, config));
  }
  
  @Deprecated
  protected static boolean includeLocation(String includeLocationConfigValue) {
    return includeLocation(includeLocationConfigValue, (Configuration)null);
  }
  
  protected static boolean includeLocation(String includeLocationConfigValue, Configuration configuration) {
    if (includeLocationConfigValue == null) {
      LoggerContext context = null;
      if (configuration != null)
        context = configuration.getLoggerContext(); 
      if (context != null)
        return !(context instanceof org.apache.logging.log4j.core.async.AsyncLoggerContext); 
      return !AsyncLoggerContextSelector.isSelected();
    } 
    return Boolean.parseBoolean(includeLocationConfigValue);
  }
  
  protected final boolean hasAppenders() {
    return !this.appenders.isEmpty();
  }
  
  @Plugin(name = "root", category = "Core", printObject = true)
  public static class RootLogger extends LoggerConfig {
    @PluginFactory
    public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") Level level, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
      List<AppenderRef> appenderRefs = Arrays.asList(refs);
      Level actualLevel = (level == null) ? Level.ERROR : level;
      boolean additive = Booleans.parseBoolean(additivity, true);
      return new LoggerConfig("", appenderRefs, filter, actualLevel, additive, properties, config, 
          includeLocation(includeLocation, config));
    }
  }
  
  protected enum LoggerConfigPredicate {
    ALL {
      boolean allow(LoggerConfig config) {
        return true;
      }
    },
    ASYNCHRONOUS_ONLY {
      boolean allow(LoggerConfig config) {
        return config instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig;
      }
    },
    SYNCHRONOUS_ONLY {
      boolean allow(LoggerConfig config) {
        return !ASYNCHRONOUS_ONLY.allow(config);
      }
    };
    
    abstract boolean allow(LoggerConfig param1LoggerConfig);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\LoggerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */