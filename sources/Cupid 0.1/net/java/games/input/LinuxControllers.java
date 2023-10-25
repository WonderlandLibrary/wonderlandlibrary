package net.java.games.input;

import java.io.IOException;

final class LinuxControllers {
  private static final LinuxEvent linux_event = new LinuxEvent();
  
  public static final synchronized boolean getNextDeviceEvent(Event event, LinuxEventDevice device) throws IOException {
    while (device.getNextEvent(linux_event)) {
      LinuxAxisDescriptor descriptor = linux_event.getDescriptor();
      LinuxComponent component = device.mapDescriptor(descriptor);
      if (component != null) {
        float value = component.convertValue(linux_event.getValue(), descriptor);
        event.set(component, value, linux_event.getNanos());
        return true;
      } 
    } 
    return false;
  }
  
  private static final LinuxAbsInfo abs_info = new LinuxAbsInfo();
  
  public static final synchronized float poll(LinuxEventComponent event_component) throws IOException {
    int native_code;
    float state;
    int native_type = event_component.getDescriptor().getType();
    switch (native_type) {
      case 1:
        native_code = event_component.getDescriptor().getCode();
        state = event_component.getDevice().isKeySet(native_code) ? 1.0F : 0.0F;
        return state;
      case 3:
        event_component.getAbsInfo(abs_info);
        return abs_info.getValue();
    } 
    throw new RuntimeException("Unkown native_type: " + native_type);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxControllers.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */