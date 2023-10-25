package org.apache.logging.log4j.core.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.ConfigurationAware;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;

public class Interpolator extends AbstractConfigurationAwareLookup {
  public static final char PREFIX_SEPARATOR = ':';
  
  private static final String LOOKUP_KEY_WEB = "web";
  
  private static final String LOOKUP_KEY_DOCKER = "docker";
  
  private static final String LOOKUP_KEY_KUBERNETES = "kubernetes";
  
  private static final String LOOKUP_KEY_SPRING = "spring";
  
  private static final String LOOKUP_KEY_JNDI = "jndi";
  
  private static final String LOOKUP_KEY_JVMRUNARGS = "jvmrunargs";
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final Map<String, StrLookup> strLookupMap = new HashMap<>();
  
  private final StrLookup defaultLookup;
  
  public Interpolator(StrLookup defaultLookup) {
    this(defaultLookup, null);
  }
  
  public Interpolator(StrLookup defaultLookup, List<String> pluginPackages) {
    this.defaultLookup = (defaultLookup == null) ? new MapLookup(new HashMap<>()) : defaultLookup;
    PluginManager manager = new PluginManager("Lookup");
    manager.collectPlugins(pluginPackages);
    Map<String, PluginType<?>> plugins = manager.getPlugins();
    for (Map.Entry<String, PluginType<?>> entry : plugins.entrySet()) {
      try {
        Class<? extends StrLookup> clazz = ((PluginType)entry.getValue()).getPluginClass().asSubclass(StrLookup.class);
        if (!clazz.getName().equals("org.apache.logging.log4j.core.lookup.JndiLookup") || JndiManager.isJndiLookupEnabled())
          this.strLookupMap.put(((String)entry.getKey()).toLowerCase(), ReflectionUtil.instantiate(clazz)); 
      } catch (Throwable t) {
        handleError(entry.getKey(), t);
      } 
    } 
  }
  
  public Interpolator() {
    this((Map<String, String>)null);
  }
  
  public Interpolator(Map<String, String> properties) {
    this.defaultLookup = new MapLookup((properties == null) ? new HashMap<>() : properties);
    this.strLookupMap.put("log4j", new Log4jLookup());
    this.strLookupMap.put("sys", new SystemPropertiesLookup());
    this.strLookupMap.put("env", new EnvironmentLookup());
    this.strLookupMap.put("main", MainMapLookup.MAIN_SINGLETON);
    this.strLookupMap.put("marker", new MarkerLookup());
    this.strLookupMap.put("java", new JavaLookup());
    this.strLookupMap.put("lower", new LowerLookup());
    this.strLookupMap.put("upper", new UpperLookup());
    if (JndiManager.isJndiLookupEnabled())
      try {
        this.strLookupMap.put("jndi", 
            Loader.newCheckedInstanceOf("org.apache.logging.log4j.core.lookup.JndiLookup", StrLookup.class));
      } catch (LinkageError|Exception e) {
        handleError("jndi", e);
      }  
    try {
      this.strLookupMap.put("jvmrunargs", 
          Loader.newCheckedInstanceOf("org.apache.logging.log4j.core.lookup.JmxRuntimeInputArgumentsLookup", StrLookup.class));
    } catch (LinkageError|Exception e) {
      handleError("jvmrunargs", e);
    } 
    this.strLookupMap.put("date", new DateLookup());
    this.strLookupMap.put("ctx", new ContextMapLookup());
    if (Constants.IS_WEB_APP) {
      try {
        this.strLookupMap.put("web", 
            Loader.newCheckedInstanceOf("org.apache.logging.log4j.web.WebLookup", StrLookup.class));
      } catch (Exception ignored) {
        handleError("web", ignored);
      } 
    } else {
      LOGGER.debug("Not in a ServletContext environment, thus not loading WebLookup plugin.");
    } 
    try {
      this.strLookupMap.put("docker", 
          Loader.newCheckedInstanceOf("org.apache.logging.log4j.docker.DockerLookup", StrLookup.class));
    } catch (Exception ignored) {
      handleError("docker", ignored);
    } 
    try {
      this.strLookupMap.put("spring", 
          Loader.newCheckedInstanceOf("org.apache.logging.log4j.spring.cloud.config.client.SpringLookup", StrLookup.class));
    } catch (Exception ignored) {
      handleError("spring", ignored);
    } 
    try {
      this.strLookupMap.put("kubernetes", 
          Loader.newCheckedInstanceOf("org.apache.logging.log4j.kubernetes.KubernetesLookup", StrLookup.class));
    } catch (Exception|NoClassDefFoundError error) {
      handleError("kubernetes", error);
    } 
  }
  
  public Map<String, StrLookup> getStrLookupMap() {
    return this.strLookupMap;
  }
  
  private void handleError(String lookupKey, Throwable t) {
    switch (lookupKey) {
      case "jndi":
        LOGGER.warn("JNDI lookup class is not available because this JRE does not support JNDI. JNDI string lookups will not be available, continuing configuration. Ignoring " + t);
      case "jvmrunargs":
        LOGGER.warn("JMX runtime input lookup class is not available because this JRE does not support JMX. JMX lookups will not be available, continuing configuration. Ignoring " + t);
      case "web":
        LOGGER.info("Log4j appears to be running in a Servlet environment, but there's no log4j-web module available. If you want better web container support, please add the log4j-web JAR to your web archive or server lib directory.");
      case "docker":
      case "spring":
        return;
      case "kubernetes":
        if (t instanceof NoClassDefFoundError)
          LOGGER.warn("Unable to create Kubernetes lookup due to missing dependency: {}", t.getMessage()); 
    } 
    LOGGER.error("Unable to create Lookup for {}", lookupKey, t);
  }
  
  public String lookup(LogEvent event, String var) {
    if (var == null)
      return null; 
    int prefixPos = var.indexOf(':');
    if (prefixPos >= 0) {
      String prefix = var.substring(0, prefixPos).toLowerCase(Locale.US);
      String name = var.substring(prefixPos + 1);
      StrLookup lookup = this.strLookupMap.get(prefix);
      if (lookup instanceof ConfigurationAware)
        ((ConfigurationAware)lookup).setConfiguration(this.configuration); 
      String value = null;
      if (lookup != null)
        value = (event == null) ? lookup.lookup(name) : lookup.lookup(event, name); 
      if (value != null)
        return value; 
      var = var.substring(prefixPos + 1);
    } 
    if (this.defaultLookup != null)
      return (event == null) ? this.defaultLookup.lookup(var) : this.defaultLookup.lookup(event, var); 
    return null;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String name : this.strLookupMap.keySet()) {
      if (sb.length() == 0) {
        sb.append('{');
      } else {
        sb.append(", ");
      } 
      sb.append(name);
    } 
    if (sb.length() > 0)
      sb.append('}'); 
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\Interpolator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */