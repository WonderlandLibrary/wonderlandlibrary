package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTFogCoord {
   public static final int GL_FOG_COORDINATE_SOURCE_EXT = 33872;
   public static final int GL_FOG_COORDINATE_EXT = 33873;
   public static final int GL_FRAGMENT_DEPTH_EXT = 33874;
   public static final int GL_CURRENT_FOG_COORDINATE_EXT = 33875;
   public static final int GL_FOG_COORDINATE_ARRAY_TYPE_EXT = 33876;
   public static final int GL_FOG_COORDINATE_ARRAY_STRIDE_EXT = 33877;
   public static final int GL_FOG_COORDINATE_ARRAY_POINTER_EXT = 33878;
   public static final int GL_FOG_COORDINATE_ARRAY_EXT = 33879;

   private EXTFogCoord() {
   }

   public static void glFogCoordfEXT(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFogCoordfEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglFogCoordfEXT(var0, var2);
   }

   static native void nglFogCoordfEXT(float var0, long var1);

   public static void glFogCoorddEXT(double var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogCoorddEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglFogCoorddEXT(var0, var3);
   }

   static native void nglFogCoorddEXT(double var0, long var2);

   public static void glFogCoordPointerEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogCoordPointerEXT;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).EXT_fog_coord_glFogCoordPointerEXT_data = var1;
      }

      nglFogCoordPointerEXT(5130, var0, MemoryUtil.getAddress(var1), var3);
   }

   public static void glFogCoordPointerEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogCoordPointerEXT;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).EXT_fog_coord_glFogCoordPointerEXT_data = var1;
      }

      nglFogCoordPointerEXT(5126, var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglFogCoordPointerEXT(int var0, int var1, long var2, long var4);

   public static void glFogCoordPointerEXT(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glFogCoordPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOenabled(var4);
      nglFogCoordPointerEXTBO(var0, var1, var2, var5);
   }

   static native void nglFogCoordPointerEXTBO(int var0, int var1, long var2, long var4);
}
