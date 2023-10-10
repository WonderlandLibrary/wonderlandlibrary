package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

abstract class CLObject extends PointerWrapperAbstract {
   protected CLObject(long var1) {
      super(var1);
   }

   final long getPointerUnsafe() {
      return this.pointer;
   }
}
