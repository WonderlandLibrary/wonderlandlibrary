// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.io.InputStream;
import org.slf4j.helpers.Util;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.text.DateFormat;

public class SimpleLoggerConfiguration
{
    private static final String CONFIGURATION_FILE = "simplelogger.properties";
    static int DEFAULT_LOG_LEVEL_DEFAULT;
    int defaultLogLevel;
    private static final boolean SHOW_DATE_TIME_DEFAULT = false;
    boolean showDateTime;
    private static final String DATE_TIME_FORMAT_STR_DEFAULT;
    private static String dateTimeFormatStr;
    DateFormat dateFormatter;
    private static final boolean SHOW_THREAD_NAME_DEFAULT = true;
    boolean showThreadName;
    static final boolean SHOW_LOG_NAME_DEFAULT = true;
    boolean showLogName;
    private static final boolean SHOW_SHORT_LOG_NAME_DEFAULT = false;
    boolean showShortLogName;
    private static final boolean LEVEL_IN_BRACKETS_DEFAULT = false;
    boolean levelInBrackets;
    private static String LOG_FILE_DEFAULT;
    private String logFile;
    OutputChoice outputChoice;
    private static final boolean CACHE_OUTPUT_STREAM_DEFAULT = false;
    private boolean cacheOutputStream;
    private static final String WARN_LEVELS_STRING_DEFAULT = "WARN";
    String warnLevelString;
    private final Properties properties;
    
    public SimpleLoggerConfiguration() {
        this.defaultLogLevel = SimpleLoggerConfiguration.DEFAULT_LOG_LEVEL_DEFAULT;
        this.showDateTime = false;
        this.dateFormatter = null;
        this.showThreadName = true;
        this.showLogName = true;
        this.showShortLogName = false;
        this.levelInBrackets = false;
        this.logFile = SimpleLoggerConfiguration.LOG_FILE_DEFAULT;
        this.outputChoice = null;
        this.cacheOutputStream = false;
        this.warnLevelString = "WARN";
        this.properties = new Properties();
    }
    
    void init() {
        this.loadProperties();
        final String defaultLogLevelString = this.getStringProperty("org.slf4j.simpleLogger.defaultLogLevel", null);
        if (defaultLogLevelString != null) {
            this.defaultLogLevel = stringToLevel(defaultLogLevelString);
        }
        this.showLogName = this.getBooleanProperty("org.slf4j.simpleLogger.showLogName", true);
        this.showShortLogName = this.getBooleanProperty("org.slf4j.simpleLogger.showShortLogName", false);
        this.showDateTime = this.getBooleanProperty("org.slf4j.simpleLogger.showDateTime", false);
        this.showThreadName = this.getBooleanProperty("org.slf4j.simpleLogger.showThreadName", true);
        SimpleLoggerConfiguration.dateTimeFormatStr = this.getStringProperty("org.slf4j.simpleLogger.dateTimeFormat", SimpleLoggerConfiguration.DATE_TIME_FORMAT_STR_DEFAULT);
        this.levelInBrackets = this.getBooleanProperty("org.slf4j.simpleLogger.levelInBrackets", false);
        this.warnLevelString = this.getStringProperty("org.slf4j.simpleLogger.warnLevelString", "WARN");
        this.logFile = this.getStringProperty("org.slf4j.simpleLogger.logFile", this.logFile);
        this.cacheOutputStream = this.getBooleanProperty("org.slf4j.simpleLogger.cacheOutputStream", false);
        this.outputChoice = computeOutputChoice(this.logFile, this.cacheOutputStream);
        if (SimpleLoggerConfiguration.dateTimeFormatStr != null) {
            try {
                this.dateFormatter = new SimpleDateFormat(SimpleLoggerConfiguration.dateTimeFormatStr);
            }
            catch (IllegalArgumentException e) {
                Util.report("Bad date format in simplelogger.properties; will output relative time", e);
            }
        }
    }
    
    private void loadProperties() {
        final InputStream in = AccessController.doPrivileged((PrivilegedAction<InputStream>)new PrivilegedAction<InputStream>() {
            public InputStream run() {
                final ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream("simplelogger.properties");
                }
                return ClassLoader.getSystemResourceAsStream("simplelogger.properties");
            }
        });
        if (null != in) {
            try {
                this.properties.load(in);
            }
            catch (IOException e) {}
            finally {
                try {
                    in.close();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    String getStringProperty(final String name, final String defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }
    
    boolean getBooleanProperty(final String name, final boolean defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }
    
    String getStringProperty(final String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException ex) {}
        return (prop == null) ? this.properties.getProperty(name) : prop;
    }
    
    static int stringToLevel(final String levelStr) {
        if ("trace".equalsIgnoreCase(levelStr)) {
            return 0;
        }
        if ("debug".equalsIgnoreCase(levelStr)) {
            return 10;
        }
        if ("info".equalsIgnoreCase(levelStr)) {
            return 20;
        }
        if ("warn".equalsIgnoreCase(levelStr)) {
            return 30;
        }
        if ("error".equalsIgnoreCase(levelStr)) {
            return 40;
        }
        if ("off".equalsIgnoreCase(levelStr)) {
            return 50;
        }
        return 20;
    }
    
    private static OutputChoice computeOutputChoice(final String logFile, final boolean cacheOutputStream) {
        if ("System.err".equalsIgnoreCase(logFile)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_ERR);
            }
            return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
        }
        else if ("System.out".equalsIgnoreCase(logFile)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_OUT);
            }
            return new OutputChoice(OutputChoice.OutputChoiceType.SYS_OUT);
        }
        else {
            try {
                final FileOutputStream fos = new FileOutputStream(logFile);
                final PrintStream printStream = new PrintStream(fos);
                return new OutputChoice(printStream);
            }
            catch (FileNotFoundException e) {
                Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
                return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
            }
        }
    }
    
    static {
        SimpleLoggerConfiguration.DEFAULT_LOG_LEVEL_DEFAULT = 20;
        DATE_TIME_FORMAT_STR_DEFAULT = null;
        SimpleLoggerConfiguration.dateTimeFormatStr = SimpleLoggerConfiguration.DATE_TIME_FORMAT_STR_DEFAULT;
        SimpleLoggerConfiguration.LOG_FILE_DEFAULT = "System.err";
    }
}
