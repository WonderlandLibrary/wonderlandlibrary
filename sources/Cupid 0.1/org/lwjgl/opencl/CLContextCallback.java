package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.PointerWrapperAbstract;

public abstract class CLContextCallback extends PointerWrapperAbstract {
  private final boolean custom;
  
  protected CLContextCallback() {
    super(CallbackUtil.getContextCallback());
    this.custom = false;
  }
  
  protected CLContextCallback(long pointer) {
    super(pointer);
    if (pointer == 0L)
      throw new RuntimeException("Invalid callback function pointer specified."); 
    this.custom = true;
  }
  
  final boolean isCustom() {
    return this.custom;
  }
  
  protected abstract void handleMessage(String paramString, ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLContextCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */