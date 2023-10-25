package org.apache.logging.log4j.core.config.composite;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.util.PropertiesUtil;

public class CompositeConfiguration extends AbstractConfiguration implements Reconfigurable {
  public static final String MERGE_STRATEGY_PROPERTY = "log4j.mergeStrategy";
  
  private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
  
  private final List<? extends AbstractConfiguration> configurations;
  
  private MergeStrategy mergeStrategy;
  
  public CompositeConfiguration(List<? extends AbstractConfiguration> configurations) {
    super(((AbstractConfiguration)configurations.get(0)).getLoggerContext(), ConfigurationSource.COMPOSITE_SOURCE);
    this.rootNode = ((AbstractConfiguration)configurations.get(0)).getRootNode();
    this.configurations = configurations;
    String mergeStrategyClassName = PropertiesUtil.getProperties().getStringProperty("log4j.mergeStrategy", DefaultMergeStrategy.class
        .getName());
    try {
      this.mergeStrategy = (MergeStrategy)Loader.newInstanceOf(mergeStrategyClassName);
    } catch (ClassNotFoundException|IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|InstantiationException ex) {
      this.mergeStrategy = new DefaultMergeStrategy();
    } 
    for (AbstractConfiguration config : configurations)
      this.mergeStrategy.mergeRootProperties(this.rootNode, config); 
    StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
    for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.rootNode.getAttributes().entrySet()) {
      String key = entry.getKey();
      String value = getConfigurationStrSubstitutor().replace(entry.getValue());
      if ("status".equalsIgnoreCase(key)) {
        statusConfig.withStatus(value.toUpperCase());
        continue;
      } 
      if ("dest".equalsIgnoreCase(key)) {
        statusConfig.withDestination(value);
        continue;
      } 
      if ("shutdownHook".equalsIgnoreCase(key)) {
        this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value);
        continue;
      } 
      if ("shutdownTimeout".equalsIgnoreCase(key)) {
        this.shutdownTimeoutMillis = Long.parseLong(value);
        continue;
      } 
      if ("verbose".equalsIgnoreCase(key)) {
        statusConfig.withVerbosity(value);
        continue;
      } 
      if ("packages".equalsIgnoreCase(key)) {
        this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR)));
        continue;
      } 
      if ("name".equalsIgnoreCase(key))
        setName(value); 
    } 
    statusConfig.initialize();
  }
  
  public void setup() {
    AbstractConfiguration targetConfiguration = this.configurations.get(0);
    staffChildConfiguration(targetConfiguration);
    WatchManager watchManager = getWatchManager();
    WatchManager targetWatchManager = targetConfiguration.getWatchManager();
    if (targetWatchManager.getIntervalSeconds() > 0) {
      watchManager.setIntervalSeconds(targetWatchManager.getIntervalSeconds());
      Map<Source, Watcher> watchers = targetWatchManager.getConfigurationWatchers();
      for (Map.Entry<Source, Watcher> entry : watchers.entrySet())
        watchManager.watch(entry.getKey(), ((Watcher)entry.getValue()).newWatcher(this, this.listeners, ((Watcher)entry
              .getValue()).getLastModified())); 
    } 
    for (AbstractConfiguration sourceConfiguration : this.configurations.subList(1, this.configurations.size())) {
      staffChildConfiguration(sourceConfiguration);
      Node sourceRoot = sourceConfiguration.getRootNode();
      this.mergeStrategy.mergConfigurations(this.rootNode, sourceRoot, getPluginManager());
      if (LOGGER.isEnabled(Level.ALL)) {
        StringBuilder sb = new StringBuilder();
        printNodes("", this.rootNode, sb);
        System.out.println(sb.toString());
      } 
      int monitorInterval = sourceConfiguration.getWatchManager().getIntervalSeconds();
      if (monitorInterval > 0) {
        int currentInterval = watchManager.getIntervalSeconds();
        if (currentInterval <= 0 || monitorInterval < currentInterval)
          watchManager.setIntervalSeconds(monitorInterval); 
        WatchManager sourceWatchManager = sourceConfiguration.getWatchManager();
        Map<Source, Watcher> watchers = sourceWatchManager.getConfigurationWatchers();
        for (Map.Entry<Source, Watcher> entry : watchers.entrySet())
          watchManager.watch(entry.getKey(), ((Watcher)entry.getValue()).newWatcher(this, this.listeners, ((Watcher)entry
                .getValue()).getLastModified())); 
      } 
    } 
  }
  
  public Configuration reconfigure() {
    LOGGER.debug("Reconfiguring composite configuration");
    List<AbstractConfiguration> configs = new ArrayList<>();
    ConfigurationFactory factory = ConfigurationFactory.getInstance();
    for (AbstractConfiguration config : this.configurations) {
      Configuration configuration;
      ConfigurationSource source = config.getConfigurationSource();
      URI sourceURI = source.getURI();
      AbstractConfiguration abstractConfiguration1 = config;
      if (sourceURI == null) {
        LOGGER.warn("Unable to determine URI for configuration {}, changes to it will be ignored", config
            .getName());
      } else {
        configuration = factory.getConfiguration(getLoggerContext(), config.getName(), sourceURI);
        if (configuration == null)
          LOGGER.warn("Unable to reload configuration {}, changes to it will be ignored", config.getName()); 
      } 
      configs.add((AbstractConfiguration)configuration);
    } 
    return (Configuration)new CompositeConfiguration(configs);
  }
  
  private void staffChildConfiguration(AbstractConfiguration childConfiguration) {
    childConfiguration.setPluginManager(this.pluginManager);
    childConfiguration.setScriptManager(this.scriptManager);
    childConfiguration.setup();
  }
  
  private void printNodes(String indent, Node node, StringBuilder sb) {
    sb.append(indent).append(node.getName()).append(" type: ").append(node.getType()).append("\n");
    sb.append(indent).append(node.getAttributes().toString()).append("\n");
    for (Node child : node.getChildren())
      printNodes(indent + "  ", child, sb); 
  }
  
  public String toString() {
    return getClass().getName() + "@" + Integer.toHexString(hashCode()) + " [configurations=" + this.configurations + ", mergeStrategy=" + this.mergeStrategy + ", rootNode=" + this.rootNode + ", listeners=" + this.listeners + ", pluginPackages=" + this.pluginPackages + ", pluginManager=" + this.pluginManager + ", isShutdownHookEnabled=" + this.isShutdownHookEnabled + ", shutdownTimeoutMillis=" + this.shutdownTimeoutMillis + ", scriptManager=" + this.scriptManager + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\composite\CompositeConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */