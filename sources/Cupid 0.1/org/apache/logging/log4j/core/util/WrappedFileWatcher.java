package org.apache.logging.log4j.core.util;

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;

public class WrappedFileWatcher extends AbstractWatcher implements FileWatcher {
  private final FileWatcher watcher;
  
  private volatile long lastModifiedMillis;
  
  public WrappedFileWatcher(FileWatcher watcher, Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
    super(configuration, reconfigurable, configurationListeners);
    this.watcher = watcher;
    this.lastModifiedMillis = lastModifiedMillis;
  }
  
  public WrappedFileWatcher(FileWatcher watcher) {
    super(null, null, null);
    this.watcher = watcher;
  }
  
  public long getLastModified() {
    return this.lastModifiedMillis;
  }
  
  public void fileModified(File file) {
    this.watcher.fileModified(file);
  }
  
  public boolean isModified() {
    long lastModified = getSource().getFile().lastModified();
    if (this.lastModifiedMillis != lastModified) {
      this.lastModifiedMillis = lastModified;
      return true;
    } 
    return false;
  }
  
  public List<ConfigurationListener> getListeners() {
    if (super.getListeners() != null)
      return Collections.unmodifiableList(super.getListeners()); 
    return null;
  }
  
  public void modified() {
    if (getListeners() != null)
      super.modified(); 
    fileModified(getSource().getFile());
    this.lastModifiedMillis = getSource().getFile().lastModified();
  }
  
  public void watching(Source source) {
    this.lastModifiedMillis = source.getFile().lastModified();
    super.watching(source);
  }
  
  public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
    WrappedFileWatcher watcher = new WrappedFileWatcher(this.watcher, getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
    if (getSource() != null)
      watcher.watching(getSource()); 
    return watcher;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\WrappedFileWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */