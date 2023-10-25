package org.lwjgl.util.jinput;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.util.plugins.Plugin;

public class LWJGLEnvironmentPlugin extends ControllerEnvironment implements Plugin {
  private final Controller[] controllers = new Controller[] { (Controller)new LWJGLKeyboard(), (Controller)new LWJGLMouse() };
  
  public Controller[] getControllers() {
    return this.controllers;
  }
  
  public boolean isSupported() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\jinput\LWJGLEnvironmentPlugin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */