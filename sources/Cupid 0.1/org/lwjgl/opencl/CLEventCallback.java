package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLEventCallback extends PointerWrapperAbstract {
  private CLObjectRegistry<CLEvent> eventRegistry;
  
  protected CLEventCallback() {
    super(CallbackUtil.getEventCallback());
  }
  
  void setRegistry(CLObjectRegistry<CLEvent> eventRegistry) {
    this.eventRegistry = eventRegistry;
  }
  
  private void handleMessage(long event_address, int event_command_exec_status) {
    handleMessage(this.eventRegistry.getObject(event_address), event_command_exec_status);
  }
  
  protected abstract void handleMessage(CLEvent paramCLEvent, int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLEventCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */