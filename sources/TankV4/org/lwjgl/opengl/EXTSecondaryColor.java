package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTSecondaryColor {
   public static final int GL_COLOR_SUM_EXT = 33880;
   public static final int GL_CURRENT_SECONDARY_COLOR_EXT = 33881;
   public static final int GL_SECONDARY_COLOR_ARRAY_SIZE_EXT = 33882;
   public static final int GL_SECONDARY_COLOR_ARRAY_TYPE_EXT = 33883;
   public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT = 33884;
   public static final int GL_SECONDARY_COLOR_ARRAY_POINTER_EXT = 33885;
   public static final int GL_SECONDARY_COLOR_ARRAY_EXT = 33886;

   private EXTSecondaryColor() {
   }

   public static void glSecondaryColor3bEXT(byte var0, byte var1, byte var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColor3bEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColor3bEXT(var0, var1, var2, var4);
   }

   static native void nglSecondaryColor3bEXT(byte var0, byte var1, byte var2, long var3);

   public static void glSecondaryColor3fEXT(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColor3fEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColor3fEXT(var0, var1, var2, var4);
   }

   static native void nglSecondaryColor3fEXT(float var0, float var1, float var2, long var3);

   public static void glSecondaryColor3dEXT(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glSecondaryColor3dEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglSecondaryColor3dEXT(var0, var2, var4, var7);
   }

   static native void nglSecondaryColor3dEXT(double var0, double var2, double var4, long var6);

   public static void glSecondaryColor3ubEXT(byte var0, byte var1, byte var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColor3ubEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColor3ubEXT(var0, var1, var2, var4);
   }

   static native void nglSecondaryColor3ubEXT(byte var0, byte var1, byte var2, long var3);

   public static void glSecondaryColorPointerEXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColorPointerEXT;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = var2;
      }

      nglSecondaryColorPointerEXT(var0, 5130, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSecondaryColorPointerEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColorPointerEXT;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = var2;
      }

      nglSecondaryColorPointerEXT(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSecondaryColorPointerEXT(int var0, boolean var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glSecondaryColorPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = var3;
      }

      nglSecondaryColorPointerEXT(var0, var1 ? 5121 : 5120, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglSecondaryColorPointerEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glSecondaryColorPointerEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glSecondaryColorPointerEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglSecondaryColorPointerEXTBO(var0, var1, var2, var3, var6);
   }

   static native void nglSecondaryColorPointerEXTBO(int var0, int var1, int var2, long var3, long var5);
}
