package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.PointerBuffer;

public final class CLProgram extends CLObjectChild<CLContext> {
  private static final CLProgramUtil util = (CLProgramUtil)CLPlatform.<CLProgram>getInfoUtilInstance(CLProgram.class, "CL_PROGRAM_UTIL");
  
  private final CLObjectRegistry<CLKernel> clKernels;
  
  CLProgram(long pointer, CLContext context) {
    super(pointer, context);
    if (isValid()) {
      context.getCLProgramRegistry().registerObject(this);
      this.clKernels = new CLObjectRegistry<CLKernel>();
    } else {
      this.clKernels = null;
    } 
  }
  
  public CLKernel getCLKernel(long id) {
    return this.clKernels.getObject(id);
  }
  
  public CLKernel[] createKernelsInProgram() {
    return util.createKernelsInProgram(this);
  }
  
  public String getInfoString(int param_name) {
    return util.getInfoString(this, param_name);
  }
  
  public int getInfoInt(int param_name) {
    return util.getInfoInt(this, param_name);
  }
  
  public long[] getInfoSizeArray(int param_name) {
    return util.getInfoSizeArray(this, param_name);
  }
  
  public CLDevice[] getInfoDevices() {
    return util.getInfoDevices(this);
  }
  
  public ByteBuffer getInfoBinaries(ByteBuffer target) {
    return util.getInfoBinaries(this, target);
  }
  
  public ByteBuffer[] getInfoBinaries(ByteBuffer[] target) {
    return util.getInfoBinaries(this, target);
  }
  
  public String getBuildInfoString(CLDevice device, int param_name) {
    return util.getBuildInfoString(this, device, param_name);
  }
  
  public int getBuildInfoInt(CLDevice device, int param_name) {
    return util.getBuildInfoInt(this, device, param_name);
  }
  
  CLObjectRegistry<CLKernel> getCLKernelRegistry() {
    return this.clKernels;
  }
  
  void registerCLKernels(PointerBuffer kernels) {
    for (int i = kernels.position(); i < kernels.limit(); i++) {
      long pointer = kernels.get(i);
      if (pointer != 0L)
        new CLKernel(pointer, this); 
    } 
  }
  
  int release() {
    try {
      return super.release();
    } finally {
      if (!isValid())
        getParent().getCLProgramRegistry().unregisterObject(this); 
    } 
  }
  
  static interface CLProgramUtil extends InfoUtil<CLProgram> {
    CLKernel[] createKernelsInProgram(CLProgram param1CLProgram);
    
    CLDevice[] getInfoDevices(CLProgram param1CLProgram);
    
    ByteBuffer getInfoBinaries(CLProgram param1CLProgram, ByteBuffer param1ByteBuffer);
    
    ByteBuffer[] getInfoBinaries(CLProgram param1CLProgram, ByteBuffer[] param1ArrayOfByteBuffer);
    
    String getBuildInfoString(CLProgram param1CLProgram, CLDevice param1CLDevice, int param1Int);
    
    int getBuildInfoInt(CLProgram param1CLProgram, CLDevice param1CLDevice, int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLProgram.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */