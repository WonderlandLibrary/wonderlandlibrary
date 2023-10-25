package org.lwjgl.opencl;

public final class CLEvent extends CLObjectChild<CLContext> {
  private static final CLEventUtil util = (CLEventUtil)CLPlatform.<CLEvent>getInfoUtilInstance(CLEvent.class, "CL_EVENT_UTIL");
  
  private final CLCommandQueue queue;
  
  CLEvent(long pointer, CLContext context) {
    this(pointer, context, (CLCommandQueue)null);
  }
  
  CLEvent(long pointer, CLCommandQueue queue) {
    this(pointer, queue.getParent(), queue);
  }
  
  CLEvent(long pointer, CLContext context, CLCommandQueue queue) {
    super(pointer, context);
    if (isValid()) {
      this.queue = queue;
      if (queue == null) {
        context.getCLEventRegistry().registerObject(this);
      } else {
        queue.getCLEventRegistry().registerObject(this);
      } 
    } else {
      this.queue = null;
    } 
  }
  
  public CLCommandQueue getCLCommandQueue() {
    return this.queue;
  }
  
  public int getInfoInt(int param_name) {
    return util.getInfoInt(this, param_name);
  }
  
  public long getProfilingInfoLong(int param_name) {
    return util.getProfilingInfoLong(this, param_name);
  }
  
  CLObjectRegistry<CLEvent> getParentRegistry() {
    if (this.queue == null)
      return getParent().getCLEventRegistry(); 
    return this.queue.getCLEventRegistry();
  }
  
  int release() {
    try {
      return super.release();
    } finally {
      if (!isValid())
        if (this.queue == null) {
          getParent().getCLEventRegistry().unregisterObject(this);
        } else {
          this.queue.getCLEventRegistry().unregisterObject(this);
        }  
    } 
  }
  
  static interface CLEventUtil extends InfoUtil<CLEvent> {
    long getProfilingInfoLong(CLEvent param1CLEvent, int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */