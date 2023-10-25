/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.properties;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterableComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggableComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;

public class PropertiesConfigurationBuilder
extends ConfigurationBuilderFactory
implements Builder<PropertiesConfiguration> {
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
    private final ConfigurationBuilder<PropertiesConfiguration> builder = PropertiesConfigurationBuilder.newConfigurationBuilder(PropertiesConfiguration.class);
    private LoggerContext loggerContext;
    private Properties rootProperties;

    public PropertiesConfigurationBuilder setRootProperties(Properties rootProperties) {
        this.rootProperties = rootProperties;
        return this;
    }

    public PropertiesConfigurationBuilder setConfigurationSource(ConfigurationSource source) {
        this.builder.setConfigurationSource(source);
        return this;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public PropertiesConfiguration build() {
        Properties properties;
        String loggerProp;
        String appenderProp;
        String name;
        String string;
        Object type;
        for (String string2 : this.rootProperties.stringPropertyNames()) {
            if (string2.contains(".")) continue;
            this.builder.addRootProperty(string2, this.rootProperties.getProperty(string2));
        }
        this.builder.setStatusLevel(Level.toLevel(this.rootProperties.getProperty(STATUS_KEY), Level.ERROR)).setShutdownHook(this.rootProperties.getProperty(SHUTDOWN_HOOK)).setShutdownTimeout(Long.parseLong(this.rootProperties.getProperty(SHUTDOWN_TIMEOUT, "0")), TimeUnit.MILLISECONDS).setVerbosity(this.rootProperties.getProperty(VERBOSE)).setDestination(this.rootProperties.getProperty(DEST)).setPackages(this.rootProperties.getProperty(PACKAGES)).setConfigurationName(this.rootProperties.getProperty(CONFIG_NAME)).setMonitorInterval(this.rootProperties.getProperty(MONITOR_INTERVAL, "0")).setAdvertiser(this.rootProperties.getProperty(ADVERTISER_KEY));
        Properties propertyPlaceholders = PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)"property");
        for (String string3 : propertyPlaceholders.stringPropertyNames()) {
            this.builder.addProperty(string3, propertyPlaceholders.getProperty(string3));
        }
        Map map = PropertiesUtil.partitionOnCommonPrefixes((Properties)PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)"script"));
        for (Map.Entry entry : map.entrySet()) {
            Properties scriptProps = (Properties)entry.getValue();
            type = (String[])scriptProps.remove(CONFIG_TYPE);
            if (type == null) {
                throw new ConfigurationException("No type provided for script - must be Script or ScriptFile");
            }
            if (((String)type).equalsIgnoreCase("script")) {
                this.builder.add(this.createScript(scriptProps));
                continue;
            }
            this.builder.add(this.createScriptFile(scriptProps));
        }
        Properties properties2 = PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)"customLevel");
        if (properties2.size() > 0) {
            for (String key : properties2.stringPropertyNames()) {
                this.builder.add(this.builder.newCustomLevel(key, Integer.parseInt(properties2.getProperty(key))));
            }
        }
        if ((string = this.rootProperties.getProperty("filters")) != null) {
            void var8_24;
            String[] filterNames = string.split(",");
            type = filterNames;
            int n = ((String[])type).length;
            boolean bl = false;
            while (++var8_24 < n) {
                Object filterName = type[var8_24];
                name = ((String)filterName).trim();
                this.builder.add(this.createFilter(name, PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)("filter." + name))));
            }
        } else {
            Map filters = PropertiesUtil.partitionOnCommonPrefixes((Properties)PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)"filter"));
            for (Map.Entry entry : filters.entrySet()) {
                this.builder.add(this.createFilter(((String)entry.getKey()).trim(), (Properties)entry.getValue()));
            }
        }
        if ((appenderProp = this.rootProperties.getProperty("appenders")) != null) {
            String[] appenderNames;
            for (String appenderName : appenderNames = appenderProp.split(",")) {
                String name2 = appenderName.trim();
                this.builder.add(this.createAppender(appenderName.trim(), PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)("appender." + name2))));
            }
        } else {
            Map appenders = PropertiesUtil.partitionOnCommonPrefixes((Properties)PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)"appender"));
            for (Map.Entry entry : appenders.entrySet()) {
                this.builder.add(this.createAppender(((String)entry.getKey()).trim(), (Properties)entry.getValue()));
            }
        }
        if ((loggerProp = this.rootProperties.getProperty("loggers")) != null) {
            String[] arrstring;
            for (String loggerName : arrstring = loggerProp.split(",")) {
                String name3 = loggerName.trim();
                if (name3.equals("root")) continue;
                this.builder.add(this.createLogger(name3, PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)("logger." + name3))));
            }
        } else {
            Map map2 = PropertiesUtil.partitionOnCommonPrefixes((Properties)PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)"logger"));
            for (Map.Entry entry : map2.entrySet()) {
                name = ((String)entry.getKey()).trim();
                if (name.equals("root")) continue;
                this.builder.add(this.createLogger(name, (Properties)entry.getValue()));
            }
        }
        if ((properties = PropertiesUtil.extractSubset((Properties)this.rootProperties, (String)"rootLogger")).size() > 0) {
            this.builder.add(this.createRootLogger(properties));
        }
        this.builder.setLoggerContext(this.loggerContext);
        return this.builder.build(false);
    }

    private ScriptComponentBuilder createScript(Properties properties) {
        String name = (String)properties.remove(CONFIG_NAME);
        String language = (String)properties.remove("language");
        String text = (String)properties.remove("text");
        ScriptComponentBuilder scriptBuilder = this.builder.newScript(name, language, text);
        return PropertiesConfigurationBuilder.processRemainingProperties(scriptBuilder, properties);
    }

    private ScriptFileComponentBuilder createScriptFile(Properties properties) {
        String name = (String)properties.remove(CONFIG_NAME);
        String path = (String)properties.remove("path");
        ScriptFileComponentBuilder scriptFileBuilder = this.builder.newScriptFile(name, path);
        return PropertiesConfigurationBuilder.processRemainingProperties(scriptFileBuilder, properties);
    }

    private AppenderComponentBuilder createAppender(String key, Properties properties) {
        String name = (String)properties.remove(CONFIG_NAME);
        if (Strings.isEmpty(name)) {
            throw new ConfigurationException("No name attribute provided for Appender " + key);
        }
        String type = (String)properties.remove(CONFIG_TYPE);
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for Appender " + key);
        }
        AppenderComponentBuilder appenderBuilder = this.builder.newAppender(name, type);
        this.addFiltersToComponent(appenderBuilder, properties);
        Properties layoutProps = PropertiesUtil.extractSubset((Properties)properties, (String)"layout");
        if (layoutProps.size() > 0) {
            appenderBuilder.add(this.createLayout(name, layoutProps));
        }
        return PropertiesConfigurationBuilder.processRemainingProperties(appenderBuilder, properties);
    }

    private FilterComponentBuilder createFilter(String key, Properties properties) {
        String type = (String)properties.remove(CONFIG_TYPE);
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for Filter " + key);
        }
        String onMatch = (String)properties.remove("onMatch");
        String onMismatch = (String)properties.remove("onMismatch");
        FilterComponentBuilder filterBuilder = this.builder.newFilter(type, onMatch, onMismatch);
        return PropertiesConfigurationBuilder.processRemainingProperties(filterBuilder, properties);
    }

    private AppenderRefComponentBuilder createAppenderRef(String key, Properties properties) {
        String ref = (String)properties.remove("ref");
        if (Strings.isEmpty(ref)) {
            throw new ConfigurationException("No ref attribute provided for AppenderRef " + key);
        }
        AppenderRefComponentBuilder appenderRefBuilder = this.builder.newAppenderRef(ref);
        String level = Strings.trimToNull((String)properties.remove("level"));
        if (!Strings.isEmpty(level)) {
            appenderRefBuilder.addAttribute("level", level);
        }
        return this.addFiltersToComponent(appenderRefBuilder, properties);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private LoggerComponentBuilder createLogger(String key, Properties properties) {
        LoggerComponentBuilder loggerBuilder;
        String name = (String)properties.remove(CONFIG_NAME);
        String location = (String)properties.remove("includeLocation");
        if (Strings.isEmpty(name)) {
            throw new ConfigurationException("No name attribute provided for Logger " + key);
        }
        String level = Strings.trimToNull((String)properties.remove("level"));
        String type = (String)properties.remove(CONFIG_TYPE);
        if (type != null) {
            if (!type.equalsIgnoreCase("asyncLogger")) throw new ConfigurationException("Unknown Logger type " + type + " for Logger " + name);
            if (location != null) {
                boolean includeLocation = Boolean.parseBoolean(location);
                loggerBuilder = this.builder.newAsyncLogger(name, level, includeLocation);
            } else {
                loggerBuilder = this.builder.newAsyncLogger(name, level);
            }
        } else if (location != null) {
            boolean includeLocation = Boolean.parseBoolean(location);
            loggerBuilder = this.builder.newLogger(name, level, includeLocation);
        } else {
            loggerBuilder = this.builder.newLogger(name, level);
        }
        this.addLoggersToComponent(loggerBuilder, properties);
        this.addFiltersToComponent(loggerBuilder, properties);
        String additivity = (String)properties.remove("additivity");
        if (Strings.isEmpty(additivity)) return loggerBuilder;
        loggerBuilder.addAttribute("additivity", additivity);
        return loggerBuilder;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private RootLoggerComponentBuilder createRootLogger(Properties properties) {
        RootLoggerComponentBuilder loggerBuilder;
        String level = Strings.trimToNull((String)properties.remove("level"));
        String type = (String)properties.remove(CONFIG_TYPE);
        String location = (String)properties.remove("includeLocation");
        if (type != null) {
            if (!type.equalsIgnoreCase("asyncRoot")) throw new ConfigurationException("Unknown Logger type for root logger" + type);
            if (location != null) {
                boolean includeLocation = Boolean.parseBoolean(location);
                loggerBuilder = this.builder.newAsyncRootLogger(level, includeLocation);
            } else {
                loggerBuilder = this.builder.newAsyncRootLogger(level);
            }
        } else if (location != null) {
            boolean includeLocation = Boolean.parseBoolean(location);
            loggerBuilder = this.builder.newRootLogger(level, includeLocation);
        } else {
            loggerBuilder = this.builder.newRootLogger(level);
        }
        this.addLoggersToComponent(loggerBuilder, properties);
        return this.addFiltersToComponent(loggerBuilder, properties);
    }

    private LayoutComponentBuilder createLayout(String appenderName, Properties properties) {
        String type = (String)properties.remove(CONFIG_TYPE);
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for Layout on Appender " + appenderName);
        }
        LayoutComponentBuilder layoutBuilder = this.builder.newLayout(type);
        return PropertiesConfigurationBuilder.processRemainingProperties(layoutBuilder, properties);
    }

    private static <B extends ComponentBuilder<B>> ComponentBuilder<B> createComponent(ComponentBuilder<?> parent, String key, Properties properties) {
        String name = (String)properties.remove(CONFIG_NAME);
        String type = (String)properties.remove(CONFIG_TYPE);
        if (Strings.isEmpty(type)) {
            throw new ConfigurationException("No type attribute provided for component " + key);
        }
        ComponentBuilder componentBuilder = parent.getBuilder().newComponent(name, type);
        return PropertiesConfigurationBuilder.processRemainingProperties(componentBuilder, properties);
    }

    private static <B extends ComponentBuilder<?>> B processRemainingProperties(B builder, Properties properties) {
        while (properties.size() > 0) {
            String propertyName = properties.stringPropertyNames().iterator().next();
            int index = propertyName.indexOf(46);
            if (index > 0) {
                String prefix = propertyName.substring(0, index);
                Properties componentProperties = PropertiesUtil.extractSubset((Properties)properties, (String)prefix);
                builder.addComponent(PropertiesConfigurationBuilder.createComponent(builder, prefix, componentProperties));
                continue;
            }
            builder.addAttribute(propertyName, properties.getProperty(propertyName));
            properties.remove(propertyName);
        }
        return builder;
    }

    private <B extends FilterableComponentBuilder<? extends ComponentBuilder<?>>> B addFiltersToComponent(B componentBuilder, Properties properties) {
        Map filters = PropertiesUtil.partitionOnCommonPrefixes((Properties)PropertiesUtil.extractSubset((Properties)properties, (String)"filter"));
        for (Map.Entry entry : filters.entrySet()) {
            componentBuilder.add(this.createFilter(((String)entry.getKey()).trim(), (Properties)entry.getValue()));
        }
        return componentBuilder;
    }

    private <B extends LoggableComponentBuilder<? extends ComponentBuilder<?>>> B addLoggersToComponent(B loggerBuilder, Properties properties) {
        Map appenderRefs = PropertiesUtil.partitionOnCommonPrefixes((Properties)PropertiesUtil.extractSubset((Properties)properties, (String)"appenderRef"));
        for (Map.Entry entry : appenderRefs.entrySet()) {
            loggerBuilder.add(this.createAppenderRef(((String)entry.getKey()).trim(), (Properties)entry.getValue()));
        }
        return loggerBuilder;
    }

    public PropertiesConfigurationBuilder setLoggerContext(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
        return this;
    }

    public LoggerContext getLoggerContext() {
        return this.loggerContext;
    }
}

