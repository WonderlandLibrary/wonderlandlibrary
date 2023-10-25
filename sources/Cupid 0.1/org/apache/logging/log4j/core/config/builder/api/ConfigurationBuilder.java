package org.apache.logging.log4j.core.config.builder.api;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.util.Builder;

public interface ConfigurationBuilder<T extends org.apache.logging.log4j.core.config.Configuration> extends Builder<T> {
  ConfigurationBuilder<T> add(ScriptComponentBuilder paramScriptComponentBuilder);
  
  ConfigurationBuilder<T> add(ScriptFileComponentBuilder paramScriptFileComponentBuilder);
  
  ConfigurationBuilder<T> add(AppenderComponentBuilder paramAppenderComponentBuilder);
  
  ConfigurationBuilder<T> add(CustomLevelComponentBuilder paramCustomLevelComponentBuilder);
  
  ConfigurationBuilder<T> add(FilterComponentBuilder paramFilterComponentBuilder);
  
  ConfigurationBuilder<T> add(LoggerComponentBuilder paramLoggerComponentBuilder);
  
  ConfigurationBuilder<T> add(RootLoggerComponentBuilder paramRootLoggerComponentBuilder);
  
  ConfigurationBuilder<T> addProperty(String paramString1, String paramString2);
  
  ScriptComponentBuilder newScript(String paramString1, String paramString2, String paramString3);
  
  ScriptFileComponentBuilder newScriptFile(String paramString);
  
  ScriptFileComponentBuilder newScriptFile(String paramString1, String paramString2);
  
  AppenderComponentBuilder newAppender(String paramString1, String paramString2);
  
  AppenderRefComponentBuilder newAppenderRef(String paramString);
  
  LoggerComponentBuilder newAsyncLogger(String paramString);
  
  LoggerComponentBuilder newAsyncLogger(String paramString, boolean paramBoolean);
  
  LoggerComponentBuilder newAsyncLogger(String paramString, Level paramLevel);
  
  LoggerComponentBuilder newAsyncLogger(String paramString, Level paramLevel, boolean paramBoolean);
  
  LoggerComponentBuilder newAsyncLogger(String paramString1, String paramString2);
  
  LoggerComponentBuilder newAsyncLogger(String paramString1, String paramString2, boolean paramBoolean);
  
  RootLoggerComponentBuilder newAsyncRootLogger();
  
  RootLoggerComponentBuilder newAsyncRootLogger(boolean paramBoolean);
  
  RootLoggerComponentBuilder newAsyncRootLogger(Level paramLevel);
  
  RootLoggerComponentBuilder newAsyncRootLogger(Level paramLevel, boolean paramBoolean);
  
  RootLoggerComponentBuilder newAsyncRootLogger(String paramString);
  
  RootLoggerComponentBuilder newAsyncRootLogger(String paramString, boolean paramBoolean);
  
  <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String paramString);
  
  <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String paramString1, String paramString2);
  
  <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String paramString1, String paramString2, String paramString3);
  
  PropertyComponentBuilder newProperty(String paramString1, String paramString2);
  
  KeyValuePairComponentBuilder newKeyValuePair(String paramString1, String paramString2);
  
  CustomLevelComponentBuilder newCustomLevel(String paramString, int paramInt);
  
  FilterComponentBuilder newFilter(String paramString, Filter.Result paramResult1, Filter.Result paramResult2);
  
  FilterComponentBuilder newFilter(String paramString1, String paramString2, String paramString3);
  
  LayoutComponentBuilder newLayout(String paramString);
  
  LoggerComponentBuilder newLogger(String paramString);
  
  LoggerComponentBuilder newLogger(String paramString, boolean paramBoolean);
  
  LoggerComponentBuilder newLogger(String paramString, Level paramLevel);
  
  LoggerComponentBuilder newLogger(String paramString, Level paramLevel, boolean paramBoolean);
  
  LoggerComponentBuilder newLogger(String paramString1, String paramString2);
  
  LoggerComponentBuilder newLogger(String paramString1, String paramString2, boolean paramBoolean);
  
  RootLoggerComponentBuilder newRootLogger();
  
  RootLoggerComponentBuilder newRootLogger(boolean paramBoolean);
  
  RootLoggerComponentBuilder newRootLogger(Level paramLevel);
  
  RootLoggerComponentBuilder newRootLogger(Level paramLevel, boolean paramBoolean);
  
  RootLoggerComponentBuilder newRootLogger(String paramString);
  
  RootLoggerComponentBuilder newRootLogger(String paramString, boolean paramBoolean);
  
  ConfigurationBuilder<T> setAdvertiser(String paramString);
  
  ConfigurationBuilder<T> setConfigurationName(String paramString);
  
  ConfigurationBuilder<T> setConfigurationSource(ConfigurationSource paramConfigurationSource);
  
  ConfigurationBuilder<T> setMonitorInterval(String paramString);
  
  ConfigurationBuilder<T> setPackages(String paramString);
  
  ConfigurationBuilder<T> setShutdownHook(String paramString);
  
  ConfigurationBuilder<T> setShutdownTimeout(long paramLong, TimeUnit paramTimeUnit);
  
  ConfigurationBuilder<T> setStatusLevel(Level paramLevel);
  
  ConfigurationBuilder<T> setVerbosity(String paramString);
  
  ConfigurationBuilder<T> setDestination(String paramString);
  
  void setLoggerContext(LoggerContext paramLoggerContext);
  
  ConfigurationBuilder<T> addRootProperty(String paramString1, String paramString2);
  
  T build(boolean paramBoolean);
  
  void writeXmlConfiguration(OutputStream paramOutputStream) throws IOException;
  
  String toXmlConfiguration();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\api\ConfigurationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */