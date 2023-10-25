/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.Map;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.ConfigurationMonitor;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.filter.Filterable;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;

public interface Configuration
extends Filterable {
    public static final String CONTEXT_PROPERTIES = "ContextProperties";

    public String getName();

    public LoggerConfig getLoggerConfig(String var1);

    public Map<String, Appender> getAppenders();

    public Map<String, LoggerConfig> getLoggers();

    public void addLoggerAppender(Logger var1, Appender var2);

    public void addLoggerFilter(Logger var1, Filter var2);

    public void setLoggerAdditive(Logger var1, boolean var2);

    public Map<String, String> getProperties();

    public void start();

    public void stop();

    public void addListener(ConfigurationListener var1);

    public void removeListener(ConfigurationListener var1);

    public StrSubstitutor getStrSubstitutor();

    public void createConfiguration(Node var1, LogEvent var2);

    public <T> T getComponent(String var1);

    public void addComponent(String var1, Object var2);

    public void setConfigurationMonitor(ConfigurationMonitor var1);

    public ConfigurationMonitor getConfigurationMonitor();

    public void setAdvertiser(Advertiser var1);

    public Advertiser getAdvertiser();

    public boolean isShutdownHookEnabled();
}

