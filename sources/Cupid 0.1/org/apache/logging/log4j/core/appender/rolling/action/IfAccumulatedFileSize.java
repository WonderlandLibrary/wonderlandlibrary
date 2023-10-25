package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.FileSize;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "IfAccumulatedFileSize", category = "Core", printObject = true)
public final class IfAccumulatedFileSize implements PathCondition {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final long thresholdBytes;
  
  private long accumulatedSize;
  
  private final PathCondition[] nestedConditions;
  
  private IfAccumulatedFileSize(long thresholdSize, PathCondition... nestedConditions) {
    if (thresholdSize <= 0L)
      throw new IllegalArgumentException("Count must be a positive integer but was " + thresholdSize); 
    this.thresholdBytes = thresholdSize;
    this.nestedConditions = PathCondition.copy(nestedConditions);
  }
  
  public long getThresholdBytes() {
    return this.thresholdBytes;
  }
  
  public List<PathCondition> getNestedConditions() {
    return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
  }
  
  public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
    this.accumulatedSize += attrs.size();
    boolean result = (this.accumulatedSize > this.thresholdBytes);
    String match = result ? ">" : "<=";
    String accept = result ? "ACCEPTED" : "REJECTED";
    LOGGER.trace("IfAccumulatedFileSize {}: {} accumulated size '{}' {} thresholdBytes '{}'", accept, relativePath, 
        Long.valueOf(this.accumulatedSize), match, Long.valueOf(this.thresholdBytes));
    if (result)
      return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs); 
    return result;
  }
  
  public void beforeFileTreeWalk() {
    this.accumulatedSize = 0L;
    IfAll.beforeFileTreeWalk(this.nestedConditions);
  }
  
  @PluginFactory
  public static IfAccumulatedFileSize createFileSizeCondition(@PluginAttribute("exceeds") String size, @PluginElement("PathConditions") PathCondition... nestedConditions) {
    if (size == null)
      LOGGER.error("IfAccumulatedFileSize missing mandatory size threshold."); 
    long threshold = (size == null) ? Long.MAX_VALUE : FileSize.parse(size, Long.MAX_VALUE);
    return new IfAccumulatedFileSize(threshold, nestedConditions);
  }
  
  public String toString() {
    String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
    return "IfAccumulatedFileSize(exceeds=" + this.thresholdBytes + nested + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAccumulatedFileSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */