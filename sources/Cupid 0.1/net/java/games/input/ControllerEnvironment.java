package net.java.games.input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public abstract class ControllerEnvironment {
  static void logln(String msg) {
    log(msg + "\n");
  }
  
  static void log(String msg) {
    Logger.getLogger(ControllerEnvironment.class.getName()).info(msg);
  }
  
  private static ControllerEnvironment defaultEnvironment = new DefaultControllerEnvironment();
  
  protected final ArrayList controllerListeners = new ArrayList();
  
  static final boolean $assertionsDisabled;
  
  public void addControllerListener(ControllerListener l) {
    assert l != null;
    this.controllerListeners.add(l);
  }
  
  public void removeControllerListener(ControllerListener l) {
    assert l != null;
    this.controllerListeners.remove(l);
  }
  
  protected void fireControllerAdded(Controller c) {
    ControllerEvent ev = new ControllerEvent(c);
    Iterator it = this.controllerListeners.iterator();
    while (it.hasNext())
      ((ControllerListener)it.next()).controllerAdded(ev); 
  }
  
  protected void fireControllerRemoved(Controller c) {
    ControllerEvent ev = new ControllerEvent(c);
    Iterator it = this.controllerListeners.iterator();
    while (it.hasNext())
      ((ControllerListener)it.next()).controllerRemoved(ev); 
  }
  
  public static ControllerEnvironment getDefaultEnvironment() {
    return defaultEnvironment;
  }
  
  public abstract Controller[] getControllers();
  
  public abstract boolean isSupported();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\ControllerEnvironment.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */