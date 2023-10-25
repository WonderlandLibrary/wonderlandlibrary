package net.java.games.input;

import net.java.games.util.plugins.Plugin;

public class AWTEnvironmentPlugin extends ControllerEnvironment implements Plugin {
  private final Controller[] controllers = new Controller[] { new AWTKeyboard(), new AWTMouse() };
  
  public Controller[] getControllers() {
    return this.controllers;
  }
  
  public boolean isSupported() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\AWTEnvironmentPlugin.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */