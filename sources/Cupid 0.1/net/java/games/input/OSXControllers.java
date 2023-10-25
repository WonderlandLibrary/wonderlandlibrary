package net.java.games.input;

import java.io.IOException;

final class OSXControllers {
  private static final OSXEvent osx_event = new OSXEvent();
  
  public static final synchronized float poll(OSXHIDElement element) throws IOException {
    element.getElementValue(osx_event);
    return element.convertValue(osx_event.getValue());
  }
  
  public static final synchronized boolean getNextDeviceEvent(Event event, OSXHIDQueue queue) throws IOException {
    if (queue.getNextEvent(osx_event)) {
      OSXComponent component = queue.mapEvent(osx_event);
      event.set(component, component.getElement().convertValue(osx_event.getValue()), osx_event.getNanos());
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\OSXControllers.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */