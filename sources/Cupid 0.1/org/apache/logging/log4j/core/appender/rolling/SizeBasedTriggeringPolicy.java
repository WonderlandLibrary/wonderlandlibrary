package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "SizeBasedTriggeringPolicy", category = "Core", printObject = true)
public class SizeBasedTriggeringPolicy extends AbstractTriggeringPolicy {
  private static final long MAX_FILE_SIZE = 10485760L;
  
  private final long maxFileSize;
  
  private RollingFileManager manager;
  
  protected SizeBasedTriggeringPolicy() {
    this.maxFileSize = 10485760L;
  }
  
  protected SizeBasedTriggeringPolicy(long maxFileSize) {
    this.maxFileSize = maxFileSize;
  }
  
  public long getMaxFileSize() {
    return this.maxFileSize;
  }
  
  public void initialize(RollingFileManager aManager) {
    this.manager = aManager;
  }
  
  public boolean isTriggeringEvent(LogEvent event) {
    boolean triggered = (this.manager.getFileSize() > this.maxFileSize);
    if (triggered)
      this.manager.getPatternProcessor().updateTime(); 
    return triggered;
  }
  
  public String toString() {
    return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ')';
  }
  
  @PluginFactory
  public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute("size") String size) {
    long maxSize = (size == null) ? 10485760L : FileSize.parse(size, 10485760L);
    return new SizeBasedTriggeringPolicy(maxSize);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\SizeBasedTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */