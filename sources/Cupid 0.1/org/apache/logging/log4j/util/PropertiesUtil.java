package org.apache.logging.log4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public final class PropertiesUtil {
  private static final String LOG4J_PROPERTIES_FILE_NAME = "log4j2.component.properties";
  
  private static final String LOG4J_SYSTEM_PROPERTIES_FILE_NAME = "log4j2.system.properties";
  
  private static final String SYSTEM = "system:";
  
  private static final PropertiesUtil LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
  
  private final Environment environment;
  
  public PropertiesUtil(Properties props) {
    this.environment = new Environment(new PropertiesPropertySource(props));
  }
  
  public PropertiesUtil(String propertiesFileName) {
    this.environment = new Environment(new PropertyFilePropertySource(propertiesFileName));
  }
  
  static Properties loadClose(InputStream in, Object source) {
    Properties props = new Properties();
    if (null != in)
      try {
        props.load(in);
      } catch (IOException e) {
        LowLevelLogUtil.logException("Unable to read " + source, e);
      } finally {
        try {
          in.close();
        } catch (IOException e) {
          LowLevelLogUtil.logException("Unable to close " + source, e);
        } 
      }  
    return props;
  }
  
  public static PropertiesUtil getProperties() {
    return LOG4J_PROPERTIES;
  }
  
  public boolean hasProperty(String name) {
    return this.environment.containsKey(name);
  }
  
  public boolean getBooleanProperty(String name) {
    return getBooleanProperty(name, false);
  }
  
  public boolean getBooleanProperty(String name, boolean defaultValue) {
    String prop = getStringProperty(name);
    return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
  }
  
  public boolean getBooleanProperty(String name, boolean defaultValueIfAbsent, boolean defaultValueIfPresent) {
    String prop = getStringProperty(name);
    return (prop == null) ? defaultValueIfAbsent : (
      prop.isEmpty() ? defaultValueIfPresent : "true".equalsIgnoreCase(prop));
  }
  
  public Boolean getBooleanProperty(String[] prefixes, String key, Supplier<Boolean> supplier) {
    for (String prefix : prefixes) {
      if (hasProperty(prefix + key))
        return Boolean.valueOf(getBooleanProperty(prefix + key)); 
    } 
    return (supplier != null) ? supplier.get() : null;
  }
  
  public Charset getCharsetProperty(String name) {
    return getCharsetProperty(name, Charset.defaultCharset());
  }
  
  public Charset getCharsetProperty(String name, Charset defaultValue) {
    String charsetName = getStringProperty(name);
    if (charsetName == null)
      return defaultValue; 
    if (Charset.isSupported(charsetName))
      return Charset.forName(charsetName); 
    ResourceBundle bundle = getCharsetsResourceBundle();
    if (bundle.containsKey(name)) {
      String mapped = bundle.getString(name);
      if (Charset.isSupported(mapped))
        return Charset.forName(mapped); 
    } 
    LowLevelLogUtil.log("Unable to get Charset '" + charsetName + "' for property '" + name + "', using default " + defaultValue + " and continuing.");
    return defaultValue;
  }
  
  public double getDoubleProperty(String name, double defaultValue) {
    String prop = getStringProperty(name);
    if (prop != null)
      try {
        return Double.parseDouble(prop);
      } catch (Exception exception) {} 
    return defaultValue;
  }
  
  public int getIntegerProperty(String name, int defaultValue) {
    String prop = getStringProperty(name);
    if (prop != null)
      try {
        return Integer.parseInt(prop);
      } catch (Exception exception) {} 
    return defaultValue;
  }
  
  public Integer getIntegerProperty(String[] prefixes, String key, Supplier<Integer> supplier) {
    for (String prefix : prefixes) {
      if (hasProperty(prefix + key))
        return Integer.valueOf(getIntegerProperty(prefix + key, 0)); 
    } 
    return (supplier != null) ? supplier.get() : null;
  }
  
  public long getLongProperty(String name, long defaultValue) {
    String prop = getStringProperty(name);
    if (prop != null)
      try {
        return Long.parseLong(prop);
      } catch (Exception exception) {} 
    return defaultValue;
  }
  
  public Long getLongProperty(String[] prefixes, String key, Supplier<Long> supplier) {
    for (String prefix : prefixes) {
      if (hasProperty(prefix + key))
        return Long.valueOf(getLongProperty(prefix + key, 0L)); 
    } 
    return (supplier != null) ? supplier.get() : null;
  }
  
  public Duration getDurationProperty(String name, Duration defaultValue) {
    String prop = getStringProperty(name);
    if (prop != null)
      return TimeUnit.getDuration(prop); 
    return defaultValue;
  }
  
  public Duration getDurationProperty(String[] prefixes, String key, Supplier<Duration> supplier) {
    for (String prefix : prefixes) {
      if (hasProperty(prefix + key))
        return getDurationProperty(prefix + key, null); 
    } 
    return (supplier != null) ? supplier.get() : null;
  }
  
  public String getStringProperty(String[] prefixes, String key, Supplier<String> supplier) {
    for (String prefix : prefixes) {
      String result = getStringProperty(prefix + key);
      if (result != null)
        return result; 
    } 
    return (supplier != null) ? supplier.get() : null;
  }
  
  public String getStringProperty(String name) {
    return this.environment.get(name);
  }
  
  public String getStringProperty(String name, String defaultValue) {
    String prop = getStringProperty(name);
    return (prop == null) ? defaultValue : prop;
  }
  
  public static Properties getSystemProperties() {
    try {
      return new Properties(System.getProperties());
    } catch (SecurityException ex) {
      LowLevelLogUtil.logException("Unable to access system properties.", ex);
      return new Properties();
    } 
  }
  
  public void reload() {
    this.environment.reload();
  }
  
  private static class Environment {
    private final Set<PropertySource> sources = new TreeSet<>(new PropertySource.Comparator());
    
    private final Map<CharSequence, String> literal = new ConcurrentHashMap<>();
    
    private final Map<CharSequence, String> normalized = new ConcurrentHashMap<>();
    
    private final Map<List<CharSequence>, String> tokenized = new ConcurrentHashMap<>();
    
    private Environment(PropertySource propertySource) {
      PropertyFilePropertySource sysProps = new PropertyFilePropertySource("log4j2.system.properties");
      try {
        sysProps.forEach((key, value) -> {
              if (System.getProperty(key) == null)
                System.setProperty(key, value); 
            });
      } catch (SecurityException securityException) {}
      this.sources.add(propertySource);
      for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
        try {
          for (PropertySource source : ServiceLoader.<PropertySource>load(PropertySource.class, classLoader))
            this.sources.add(source); 
        } catch (Throwable throwable) {}
      } 
      reload();
    }
    
    private synchronized void reload() {
      this.literal.clear();
      this.normalized.clear();
      this.tokenized.clear();
      for (Iterator<PropertySource> iterator = this.sources.iterator(); iterator.hasNext(); ) {
        PropertySource source = iterator.next();
        source.forEach((key, value) -> {
              if (key != null && value != null) {
                this.literal.put(key, value);
                List<CharSequence> tokens = PropertySource.Util.tokenize(key);
                if (tokens.isEmpty()) {
                  this.normalized.put(source.getNormalForm(Collections.singleton(key)), value);
                } else {
                  this.normalized.put(source.getNormalForm(tokens), value);
                  this.tokenized.put(tokens, value);
                } 
              } 
            });
      } 
    }
    
    private static boolean hasSystemProperty(String key) {
      try {
        return System.getProperties().containsKey(key);
      } catch (SecurityException ignored) {
        return false;
      } 
    }
    
    private String get(String key) {
      if (this.normalized.containsKey(key))
        return this.normalized.get(key); 
      if (this.literal.containsKey(key))
        return this.literal.get(key); 
      if (hasSystemProperty(key))
        return System.getProperty(key); 
      for (PropertySource source : this.sources) {
        if (source.containsProperty(key))
          return source.getProperty(key); 
      } 
      return this.tokenized.get(PropertySource.Util.tokenize(key));
    }
    
    private boolean containsKey(String key) {
      return (this.normalized.containsKey(key) || this.literal
        .containsKey(key) || 
        hasSystemProperty(key) || this.tokenized
        .containsKey(PropertySource.Util.tokenize(key)));
    }
  }
  
  public static Properties extractSubset(Properties properties, String prefix) {
    Properties subset = new Properties();
    if (prefix == null || prefix.length() == 0)
      return subset; 
    String prefixToMatch = (prefix.charAt(prefix.length() - 1) != '.') ? (prefix + '.') : prefix;
    List<String> keys = new ArrayList<>();
    for (String key : properties.stringPropertyNames()) {
      if (key.startsWith(prefixToMatch)) {
        subset.setProperty(key.substring(prefixToMatch.length()), properties.getProperty(key));
        keys.add(key);
      } 
    } 
    for (String key : keys)
      properties.remove(key); 
    return subset;
  }
  
  static ResourceBundle getCharsetsResourceBundle() {
    return ResourceBundle.getBundle("Log4j-charsets");
  }
  
  public static Map<String, Properties> partitionOnCommonPrefixes(Properties properties) {
    Map<String, Properties> parts = new ConcurrentHashMap<>();
    for (String key : properties.stringPropertyNames()) {
      String prefix = key.substring(0, key.indexOf('.'));
      if (!parts.containsKey(prefix))
        parts.put(prefix, new Properties()); 
      ((Properties)parts.get(prefix)).setProperty(key.substring(key.indexOf('.') + 1), properties.getProperty(key));
    } 
    return parts;
  }
  
  public boolean isOsWindows() {
    return getStringProperty("os.name", "").startsWith("Windows");
  }
  
  private enum TimeUnit {
    NANOS("ns,nano,nanos,nanosecond,nanoseconds", ChronoUnit.NANOS),
    MICROS("us,micro,micros,microsecond,microseconds", ChronoUnit.MICROS),
    MILLIS("ms,milli,millis,millsecond,milliseconds", ChronoUnit.MILLIS),
    SECONDS("s,second,seconds", ChronoUnit.SECONDS),
    MINUTES("m,minute,minutes", ChronoUnit.MINUTES),
    HOURS("h,hour,hours", ChronoUnit.HOURS),
    DAYS("d,day,days", ChronoUnit.DAYS);
    
    private final String[] descriptions;
    
    private final ChronoUnit timeUnit;
    
    TimeUnit(String descriptions, ChronoUnit timeUnit) {
      this.descriptions = descriptions.split(",");
      this.timeUnit = timeUnit;
    }
    
    ChronoUnit getTimeUnit() {
      return this.timeUnit;
    }
    
    static Duration getDuration(String time) {
      String value = time.trim();
      TemporalUnit temporalUnit = ChronoUnit.MILLIS;
      long timeVal = 0L;
      for (TimeUnit timeUnit : values()) {
        for (String suffix : timeUnit.descriptions) {
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\PropertiesUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */