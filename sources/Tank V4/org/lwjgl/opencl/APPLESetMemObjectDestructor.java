package org.lwjgl.opencl;

import org.lwjgl.BufferChecks;

public final class APPLESetMemObjectDestructor {
   private APPLESetMemObjectDestructor() {
   }

   public static int clSetMemObjectDestructorAPPLE(CLMem var0, CLMemObjectDestructorCallback var1) {
      long var2 = CLCapabilities.clSetMemObjectDestructorAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = CallbackUtil.createGlobalRef(var1);
      boolean var6 = false;
      int var9 = nclSetMemObjectDestructorAPPLE(var0.getPointer(), var1.getPointer(), var4, var2);
      CallbackUtil.checkCallback(var9, var4);
      return var9;
   }

   static native int nclSetMemObjectDestructorAPPLE(long var0, long var2, long var4, long var6);
}
