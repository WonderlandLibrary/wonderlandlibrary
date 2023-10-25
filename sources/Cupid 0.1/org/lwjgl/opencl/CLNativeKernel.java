package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.PointerWrapperAbstract;

public abstract class CLNativeKernel extends PointerWrapperAbstract {
  protected CLNativeKernel() {
    super(CallbackUtil.getNativeKernelCallback());
  }
  
  protected abstract void execute(ByteBuffer[] paramArrayOfByteBuffer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLNativeKernel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */