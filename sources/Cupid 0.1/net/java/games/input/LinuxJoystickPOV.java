package net.java.games.input;

public class LinuxJoystickPOV extends LinuxJoystickAxis {
  private LinuxJoystickAxis hatX;
  
  private LinuxJoystickAxis hatY;
  
  LinuxJoystickPOV(Component.Identifier.Axis id, LinuxJoystickAxis hatX, LinuxJoystickAxis hatY) {
    super(id, false);
    this.hatX = hatX;
    this.hatY = hatY;
  }
  
  protected LinuxJoystickAxis getXAxis() {
    return this.hatX;
  }
  
  protected LinuxJoystickAxis getYAxis() {
    return this.hatY;
  }
  
  protected void updateValue() {
    float last_x = this.hatX.getPollData();
    float last_y = this.hatY.getPollData();
    resetHasPolled();
    if (last_x == -1.0F && last_y == -1.0F) {
      setValue(0.125F);
    } else if (last_x == -1.0F && last_y == 0.0F) {
      setValue(1.0F);
    } else if (last_x == -1.0F && last_y == 1.0F) {
      setValue(0.875F);
    } else if (last_x == 0.0F && last_y == -1.0F) {
      setValue(0.25F);
    } else if (last_x == 0.0F && last_y == 0.0F) {
      setValue(0.0F);
    } else if (last_x == 0.0F && last_y == 1.0F) {
      setValue(0.75F);
    } else if (last_x == 1.0F && last_y == -1.0F) {
      setValue(0.375F);
    } else if (last_x == 1.0F && last_y == 0.0F) {
      setValue(0.5F);
    } else if (last_x == 1.0F && last_y == 1.0F) {
      setValue(0.625F);
    } else {
      LinuxEnvironmentPlugin.logln("Unknown values x = " + last_x + " | y = " + last_y);
      setValue(0.0F);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxJoystickPOV.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */