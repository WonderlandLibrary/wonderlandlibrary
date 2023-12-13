package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTPointParameters {
   public static final int GL_POINT_SIZE_MIN_EXT = 33062;
   public static final int GL_POINT_SIZE_MAX_EXT = 33063;
   public static final int GL_POINT_FADE_THRESHOLD_SIZE_EXT = 33064;
   public static final int GL_DISTANCE_ATTENUATION_EXT = 33065;

   private EXTPointParameters() {
   }

   public static void glPointParameterfEXT(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameterfEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglPointParameterfEXT(var0, var1, var3);
   }

   static native void nglPointParameterfEXT(int var0, float var1, long var2);

   public static void glPointParameterEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameterfvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglPointParameterfvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPointParameterfvEXT(int var0, long var1, long var3);
}
