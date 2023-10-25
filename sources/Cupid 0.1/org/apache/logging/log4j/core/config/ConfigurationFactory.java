package org.apache.logging.log4j.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.lookup.ConfigurationStrSubstitutor;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.core.util.BasicAuthorizationProvider;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;

public abstract class ConfigurationFactory extends ConfigurationBuilderFactory {
  public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
  
  public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
  
  public static final String LOG4J1_CONFIGURATION_FILE_PROPERTY = "log4j.configuration";
  
  public static final String LOG4J1_EXPERIMENTAL = "log4j1.compatibility";
  
  public static final String AUTHORIZATION_PROVIDER = "log4j2.authorizationProvider";
  
  public static final String CATEGORY = "ConfigurationFactory";
  
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  protected static final String TEST_PREFIX = "log4j2-test";
  
  protected static final String DEFAULT_PREFIX = "log4j2";
  
  protected static final String LOG4J1_VERSION = "1";
  
  protected static final String LOG4J2_VERSION = "2";
  
  private static final String CLASS_LOADER_SCHEME = "classloader";
  
  private static final String CLASS_PATH_SCHEME = "classpath";
  
  private static final String OVERRIDE_PARAM = "override";
  
  private static volatile List<ConfigurationFactory> factories;
  
  private static ConfigurationFactory configFactory = new Factory();
  
  protected final StrSubstitutor substitutor = (StrSubstitutor)new ConfigurationStrSubstitutor((StrLookup)new Interpolator());
  
  private static final Lock LOCK = new ReentrantLock();
  
  private static final String HTTPS = "https";
  
  private static final String HTTP = "http";
  
  private static volatile AuthorizationProvider authorizationProvider;
  
  public static ConfigurationFactory getInstance() {
    if (factories == null) {
      LOCK.lock();
      try {
        if (factories == null) {
          List<ConfigurationFactory> list = new ArrayList<>();
          PropertiesUtil props = PropertiesUtil.getProperties();
          String factoryClass = props.getStringProperty("log4j.configurationFactory");
          if (factoryClass != null)
            addFactory(list, factoryClass); 
          PluginManager manager = new PluginManager("ConfigurationFactory");
          manager.collectPlugins();
          Map<String, PluginType<?>> plugins = manager.getPlugins();
          List<Class<? extends ConfigurationFactory>> ordered = new ArrayList<>(plugins.size());
          for (PluginType<?> type : plugins.values()) {
            try {
              ordered.add(type.getPluginClass().asSubclass(ConfigurationFactory.class));
            } catch (Exception ex) {
              LOGGER.warn("Unable to add class {}", type.getPluginClass(), ex);
            } 
          } 
          Collections.sort(ordered, OrderComparator.getInstance());
          for (Class<? extends ConfigurationFactory> clazz : ordered)
            addFactory(list, clazz); 
          factories = Collections.unmodifiableList(list);
          authorizationProvider = authorizationProvider(props);
        } 
      } finally {
        LOCK.unlock();
      } 
    } 
    LOGGER.debug("Using configurationFactory {}", configFactory);
    return configFactory;
  }
  
  public static AuthorizationProvider authorizationProvider(PropertiesUtil props) {
    BasicAuthorizationProvider basicAuthorizationProvider;
    String authClass = props.getStringProperty("log4j2.authorizationProvider");
    AuthorizationProvider provider = null;
    if (authClass != null)
      try {
        Object obj = LoaderUtil.newInstanceOf(authClass);
        if (obj instanceof AuthorizationProvider) {
          provider = (AuthorizationProvider)obj;
        } else {
          LOGGER.warn("{} is not an AuthorizationProvider, using default", obj.getClass().getName());
        } 
      } catch (Exception ex) {
        LOGGER.warn("Unable to create {}, using default: {}", authClass, ex.getMessage());
      }  
    if (provider == null)
      basicAuthorizationProvider = new BasicAuthorizationProvider(props); 
    return (AuthorizationProvider)basicAuthorizationProvider;
  }
  
  public static AuthorizationProvider getAuthorizationProvider() {
    return authorizationProvider;
  }
  
  private static void addFactory(Collection<ConfigurationFactory> list, String factoryClass) {
    try {
      addFactory(list, Loader.loadClass(factoryClass).asSubclass(ConfigurationFactory.class));
    } catch (Exception ex) {
      LOGGER.error("Unable to load class {}", factoryClass, ex);
    } 
  }
  
  private static void addFactory(Collection<ConfigurationFactory> list, Class<? extends ConfigurationFactory> factoryClass) {
    try {
      list.add(ReflectionUtil.instantiate(factoryClass));
    } catch (Exception ex) {
      LOGGER.error("Unable to create instance of {}", factoryClass.getName(), ex);
    } 
  }
  
  public static void setConfigurationFactory(ConfigurationFactory factory) {
    configFactory = factory;
  }
  
  public static void resetConfigurationFactory() {
    configFactory = new Factory();
  }
  
  public static void removeConfigurationFactory(ConfigurationFactory factory) {
    if (configFactory == factory)
      configFactory = new Factory(); 
  }
  
  protected abstract String[] getSupportedTypes();
  
  protected String getTestPrefix() {
    return "log4j2-test";
  }
  
  protected String getDefaultPrefix() {
    return "log4j2";
  }
  
  protected String getVersion() {
    return "2";
  }
  
  protected boolean isActive() {
    return true;
  }
  
  public abstract Configuration getConfiguration(LoggerContext paramLoggerContext, ConfigurationSource paramConfigurationSource);
  
  public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
    if (!isActive())
      return null; 
    if (configLocation != null) {
      ConfigurationSource source = ConfigurationSource.fromUri(configLocation);
      if (source != null)
        return getConfiguration(loggerContext, source); 
    } 
    return null;
  }
  
  public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation, ClassLoader loader) {
    if (!isActive())
      return null; 
    if (loader == null)
      return getConfiguration(loggerContext, name, configLocation); 
    if (isClassLoaderUri(configLocation)) {
      String path = extractClassLoaderUriPath(configLocation);
      ConfigurationSource source = ConfigurationSource.fromResource(path, loader);
      if (source != null) {
        Configuration configuration = getConfiguration(loggerContext, source);
        if (configuration != null)
          return configuration; 
      } 
    } 
    return getConfiguration(loggerContext, name, configLocation);
  }
  
  static boolean isClassLoaderUri(URI uri) {
    if (uri == null)
      return false; 
    String scheme = uri.getScheme();
    return (scheme == null || scheme.equals("classloader") || scheme.equals("classpath"));
  }
  
  static String extractClassLoaderUriPath(URI uri) {
    return (uri.getScheme() == null) ? uri.getPath() : uri.getSchemeSpecificPart();
  }
  
  protected ConfigurationSource getInputFromString(String config, ClassLoader loader) {
    try {
      URL url = new URL(config);
      URLConnection urlConnection = UrlConnectionFactory.createConnection(url);
      File file = FileUtils.fileFromUri(url.toURI());
      if (file != null)
        return new ConfigurationSource(urlConnection.getInputStream(), FileUtils.fileFromUri(url.toURI())); 
      return new ConfigurationSource(urlConnection.getInputStream(), url, urlConnection.getLastModified());
    } catch (Exception ex) {
      ConfigurationSource source = ConfigurationSource.fromResource(config, loader);
      if (source == null)
        try {
          File file = new File(config);
          return new ConfigurationSource(new FileInputStream(file), file);
        } catch (FileNotFoundException fnfe) {
          LOGGER.catching(Level.DEBUG, fnfe);
        }  
      return source;
    } 
  }
  
  private static class Factory extends ConfigurationFactory {
    private static final String ALL_TYPES = "*";
    
    private Factory() {}
    
    public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
      if (configLocation == null) {
        String configLocationStr = this.substitutor.replace(PropertiesUtil.getProperties()
            .getStringProperty("log4j.configurationFile"));
        if (configLocationStr != null) {
          String[] sources = parseConfigLocations(configLocationStr);
          if (sources.length > 1) {
            List<AbstractConfiguration> configs = new ArrayList<>();
            for (String sourceLocation : sources) {
              Configuration configuration = getConfiguration(loggerContext, sourceLocation.trim());
              if (configuration != null) {
                if (configuration instanceof AbstractConfiguration) {
                  configs.add((AbstractConfiguration)configuration);
                } else {
                  LOGGER.error("Failed to created configuration at {}", sourceLocation);
                  return null;
                } 
              } else {
                LOGGER.warn("Unable to create configuration for {}, ignoring", sourceLocation);
              } 
            } 
            if (configs.size() > 1)
              return (Configuration)new CompositeConfiguration(configs); 
            if (configs.size() == 1)
              return configs.get(0); 
          } 
          return getConfiguration(loggerContext, configLocationStr);
        } 
        String log4j1ConfigStr = this.substitutor.replace(PropertiesUtil.getProperties()
            .getStringProperty("log4j.configuration"));
        if (log4j1ConfigStr != null) {
          System.setProperty("log4j1.compatibility", "true");
          return getConfiguration("1", loggerContext, log4j1ConfigStr);
        } 
        for (ConfigurationFactory factory : getFactories()) {
          String[] types = factory.getSupportedTypes();
          if (types != null)
            for (String type : types) {
              if (type.equals("*")) {
                Configuration configuration = factory.getConfiguration(loggerContext, name, configLocation);
                if (configuration != null)
                  return configuration; 
              } 
            }  
        } 
      } else {
        String[] sources = parseConfigLocations(configLocation);
        if (sources.length > 1) {
          List<AbstractConfiguration> configs = new ArrayList<>();
          for (String sourceLocation : sources) {
            Configuration configuration = getConfiguration(loggerContext, sourceLocation.trim());
            if (configuration instanceof AbstractConfiguration) {
              configs.add((AbstractConfiguration)configuration);
            } else {
              LOGGER.error("Failed to created configuration at {}", sourceLocation);
              return null;
            } 
          } 
          return (Configuration)new CompositeConfiguration(configs);
        } 
        String configLocationStr = configLocation.toString();
        for (ConfigurationFactory factory : getFactories()) {
          String[] types = factory.getSupportedTypes();
          if (types != null)
            for (String type : types) {
              if (type.equals("*") || configLocationStr.endsWith(type)) {
                Configuration configuration = factory.getConfiguration(loggerContext, name, configLocation);
                if (configuration != null)
                  return configuration; 
              } 
            }  
        } 
      } 
      Configuration config = getConfiguration(loggerContext, true, name);
      if (config == null) {
        config = getConfiguration(loggerContext, true, (String)null);
        if (config == null) {
          config = getConfiguration(loggerContext, false, name);
          if (config == null)
            config = getConfiguration(loggerContext, false, (String)null); 
        } 
      } 
      if (config != null)
        return config; 
      LOGGER.warn("No Log4j 2 configuration file found. Using default configuration (logging only errors to the console), or user programmatically provided configurations. Set system property 'log4j2.debug' to show Log4j 2 internal initialization logging. See https://logging.apache.org/log4j/2.x/manual/configuration.html for instructions on how to configure Log4j 2");
      return new DefaultConfiguration();
    }
    
    private Configuration getConfiguration(LoggerContext loggerContext, String configLocationStr) {
      return getConfiguration((String)null, loggerContext, configLocationStr);
    }
    
    private Configuration getConfiguration(String requiredVersion, LoggerContext loggerContext, String configLocationStr) {
      ConfigurationSource source = null;
      try {
        source = ConfigurationSource.fromUri(NetUtils.toURI(configLocationStr));
      } catch (Exception ex) {
        LOGGER.catching(Level.DEBUG, ex);
      } 
      if (source == null) {
        ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
        source = getInputFromString(configLocationStr, loader);
      } 
      if (source != null)
        for (ConfigurationFactory factory : getFactories()) {
          if (requiredVersion != null && !factory.getVersion().equals(requiredVersion))
            continue; 
          String[] types = factory.getSupportedTypes();
          if (types != null)
            for (String type : types) {
              if (type.equals("*") || configLocationStr.endsWith(type)) {
                Configuration config = factory.getConfiguration(loggerContext, source);
                if (config != null)
                  return config; 
              } 
            }  
        }  
      return null;
    }
    
    private Configuration getConfiguration(LoggerContext loggerContext, boolean isTest, String name) {
      boolean named = Strings.isNotEmpty(name);
      ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
      for (ConfigurationFactory factory : getFactories()) {
        String prefix = isTest ? factory.getTestPrefix() : factory.getDefaultPrefix();
        String[] types = factory.getSupportedTypes();
        if (types == null)
          continue; 
        for (String suffix : types) {
          if (!suffix.equals("*")) {
            String configName = named ? (prefix + name + suffix) : (prefix + suffix);
            ConfigurationSource source = ConfigurationSource.fromResource(configName, loader);
            if (source != null) {
              if (!factory.isActive())
                LOGGER.warn("Found configuration file {} for inactive ConfigurationFactory {}", configName, factory.getClass().getName()); 
              return factory.getConfiguration(loggerContext, source);
            } 
          } 
        } 
      } 
      return null;
    }
    
    public String[] getSupportedTypes() {
      return null;
    }
    
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
      if (source != null) {
        String config = source.getLocation();
        for (ConfigurationFactory factory : getFactories()) {
          String[] types = factory.getSupportedTypes();
          if (types != null)
            for (String type : types) {
              if (type.equals("*") || (config != null && config.endsWith(type))) {
                Configuration c = factory.getConfiguration(loggerContext, source);
                if (c != null) {
                  LOGGER.debug("Loaded configuration from {}", source);
                  return c;
                } 
                LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", config);
                return null;
              } 
            }  
        } 
      } 
      LOGGER.error("Cannot process configuration, input source is null");
      return null;
    }
    
    private String[] parseConfigLocations(URI configLocations) {
      String[] uris = configLocations.toString().split("\\?");
      List<String> locations = new ArrayList<>();
      if (uris.length > 1) {
        locations.add(uris[0]);
        String[] pairs = configLocations.getQuery().split("&");
        for (String pair : pairs) {
          int idx = pair.indexOf("=");
          try {
            String key = (idx > 0) ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (key.equalsIgnoreCase("override"))
              locations.add(URLDecoder.decode(pair.substring(idx + 1), "UTF-8")); 
          } catch (UnsupportedEncodingException ex) {
            LOGGER.warn("Invalid query parameter in {}", configLocations);
          } 
        } 
        return locations.<String>toArray(Strings.EMPTY_ARRAY);
      } 
      return new String[] { uris[0] };
    }
    
    private String[] parseConfigLocations(String configLocations) {
      String[] uris = configLocations.split(",");
      if (uris.length > 1)
        return uris; 
      try {
        return parseConfigLocations(new URI(configLocations));
      } catch (URISyntaxException ex) {
        LOGGER.warn("Error parsing URI {}", configLocations);
        return new String[] { configLocations };
      } 
    }
  }
  
  static List<ConfigurationFactory> getFactories() {
    return factories;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\ConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */