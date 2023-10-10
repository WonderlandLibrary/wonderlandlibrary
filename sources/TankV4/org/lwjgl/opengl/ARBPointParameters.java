package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBPointParameters {
   public static final int GL_POINT_SIZE_MIN_ARB = 33062;
   public static final int GL_POINT_SIZE_MAX_ARB = 33063;
   public static final int GL_POINT_FADE_THRESHOLD_SIZE_ARB = 33064;
   public static final int GL_POINT_DISTANCE_ATTENUATION_ARB = 33065;

   private ARBPointParameters() {
   }

   public static void glPointParameterfARB(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameterfARB;
      BufferChecks.checkFunctionAddress(var3);
      nglPointParameterfARB(var0, var1, var3);
   }

   static native void nglPointParameterfARB(int var0, float var1, long var2);

   public static void glPointParameterARB(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameterfvARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglPointParameterfvARB(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPointParameterfvARB(int var0, long var1, long var3);
}
