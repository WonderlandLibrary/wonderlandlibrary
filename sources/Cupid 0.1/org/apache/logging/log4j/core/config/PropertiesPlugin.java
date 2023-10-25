package org.apache.logging.log4j.core.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name = "properties", category = "Core", printObject = true)
public final class PropertiesPlugin {
  @PluginFactory
  public static StrLookup configureSubstitutor(@PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config) {
    if (properties == null)
      return (StrLookup)new Interpolator(config.getProperties()); 
    Map<String, String> map = new HashMap<>(config.getProperties());
    for (Property prop : properties)
      map.put(prop.getName(), prop.getValue()); 
    return (StrLookup)new Interpolator((StrLookup)new MapLookup(map), config.getPluginPackages());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\PropertiesPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */