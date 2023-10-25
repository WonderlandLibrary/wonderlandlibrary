package net.java.games.input;

import java.util.ArrayList;
import java.util.List;

public class DirectAndRawInputEnvironmentPlugin extends ControllerEnvironment {
  private RawInputEnvironmentPlugin rawPlugin;
  
  private DirectInputEnvironmentPlugin dinputPlugin;
  
  private Controller[] controllers = null;
  
  public DirectAndRawInputEnvironmentPlugin() {
    this.dinputPlugin = new DirectInputEnvironmentPlugin();
    this.rawPlugin = new RawInputEnvironmentPlugin();
  }
  
  public Controller[] getControllers() {
    if (this.controllers == null) {
      boolean rawKeyboardFound = false;
      boolean rawMouseFound = false;
      List tempControllers = new ArrayList();
      Controller[] dinputControllers = this.dinputPlugin.getControllers();
      Controller[] rawControllers = this.rawPlugin.getControllers();
      int i;
      for (i = 0; i < rawControllers.length; i++) {
        tempControllers.add(rawControllers[i]);
        if (rawControllers[i].getType() == Controller.Type.KEYBOARD) {
          rawKeyboardFound = true;
        } else if (rawControllers[i].getType() == Controller.Type.MOUSE) {
          rawMouseFound = true;
        } 
      } 
      for (i = 0; i < dinputControllers.length; i++) {
        if (dinputControllers[i].getType() == Controller.Type.KEYBOARD) {
          if (!rawKeyboardFound)
            tempControllers.add(dinputControllers[i]); 
        } else if (dinputControllers[i].getType() == Controller.Type.MOUSE) {
          if (!rawMouseFound)
            tempControllers.add(dinputControllers[i]); 
        } else {
          tempControllers.add(dinputControllers[i]);
        } 
      } 
      this.controllers = tempControllers.<Controller>toArray(new Controller[0]);
    } 
    return this.controllers;
  }
  
  public boolean isSupported() {
    return (this.rawPlugin.isSupported() || this.dinputPlugin.isSupported());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\DirectAndRawInputEnvironmentPlugin.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */