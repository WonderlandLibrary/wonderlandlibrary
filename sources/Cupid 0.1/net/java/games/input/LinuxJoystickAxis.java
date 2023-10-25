package net.java.games.input;

import java.io.IOException;

class LinuxJoystickAxis extends AbstractComponent {
  private float value;
  
  private boolean analog;
  
  public LinuxJoystickAxis(Component.Identifier.Axis axis_id) {
    this(axis_id, true);
  }
  
  public LinuxJoystickAxis(Component.Identifier.Axis axis_id, boolean analog) {
    super(axis_id.getName(), axis_id);
    this.analog = analog;
  }
  
  public final boolean isRelative() {
    return false;
  }
  
  public final boolean isAnalog() {
    return this.analog;
  }
  
  final void setValue(float value) {
    this.value = value;
    resetHasPolled();
  }
  
  protected final float poll() throws IOException {
    return this.value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxJoystickAxis.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */