package org.apache.logging.log4j.core.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.core.util.Patterns;

public class JsonConfiguration extends AbstractConfiguration implements Reconfigurable {
  private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
  
  private final List<Status> status = new ArrayList<>();
  
  private JsonNode root;
  
  public JsonConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
    super(loggerContext, configSource);
    File configFile = configSource.getFile();
    try {
      byte[] buffer;
      try (InputStream configStream = configSource.getInputStream()) {
        buffer = toByteArray(configStream);
      } 
      InputStream is = new ByteArrayInputStream(buffer);
      this.root = getObjectMapper().readTree(is);
      if (this.root.size() == 1)
        for (JsonNode node : this.root)
          this.root = node;  
      processAttributes(this.rootNode, this.root);
      StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
      int monitorIntervalSeconds = 0;
      for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.rootNode.getAttributes().entrySet()) {
        String key = entry.getKey();
        String value = getConfigurationStrSubstitutor().replace(entry.getValue());
        if ("status".equalsIgnoreCase(key)) {
          statusConfig.withStatus(value);
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
        if ("verbose".equalsIgnoreCase(entry.getKey())) {
          statusConfig.withVerbosity(value);
          continue;
        } 
        if ("packages".equalsIgnoreCase(key)) {
          this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR)));
          continue;
        } 
        if ("name".equalsIgnoreCase(key)) {
          setName(value);
          continue;
        } 
        if ("monitorInterval".equalsIgnoreCase(key)) {
          monitorIntervalSeconds = Integer.parseInt(value);
          continue;
        } 
        if ("advertiser".equalsIgnoreCase(key))
          createAdvertiser(value, configSource, buffer, "application/json"); 
      } 
      initializeWatchers(this, configSource, monitorIntervalSeconds);
      statusConfig.initialize();
      if (getName() == null)
        setName(configSource.getLocation()); 
    } catch (Exception ex) {
      LOGGER.error("Error parsing " + configSource.getLocation(), ex);
    } 
  }
  
  protected ObjectMapper getObjectMapper() {
    return (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
  }
  
  public void setup() {
    Iterator<Map.Entry<String, JsonNode>> iter = this.root.fields();
    List<Node> children = this.rootNode.getChildren();
    while (iter.hasNext()) {
      Map.Entry<String, JsonNode> entry = iter.next();
      JsonNode n = entry.getValue();
      if (n.isObject()) {
        LOGGER.debug("Processing node for object {}", entry.getKey());
        children.add(constructNode(entry.getKey(), this.rootNode, n));
        continue;
      } 
      if (n.isArray())
        LOGGER.error("Arrays are not supported at the root configuration."); 
    } 
    LOGGER.debug("Completed parsing configuration");
    if (this.status.size() > 0)
      for (Status s : this.status)
        LOGGER.error("Error processing element {}: {}", s.name, s.errorType);  
  }
  
  public Configuration reconfigure() {
    try {
      ConfigurationSource source = getConfigurationSource().resetInputStream();
      if (source == null)
        return null; 
      return (Configuration)new JsonConfiguration(getLoggerContext(), source);
    } catch (IOException ex) {
      LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
      return null;
    } 
  }
  
  private Node constructNode(String name, Node parent, JsonNode jsonNode) {
    String t;
    PluginType<?> type = this.pluginManager.getPluginType(name);
    Node node = new Node(parent, name, type);
    processAttributes(node, jsonNode);
    Iterator<Map.Entry<String, JsonNode>> iter = jsonNode.fields();
    List<Node> children = node.getChildren();
    while (iter.hasNext()) {
      Map.Entry<String, JsonNode> entry = iter.next();
      JsonNode n = entry.getValue();
      if (n.isArray() || n.isObject()) {
        if (type == null)
          this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND)); 
        if (n.isArray()) {
          LOGGER.debug("Processing node for array {}", entry.getKey());
          for (int i = 0; i < n.size(); i++) {
            String pluginType = getType(n.get(i), entry.getKey());
            PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
            Node item = new Node(node, entry.getKey(), entryType);
            processAttributes(item, n.get(i));
            if (pluginType.equals(entry.getKey())) {
              LOGGER.debug("Processing {}[{}]", entry.getKey(), Integer.valueOf(i));
            } else {
              LOGGER.debug("Processing {} {}[{}]", pluginType, entry.getKey(), Integer.valueOf(i));
            } 
            Iterator<Map.Entry<String, JsonNode>> itemIter = n.get(i).fields();
            List<Node> itemChildren = item.getChildren();
            while (itemIter.hasNext()) {
              Map.Entry<String, JsonNode> itemEntry = itemIter.next();
              if (((JsonNode)itemEntry.getValue()).isObject()) {
                LOGGER.debug("Processing node for object {}", itemEntry.getKey());
                itemChildren.add(constructNode(itemEntry.getKey(), item, itemEntry.getValue()));
                continue;
              } 
              if (((JsonNode)itemEntry.getValue()).isArray()) {
                JsonNode array = itemEntry.getValue();
                String entryName = itemEntry.getKey();
                LOGGER.debug("Processing array for object {}", entryName);
                for (int j = 0; j < array.size(); j++)
                  itemChildren.add(constructNode(entryName, item, array.get(j))); 
              } 
            } 
            children.add(item);
          } 
          continue;
        } 
        LOGGER.debug("Processing node for object {}", entry.getKey());
        children.add(constructNode(entry.getKey(), node, n));
        continue;
      } 
      LOGGER.debug("Node {} is of type {}", entry.getKey(), n.getNodeType());
    } 
    if (type == null) {
      t = "null";
    } else {
      t = type.getElementName() + ':' + type.getPluginClass();
    } 
    String p = (node.getParent() == null) ? "null" : ((node.getParent().getName() == null) ? "root" : node.getParent().getName());
    LOGGER.debug("Returning {} with parent {} of type {}", node.getName(), p, t);
    return node;
  }
  
  private String getType(JsonNode node, String name) {
    Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
    while (iter.hasNext()) {
      Map.Entry<String, JsonNode> entry = iter.next();
      if (((String)entry.getKey()).equalsIgnoreCase("type")) {
        JsonNode n = entry.getValue();
        if (n.isValueNode())
          return n.asText(); 
      } 
    } 
    return name;
  }
  
  private void processAttributes(Node parent, JsonNode node) {
    Map<String, String> attrs = parent.getAttributes();
    Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
    while (iter.hasNext()) {
      Map.Entry<String, JsonNode> entry = iter.next();
      if (!((String)entry.getKey()).equalsIgnoreCase("type")) {
        JsonNode n = entry.getValue();
        if (n.isValueNode())
          attrs.put(entry.getKey(), n.asText()); 
      } 
    } 
  }
  
  public String toString() {
    return getClass().getSimpleName() + "[location=" + getConfigurationSource() + "]";
  }
  
  private enum ErrorType {
    CLASS_NOT_FOUND;
  }
  
  private static class Status {
    private final JsonNode node;
    
    private final String name;
    
    private final JsonConfiguration.ErrorType errorType;
    
    public Status(String name, JsonNode node, JsonConfiguration.ErrorType errorType) {
      this.name = name;
      this.node = node;
      this.errorType = errorType;
    }
    
    public String toString() {
      return "Status [name=" + this.name + ", errorType=" + this.errorType + ", node=" + this.node + "]";
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\json\JsonConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */