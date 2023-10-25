package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "IfAny", category = "Core", printObject = true)
public final class IfAny implements PathCondition {
  private final PathCondition[] components;
  
  private IfAny(PathCondition... filters) {
    this.components = Objects.<PathCondition[]>requireNonNull(filters, "filters");
  }
  
  public PathCondition[] getDeleteFilters() {
    return this.components;
  }
  
  public boolean accept(Path baseDir, Path relativePath, BasicFileAttributes attrs) {
    for (PathCondition component : this.components) {
      if (component.accept(baseDir, relativePath, attrs))
        return true; 
    } 
    return false;
  }
  
  public void beforeFileTreeWalk() {
    for (PathCondition condition : this.components)
      condition.beforeFileTreeWalk(); 
  }
  
  @PluginFactory
  public static IfAny createOrCondition(@PluginElement("PathConditions") PathCondition... components) {
    return new IfAny(components);
  }
  
  public String toString() {
    return "IfAny" + Arrays.toString((Object[])this.components);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAny.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */