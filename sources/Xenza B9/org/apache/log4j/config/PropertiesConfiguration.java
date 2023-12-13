// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

import java.util.Hashtable;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import java.util.Iterator;
import java.util.SortedMap;
import org.apache.log4j.bridge.FilterAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.apache.log4j.builders.BuilderManager;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.bridge.AppenderAdapter;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Objects;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.log4j.helpers.OptionConverter;
import java.io.IOException;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.config.Configuration;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.Properties;
import org.apache.log4j.Appender;
import java.util.Map;

public class PropertiesConfiguration extends Log4j1Configuration
{
    private static final String CATEGORY_PREFIX = "log4j.category.";
    private static final String LOGGER_PREFIX = "log4j.logger.";
    private static final String ADDITIVITY_PREFIX = "log4j.additivity.";
    private static final String ROOT_CATEGORY_PREFIX = "log4j.rootCategory";
    private static final String ROOT_LOGGER_PREFIX = "log4j.rootLogger";
    private static final String APPENDER_PREFIX = "log4j.appender.";
    private static final String LOGGER_REF = "logger-ref";
    private static final String ROOT_REF = "root-ref";
    private static final String APPENDER_REF_TAG = "appender-ref";
    private static final String RESET_KEY = "log4j.reset";
    public static final String THRESHOLD_KEY = "log4j.threshold";
    public static final String DEBUG_KEY = "log4j.debug";
    private static final String INTERNAL_ROOT_NAME = "root";
    private final Map<String, Appender> registry;
    private Properties properties;
    
    public PropertiesConfiguration(final LoggerContext loggerContext, final ConfigurationSource source, final int monitorIntervalSeconds) {
        super(loggerContext, source, monitorIntervalSeconds);
        this.registry = new HashMap<String, Appender>();
    }
    
    public PropertiesConfiguration(final LoggerContext loggerContext, final Properties properties) {
        super(loggerContext, ConfigurationSource.NULL_SOURCE, 0);
        this.registry = new HashMap<String, Appender>();
        this.properties = properties;
    }
    
    public PropertiesConfiguration(final org.apache.logging.log4j.spi.LoggerContext loggerContext, final Properties properties) {
        this((LoggerContext)loggerContext, properties);
    }
    
    public void doConfigure() {
        if (this.properties == null) {
            this.properties = new Properties();
            final InputStream inputStream = this.getConfigurationSource().getInputStream();
            if (inputStream != null) {
                try {
                    this.properties.load(inputStream);
                }
                catch (final Exception e) {
                    PropertiesConfiguration.LOGGER.error("Could not read configuration file [{}].", this.getConfigurationSource().toString(), e);
                    return;
                }
            }
        }
        this.doConfigure(this.properties);
    }
    
    @Override
    public Configuration reconfigure() {
        try {
            final ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            final Configuration config = new PropertiesConfigurationFactory().getConfiguration(this.getLoggerContext(), source);
            return (config == null || config.getState() != LifeCycle.State.INITIALIZING) ? null : config;
        }
        catch (final IOException ex) {
            PropertiesConfiguration.LOGGER.error("Cannot locate file {}: {}", this.getConfigurationSource(), ex);
            return null;
        }
    }
    
    private void doConfigure(final Properties properties) {
        String status = "error";
        String value = properties.getProperty("log4j.debug");
        if (value == null) {
            value = properties.getProperty("log4j.configDebug");
            if (value != null) {
                PropertiesConfiguration.LOGGER.warn("[log4j.configDebug] is deprecated. Use [log4j.debug] instead.");
            }
        }
        if (value != null) {
            status = (OptionConverter.toBoolean(value, false) ? "debug" : "error");
        }
        final StatusConfiguration statusConfig = new StatusConfiguration().withStatus(status);
        statusConfig.initialize();
        final String reset = properties.getProperty("log4j.reset");
        if (reset != null && OptionConverter.toBoolean(reset, false)) {
            LogManager.resetConfiguration();
        }
        final String threshold = OptionConverter.findAndSubst("log4j.threshold", properties);
        if (threshold != null) {
            final Level level = OptionConverter.convertLevel(threshold.trim(), Level.ALL);
            this.addFilter(ThresholdFilter.createFilter(level, Filter.Result.NEUTRAL, Filter.Result.DENY));
        }
        this.configureRoot(properties);
        this.parseLoggers(properties);
        PropertiesConfiguration.LOGGER.debug("Finished configuring.");
    }
    
    private void configureRoot(final Properties props) {
        String effectiveFrefix = "log4j.rootLogger";
        String value = OptionConverter.findAndSubst("log4j.rootLogger", props);
        if (value == null) {
            value = OptionConverter.findAndSubst("log4j.rootCategory", props);
            effectiveFrefix = "log4j.rootCategory";
        }
        if (value == null) {
            PropertiesConfiguration.LOGGER.debug("Could not find root logger information. Is this OK?");
        }
        else {
            final LoggerConfig root = this.getRootLogger();
            this.parseLogger(props, root, effectiveFrefix, "root", value);
        }
    }
    
    private void parseLoggers(final Properties props) {
        final Enumeration<?> enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            final String key = Objects.toString(enumeration.nextElement(), null);
            if (key.startsWith("log4j.category.") || key.startsWith("log4j.logger.")) {
                String loggerName = null;
                if (key.startsWith("log4j.category.")) {
                    loggerName = key.substring("log4j.category.".length());
                }
                else if (key.startsWith("log4j.logger.")) {
                    loggerName = key.substring("log4j.logger.".length());
                }
                final String value = OptionConverter.findAndSubst(key, props);
                LoggerConfig loggerConfig = this.getLogger(loggerName);
                if (loggerConfig == null) {
                    final boolean additivity = this.getAdditivityForLogger(props, loggerName);
                    loggerConfig = new LoggerConfig(loggerName, Level.ERROR, additivity);
                    this.addLogger(loggerName, loggerConfig);
                }
                this.parseLogger(props, loggerConfig, key, loggerName, value);
            }
        }
    }
    
    private boolean getAdditivityForLogger(final Properties props, final String loggerName) {
        boolean additivity = true;
        final String key = "log4j.additivity." + loggerName;
        final String value = OptionConverter.findAndSubst(key, props);
        PropertiesConfiguration.LOGGER.debug("Handling {}=[{}]", key, value);
        if (value != null && !value.equals("")) {
            additivity = OptionConverter.toBoolean(value, true);
        }
        return additivity;
    }
    
    private void parseLogger(final Properties props, final LoggerConfig loggerConfig, final String optionKey, final String loggerName, final String value) {
        PropertiesConfiguration.LOGGER.debug("Parsing for [{}] with value=[{}].", loggerName, value);
        final StringTokenizer st = new StringTokenizer(value, ",");
        if (!value.startsWith(",") && !value.equals("")) {
            if (!st.hasMoreTokens()) {
                return;
            }
            final String levelStr = st.nextToken();
            PropertiesConfiguration.LOGGER.debug("Level token is [{}].", levelStr);
            final Level level = (levelStr == null) ? Level.ERROR : OptionConverter.convertLevel(levelStr, Level.DEBUG);
            loggerConfig.setLevel(level);
            PropertiesConfiguration.LOGGER.debug("Logger {} level set to {}", loggerName, level);
        }
        while (st.hasMoreTokens()) {
            final String appenderName = st.nextToken().trim();
            if (appenderName != null) {
                if (appenderName.equals(",")) {
                    continue;
                }
                PropertiesConfiguration.LOGGER.debug("Parsing appender named \"{}\".", appenderName);
                final Appender appender = this.parseAppender(props, appenderName);
                if (appender != null) {
                    PropertiesConfiguration.LOGGER.debug("Adding appender named [{}] to loggerConfig [{}].", appenderName, loggerConfig.getName());
                    loggerConfig.addAppender(this.getAppender(appenderName), null, null);
                }
                else {
                    PropertiesConfiguration.LOGGER.debug("Appender named [{}] not found.", appenderName);
                }
            }
        }
    }
    
    public Appender parseAppender(final Properties props, final String appenderName) {
        Appender appender = this.registry.get(appenderName);
        if (appender != null) {
            PropertiesConfiguration.LOGGER.debug("Appender \"" + appenderName + "\" was already parsed.");
            return appender;
        }
        final String prefix = "log4j.appender." + appenderName;
        final String layoutPrefix = prefix + ".layout";
        final String filterPrefix = "log4j.appender." + appenderName + ".filter.";
        final String className = OptionConverter.findAndSubst(prefix, props);
        if (className == null) {
            PropertiesConfiguration.LOGGER.debug("Appender \"" + appenderName + "\" does not exist.");
            return null;
        }
        appender = this.manager.parseAppender(appenderName, className, prefix, layoutPrefix, filterPrefix, props, this);
        if (appender == null) {
            appender = this.buildAppender(appenderName, className, prefix, layoutPrefix, filterPrefix, props);
        }
        else {
            this.registry.put(appenderName, appender);
            this.addAppender(AppenderAdapter.adapt(appender));
        }
        return appender;
    }
    
    private Appender buildAppender(final String appenderName, final String className, final String prefix, final String layoutPrefix, final String filterPrefix, final Properties props) {
        final Appender appender = newInstanceOf(className, "Appender");
        if (appender == null) {
            return null;
        }
        appender.setName(appenderName);
        appender.setLayout(this.parseLayout(layoutPrefix, appenderName, props));
        final String errorHandlerPrefix = prefix + ".errorhandler";
        final String errorHandlerClass = OptionConverter.findAndSubst(errorHandlerPrefix, props);
        if (errorHandlerClass != null) {
            final ErrorHandler eh = this.parseErrorHandler(props, errorHandlerPrefix, errorHandlerClass, appender);
            if (eh != null) {
                appender.setErrorHandler(eh);
            }
        }
        appender.addFilter(this.parseAppenderFilters(props, filterPrefix, appenderName));
        final String[] keys = { layoutPrefix };
        this.addProperties(appender, keys, props, prefix);
        this.addAppender(AppenderAdapter.adapt(appender));
        this.registry.put(appenderName, appender);
        return appender;
    }
    
    public Layout parseLayout(final String layoutPrefix, final String appenderName, final Properties props) {
        final String layoutClass = OptionConverter.findAndSubst(layoutPrefix, props);
        if (layoutClass == null) {
            return null;
        }
        Layout layout = this.manager.parse(layoutClass, layoutPrefix, props, this, BuilderManager.INVALID_LAYOUT);
        if (layout == null) {
            layout = this.buildLayout(layoutPrefix, layoutClass, appenderName, props);
        }
        return layout;
    }
    
    private Layout buildLayout(final String layoutPrefix, final String className, final String appenderName, final Properties props) {
        final Layout layout = newInstanceOf(className, "Layout");
        if (layout == null) {
            return null;
        }
        PropertiesConfiguration.LOGGER.debug("Parsing layout options for \"{}\".", appenderName);
        PropertySetter.setProperties(layout, props, layoutPrefix + ".");
        PropertiesConfiguration.LOGGER.debug("End of parsing for \"{}\".", appenderName);
        return layout;
    }
    
    public ErrorHandler parseErrorHandler(final Properties props, final String errorHandlerPrefix, final String errorHandlerClass, final Appender appender) {
        final ErrorHandler eh = newInstanceOf(errorHandlerClass, "ErrorHandler");
        final String[] keys = { errorHandlerPrefix + "." + "root-ref", errorHandlerPrefix + "." + "logger-ref", errorHandlerPrefix + "." + "appender-ref" };
        this.addProperties(eh, keys, props, errorHandlerPrefix);
        return eh;
    }
    
    public void addProperties(final Object obj, final String[] keys, final Properties props, final String prefix) {
        final Properties edited = new Properties();
        props.stringPropertyNames().stream().filter(name -> {
            if (name.startsWith(prefix)) {
                final int length = keys.length;
                int i = 0;
                while (i < length) {
                    final String key = keys[i];
                    if (name.equals(key)) {
                        return false;
                    }
                    else {
                        ++i;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }).forEach(name -> ((Hashtable<String, String>)edited).put(name, props.getProperty(name)));
        PropertySetter.setProperties(obj, edited, prefix + ".");
    }
    
    public org.apache.log4j.spi.Filter parseAppenderFilters(final Properties props, final String filterPrefix, final String appenderName) {
        final int fIdx = filterPrefix.length();
        final SortedMap<String, List<NameValue>> filters = new TreeMap<String, List<NameValue>>();
        final Enumeration<?> e = ((Hashtable<?, V>)props).keys();
        String name = "";
        while (e.hasMoreElements()) {
            final String key = (String)e.nextElement();
            if (key.startsWith(filterPrefix)) {
                final int dotIdx = key.indexOf(46, fIdx);
                String filterKey = key;
                if (dotIdx != -1) {
                    filterKey = key.substring(0, dotIdx);
                    name = key.substring(dotIdx + 1);
                }
                final List<NameValue> filterOpts = filters.computeIfAbsent(filterKey, k -> new ArrayList());
                if (dotIdx == -1) {
                    continue;
                }
                final String value = OptionConverter.findAndSubst(key, props);
                filterOpts.add(new NameValue(name, value));
            }
        }
        org.apache.log4j.spi.Filter head = null;
        for (final Map.Entry<String, List<NameValue>> entry : filters.entrySet()) {
            final String clazz = props.getProperty(entry.getKey());
            org.apache.log4j.spi.Filter filter = null;
            if (clazz != null) {
                filter = this.manager.parse(clazz, entry.getKey(), props, this, BuilderManager.INVALID_FILTER);
                if (filter == null) {
                    PropertiesConfiguration.LOGGER.debug("Filter key: [{}] class: [{}] props: {}", entry.getKey(), clazz, entry.getValue());
                    filter = this.buildFilter(clazz, appenderName, entry.getValue());
                }
            }
            head = FilterAdapter.addFilter(head, filter);
        }
        return head;
    }
    
    private org.apache.log4j.spi.Filter buildFilter(final String className, final String appenderName, final List<NameValue> props) {
        final org.apache.log4j.spi.Filter filter = newInstanceOf(className, "Filter");
        if (filter != null) {
            final PropertySetter propSetter = new PropertySetter(filter);
            for (final NameValue property : props) {
                propSetter.setProperty(property.key, property.value);
            }
            propSetter.activate();
        }
        return filter;
    }
    
    public TriggeringPolicy parseTriggeringPolicy(final Properties props, final String policyPrefix) {
        final String policyClass = OptionConverter.findAndSubst(policyPrefix, props);
        if (policyClass == null) {
            return null;
        }
        return this.manager.parse(policyClass, policyPrefix, props, this, (TriggeringPolicy)null);
    }
    
    private static <T> T newInstanceOf(final String className, final String type) {
        try {
            return LoaderUtil.newInstanceOf(className);
        }
        catch (final ReflectiveOperationException ex) {
            PropertiesConfiguration.LOGGER.error("Unable to create {} {} due to {}:{}", type, className, ex.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }
    
    private static class NameValue
    {
        String key;
        String value;
        
        NameValue(final String key, final String value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
