package org.lwjgl.openal;

import java.util.HashMap;

public final class ALCdevice {
  final long device;
  
  private boolean valid;
  
  private final HashMap<Long, ALCcontext> contexts = new HashMap<Long, ALCcontext>();
  
  ALCdevice(long device) {
    this.device = device;
    this.valid = true;
  }
  
  public boolean equals(Object device) {
    if (device instanceof ALCdevice)
      return (((ALCdevice)device).device == this.device); 
    return super.equals(device);
  }
  
  void addContext(ALCcontext context) {
    synchronized (this.contexts) {
      this.contexts.put(Long.valueOf(context.context), context);
    } 
  }
  
  void removeContext(ALCcontext context) {
    synchronized (this.contexts) {
      this.contexts.remove(Long.valueOf(context.context));
    } 
  }
  
  void setInvalid() {
    this.valid = false;
    synchronized (this.contexts) {
      for (ALCcontext context : this.contexts.values())
        context.setInvalid(); 
    } 
    this.contexts.clear();
  }
  
  public boolean isValid() {
    return this.valid;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\openal\ALCdevice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */