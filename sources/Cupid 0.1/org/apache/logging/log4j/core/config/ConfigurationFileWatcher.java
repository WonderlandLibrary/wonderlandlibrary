package org.apache.logging.log4j.core.config;

import java.io.File;
import java.util.List;
import org.apache.logging.log4j.core.util.AbstractWatcher;
import org.apache.logging.log4j.core.util.FileWatcher;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.Watcher;

public class ConfigurationFileWatcher extends AbstractWatcher implements FileWatcher {
  private File file;
  
  private long lastModifiedMillis;
  
  public ConfigurationFileWatcher(Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
    super(configuration, reconfigurable, configurationListeners);
    this.lastModifiedMillis = lastModifiedMillis;
  }
  
  public long getLastModified() {
    return (this.file != null) ? this.file.lastModified() : 0L;
  }
  
  public void fileModified(File file) {
    this.lastModifiedMillis = file.lastModified();
  }
  
  public void watching(Source source) {
    this.file = source.getFile();
    this.lastModifiedMillis = this.file.lastModified();
    super.watching(source);
  }
  
  public boolean isModified() {
    return (this.lastModifiedMillis != this.file.lastModified());
  }
  
  public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
    ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
    if (getSource() != null)
      watcher.watching(getSource()); 
    return (Watcher)watcher;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\ConfigurationFileWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */