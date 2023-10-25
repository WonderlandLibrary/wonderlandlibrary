package org.lwjgl.opencl;

public final class CLKernel extends CLObjectChild<CLProgram> {
  private static final CLKernelUtil util = (CLKernelUtil)CLPlatform.<CLKernel>getInfoUtilInstance(CLKernel.class, "CL_KERNEL_UTIL");
  
  CLKernel(long pointer, CLProgram program) {
    super(pointer, program);
    if (isValid())
      program.getCLKernelRegistry().registerObject(this); 
  }
  
  public CLKernel setArg(int index, byte value) {
    util.setArg(this, index, value);
    return this;
  }
  
  public CLKernel setArg(int index, short value) {
    util.setArg(this, index, value);
    return this;
  }
  
  public CLKernel setArg(int index, int value) {
    util.setArg(this, index, value);
    return this;
  }
  
  public CLKernel setArg(int index, long value) {
    util.setArg(this, index, value);
    return this;
  }
  
  public CLKernel setArg(int index, float value) {
    util.setArg(this, index, value);
    return this;
  }
  
  public CLKernel setArg(int index, double value) {
    util.setArg(this, index, value);
    return this;
  }
  
  public CLKernel setArg(int index, CLObject value) {
    util.setArg(this, index, value);
    return this;
  }
  
  public CLKernel setArgSize(int index, long size) {
    util.setArgSize(this, index, size);
    return this;
  }
  
  public String getInfoString(int param_name) {
    return util.getInfoString(this, param_name);
  }
  
  public int getInfoInt(int param_name) {
    return util.getInfoInt(this, param_name);
  }
  
  public long getWorkGroupInfoSize(CLDevice device, int param_name) {
    return util.getWorkGroupInfoSize(this, device, param_name);
  }
  
  public long[] getWorkGroupInfoSizeArray(CLDevice device, int param_name) {
    return util.getWorkGroupInfoSizeArray(this, device, param_name);
  }
  
  public long getWorkGroupInfoLong(CLDevice device, int param_name) {
    return util.getWorkGroupInfoLong(this, device, param_name);
  }
  
  int release() {
    try {
      return super.release();
    } finally {
      if (!isValid())
        getParent().getCLKernelRegistry().unregisterObject(this); 
    } 
  }
  
  static interface CLKernelUtil extends InfoUtil<CLKernel> {
    void setArg(CLKernel param1CLKernel, int param1Int, byte param1Byte);
    
    void setArg(CLKernel param1CLKernel, int param1Int, short param1Short);
    
    void setArg(CLKernel param1CLKernel, int param1Int1, int param1Int2);
    
    void setArg(CLKernel param1CLKernel, int param1Int, long param1Long);
    
    void setArg(CLKernel param1CLKernel, int param1Int, float param1Float);
    
    void setArg(CLKernel param1CLKernel, int param1Int, double param1Double);
    
    void setArg(CLKernel param1CLKernel, int param1Int, CLObject param1CLObject);
    
    void setArgSize(CLKernel param1CLKernel, int param1Int, long param1Long);
    
    long getWorkGroupInfoSize(CLKernel param1CLKernel, CLDevice param1CLDevice, int param1Int);
    
    long[] getWorkGroupInfoSizeArray(CLKernel param1CLKernel, CLDevice param1CLDevice, int param1Int);
    
    long getWorkGroupInfoLong(CLKernel param1CLKernel, CLDevice param1CLDevice, int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLKernel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */