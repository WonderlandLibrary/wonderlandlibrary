package org.apache.logging.log4j.core.appender.rolling;

import java.util.Objects;
import org.apache.logging.log4j.core.appender.rolling.action.Action;

public final class RolloverDescriptionImpl implements RolloverDescription {
  private final String activeFileName;
  
  private final boolean append;
  
  private final Action synchronous;
  
  private final Action asynchronous;
  
  public RolloverDescriptionImpl(String activeFileName, boolean append, Action synchronous, Action asynchronous) {
    Objects.requireNonNull(activeFileName, "activeFileName");
    this.append = append;
    this.activeFileName = activeFileName;
    this.synchronous = synchronous;
    this.asynchronous = asynchronous;
  }
  
  public String getActiveFileName() {
    return this.activeFileName;
  }
  
  public boolean getAppend() {
    return this.append;
  }
  
  public Action getSynchronous() {
    return this.synchronous;
  }
  
  public Action getAsynchronous() {
    return this.asynchronous;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\RolloverDescriptionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */