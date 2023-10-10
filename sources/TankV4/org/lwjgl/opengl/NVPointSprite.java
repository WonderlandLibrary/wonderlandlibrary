package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVPointSprite {
   public static final int GL_POINT_SPRITE_NV = 34913;
   public static final int GL_COORD_REPLACE_NV = 34914;
   public static final int GL_POINT_SPRITE_R_MODE_NV = 34915;

   private NVPointSprite() {
   }

   public static void glPointParameteriNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameteriNV;
      BufferChecks.checkFunctionAddress(var3);
      nglPointParameteriNV(var0, var1, var3);
   }

   static native void nglPointParameteriNV(int var0, int var1, long var2);

   public static void glPointParameterNV(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameterivNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglPointParameterivNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPointParameterivNV(int var0, long var1, long var3);
}
