package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ATIEnvmapBumpmap {
   public static final int GL_BUMP_ROT_MATRIX_ATI = 34677;
   public static final int GL_BUMP_ROT_MATRIX_SIZE_ATI = 34678;
   public static final int GL_BUMP_NUM_TEX_UNITS_ATI = 34679;
   public static final int GL_BUMP_TEX_UNITS_ATI = 34680;
   public static final int GL_DUDV_ATI = 34681;
   public static final int GL_DU8DV8_ATI = 34682;
   public static final int GL_BUMP_ENVMAP_ATI = 34683;
   public static final int GL_BUMP_TARGET_ATI = 34684;

   private ATIEnvmapBumpmap() {
   }

   public static void glTexBumpParameterATI(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexBumpParameterfvATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglTexBumpParameterfvATI(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglTexBumpParameterfvATI(int var0, long var1, long var3);

   public static void glTexBumpParameterATI(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexBumpParameterivATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglTexBumpParameterivATI(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglTexBumpParameterivATI(int var0, long var1, long var3);

   public static void glGetTexBumpParameterATI(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexBumpParameterfvATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglGetTexBumpParameterfvATI(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetTexBumpParameterfvATI(int var0, long var1, long var3);

   public static void glGetTexBumpParameterATI(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexBumpParameterivATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglGetTexBumpParameterivATI(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetTexBumpParameterivATI(int var0, long var1, long var3);
}
