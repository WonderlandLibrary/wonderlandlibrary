// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.io.IOException;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.ConfigurationException;
import java.util.Map;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.Level;
import java.io.InputStream;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import java.util.Properties;

public class Log4j1ConfigurationParser
{
    private static final String COMMA_DELIMITED_RE = "\\s*,\\s*";
    private static final String ROOTLOGGER = "rootLogger";
    private static final String ROOTCATEGORY = "rootCategory";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String RELATIVE = "RELATIVE";
    private static final String NULL = "NULL";
    private final Properties properties;
    private final ConfigurationBuilder<BuiltConfiguration> builder;
    
    public Log4j1ConfigurationParser() {
        this.properties = new Properties();
        this.builder = ConfigurationBuilderFactory.newConfigurationBuilder();
    }
    
    public ConfigurationBuilder<BuiltConfiguration> buildConfigurationBuilder(final InputStream input) throws IOException {
        try {
            this.properties.load(input);
            final String rootCategoryValue = this.getLog4jValue("rootCategory");
            final String rootLoggerValue = this.getLog4jValue("rootLogger");
            if (rootCategoryValue == null && rootLoggerValue == null) {
                this.warn("Missing rootCategory or rootLogger in " + input);
            }
            this.builder.setConfigurationName("Log4j1");
            final String debugValue = this.getLog4jValue("debug");
            if (Boolean.parseBoolean(debugValue)) {
                this.builder.setStatusLevel(Level.DEBUG);
            }
            final String threshold = OptionConverter.findAndSubst("log4j.threshold", this.properties);
            if (threshold != null) {
                final Level level = OptionConverter.convertLevel(threshold.trim(), Level.ALL);
                this.builder.add(this.builder.newFilter("ThresholdFilter", Filter.Result.NEUTRAL, Filter.Result.DENY).addAttribute("level", level));
            }
            this.buildRootLogger(this.getLog4jValue("rootCategory"));
            this.buildRootLogger(this.getLog4jValue("rootLogger"));
            final Map<String, String> appenderNameToClassName = this.buildClassToPropertyPrefixMap();
            for (final Map.Entry<String, String> entry : appenderNameToClassName.entrySet()) {
                final String appenderName = entry.getKey();
                final String appenderClass = entry.getValue();
                this.buildAppender(appenderName, appenderClass);
            }
            this.buildLoggers("log4j.category.");
            this.buildLoggers("log4j.logger.");
            this.buildProperties();
            return this.builder;
        }
        catch (final IllegalArgumentException e) {
            throw new ConfigurationException(e);
        }
    }
    
    private void buildProperties() {
        for (final Map.Entry<Object, Object> entry : new TreeMap(this.properties).entrySet()) {
            final String key = entry.getKey().toString();
            if (!key.startsWith("log4j.") && !key.equals("rootCategory") && !key.equals("rootLogger")) {
                this.builder.addProperty(key, Objects.toString(entry.getValue(), ""));
            }
        }
    }
    
    private void warn(final String string) {
        System.err.println(string);
    }
    
    private Map<String, String> buildClassToPropertyPrefixMap() {
        final String prefix = "log4j.appender.";
        final int preLength = "log4j.appender.".length();
        final Map<String, String> map = new HashMap<String, String>();
        for (final Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            final Object keyObj = entry.getKey();
            if (keyObj != null) {
                final String key = keyObj.toString().trim();
                if (!key.startsWith("log4j.appender.") || key.indexOf(46, preLength) >= 0) {
                    continue;
                }
                final String name = key.substring(preLength);
                final Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                map.put(name, value.toString().trim());
            }
        }
        return map;
    }
    
    private void buildAppender(final String appenderName, final String appenderClass) {
        switch (appenderClass) {
            case "org.apache.log4j.ConsoleAppender": {
                this.buildConsoleAppender(appenderName);
                break;
            }
            case "org.apache.log4j.FileAppender": {
                this.buildFileAppender(appenderName);
                break;
            }
            case "org.apache.log4j.DailyRollingFileAppender": {
                this.buildDailyRollingFileAppender(appenderName);
                break;
            }
            case "org.apache.log4j.RollingFileAppender": {
                this.buildRollingFileAppender(appenderName);
                break;
            }
            case "org.apache.log4j.varia.NullAppender": {
                this.buildNullAppender(appenderName);
                break;
            }
            default: {
                this.reportWarning("Unknown appender class: " + appenderClass + "; ignoring appender: " + appenderName);
                break;
            }
        }
    }
    
    private void buildConsoleAppender(final String appenderName) {
        final AppenderComponentBuilder appenderBuilder = this.builder.newAppender(appenderName, "Console");
        final String targetValue = this.getLog4jAppenderValue(appenderName, "Target", "System.out");
        if (targetValue != null) {
            final String s = targetValue;
            ConsoleAppender.Target target = null;
            switch (s) {
                case "System.out": {
                    target = ConsoleAppender.Target.SYSTEM_OUT;
                    break;
                }
                case "System.err": {
                    target = ConsoleAppender.Target.SYSTEM_ERR;
                    break;
                }
                default: {
                    this.reportWarning("Unknown value for console Target: " + targetValue);
                    target = null;
                    break;
                }
            }
            if (target != null) {
                appenderBuilder.addAttribute("target", target);
            }
        }
        this.buildAttribute(appenderName, appenderBuilder, "Follow", "follow");
        if ("false".equalsIgnoreCase(this.getLog4jAppenderValue(appenderName, "ImmediateFlush"))) {
            this.reportWarning("ImmediateFlush=false is not supported on Console appender");
        }
        this.buildAppenderLayout(appenderName, appenderBuilder);
        this.builder.add(appenderBuilder);
    }
    
    private void buildFileAppender(final String appenderName) {
        final AppenderComponentBuilder appenderBuilder = this.builder.newAppender(appenderName, "File");
        this.buildFileAppender(appenderName, appenderBuilder);
        this.builder.add(appenderBuilder);
    }
    
    private void buildFileAppender(final String appenderName, final AppenderComponentBuilder appenderBuilder) {
        this.buildMandatoryAttribute(appenderName, appenderBuilder, "File", "fileName");
        this.buildAttribute(appenderName, appenderBuilder, "Append", "append");
        this.buildAttribute(appenderName, appenderBuilder, "BufferedIO", "bufferedIo");
        this.buildAttribute(appenderName, appenderBuilder, "BufferSize", "bufferSize");
        this.buildAttribute(appenderName, appenderBuilder, "ImmediateFlush", "immediateFlush");
        this.buildAppenderLayout(appenderName, appenderBuilder);
    }
    
    private void buildDailyRollingFileAppender(final String appenderName) {
        final AppenderComponentBuilder appenderBuilder = this.builder.newAppender(appenderName, "RollingFile");
        this.buildFileAppender(appenderName, appenderBuilder);
        final String fileName = this.getLog4jAppenderValue(appenderName, "File");
        final String datePattern = this.getLog4jAppenderValue(appenderName, "DatePattern", ".yyyy-MM-dd");
        appenderBuilder.addAttribute("filePattern", fileName + "%d{" + datePattern + "}");
        final ComponentBuilder<?> triggeringPolicy = this.builder.newComponent("Policies").addComponent(this.builder.newComponent("TimeBasedTriggeringPolicy").addAttribute("modulate", true));
        appenderBuilder.addComponent(triggeringPolicy);
        appenderBuilder.addComponent(this.builder.newComponent("DefaultRolloverStrategy").addAttribute("max", Integer.MAX_VALUE));
        this.builder.add(appenderBuilder);
    }
    
    private void buildRollingFileAppender(final String appenderName) {
        final AppenderComponentBuilder appenderBuilder = this.builder.newAppender(appenderName, "RollingFile");
        this.buildFileAppender(appenderName, appenderBuilder);
        final String fileName = this.getLog4jAppenderValue(appenderName, "File");
        appenderBuilder.addAttribute("filePattern", fileName + ".%i");
        final String maxFileSizeString = this.getLog4jAppenderValue(appenderName, "MaxFileSize", "10485760");
        final String maxBackupIndexString = this.getLog4jAppenderValue(appenderName, "MaxBackupIndex", "1");
        final ComponentBuilder<?> triggeringPolicy = this.builder.newComponent("Policies").addComponent(this.builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", maxFileSizeString));
        appenderBuilder.addComponent(triggeringPolicy);
        appenderBuilder.addComponent(this.builder.newComponent("DefaultRolloverStrategy").addAttribute("max", maxBackupIndexString));
        this.builder.add(appenderBuilder);
    }
    
    private void buildAttribute(final String componentName, final ComponentBuilder<?> componentBuilder, final String sourceAttributeName, final String targetAttributeName) {
        final String attributeValue = this.getLog4jAppenderValue(componentName, sourceAttributeName);
        if (attributeValue != null) {
            componentBuilder.addAttribute(targetAttributeName, attributeValue);
        }
    }
    
    private void buildMandatoryAttribute(final String componentName, final ComponentBuilder<?> componentBuilder, final String sourceAttributeName, final String targetAttributeName) {
        final String attributeValue = this.getLog4jAppenderValue(componentName, sourceAttributeName);
        if (attributeValue != null) {
            componentBuilder.addAttribute(targetAttributeName, attributeValue);
        }
        else {
            this.reportWarning("Missing " + sourceAttributeName + " for " + componentName);
        }
    }
    
    private void buildNullAppender(final String appenderName) {
        final AppenderComponentBuilder appenderBuilder = this.builder.newAppender(appenderName, "Null");
        this.builder.add(appenderBuilder);
    }
    
    private void buildAppenderLayout(final String name, final AppenderComponentBuilder appenderBuilder) {
        final String layoutClass = this.getLog4jAppenderValue(name, "layout", null);
        if (layoutClass != null) {
            final String s = layoutClass;
            switch (s) {
                case "org.apache.log4j.PatternLayout":
                case "org.apache.log4j.EnhancedPatternLayout": {
                    String pattern = this.getLog4jAppenderValue(name, "layout.ConversionPattern", null);
                    if (pattern != null) {
                        pattern = pattern.replaceAll("%([-\\.\\d]*)p(?!\\w)", "%$1v1Level").replaceAll("%([-\\.\\d]*)x(?!\\w)", "%$1ndc").replaceAll("%([-\\.\\d]*)X(?!\\w)", "%$1properties");
                    }
                    else {
                        pattern = "%m%n";
                    }
                    appenderBuilder.add(this.newPatternLayout(pattern));
                    break;
                }
                case "org.apache.log4j.SimpleLayout": {
                    appenderBuilder.add(this.newPatternLayout("%v1Level - %m%n"));
                    break;
                }
                case "org.apache.log4j.TTCCLayout": {
                    String pattern = "";
                    final String dateFormat = this.getLog4jAppenderValue(name, "layout.DateFormat", "RELATIVE");
                    final String timezone = this.getLog4jAppenderValue(name, "layout.TimeZone", null);
                    if (dateFormat != null) {
                        if ("RELATIVE".equalsIgnoreCase(dateFormat)) {
                            pattern += "%r ";
                        }
                        else if (!"NULL".equalsIgnoreCase(dateFormat)) {
                            pattern = pattern + "%d{" + dateFormat + "}";
                            if (timezone != null) {
                                pattern = pattern + "{" + timezone + "}";
                            }
                            pattern += " ";
                        }
                    }
                    if (Boolean.parseBoolean(this.getLog4jAppenderValue(name, "layout.ThreadPrinting", "true"))) {
                        pattern += "[%t] ";
                    }
                    pattern += "%p ";
                    if (Boolean.parseBoolean(this.getLog4jAppenderValue(name, "layout.CategoryPrefixing", "true"))) {
                        pattern += "%c ";
                    }
                    if (Boolean.parseBoolean(this.getLog4jAppenderValue(name, "layout.ContextPrinting", "true"))) {
                        pattern += "%notEmpty{%ndc }";
                    }
                    pattern += "- %m%n";
                    appenderBuilder.add(this.newPatternLayout(pattern));
                    break;
                }
                case "org.apache.log4j.HTMLLayout": {
                    final LayoutComponentBuilder htmlLayout = this.builder.newLayout("HtmlLayout");
                    htmlLayout.addAttribute("title", this.getLog4jAppenderValue(name, "layout.Title", "Log4J Log Messages"));
                    htmlLayout.addAttribute("locationInfo", Boolean.parseBoolean(this.getLog4jAppenderValue(name, "layout.LocationInfo", "false")));
                    appenderBuilder.add(htmlLayout);
                    break;
                }
                case "org.apache.log4j.xml.XMLLayout": {
                    final LayoutComponentBuilder xmlLayout = this.builder.newLayout("Log4j1XmlLayout");
                    xmlLayout.addAttribute("locationInfo", Boolean.parseBoolean(this.getLog4jAppenderValue(name, "layout.LocationInfo", "false")));
                    xmlLayout.addAttribute("properties", Boolean.parseBoolean(this.getLog4jAppenderValue(name, "layout.Properties", "false")));
                    appenderBuilder.add(xmlLayout);
                    break;
                }
                default: {
                    this.reportWarning("Unknown layout class: " + layoutClass);
                    break;
                }
            }
        }
    }
    
    private LayoutComponentBuilder newPatternLayout(final String pattern) {
        final LayoutComponentBuilder layoutBuilder = this.builder.newLayout("PatternLayout");
        if (pattern != null) {
            layoutBuilder.addAttribute("pattern", pattern);
        }
        return layoutBuilder;
    }
    
    private void buildRootLogger(final String rootLoggerValue) {
        if (rootLoggerValue == null) {
            return;
        }
        final String[] rootLoggerParts = rootLoggerValue.split("\\s*,\\s*");
        final String rootLoggerLevel = this.getLevelString(rootLoggerParts, Level.ERROR.name());
        final RootLoggerComponentBuilder loggerBuilder = this.builder.newRootLogger(rootLoggerLevel);
        final String[] sortedAppenderNames = Arrays.copyOfRange(rootLoggerParts, 1, rootLoggerParts.length);
        Arrays.sort(sortedAppenderNames);
        for (final String appender : sortedAppenderNames) {
            loggerBuilder.add(this.builder.newAppenderRef(appender));
        }
        this.builder.add(loggerBuilder);
    }
    
    private String getLevelString(final String[] loggerParts, final String defaultLevel) {
        return (loggerParts.length > 0) ? loggerParts[0] : defaultLevel;
    }
    
    private void buildLoggers(final String prefix) {
        final int preLength = prefix.length();
        for (final Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            final Object keyObj = entry.getKey();
            if (keyObj != null) {
                final String key = keyObj.toString().trim();
                if (!key.startsWith(prefix)) {
                    continue;
                }
                final String name = key.substring(preLength);
                final Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                final String valueStr = value.toString().trim();
                final String[] split = valueStr.split("\\s*,\\s*");
                final String level = this.getLevelString(split, null);
                if (level == null) {
                    this.warn("Level is missing for entry " + entry);
                }
                else {
                    final LoggerComponentBuilder newLogger = this.builder.newLogger(name, level);
                    if (split.length > 1) {
                        final String[] sortedAppenderNames = Arrays.copyOfRange(split, 1, split.length);
                        Arrays.sort(sortedAppenderNames);
                        for (final String appenderName : sortedAppenderNames) {
                            newLogger.add(this.builder.newAppenderRef(appenderName));
                        }
                    }
                    this.builder.add(newLogger);
                }
            }
        }
    }
    
    private String getLog4jAppenderValue(final String appenderName, final String attributeName) {
        return this.getProperty("log4j.appender." + appenderName + "." + attributeName);
    }
    
    private String getProperty(final String key) {
        final String value = this.properties.getProperty(key);
        final String substVars = OptionConverter.substVars(value, this.properties);
        return (substVars == null) ? null : substVars.trim();
    }
    
    private String getProperty(final String key, final String defaultValue) {
        final String value = this.getProperty(key);
        return (value == null) ? defaultValue : value;
    }
    
    private String getLog4jAppenderValue(final String appenderName, final String attributeName, final String defaultValue) {
        return this.getProperty("log4j.appender." + appenderName + "." + attributeName, defaultValue);
    }
    
    private String getLog4jValue(final String key) {
        return this.getProperty("log4j." + key);
    }
    
    private void reportWarning(final String msg) {
        StatusLogger.getLogger().warn("Log4j 1 configuration parser: " + msg);
    }
}
