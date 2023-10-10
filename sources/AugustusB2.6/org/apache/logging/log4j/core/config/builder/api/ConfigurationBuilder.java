// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.api;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.Configuration;

public interface ConfigurationBuilder<T extends Configuration> extends Builder<T>
{
    ConfigurationBuilder<T> add(final ScriptComponentBuilder builder);
    
    ConfigurationBuilder<T> add(final ScriptFileComponentBuilder builder);
    
    ConfigurationBuilder<T> add(final AppenderComponentBuilder builder);
    
    ConfigurationBuilder<T> add(final CustomLevelComponentBuilder builder);
    
    ConfigurationBuilder<T> add(final FilterComponentBuilder builder);
    
    ConfigurationBuilder<T> add(final LoggerComponentBuilder builder);
    
    ConfigurationBuilder<T> add(final RootLoggerComponentBuilder builder);
    
    ConfigurationBuilder<T> addProperty(final String key, final String value);
    
    ScriptComponentBuilder newScript(final String name, final String language, final String text);
    
    ScriptFileComponentBuilder newScriptFile(final String path);
    
    ScriptFileComponentBuilder newScriptFile(final String name, final String path);
    
    AppenderComponentBuilder newAppender(final String name, final String pluginName);
    
    AppenderRefComponentBuilder newAppenderRef(final String ref);
    
    LoggerComponentBuilder newAsyncLogger(final String name);
    
    LoggerComponentBuilder newAsyncLogger(final String name, final boolean includeLocation);
    
    LoggerComponentBuilder newAsyncLogger(final String name, final Level level);
    
    LoggerComponentBuilder newAsyncLogger(final String name, final Level level, final boolean includeLocation);
    
    LoggerComponentBuilder newAsyncLogger(final String name, final String level);
    
    LoggerComponentBuilder newAsyncLogger(final String name, final String level, final boolean includeLocation);
    
    RootLoggerComponentBuilder newAsyncRootLogger();
    
    RootLoggerComponentBuilder newAsyncRootLogger(final boolean includeLocation);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final Level level);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final Level level, final boolean includeLocation);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final String level);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final String level, final boolean includeLocation);
    
     <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String pluginName);
    
     <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String name, final String pluginName);
    
     <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String name, final String pluginName, final String value);
    
    PropertyComponentBuilder newProperty(final String name, final String value);
    
    KeyValuePairComponentBuilder newKeyValuePair(final String key, final String value);
    
    CustomLevelComponentBuilder newCustomLevel(final String name, final int level);
    
    FilterComponentBuilder newFilter(final String pluginName, final Filter.Result onMatch, final Filter.Result onMismatch);
    
    FilterComponentBuilder newFilter(final String pluginName, final String onMatch, final String onMismatch);
    
    LayoutComponentBuilder newLayout(final String pluginName);
    
    LoggerComponentBuilder newLogger(final String name);
    
    LoggerComponentBuilder newLogger(final String name, final boolean includeLocation);
    
    LoggerComponentBuilder newLogger(final String name, final Level level);
    
    LoggerComponentBuilder newLogger(final String name, final Level level, final boolean includeLocation);
    
    LoggerComponentBuilder newLogger(final String name, final String level);
    
    LoggerComponentBuilder newLogger(final String name, final String level, final boolean includeLocation);
    
    RootLoggerComponentBuilder newRootLogger();
    
    RootLoggerComponentBuilder newRootLogger(final boolean includeLocation);
    
    RootLoggerComponentBuilder newRootLogger(final Level level);
    
    RootLoggerComponentBuilder newRootLogger(final Level level, final boolean includeLocation);
    
    RootLoggerComponentBuilder newRootLogger(final String level);
    
    RootLoggerComponentBuilder newRootLogger(final String level, final boolean includeLocation);
    
    ConfigurationBuilder<T> setAdvertiser(final String advertiser);
    
    ConfigurationBuilder<T> setConfigurationName(final String name);
    
    ConfigurationBuilder<T> setConfigurationSource(final ConfigurationSource configurationSource);
    
    ConfigurationBuilder<T> setMonitorInterval(final String intervalSeconds);
    
    ConfigurationBuilder<T> setPackages(final String packages);
    
    ConfigurationBuilder<T> setShutdownHook(final String flag);
    
    ConfigurationBuilder<T> setShutdownTimeout(final long timeout, final TimeUnit timeUnit);
    
    ConfigurationBuilder<T> setStatusLevel(final Level level);
    
    ConfigurationBuilder<T> setVerbosity(final String verbosity);
    
    ConfigurationBuilder<T> setDestination(final String destination);
    
    void setLoggerContext(final LoggerContext loggerContext);
    
    ConfigurationBuilder<T> addRootProperty(final String key, final String value);
    
    T build(final boolean initialize);
    
    void writeXmlConfiguration(final OutputStream output) throws IOException;
    
    String toXmlConfiguration();
}
