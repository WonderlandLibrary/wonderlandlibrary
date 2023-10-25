package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CompositeAction extends AbstractAction {
  private final Action[] actions;
  
  private final boolean stopOnError;
  
  public CompositeAction(List<Action> actions, boolean stopOnError) {
    this.actions = new Action[actions.size()];
    actions.toArray(this.actions);
    this.stopOnError = stopOnError;
  }
  
  public void run() {
    try {
      execute();
    } catch (IOException ex) {
      LOGGER.warn("Exception during file rollover.", ex);
    } 
  }
  
  public boolean execute() throws IOException {
    if (this.stopOnError) {
      for (Action action : this.actions) {
        if (!action.execute())
          return false; 
      } 
      return true;
    } 
    boolean status = true;
    IOException exception = null;
    for (Action action : this.actions) {
      try {
        status &= action.execute();
      } catch (IOException ex) {
        status = false;
        if (exception == null)
          exception = ex; 
      } 
    } 
    if (exception != null)
      throw exception; 
    return status;
  }
  
  public String toString() {
    return CompositeAction.class.getSimpleName() + Arrays.toString((Object[])this.actions);
  }
  
  public Action[] getActions() {
    return this.actions;
  }
  
  public boolean isStopOnError() {
    return this.stopOnError;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\CompositeAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */