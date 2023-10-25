package org.lwjgl.input;

import java.util.ArrayList;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.LWJGLException;

public class Controllers {
  private static ArrayList<JInputController> controllers = new ArrayList<JInputController>();
  
  private static int controllerCount;
  
  private static ArrayList<ControllerEvent> events = new ArrayList<ControllerEvent>();
  
  private static ControllerEvent event;
  
  private static boolean created;
  
  public static void create() throws LWJGLException {
    if (created)
      return; 
    try {
      ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
      Controller[] found = env.getControllers();
      ArrayList<Controller> lollers = new ArrayList<Controller>();
      for (Controller c : found) {
        if (!c.getType().equals(Controller.Type.KEYBOARD) && !c.getType().equals(Controller.Type.MOUSE))
          lollers.add(c); 
      } 
      for (Controller c : lollers)
        createController(c); 
      created = true;
    } catch (Throwable e) {
      throw new LWJGLException("Failed to initialise controllers", e);
    } 
  }
  
  private static void createController(Controller c) {
    Controller[] subControllers = c.getControllers();
    if (subControllers.length == 0) {
      JInputController controller = new JInputController(controllerCount, c);
      controllers.add(controller);
      controllerCount++;
    } else {
      for (Controller sub : subControllers)
        createController(sub); 
    } 
  }
  
  public static Controller getController(int index) {
    return controllers.get(index);
  }
  
  public static int getControllerCount() {
    return controllers.size();
  }
  
  public static void poll() {
    for (int i = 0; i < controllers.size(); i++)
      getController(i).poll(); 
  }
  
  public static void clearEvents() {
    events.clear();
  }
  
  public static boolean next() {
    if (events.size() == 0) {
      event = null;
      return false;
    } 
    event = events.remove(0);
    return (event != null);
  }
  
  public static boolean isCreated() {
    return created;
  }
  
  public static void destroy() {}
  
  public static Controller getEventSource() {
    return event.getSource();
  }
  
  public static int getEventControlIndex() {
    return event.getControlIndex();
  }
  
  public static boolean isEventButton() {
    return event.isButton();
  }
  
  public static boolean isEventAxis() {
    return event.isAxis();
  }
  
  public static boolean isEventXAxis() {
    return event.isXAxis();
  }
  
  public static boolean isEventYAxis() {
    return event.isYAxis();
  }
  
  public static boolean isEventPovX() {
    return event.isPovX();
  }
  
  public static boolean isEventPovY() {
    return event.isPovY();
  }
  
  public static long getEventNanoseconds() {
    return event.getTimeStamp();
  }
  
  public static boolean getEventButtonState() {
    return event.getButtonState();
  }
  
  public static float getEventXAxisValue() {
    return event.getXAxisValue();
  }
  
  public static float getEventYAxisValue() {
    return event.getYAxisValue();
  }
  
  static void addEvent(ControllerEvent event) {
    if (event != null)
      events.add(event); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\input\Controllers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */