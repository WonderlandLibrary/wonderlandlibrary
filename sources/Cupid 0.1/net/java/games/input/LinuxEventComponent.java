package net.java.games.input;

import java.io.IOException;

final class LinuxEventComponent {
  private final LinuxEventDevice device;
  
  private final Component.Identifier identifier;
  
  private final Controller.Type button_trait;
  
  private final boolean is_relative;
  
  private final LinuxAxisDescriptor descriptor;
  
  private final int min;
  
  private final int max;
  
  private final int flat;
  
  static final boolean $assertionsDisabled;
  
  public LinuxEventComponent(LinuxEventDevice device, Component.Identifier identifier, boolean is_relative, int native_type, int native_code) throws IOException {
    this.device = device;
    this.identifier = identifier;
    if (native_type == 1) {
      this.button_trait = LinuxNativeTypesMap.guessButtonTrait(native_code);
    } else {
      this.button_trait = Controller.Type.UNKNOWN;
    } 
    this.is_relative = is_relative;
    this.descriptor = new LinuxAxisDescriptor();
    this.descriptor.set(native_type, native_code);
    if (native_type == 3) {
      LinuxAbsInfo abs_info = new LinuxAbsInfo();
      getAbsInfo(abs_info);
      this.min = abs_info.getMin();
      this.max = abs_info.getMax();
      this.flat = abs_info.getFlat();
    } else {
      this.min = Integer.MIN_VALUE;
      this.max = Integer.MAX_VALUE;
      this.flat = 0;
    } 
  }
  
  public final LinuxEventDevice getDevice() {
    return this.device;
  }
  
  public final void getAbsInfo(LinuxAbsInfo abs_info) throws IOException {
    assert this.descriptor.getType() == 3;
    this.device.getAbsInfo(this.descriptor.getCode(), abs_info);
  }
  
  public final Controller.Type getButtonTrait() {
    return this.button_trait;
  }
  
  public final Component.Identifier getIdentifier() {
    return this.identifier;
  }
  
  public final LinuxAxisDescriptor getDescriptor() {
    return this.descriptor;
  }
  
  public final boolean isRelative() {
    return this.is_relative;
  }
  
  public final boolean isAnalog() {
    return (this.identifier instanceof Component.Identifier.Axis && this.identifier != Component.Identifier.Axis.POV);
  }
  
  final float convertValue(float value) {
    if (this.identifier instanceof Component.Identifier.Axis && !this.is_relative) {
      if (this.min == this.max)
        return 0.0F; 
      if (value > this.max) {
        value = this.max;
      } else if (value < this.min) {
        value = this.min;
      } 
      return 2.0F * (value - this.min) / (this.max - this.min) - 1.0F;
    } 
    return value;
  }
  
  final float getDeadZone() {
    return this.flat / 2.0F * (this.max - this.min);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxEventComponent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */