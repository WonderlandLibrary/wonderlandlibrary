package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "CustomLevels", category = "Core", printObject = true)
public final class CustomLevels {
  private final List<CustomLevelConfig> customLevels;
  
  private CustomLevels(CustomLevelConfig[] customLevels) {
    this.customLevels = new ArrayList<>(Arrays.asList(customLevels));
  }
  
  @PluginFactory
  public static CustomLevels createCustomLevels(@PluginElement("CustomLevels") CustomLevelConfig[] customLevels) {
    return new CustomLevels((customLevels == null) ? CustomLevelConfig.EMPTY_ARRAY : customLevels);
  }
  
  public List<CustomLevelConfig> getCustomLevels() {
    return this.customLevels;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\CustomLevels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */