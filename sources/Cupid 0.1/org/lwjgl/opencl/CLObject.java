package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

abstract class CLObject extends PointerWrapperAbstract {
  protected CLObject(long pointer) {
    super(pointer);
  }
  
  final long getPointerUnsafe() {
    return this.pointer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */