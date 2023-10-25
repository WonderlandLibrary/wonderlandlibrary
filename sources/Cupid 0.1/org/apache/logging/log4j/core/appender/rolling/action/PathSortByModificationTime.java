package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "SortByModificationTime", category = "Core", printObject = true)
public class PathSortByModificationTime implements PathSorter, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final boolean recentFirst;
  
  private final int multiplier;
  
  public PathSortByModificationTime(boolean recentFirst) {
    this.recentFirst = recentFirst;
    this.multiplier = recentFirst ? 1 : -1;
  }
  
  @PluginFactory
  public static PathSorter createSorter(@PluginAttribute(value = "recentFirst", defaultBoolean = true) boolean recentFirst) {
    return new PathSortByModificationTime(recentFirst);
  }
  
  public boolean isRecentFirst() {
    return this.recentFirst;
  }
  
  public int compare(PathWithAttributes path1, PathWithAttributes path2) {
    long lastModified1 = path1.getAttributes().lastModifiedTime().toMillis();
    long lastModified2 = path2.getAttributes().lastModifiedTime().toMillis();
    int result = Long.signum(lastModified2 - lastModified1);
    if (result == 0)
      try {
        result = path2.getPath().compareTo(path1.getPath());
      } catch (ClassCastException ex) {
        result = path2.getPath().toString().compareTo(path1.getPath().toString());
      }  
    return this.multiplier * result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\PathSortByModificationTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */