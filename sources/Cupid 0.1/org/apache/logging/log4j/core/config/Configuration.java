package org.apache.logging.log4j.core.config;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.filter.Filterable;
import org.apache.logging.log4j.core.lookup.ConfigurationStrSubstitutor;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.script.ScriptManager;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.WatchManager;

public interface Configuration extends Filterable {
  public static final String CONTEXT_PROPERTIES = "ContextProperties";
  
  String getName();
  
  LoggerConfig getLoggerConfig(String paramString);
  
  <T extends Appender> T getAppender(String paramString);
  
  Map<String, Appender> getAppenders();
  
  void addAppender(Appender paramAppender);
  
  Map<String, LoggerConfig> getLoggers();
  
  void addLoggerAppender(Logger paramLogger, Appender paramAppender);
  
  void addLoggerFilter(Logger paramLogger, Filter paramFilter);
  
  void setLoggerAdditive(Logger paramLogger, boolean paramBoolean);
  
  void addLogger(String paramString, LoggerConfig paramLoggerConfig);
  
  void removeLogger(String paramString);
  
  List<String> getPluginPackages();
  
  Map<String, String> getProperties();
  
  LoggerConfig getRootLogger();
  
  void addListener(ConfigurationListener paramConfigurationListener);
  
  void removeListener(ConfigurationListener paramConfigurationListener);
  
  StrSubstitutor getStrSubstitutor();
  
  default StrSubstitutor getConfigurationStrSubstitutor() {
    StrSubstitutor defaultSubstitutor = getStrSubstitutor();
    if (defaultSubstitutor == null)
      return (StrSubstitutor)new ConfigurationStrSubstitutor(); 
    return (StrSubstitutor)new ConfigurationStrSubstitutor(defaultSubstitutor);
  }
  
  void createConfiguration(Node paramNode, LogEvent paramLogEvent);
  
  <T> T getComponent(String paramString);
  
  void addComponent(String paramString, Object paramObject);
  
  void setAdvertiser(Advertiser paramAdvertiser);
  
  Advertiser getAdvertiser();
  
  boolean isShutdownHookEnabled();
  
  long getShutdownTimeoutMillis();
  
  ConfigurationScheduler getScheduler();
  
  ConfigurationSource getConfigurationSource();
  
  List<CustomLevelConfig> getCustomLevels();
  
  ScriptManager getScriptManager();
  
  AsyncLoggerConfigDelegate getAsyncLoggerConfigDelegate();
  
  WatchManager getWatchManager();
  
  ReliabilityStrategy getReliabilityStrategy(LoggerConfig paramLoggerConfig);
  
  NanoClock getNanoClock();
  
  void setNanoClock(NanoClock paramNanoClock);
  
  LoggerContext getLoggerContext();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\Configuration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */