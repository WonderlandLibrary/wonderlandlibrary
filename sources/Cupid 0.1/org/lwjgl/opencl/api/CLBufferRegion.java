package org.lwjgl.opencl.api;

import org.lwjgl.PointerBuffer;

public final class CLBufferRegion {
  public static final int STRUCT_SIZE = 2 * PointerBuffer.getPointerSize();
  
  private final int origin;
  
  private final int size;
  
  public CLBufferRegion(int origin, int size) {
    this.origin = origin;
    this.size = size;
  }
  
  public int getOrigin() {
    return this.origin;
  }
  
  public int getSize() {
    return this.size;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\api\CLBufferRegion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */