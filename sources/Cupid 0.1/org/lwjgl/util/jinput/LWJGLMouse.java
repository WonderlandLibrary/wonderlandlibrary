package org.lwjgl.util.jinput;

import java.io.IOException;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.Mouse;
import org.lwjgl.input.Mouse;

final class LWJGLMouse extends Mouse {
  private static final int EVENT_X = 1;
  
  private static final int EVENT_Y = 2;
  
  private static final int EVENT_WHEEL = 3;
  
  private static final int EVENT_BUTTON = 4;
  
  private static final int EVENT_DONE = 5;
  
  private int event_state = 5;
  
  LWJGLMouse() {
    super("LWJGLMouse", createComponents(), new net.java.games.input.Controller[0], new net.java.games.input.Rumbler[0]);
  }
  
  private static Component[] createComponents() {
    return new Component[] { (Component)new Axis(Component.Identifier.Axis.X), (Component)new Axis(Component.Identifier.Axis.Y), (Component)new Axis(Component.Identifier.Axis.Z), (Component)new Button(Component.Identifier.Button.LEFT), (Component)new Button(Component.Identifier.Button.MIDDLE), (Component)new Button(Component.Identifier.Button.RIGHT) };
  }
  
  public synchronized void pollDevice() throws IOException {
    if (!Mouse.isCreated())
      return; 
    Mouse.poll();
    for (int i = 0; i < 3; i++)
      setButtonState(i); 
  }
  
  private Button map(int lwjgl_button) {
    switch (lwjgl_button) {
      case 0:
        return (Button)getLeft();
      case 1:
        return (Button)getRight();
      case 2:
        return (Button)getMiddle();
    } 
    return null;
  }
  
  private void setButtonState(int lwjgl_button) {
    Button button = map(lwjgl_button);
    if (button != null)
      button.setValue(Mouse.isButtonDown(lwjgl_button) ? 1.0F : 0.0F); 
  }
  
  protected synchronized boolean getNextDeviceEvent(Event event) throws IOException {
    if (!Mouse.isCreated())
      return false; 
    while (true) {
      int dx, dy, dwheel, lwjgl_button;
      long nanos = Mouse.getEventNanoseconds();
      switch (this.event_state) {
        case 1:
          this.event_state = 2;
          dx = Mouse.getEventDX();
          if (dx != 0) {
            event.set(getX(), dx, nanos);
            return true;
          } 
        case 2:
          this.event_state = 3;
          dy = -Mouse.getEventDY();
          if (dy != 0) {
            event.set(getY(), dy, nanos);
            return true;
          } 
        case 3:
          this.event_state = 4;
          dwheel = Mouse.getEventDWheel();
          if (dwheel != 0) {
            event.set(getWheel(), dwheel, nanos);
            return true;
          } 
        case 4:
          this.event_state = 5;
          lwjgl_button = Mouse.getEventButton();
          if (lwjgl_button != -1) {
            Button button = map(lwjgl_button);
            if (button != null) {
              event.set((Component)button, Mouse.getEventButtonState() ? 1.0F : 0.0F, nanos);
              return true;
            } 
          } 
        case 5:
          if (!Mouse.next())
            return false; 
          this.event_state = 1;
      } 
    } 
  }
  
  static final class Axis extends AbstractComponent {
    Axis(Component.Identifier.Axis axis_id) {
      super(axis_id.getName(), (Component.Identifier)axis_id);
    }
    
    public boolean isRelative() {
      return true;
    }
    
    protected float poll() throws IOException {
      return 0.0F;
    }
    
    public boolean isAnalog() {
      return true;
    }
  }
  
  static final class Button extends AbstractComponent {
    private float value;
    
    Button(Component.Identifier.Button button_id) {
      super(button_id.getName(), (Component.Identifier)button_id);
    }
    
    void setValue(float value) {
      this.value = value;
    }
    
    protected float poll() throws IOException {
      return this.value;
    }
    
    public boolean isRelative() {
      return false;
    }
    
    public boolean isAnalog() {
      return false;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\jinput\LWJGLMouse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */