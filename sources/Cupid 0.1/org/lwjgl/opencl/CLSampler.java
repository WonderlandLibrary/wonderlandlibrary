package org.lwjgl.opencl;

public final class CLSampler extends CLObjectChild<CLContext> {
  private static final InfoUtil<CLSampler> util = CLPlatform.getInfoUtilInstance(CLSampler.class, "CL_SAMPLER_UTIL");
  
  CLSampler(long pointer, CLContext context) {
    super(pointer, context);
    if (isValid())
      context.getCLSamplerRegistry().registerObject(this); 
  }
  
  public int getInfoInt(int param_name) {
    return util.getInfoInt(this, param_name);
  }
  
  public long getInfoLong(int param_name) {
    return util.getInfoLong(this, param_name);
  }
  
  int release() {
    try {
      return super.release();
    } finally {
      if (!isValid())
        getParent().getCLSamplerRegistry().unregisterObject(this); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLSampler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */