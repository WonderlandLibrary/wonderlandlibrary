// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.script.ScriptManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.util.List;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import java.util.Map;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.filter.Filterable;

public interface Configuration extends Filterable
{
    public static final String CONTEXT_PROPERTIES = "ContextProperties";
    
    String getName();
    
    LoggerConfig getLoggerConfig(final String name);
    
     <T extends Appender> T getAppender(final String name);
    
    Map<String, Appender> getAppenders();
    
    void addAppender(final Appender appender);
    
    Map<String, LoggerConfig> getLoggers();
    
    void addLoggerAppender(final Logger logger, final Appender appender);
    
    void addLoggerFilter(final Logger logger, final Filter filter);
    
    void setLoggerAdditive(final Logger logger, final boolean additive);
    
    void addLogger(final String name, final LoggerConfig loggerConfig);
    
    void removeLogger(final String name);
    
    List<String> getPluginPackages();
    
    Map<String, String> getProperties();
    
    LoggerConfig getRootLogger();
    
    void addListener(final ConfigurationListener listener);
    
    void removeListener(final ConfigurationListener listener);
    
    StrSubstitutor getStrSubstitutor();
    
    void createConfiguration(final Node node, final LogEvent event);
    
     <T> T getComponent(final String name);
    
    void addComponent(final String name, final Object object);
    
    void setAdvertiser(final Advertiser advertiser);
    
    Advertiser getAdvertiser();
    
    boolean isShutdownHookEnabled();
    
    long getShutdownTimeoutMillis();
    
    ConfigurationScheduler getScheduler();
    
    ConfigurationSource getConfigurationSource();
    
    List<CustomLevelConfig> getCustomLevels();
    
    ScriptManager getScriptManager();
    
    AsyncLoggerConfigDelegate getAsyncLoggerConfigDelegate();
    
    WatchManager getWatchManager();
    
    ReliabilityStrategy getReliabilityStrategy(final LoggerConfig loggerConfig);
    
    NanoClock getNanoClock();
    
    void setNanoClock(final NanoClock nanoClock);
    
    LoggerContext getLoggerContext();
}
