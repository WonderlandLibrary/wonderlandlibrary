package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLEvent;

public final class ARBCLEvent {
   public static final int GL_SYNC_CL_EVENT_ARB = 33344;
   public static final int GL_SYNC_CL_EVENT_COMPLETE_ARB = 33345;

   private ARBCLEvent() {
   }

   public static GLSync glCreateSyncFromCLeventARB(CLContext var0, CLEvent var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glCreateSyncFromCLeventARB;
      BufferChecks.checkFunctionAddress(var4);
      GLSync var6 = new GLSync(nglCreateSyncFromCLeventARB(var0.getPointer(), var1.getPointer(), var2, var4));
      return var6;
   }

   static native long nglCreateSyncFromCLeventARB(long var0, long var2, int var4, long var5);
}
