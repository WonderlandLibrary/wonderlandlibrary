package org.apache.logging.log4j.core.util;

import java.util.List;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;

public interface Watcher {
  public static final String CATEGORY = "Watcher";
  
  public static final String ELEMENT_TYPE = "watcher";
  
  List<ConfigurationListener> getListeners();
  
  void modified();
  
  boolean isModified();
  
  long getLastModified();
  
  void watching(Source paramSource);
  
  Source getSource();
  
  Watcher newWatcher(Reconfigurable paramReconfigurable, List<ConfigurationListener> paramList, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Watcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */