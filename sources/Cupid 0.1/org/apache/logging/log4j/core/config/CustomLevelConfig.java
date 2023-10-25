package org.apache.logging.log4j.core.config;

import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "CustomLevel", category = "Core", printObject = true)
public final class CustomLevelConfig {
  static final CustomLevelConfig[] EMPTY_ARRAY = new CustomLevelConfig[0];
  
  private final String levelName;
  
  private final int intLevel;
  
  private CustomLevelConfig(String levelName, int intLevel) {
    this.levelName = Objects.<String>requireNonNull(levelName, "levelName is null");
    this.intLevel = intLevel;
  }
  
  @PluginFactory
  public static CustomLevelConfig createLevel(@PluginAttribute("name") String levelName, @PluginAttribute("intLevel") int intLevel) {
    StatusLogger.getLogger().debug("Creating CustomLevel(name='{}', intValue={})", levelName, Integer.valueOf(intLevel));
    Level.forName(levelName, intLevel);
    return new CustomLevelConfig(levelName, intLevel);
  }
  
  public String getLevelName() {
    return this.levelName;
  }
  
  public int getIntLevel() {
    return this.intLevel;
  }
  
  public int hashCode() {
    return this.intLevel ^ this.levelName.hashCode();
  }
  
  public boolean equals(Object object) {
    if (this == object)
      return true; 
    if (!(object instanceof CustomLevelConfig))
      return false; 
    CustomLevelConfig other = (CustomLevelConfig)object;
    return (this.intLevel == other.intLevel && this.levelName.equals(other.levelName));
  }
  
  public String toString() {
    return "CustomLevel[name=" + this.levelName + ", intLevel=" + this.intLevel + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\CustomLevelConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */