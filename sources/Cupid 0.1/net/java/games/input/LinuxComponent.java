package net.java.games.input;

import java.io.IOException;

class LinuxComponent extends AbstractComponent {
  private final LinuxEventComponent component;
  
  public LinuxComponent(LinuxEventComponent component) {
    super(component.getIdentifier().getName(), component.getIdentifier());
    this.component = component;
  }
  
  public final boolean isRelative() {
    return this.component.isRelative();
  }
  
  public final boolean isAnalog() {
    return this.component.isAnalog();
  }
  
  protected float poll() throws IOException {
    return convertValue(LinuxControllers.poll(this.component), this.component.getDescriptor());
  }
  
  float convertValue(float value, LinuxAxisDescriptor descriptor) {
    return getComponent().convertValue(value);
  }
  
  public final float getDeadZone() {
    return this.component.getDeadZone();
  }
  
  public final LinuxEventComponent getComponent() {
    return this.component;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxComponent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */