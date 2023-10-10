// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.time.temporal.TemporalUnit;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.ServiceLoader;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.time.Duration;
import java.util.ResourceBundle;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil
{
    private static final String LOG4J_PROPERTIES_FILE_NAME = "log4j2.component.properties";
    private static final String LOG4J_SYSTEM_PROPERTIES_FILE_NAME = "log4j2.system.properties";
    private static final String SYSTEM = "system:";
    private static final PropertiesUtil LOG4J_PROPERTIES;
    private final Environment environment;
    
    public PropertiesUtil(final Properties props) {
        this.environment = new Environment((PropertySource)new PropertiesPropertySource(props));
    }
    
    public PropertiesUtil(final String propertiesFileName) {
        this.environment = new Environment((PropertySource)new PropertyFilePropertySource(propertiesFileName));
    }
    
    static Properties loadClose(final InputStream in, final Object source) {
        final Properties props = new Properties();
        if (null != in) {
            try {
                props.load(in);
            }
            catch (IOException e) {
                LowLevelLogUtil.logException("Unable to read " + source, e);
                try {
                    in.close();
                }
                catch (IOException e) {
                    LowLevelLogUtil.logException("Unable to close " + source, e);
                }
            }
            finally {
                try {
                    in.close();
                }
                catch (IOException e2) {
                    LowLevelLogUtil.logException("Unable to close " + source, e2);
                }
            }
        }
        return props;
    }
    
    public static PropertiesUtil getProperties() {
        return PropertiesUtil.LOG4J_PROPERTIES;
    }
    
    public boolean hasProperty(final String name) {
        return this.environment.containsKey(name);
    }
    
    public boolean getBooleanProperty(final String name) {
        return this.getBooleanProperty(name, false);
    }
    
    public boolean getBooleanProperty(final String name, final boolean defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }
    
    public boolean getBooleanProperty(final String name, final boolean defaultValueIfAbsent, final boolean defaultValueIfPresent) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValueIfAbsent : (prop.isEmpty() ? defaultValueIfPresent : "true".equalsIgnoreCase(prop));
    }
    
    public Boolean getBooleanProperty(final String[] prefixes, final String key, final Supplier<Boolean> supplier) {
        for (final String prefix : prefixes) {
            if (this.hasProperty(prefix + key)) {
                return this.getBooleanProperty(prefix + key);
            }
        }
        return (supplier != null) ? supplier.get() : null;
    }
    
    public Charset getCharsetProperty(final String name) {
        return this.getCharsetProperty(name, Charset.defaultCharset());
    }
    
    public Charset getCharsetProperty(final String name, final Charset defaultValue) {
        final String charsetName = this.getStringProperty(name);
        if (charsetName == null) {
            return defaultValue;
        }
        if (Charset.isSupported(charsetName)) {
            return Charset.forName(charsetName);
        }
        final ResourceBundle bundle = getCharsetsResourceBundle();
        if (bundle.containsKey(name)) {
            final String mapped = bundle.getString(name);
            if (Charset.isSupported(mapped)) {
                return Charset.forName(mapped);
            }
        }
        LowLevelLogUtil.log("Unable to get Charset '" + charsetName + "' for property '" + name + "', using default " + defaultValue + " and continuing.");
        return defaultValue;
    }
    
    public double getDoubleProperty(final String name, final double defaultValue) {
        final String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Double.parseDouble(prop);
            }
            catch (Exception ex) {}
        }
        return defaultValue;
    }
    
    public int getIntegerProperty(final String name, final int defaultValue) {
        final String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Integer.parseInt(prop);
            }
            catch (Exception ex) {}
        }
        return defaultValue;
    }
    
    public Integer getIntegerProperty(final String[] prefixes, final String key, final Supplier<Integer> supplier) {
        for (final String prefix : prefixes) {
            if (this.hasProperty(prefix + key)) {
                return this.getIntegerProperty(prefix + key, 0);
            }
        }
        return (supplier != null) ? supplier.get() : null;
    }
    
    public long getLongProperty(final String name, final long defaultValue) {
        final String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Long.parseLong(prop);
            }
            catch (Exception ex) {}
        }
        return defaultValue;
    }
    
    public Long getLongProperty(final String[] prefixes, final String key, final Supplier<Long> supplier) {
        for (final String prefix : prefixes) {
            if (this.hasProperty(prefix + key)) {
                return this.getLongProperty(prefix + key, 0L);
            }
        }
        return (supplier != null) ? supplier.get() : null;
    }
    
    public Duration getDurationProperty(final String name, final Duration defaultValue) {
        final String prop = this.getStringProperty(name);
        if (prop != null) {
            return TimeUnit.getDuration(prop);
        }
        return defaultValue;
    }
    
    public Duration getDurationProperty(final String[] prefixes, final String key, final Supplier<Duration> supplier) {
        for (final String prefix : prefixes) {
            if (this.hasProperty(prefix + key)) {
                return this.getDurationProperty(prefix + key, null);
            }
        }
        return (supplier != null) ? supplier.get() : null;
    }
    
    public String getStringProperty(final String[] prefixes, final String key, final Supplier<String> supplier) {
        for (final String prefix : prefixes) {
            final String result = this.getStringProperty(prefix + key);
            if (result != null) {
                return result;
            }
        }
        return (supplier != null) ? supplier.get() : null;
    }
    
    public String getStringProperty(final String name) {
        return this.environment.get(name);
    }
    
    public String getStringProperty(final String name, final String defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }
    
    public static Properties getSystemProperties() {
        try {
            return new Properties(System.getProperties());
        }
        catch (SecurityException ex) {
            LowLevelLogUtil.logException("Unable to access system properties.", ex);
            return new Properties();
        }
    }
    
    public void reload() {
        this.environment.reload();
    }
    
    public static Properties extractSubset(final Properties properties, final String prefix) {
        final Properties subset = new Properties();
        if (prefix == null || prefix.length() == 0) {
            return subset;
        }
        final String prefixToMatch = (prefix.charAt(prefix.length() - 1) != '.') ? (prefix + '.') : prefix;
        final List<String> keys = new ArrayList<String>();
        for (final String key : properties.stringPropertyNames()) {
            if (key.startsWith(prefixToMatch)) {
                subset.setProperty(key.substring(prefixToMatch.length()), properties.getProperty(key));
                keys.add(key);
            }
        }
        for (final String key : keys) {
            properties.remove(key);
        }
        return subset;
    }
    
    static ResourceBundle getCharsetsResourceBundle() {
        return ResourceBundle.getBundle("Log4j-charsets");
    }
    
    public static Map<String, Properties> partitionOnCommonPrefixes(final Properties properties) {
        final Map<String, Properties> parts = new ConcurrentHashMap<String, Properties>();
        for (final String key : properties.stringPropertyNames()) {
            final String prefix = key.substring(0, key.indexOf(46));
            if (!parts.containsKey(prefix)) {
                parts.put(prefix, new Properties());
            }
            parts.get(prefix).setProperty(key.substring(key.indexOf(46) + 1), properties.getProperty(key));
        }
        return parts;
    }
    
    public boolean isOsWindows() {
        return this.getStringProperty("os.name", "").startsWith("Windows");
    }
    
    static {
        LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
    }
    
    private static class Environment
    {
        private final Set<PropertySource> sources;
        private final Map<CharSequence, String> literal;
        private final Map<CharSequence, String> normalized;
        private final Map<List<CharSequence>, String> tokenized;
        
        private Environment(final PropertySource propertySource) {
            this.sources = new TreeSet<PropertySource>(new PropertySource.Comparator());
            this.literal = new ConcurrentHashMap<CharSequence, String>();
            this.normalized = new ConcurrentHashMap<CharSequence, String>();
            this.tokenized = new ConcurrentHashMap<List<CharSequence>, String>();
            final PropertyFilePropertySource sysProps = new PropertyFilePropertySource("log4j2.system.properties");
            try {
                sysProps.forEach((key, value) -> {
                    if (System.getProperty(key) == null) {
                        System.setProperty(key, value);
                    }
                    return;
                });
            }
            catch (SecurityException ex) {}
            this.sources.add(propertySource);
            for (final ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
                try {
                    for (final PropertySource source : ServiceLoader.load(PropertySource.class, classLoader)) {
                        this.sources.add(source);
                    }
                }
                catch (Throwable t) {}
            }
            this.reload();
        }
        
        private synchronized void reload() {
            this.literal.clear();
            this.normalized.clear();
            this.tokenized.clear();
            for (final PropertySource source : this.sources) {
                List<CharSequence> tokens;
                final PropertySource propertySource;
                source.forEach((key, value) -> {
                    if (key != null && value != null) {
                        this.literal.put(key, value);
                        tokens = PropertySource.Util.tokenize(key);
                        if (tokens.isEmpty()) {
                            this.normalized.put(propertySource.getNormalForm(Collections.singleton(key)), value);
                        }
                        else {
                            this.normalized.put(propertySource.getNormalForm(tokens), value);
                            this.tokenized.put(tokens, value);
                        }
                    }
                });
            }
        }
        
        private static boolean hasSystemProperty(final String key) {
            try {
                return System.getProperties().containsKey(key);
            }
            catch (SecurityException ignored) {
                return false;
            }
        }
        
        private String get(final String key) {
            if (this.normalized.containsKey(key)) {
                return this.normalized.get(key);
            }
            if (this.literal.containsKey(key)) {
                return this.literal.get(key);
            }
            if (hasSystemProperty(key)) {
                return System.getProperty(key);
            }
            for (final PropertySource source : this.sources) {
                if (source.containsProperty(key)) {
                    return source.getProperty(key);
                }
            }
            return this.tokenized.get(PropertySource.Util.tokenize(key));
        }
        
        private boolean containsKey(final String key) {
            return this.normalized.containsKey(key) || this.literal.containsKey(key) || hasSystemProperty(key) || this.tokenized.containsKey(PropertySource.Util.tokenize(key));
        }
    }
    
    private enum TimeUnit
    {
        NANOS("ns,nano,nanos,nanosecond,nanoseconds", ChronoUnit.NANOS), 
        MICROS("us,micro,micros,microsecond,microseconds", ChronoUnit.MICROS), 
        MILLIS("ms,milli,millis,millsecond,milliseconds", ChronoUnit.MILLIS), 
        SECONDS("s,second,seconds", ChronoUnit.SECONDS), 
        MINUTES("m,minute,minutes", ChronoUnit.MINUTES), 
        HOURS("h,hour,hours", ChronoUnit.HOURS), 
        DAYS("d,day,days", ChronoUnit.DAYS);
        
        private final String[] descriptions;
        private final ChronoUnit timeUnit;
        
        private TimeUnit(final String descriptions, final ChronoUnit timeUnit) {
            this.descriptions = descriptions.split(",");
            this.timeUnit = timeUnit;
        }
        
        ChronoUnit getTimeUnit() {
            return this.timeUnit;
        }
        
        static Duration getDuration(final String time) {
            final String value = time.trim();
            TemporalUnit temporalUnit = ChronoUnit.MILLIS;
            long timeVal = 0L;
            for (final TimeUnit timeUnit : values()) {
                for (final String suffix : timeUnit.descriptions) {
                    if (value.endsWith(suffix)) {
                        temporalUnit = timeUnit.timeUnit;
                        timeVal = Long.parseLong(value.substring(0, value.length() - suffix.length()));
                    }
                }
            }
            return Duration.of(timeVal, temporalUnit);
        }
    }
}
