package org.apache.logging.log4j.core.config.builder.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.core.util.Patterns;

public class BuiltConfiguration extends AbstractConfiguration {
  private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
  
  private final StatusConfiguration statusConfig;
  
  protected Component rootComponent;
  
  private Component loggersComponent;
  
  private Component appendersComponent;
  
  private Component filtersComponent;
  
  private Component propertiesComponent;
  
  private Component customLevelsComponent;
  
  private Component scriptsComponent;
  
  private String contentType = "text";
  
  public BuiltConfiguration(LoggerContext loggerContext, ConfigurationSource source, Component rootComponent) {
    super(loggerContext, source);
    this.statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
    for (Component component : rootComponent.getComponents()) {
      switch (component.getPluginType()) {
        case "Scripts":
          this.scriptsComponent = component;
        case "Loggers":
          this.loggersComponent = component;
        case "Appenders":
          this.appendersComponent = component;
        case "Filters":
          this.filtersComponent = component;
        case "Properties":
          this.propertiesComponent = component;
        case "CustomLevels":
          this.customLevelsComponent = component;
      } 
    } 
    this.rootComponent = rootComponent;
  }
  
  public void setup() {
    List<Node> children = this.rootNode.getChildren();
    if (this.propertiesComponent.getComponents().size() > 0)
      children.add(convertToNode(this.rootNode, this.propertiesComponent)); 
    if (this.scriptsComponent.getComponents().size() > 0)
      children.add(convertToNode(this.rootNode, this.scriptsComponent)); 
    if (this.customLevelsComponent.getComponents().size() > 0)
      children.add(convertToNode(this.rootNode, this.customLevelsComponent)); 
    children.add(convertToNode(this.rootNode, this.loggersComponent));
    children.add(convertToNode(this.rootNode, this.appendersComponent));
    if (this.filtersComponent.getComponents().size() > 0)
      if (this.filtersComponent.getComponents().size() == 1) {
        children.add(convertToNode(this.rootNode, this.filtersComponent.getComponents().get(0)));
      } else {
        children.add(convertToNode(this.rootNode, this.filtersComponent));
      }  
    this.rootComponent = null;
  }
  
  public String getContentType() {
    return this.contentType;
  }
  
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
  
  public void createAdvertiser(String advertiserString, ConfigurationSource configSource) {
    byte[] buffer = null;
    try {
      if (configSource != null) {
        InputStream is = configSource.getInputStream();
        if (is != null)
          buffer = toByteArray(is); 
      } 
    } catch (IOException ioe) {
      LOGGER.warn("Unable to read configuration source " + configSource.toString());
    } 
    createAdvertiser(advertiserString, configSource, buffer, this.contentType);
  }
  
  public StatusConfiguration getStatusConfiguration() {
    return this.statusConfig;
  }
  
  public void setPluginPackages(String packages) {
    this.pluginPackages.addAll(Arrays.asList(packages.split(Patterns.COMMA_SEPARATOR)));
  }
  
  public void setShutdownHook(String flag) {
    this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(flag);
  }
  
  public void setShutdownTimeoutMillis(long shutdownTimeoutMillis) {
    this.shutdownTimeoutMillis = shutdownTimeoutMillis;
  }
  
  public void setMonitorInterval(int intervalSeconds) {
    if (this instanceof Reconfigurable && intervalSeconds > 0)
      initializeWatchers((Reconfigurable)this, getConfigurationSource(), intervalSeconds); 
  }
  
  public PluginManager getPluginManager() {
    return this.pluginManager;
  }
  
  protected Node convertToNode(Node parent, Component component) {
    String name = component.getPluginType();
    PluginType<?> pluginType = this.pluginManager.getPluginType(name);
    Node node = new Node(parent, name, pluginType);
    node.getAttributes().putAll(component.getAttributes());
    node.setValue(component.getValue());
    List<Node> children = node.getChildren();
    for (Component child : component.getComponents())
      children.add(convertToNode(node, child)); 
    return node;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\BuiltConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */