package net.java.games.input;

import java.io.IOException;

final class LinuxPOV extends LinuxComponent {
  private final LinuxEventComponent component_x;
  
  private final LinuxEventComponent component_y;
  
  private float last_x;
  
  private float last_y;
  
  public LinuxPOV(LinuxEventComponent component_x, LinuxEventComponent component_y) {
    super(component_x);
    this.component_x = component_x;
    this.component_y = component_y;
  }
  
  protected final float poll() throws IOException {
    this.last_x = LinuxControllers.poll(this.component_x);
    this.last_y = LinuxControllers.poll(this.component_y);
    return convertValue(0.0F, null);
  }
  
  public float convertValue(float value, LinuxAxisDescriptor descriptor) {
    if (descriptor == this.component_x.getDescriptor())
      this.last_x = value; 
    if (descriptor == this.component_y.getDescriptor())
      this.last_y = value; 
    if (this.last_x == -1.0F && this.last_y == -1.0F)
      return 0.125F; 
    if (this.last_x == -1.0F && this.last_y == 0.0F)
      return 1.0F; 
    if (this.last_x == -1.0F && this.last_y == 1.0F)
      return 0.875F; 
    if (this.last_x == 0.0F && this.last_y == -1.0F)
      return 0.25F; 
    if (this.last_x == 0.0F && this.last_y == 0.0F)
      return 0.0F; 
    if (this.last_x == 0.0F && this.last_y == 1.0F)
      return 0.75F; 
    if (this.last_x == 1.0F && this.last_y == -1.0F)
      return 0.375F; 
    if (this.last_x == 1.0F && this.last_y == 0.0F)
      return 0.5F; 
    if (this.last_x == 1.0F && this.last_y == 1.0F)
      return 0.625F; 
    LinuxEnvironmentPlugin.logln("Unknown values x = " + this.last_x + " | y = " + this.last_y);
    return 0.0F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxPOV.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */