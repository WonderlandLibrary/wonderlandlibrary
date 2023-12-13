package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class ARBViewportArray {
   public static final int GL_MAX_VIEWPORTS = 33371;
   public static final int GL_VIEWPORT_SUBPIXEL_BITS = 33372;
   public static final int GL_VIEWPORT_BOUNDS_RANGE = 33373;
   public static final int GL_LAYER_PROVOKING_VERTEX = 33374;
   public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 33375;
   public static final int GL_SCISSOR_BOX = 3088;
   public static final int GL_VIEWPORT = 2978;
   public static final int GL_DEPTH_RANGE = 2928;
   public static final int GL_SCISSOR_TEST = 3089;
   public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
   public static final int GL_LAST_VERTEX_CONVENTION = 36430;
   public static final int GL_PROVOKING_VERTEX = 36431;
   public static final int GL_UNDEFINED_VERTEX = 33376;

   private ARBViewportArray() {
   }

   public static void glViewportArray(int var0, FloatBuffer var1) {
      GL41.glViewportArray(var0, var1);
   }

   public static void glViewportIndexedf(int var0, float var1, float var2, float var3, float var4) {
      GL41.glViewportIndexedf(var0, var1, var2, var3, var4);
   }

   public static void glViewportIndexed(int var0, FloatBuffer var1) {
      GL41.glViewportIndexed(var0, var1);
   }

   public static void glScissorArray(int var0, IntBuffer var1) {
      GL41.glScissorArray(var0, var1);
   }

   public static void glScissorIndexed(int var0, int var1, int var2, int var3, int var4) {
      GL41.glScissorIndexed(var0, var1, var2, var3, var4);
   }

   public static void glScissorIndexed(int var0, IntBuffer var1) {
      GL41.glScissorIndexed(var0, var1);
   }

   public static void glDepthRangeArray(int var0, DoubleBuffer var1) {
      GL41.glDepthRangeArray(var0, var1);
   }

   public static void glDepthRangeIndexed(int var0, double var1, double var3) {
      GL41.glDepthRangeIndexed(var0, var1, var3);
   }

   public static void glGetFloat(int var0, int var1, FloatBuffer var2) {
      GL41.glGetFloat(var0, var1, var2);
   }

   public static float glGetFloat(int var0, int var1) {
      return GL41.glGetFloat(var0, var1);
   }

   public static void glGetDouble(int var0, int var1, DoubleBuffer var2) {
      GL41.glGetDouble(var0, var1, var2);
   }

   public static double glGetDouble(int var0, int var1) {
      return GL41.glGetDouble(var0, var1);
   }

   public static void glGetIntegerIndexedEXT(int var0, int var1, IntBuffer var2) {
      EXTDrawBuffers2.glGetIntegerIndexedEXT(var0, var1, var2);
   }

   public static int glGetIntegerIndexedEXT(int var0, int var1) {
      return EXTDrawBuffers2.glGetIntegerIndexedEXT(var0, var1);
   }

   public static void glEnableIndexedEXT(int var0, int var1) {
      EXTDrawBuffers2.glEnableIndexedEXT(var0, var1);
   }

   public static void glDisableIndexedEXT(int var0, int var1) {
      EXTDrawBuffers2.glDisableIndexedEXT(var0, var1);
   }

   public static boolean glIsEnabledIndexedEXT(int var0, int var1) {
      return EXTDrawBuffers2.glIsEnabledIndexedEXT(var0, var1);
   }
}
