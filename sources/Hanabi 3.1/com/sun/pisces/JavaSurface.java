package com.sun.pisces;

import java.nio.IntBuffer;

public final class JavaSurface extends AbstractSurface {
   private IntBuffer dataBuffer;
   private int[] dataInt;

   public JavaSurface(int[] var1, int var2, int var3, int var4) {
      super(var3, var4);
      if (var1.length / var3 < var4) {
         throw new IllegalArgumentException("width(=" + var3 + ") * height(=" + var4 + ") is greater than dataInt.length(=" + var1.length + ")");
      } else {
         this.dataInt = var1;
         this.dataBuffer = IntBuffer.wrap(this.dataInt);
         this.initialize(var2, var3, var4);
      }
   }

   public IntBuffer getDataIntBuffer() {
      return this.dataBuffer;
   }

   private native void initialize(int var1, int var2, int var3);
}
