package org.apache.logging.log4j.core.config.plugins.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;

public class PluginManager {
  private static final CopyOnWriteArrayList<String> PACKAGES = new CopyOnWriteArrayList<>();
  
  private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private Map<String, PluginType<?>> plugins = new HashMap<>();
  
  private final String category;
  
  public PluginManager(String category) {
    this.category = category;
  }
  
  @Deprecated
  public static void main(String[] args) {
    System.err.println("ERROR: this tool is superseded by the annotation processor included in log4j-core.");
    System.err.println("If the annotation processor does not work for you, please see the manual page:");
    System.err.println("http://logging.apache.org/log4j/2.x/manual/configuration.html#ConfigurationSyntax");
    System.exit(-1);
  }
  
  public static void addPackage(String p) {
    if (Strings.isBlank(p))
      return; 
    PACKAGES.addIfAbsent(p);
  }
  
  public static void addPackages(Collection<String> packages) {
    for (String pkg : packages) {
      if (Strings.isNotBlank(pkg))
        PACKAGES.addIfAbsent(pkg); 
    } 
  }
  
  public PluginType<?> getPluginType(String name) {
    return this.plugins.get(name.toLowerCase());
  }
  
  public Map<String, PluginType<?>> getPlugins() {
    return this.plugins;
  }
  
  public void collectPlugins() {
    collectPlugins(null);
  }
  
  public void collectPlugins(List<String> packages) {
    String categoryLowerCase = this.category.toLowerCase();
    Map<String, PluginType<?>> newPlugins = new LinkedHashMap<>();
    Map<String, List<PluginType<?>>> builtInPlugins = PluginRegistry.getInstance().loadFromMainClassLoader();
    if (builtInPlugins.isEmpty())
      builtInPlugins = PluginRegistry.getInstance().loadFromPackage("org.apache.logging.log4j.core"); 
    mergeByName(newPlugins, builtInPlugins.get(categoryLowerCase));
    for (Map<String, List<PluginType<?>>> pluginsByCategory : PluginRegistry.getInstance().getPluginsByCategoryByBundleId().values())
      mergeByName(newPlugins, pluginsByCategory.get(categoryLowerCase)); 
    for (String pkg : PACKAGES)
      mergeByName(newPlugins, PluginRegistry.getInstance().loadFromPackage(pkg).get(categoryLowerCase)); 
    if (packages != null)
      for (String pkg : packages)
        mergeByName(newPlugins, PluginRegistry.getInstance().loadFromPackage(pkg).get(categoryLowerCase));  
    LOGGER.debug("PluginManager '{}' found {} plugins", this.category, Integer.valueOf(newPlugins.size()));
    this.plugins = newPlugins;
  }
  
  private static void mergeByName(Map<String, PluginType<?>> newPlugins, List<PluginType<?>> plugins) {
    if (plugins == null)
      return; 
    for (PluginType<?> pluginType : plugins) {
      String key = pluginType.getKey();
      PluginType<?> existing = newPlugins.get(key);
      if (existing == null) {
        newPlugins.put(key, pluginType);
        continue;
      } 
      if (!existing.getPluginClass().equals(pluginType.getPluginClass()))
        LOGGER.warn("Plugin [{}] is already mapped to {}, ignoring {}", key, existing
            .getPluginClass(), pluginType.getPluginClass()); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */