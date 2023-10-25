package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractAction implements Action {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private boolean complete = false;
  
  private boolean interrupted = false;
  
  public abstract boolean execute() throws IOException;
  
  public synchronized void run() {
    if (!this.interrupted) {
      try {
        execute();
      } catch (RuntimeException|IOException ex) {
        reportException(ex);
      } catch (Error e) {
        reportException(new RuntimeException(e));
      } 
      this.complete = true;
      this.interrupted = true;
    } 
  }
  
  public synchronized void close() {
    this.interrupted = true;
  }
  
  public boolean isComplete() {
    return this.complete;
  }
  
  public boolean isInterrupted() {
    return this.interrupted;
  }
  
  protected void reportException(Exception ex) {
    LOGGER.warn("Exception reported by action '{}'", getClass(), ex);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\AbstractAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */