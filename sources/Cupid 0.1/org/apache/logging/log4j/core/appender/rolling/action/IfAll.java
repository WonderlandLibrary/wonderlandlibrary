package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "IfAll", category = "Core", printObject = true)
public final class IfAll implements PathCondition {
  private final PathCondition[] components;
  
  private IfAll(PathCondition... filters) {
    this.components = Objects.<PathCondition[]>requireNonNull(filters, "filters");
  }
  
  public PathCondition[] getDeleteFilters() {
    return this.components;
  }
  
  public boolean accept(Path baseDir, Path relativePath, BasicFileAttributes attrs) {
    if (this.components == null || this.components.length == 0)
      return false; 
    return accept(this.components, baseDir, relativePath, attrs);
  }
  
  public static boolean accept(PathCondition[] list, Path baseDir, Path relativePath, BasicFileAttributes attrs) {
    for (PathCondition component : list) {
      if (!component.accept(baseDir, relativePath, attrs))
        return false; 
    } 
    return true;
  }
  
  public void beforeFileTreeWalk() {
    beforeFileTreeWalk(this.components);
  }
  
  public static void beforeFileTreeWalk(PathCondition[] nestedConditions) {
    for (PathCondition condition : nestedConditions)
      condition.beforeFileTreeWalk(); 
  }
  
  @PluginFactory
  public static IfAll createAndCondition(@PluginElement("PathConditions") PathCondition... components) {
    return new IfAll(components);
  }
  
  public String toString() {
    return "IfAll" + Arrays.toString((Object[])this.components);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */