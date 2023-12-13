package org.lwjgl.opencl.api;

import org.lwjgl.PointerBuffer;

public final class CLBufferRegion {
   public static final int STRUCT_SIZE = 2 * PointerBuffer.getPointerSize();
   private final int origin;
   private final int size;

   public CLBufferRegion(int var1, int var2) {
      this.origin = var1;
      this.size = var2;
   }

   public int getOrigin() {
      return this.origin;
   }

   public int getSize() {
      return this.size;
   }
}
