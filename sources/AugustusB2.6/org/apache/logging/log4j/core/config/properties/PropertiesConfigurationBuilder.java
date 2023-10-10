// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.properties;

import org.apache.logging.log4j.core.config.builder.api.LoggableComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterableComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.ConfigurationException;
import java.util.Map;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.util.Properties;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;

public class PropertiesConfigurationBuilder extends ConfigurationBuilderFactory implements Builder<PropertiesConfiguration>
{
    private static final String ADVERTISER_KEY = "advertiser";
    private static final String STATUS_KEY = "status";
    private static final String SHUTDOWN_HOOK = "shutdownHook";
    private static final String SHUTDOWN_TIMEOUT = "shutdownTimeout";
    private static final String VERBOSE = "verbose";
    private static final String DEST = "dest";
    private static final String PACKAGES = "packages";
    private static final String CONFIG_NAME = "name";
    private static final String MONITOR_INTERVAL = "monitorInterval";
    private static final String CONFIG_TYPE = "type";
    private final ConfigurationBuilder<PropertiesConfiguration> builder;
    private LoggerContext loggerContext;
    private Properties rootProperties;
    
    public PropertiesConfigurationBuilder() {
        this.builder = ConfigurationBuilderFactory.newConfigurationBuilder(PropertiesConfiguration.class);
    }
    
    public PropertiesConfigurationBuilder setRootProperties(final Properties rootProperties) {
        this.rootProperties = rootProperties;
        return this;
    }
    
    public PropertiesConfigurationBuilder setConfigurationSource(final ConfigurationSource source) {
        this.builder.setConfigurationSource(source);
        return this;
    }
    
    @Override
    public PropertiesConfiguration build() {
        for (final String key : this.rootProperties.stringPropertyNames()) {
            if (!key.contains(".")) {
                this.builder.addRootProperty(key, this.rootProperties.getProperty(key));
            }
        }
        this.builder.setStatusLevel(Level.toLevel(this.rootProperties.getProperty("status"), Level.ERROR)).setShutdownHook(this.rootProperties.getProperty("shutdownHook")).setShutdownTimeout(Long.parseLong(this.rootProperties.getProperty("shutdownTimeout", "0")), TimeUnit.MILLISECONDS).setVerbosity(this.rootProperties.getProperty("verbose")).setDestination(this.rootProperties.getProperty("dest")).setPackages(this.rootProperties.getProperty("packages")).setConfigurationName(this.rootProperties.getProperty("name")).setMonitorInterval(this.rootProperties.getProperty("monitorInterval", "0")).setAdvertiser(this.rootProperties.getProperty("advertiser"));
        final Properties propertyPlaceholders = PropertiesUtil.extractSubset(this.rootProperties, "property");
        for (final String key2 : propertyPlaceholders.stringPropertyNames()) {
            this.builder.addProperty(key2, propertyPlaceholders.getProperty(key2));
        }
        final Map<String, Properties> scripts = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "script"));
        for (final Map.Entry<String, Properties> entry : scripts.entrySet()) {
            final Properties scriptProps = entry.getValue();
            final String type = (String)scriptProps.remove("type");
            if (type == null) {
                throw new ConfigurationException("No type provided for script - must be Script or ScriptFile");
            }
            if (type.equalsIgnoreCase("script")) {
                this.builder.add(this.createScript(scriptProps));
            }
            else {
                this.builder.add(this.createScriptFile(scriptProps));
            }
        }
        final Properties levelProps = PropertiesUtil.extractSubset(this.rootProperties, "customLevel");
        if (levelProps.size() > 0) {
            for (final String key3 : levelProps.stringPropertyNames()) {
                this.builder.add(this.builder.newCustomLevel(key3, Integer.parseInt(levelProps.getProperty(key3))));
            }
        }
        final String filterProp = this.rootProperties.getProperty("filters");
        if (filterProp != null) {
            final String[] split;
            final String[] filterNames = split = filterProp.split(",");
            for (final String filterName : split) {
                final String name = filterName.trim();
                this.builder.add(this.createFilter(name, PropertiesUtil.extractSubset(this.rootProperties, "filter." + name)));
            }
        }
        else {
            final Map<String, Properties> filters = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "filter"));
            for (final Map.Entry<String, Properties> entry2 : filters.entrySet()) {
                this.builder.add(this.createFilter(entry2.getKey().trim(), entry2.getValue()));
            }
        }
        final String appenderProp = this.rootProperties.getProperty("appenders");
        if (appenderProp != null) {
            final String[] split2;
            final String[] appenderNames = split2 = appenderProp.split(",");
            for (final String appenderName : split2) {
                final String name2 = appenderName.trim();
                this.builder.add(this.createAppender(appenderName.trim(), PropertiesUtil.extractSubset(this.rootProperties, "appender." + name2)));
            }
        }
        else {
            final Map<String, Properties> appenders = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "appender"));
            for (final Map.Entry<String, Properties> entry3 : appenders.entrySet()) {
                this.builder.add(this.createAppender(entry3.getKey().trim(), entry3.getValue()));
            }
        }
        final String loggerProp = this.rootProperties.getProperty("loggers");
        if (loggerProp != null) {
            final String[] split3;
            final String[] loggerNames = split3 = loggerProp.split(",");
            for (final String loggerName : split3) {
                final String name3 = loggerName.trim();
                if (!name3.equals("root")) {
                    this.builder.add(this.createLogger(name3, PropertiesUtil.extractSubset(this.rootProperties, "logger." + name3)));
                }
            }
        }
        else {
            final Map<String, Properties> loggers = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "logger"));
            for (final Map.Entry<String, Properties> entry4 : loggers.entrySet()) {
                final String name = entry4.getKey().trim();
                if (!name.equals("root")) {
                    this.builder.add(this.createLogger(name, entry4.getValue()));
                }
            }
        }
        final Properties props = PropertiesUtil.extractSubset(this.rootProperties, "rootLogger");
        if (props.size() > 0) {
            this.builder.add(this.createRootLogger(props));
        }
        this.builder.setLoggerContext(this.loggerContext);
        return this.builder.build(false);
    }
    
    private ScriptComponentBuilder createScript(final Properties properties) {
        final String name = (String)properties.remove("name");
        final String language = (String)properties.remove("language");
        final String text = (String)properties.remove("text");
        final ScriptComponentBuilder scriptBuilder = this.builder.newScript(name, language, text);
        return processRemainingProperties(scriptBuilder, properties);
    }
    
    private ScriptFileComponentBuilder createScriptFile(final Properties properties) {
        final String name = (String)properties.remove("name");
        final String path = (String)properties.remove("path");
        final ScriptFileComponentBuilder scriptFileBuilder = this.builder.newScriptFile(name, path);
        return processRemainingProperties(scriptFileBuilder, properties);
    }
    
    private AppenderComponentBuilder createAppender(final String key, final Properties properties) {
        final String name = (String)properties.remove("name");
        if (Strings.isEmpty(name)) {
            throw new ConfigurationException("No name attribute provided for Appender " + key);
        }
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for Appender " + key);
        }
        final AppenderComponentBuilder appenderBuilder = this.builder.newAppender(name, type);
        this.addFiltersToComponent(appenderBuilder, properties);
        final Properties layoutProps = PropertiesUtil.extractSubset(properties, "layout");
        if (layoutProps.size() > 0) {
            appenderBuilder.add(this.createLayout(name, layoutProps));
        }
        return processRemainingProperties(appenderBuilder, properties);
    }
    
    private FilterComponentBuilder createFilter(final String key, final Properties properties) {
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for Filter " + key);
        }
        final String onMatch = (String)properties.remove("onMatch");
        final String onMismatch = (String)properties.remove("onMismatch");
        final FilterComponentBuilder filterBuilder = this.builder.newFilter(type, onMatch, onMismatch);
        return processRemainingProperties(filterBuilder, properties);
    }
    
    private AppenderRefComponentBuilder createAppenderRef(final String key, final Properties properties) {
        final String ref = (String)properties.remove("ref");
        if (Strings.isEmpty(ref)) {
            throw new ConfigurationException("No ref attribute provided for AppenderRef " + key);
        }
        final AppenderRefComponentBuilder appenderRefBuilder = this.builder.newAppenderRef(ref);
        final String level = Strings.trimToNull((String)properties.remove("level"));
        if (!Strings.isEmpty(level)) {
            appenderRefBuilder.addAttribute("level", level);
        }
        return this.addFiltersToComponent(appenderRefBuilder, properties);
    }
    
    private LoggerComponentBuilder createLogger(final String key, final Properties properties) {
        final String name = (String)properties.remove("name");
        final String location = (String)properties.remove("includeLocation");
        if (Strings.isEmpty(name)) {
            throw new ConfigurationException("No name attribute provided for Logger " + key);
        }
        final String level = Strings.trimToNull((String)properties.remove("level"));
        final String type = (String)properties.remove("type");
        LoggerComponentBuilder loggerBuilder;
        if (type != null) {
            if (!type.equalsIgnoreCase("asyncLogger")) {
                throw new ConfigurationException("Unknown Logger type " + type + " for Logger " + name);
            }
            if (location != null) {
                final boolean includeLocation = Boolean.parseBoolean(location);
                loggerBuilder = this.builder.newAsyncLogger(name, level, includeLocation);
            }
            else {
                loggerBuilder = this.builder.newAsyncLogger(name, level);
            }
        }
        else if (location != null) {
            final boolean includeLocation = Boolean.parseBoolean(location);
            loggerBuilder = this.builder.newLogger(name, level, includeLocation);
        }
        else {
            loggerBuilder = this.builder.newLogger(name, level);
        }
        this.addLoggersToComponent(loggerBuilder, properties);
        this.addFiltersToComponent(loggerBuilder, properties);
        final String additivity = (String)properties.remove("additivity");
        if (!Strings.isEmpty(additivity)) {
            loggerBuilder.addAttribute("additivity", additivity);
        }
        return loggerBuilder;
    }
    
    private RootLoggerComponentBuilder createRootLogger(final Properties properties) {
        final String level = Strings.trimToNull((String)properties.remove("level"));
        final String type = (String)properties.remove("type");
        final String location = (String)properties.remove("includeLocation");
        RootLoggerComponentBuilder loggerBuilder;
        if (type != null) {
            if (!type.equalsIgnoreCase("asyncRoot")) {
                throw new ConfigurationException("Unknown Logger type for root logger" + type);
            }
            if (location != null) {
                final boolean includeLocation = Boolean.parseBoolean(location);
                loggerBuilder = this.builder.newAsyncRootLogger(level, includeLocation);
            }
            else {
                loggerBuilder = this.builder.newAsyncRootLogger(level);
            }
        }
        else if (location != null) {
            final boolean includeLocation = Boolean.parseBoolean(location);
            loggerBuilder = this.builder.newRootLogger(level, includeLocation);
        }
        else {
            loggerBuilder = this.builder.newRootLogger(level);
        }
        this.addLoggersToComponent(loggerBuilder, properties);
        return this.addFiltersToComponent(loggerBuilder, properties);
    }
    
    private LayoutComponentBuilder createLayout(final String appenderName, final Properties properties) {
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for Layout on Appender " + appenderName);
        }
        final LayoutComponentBuilder layoutBuilder = this.builder.newLayout(type);
        return processRemainingProperties(layoutBuilder, properties);
    }
    
    private static <B extends ComponentBuilder<B>> ComponentBuilder<B> createComponent(final ComponentBuilder<?> parent, final String key, final Properties properties) {
        final String name = (String)properties.remove("name");
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for component " + key);
        }
        final ComponentBuilder<B> componentBuilder = parent.getBuilder().newComponent(name, type);
        return processRemainingProperties(componentBuilder, properties);
    }
    
    private static <B extends ComponentBuilder<?>> B processRemainingProperties(final B builder, final Properties properties) {
        while (properties.size() > 0) {
            final String propertyName = properties.stringPropertyNames().iterator().next();
            final int index = propertyName.indexOf(46);
            if (index > 0) {
                final String prefix = propertyName.substring(0, index);
                final Properties componentProperties = PropertiesUtil.extractSubset(properties, prefix);
                builder.addComponent(createComponent(builder, prefix, componentProperties));
            }
            else {
                builder.addAttribute(propertyName, properties.getProperty(propertyName));
                properties.remove(propertyName);
            }
        }
        return builder;
    }
    
    private <B extends FilterableComponentBuilder<? extends ComponentBuilder<?>>> B addFiltersToComponent(final B componentBuilder, final Properties properties) {
        final Map<String, Properties> filters = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(properties, "filter"));
        for (final Map.Entry<String, Properties> entry : filters.entrySet()) {
            componentBuilder.add(this.createFilter(entry.getKey().trim(), entry.getValue()));
        }
        return componentBuilder;
    }
    
    private <B extends LoggableComponentBuilder<? extends ComponentBuilder<?>>> B addLoggersToComponent(final B loggerBuilder, final Properties properties) {
        final Map<String, Properties> appenderRefs = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(properties, "appenderRef"));
        for (final Map.Entry<String, Properties> entry : appenderRefs.entrySet()) {
            loggerBuilder.add(this.createAppenderRef(entry.getKey().trim(), entry.getValue()));
        }
        return loggerBuilder;
    }
    
    public PropertiesConfigurationBuilder setLoggerContext(final LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
        return this;
    }
    
    public LoggerContext getLoggerContext() {
        return this.loggerContext;
    }
}
