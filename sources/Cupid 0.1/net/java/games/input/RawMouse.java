package net.java.games.input;

import java.io.IOException;

final class RawMouse extends Mouse {
  private static final int EVENT_DONE = 1;
  
  private static final int EVENT_X = 2;
  
  private static final int EVENT_Y = 3;
  
  private static final int EVENT_Z = 4;
  
  private static final int EVENT_BUTTON_0 = 5;
  
  private static final int EVENT_BUTTON_1 = 6;
  
  private static final int EVENT_BUTTON_2 = 7;
  
  private static final int EVENT_BUTTON_3 = 8;
  
  private static final int EVENT_BUTTON_4 = 9;
  
  private final RawDevice device;
  
  private final RawMouseEvent current_event = new RawMouseEvent();
  
  private int event_state = 1;
  
  protected RawMouse(String name, RawDevice device, Component[] components, Controller[] children, Rumbler[] rumblers) throws IOException {
    super(name, components, children, rumblers);
    this.device = device;
  }
  
  public final void pollDevice() throws IOException {
    this.device.pollMouse();
  }
  
  private static final boolean makeButtonEvent(RawMouseEvent mouse_event, Event event, Component button_component, int down_flag, int up_flag) {
    if ((mouse_event.getButtonFlags() & down_flag) != 0) {
      event.set(button_component, 1.0F, mouse_event.getNanos());
      return true;
    } 
    if ((mouse_event.getButtonFlags() & up_flag) != 0) {
      event.set(button_component, 0.0F, mouse_event.getNanos());
      return true;
    } 
    return false;
  }
  
  protected final synchronized boolean getNextDeviceEvent(Event event) throws IOException {
    while (true) {
      int rel_x;
      int rel_y;
      int wheel;
      switch (this.event_state) {
        case 1:
          if (!this.device.getNextMouseEvent(this.current_event))
            return false; 
          this.event_state = 2;
          continue;
        case 2:
          rel_x = this.device.getEventRelativeX();
          this.event_state = 3;
          if (rel_x != 0) {
            event.set(getX(), rel_x, this.current_event.getNanos());
            return true;
          } 
          continue;
        case 3:
          rel_y = this.device.getEventRelativeY();
          this.event_state = 4;
          if (rel_y != 0) {
            event.set(getY(), rel_y, this.current_event.getNanos());
            return true;
          } 
          continue;
        case 4:
          wheel = this.current_event.getWheelDelta();
          this.event_state = 5;
          if (wheel != 0) {
            event.set(getWheel(), wheel, this.current_event.getNanos());
            return true;
          } 
          continue;
        case 5:
          this.event_state = 6;
          if (makeButtonEvent(this.current_event, event, getPrimaryButton(), 1, 2))
            return true; 
          continue;
        case 6:
          this.event_state = 7;
          if (makeButtonEvent(this.current_event, event, getSecondaryButton(), 4, 8))
            return true; 
          continue;
        case 7:
          this.event_state = 8;
          if (makeButtonEvent(this.current_event, event, getTertiaryButton(), 16, 32))
            return true; 
          continue;
        case 8:
          this.event_state = 9;
          if (makeButtonEvent(this.current_event, event, getButton3(), 64, 128))
            return true; 
          continue;
        case 9:
          this.event_state = 1;
          if (makeButtonEvent(this.current_event, event, getButton4(), 256, 512))
            return true; 
          continue;
      } 
      break;
    } 
    throw new RuntimeException("Unknown event state: " + this.event_state);
  }
  
  protected final void setDeviceEventQueueSize(int size) throws IOException {
    this.device.setBufferSize(size);
  }
  
  static final class Axis extends AbstractComponent {
    private final RawDevice device;
    
    public Axis(RawDevice device, Component.Identifier.Axis axis) {
      super(axis.getName(), axis);
      this.device = device;
    }
    
    public final boolean isRelative() {
      return true;
    }
    
    public final boolean isAnalog() {
      return true;
    }
    
    protected final float poll() throws IOException {
      if (getIdentifier() == Component.Identifier.Axis.X)
        return this.device.getRelativeX(); 
      if (getIdentifier() == Component.Identifier.Axis.Y)
        return this.device.getRelativeY(); 
      if (getIdentifier() == Component.Identifier.Axis.Z)
        return this.device.getWheel(); 
      throw new RuntimeException("Unknown raw axis: " + getIdentifier());
    }
  }
  
  static final class Button extends AbstractComponent {
    private final RawDevice device;
    
    private final int button_id;
    
    public Button(RawDevice device, Component.Identifier.Button id, int button_id) {
      super(id.getName(), id);
      this.device = device;
      this.button_id = button_id;
    }
    
    protected final float poll() throws IOException {
      return this.device.getButtonState(this.button_id) ? 1.0F : 0.0F;
    }
    
    public final boolean isAnalog() {
      return false;
    }
    
    public final boolean isRelative() {
      return false;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\RawMouse.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */