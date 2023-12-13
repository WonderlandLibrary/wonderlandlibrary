package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class ARBVertexBlend {
   public static final int GL_MAX_VERTEX_UNITS_ARB = 34468;
   public static final int GL_ACTIVE_VERTEX_UNITS_ARB = 34469;
   public static final int GL_WEIGHT_SUM_UNITY_ARB = 34470;
   public static final int GL_VERTEX_BLEND_ARB = 34471;
   public static final int GL_CURRENT_WEIGHT_ARB = 34472;
   public static final int GL_WEIGHT_ARRAY_TYPE_ARB = 34473;
   public static final int GL_WEIGHT_ARRAY_STRIDE_ARB = 34474;
   public static final int GL_WEIGHT_ARRAY_SIZE_ARB = 34475;
   public static final int GL_WEIGHT_ARRAY_POINTER_ARB = 34476;
   public static final int GL_WEIGHT_ARRAY_ARB = 34477;
   public static final int GL_MODELVIEW0_ARB = 5888;
   public static final int GL_MODELVIEW1_ARB = 34058;
   public static final int GL_MODELVIEW2_ARB = 34594;
   public static final int GL_MODELVIEW3_ARB = 34595;
   public static final int GL_MODELVIEW4_ARB = 34596;
   public static final int GL_MODELVIEW5_ARB = 34597;
   public static final int GL_MODELVIEW6_ARB = 34598;
   public static final int GL_MODELVIEW7_ARB = 34599;
   public static final int GL_MODELVIEW8_ARB = 34600;
   public static final int GL_MODELVIEW9_ARB = 34601;
   public static final int GL_MODELVIEW10_ARB = 34602;
   public static final int GL_MODELVIEW11_ARB = 34603;
   public static final int GL_MODELVIEW12_ARB = 34604;
   public static final int GL_MODELVIEW13_ARB = 34605;
   public static final int GL_MODELVIEW14_ARB = 34606;
   public static final int GL_MODELVIEW15_ARB = 34607;
   public static final int GL_MODELVIEW16_ARB = 34608;
   public static final int GL_MODELVIEW17_ARB = 34609;
   public static final int GL_MODELVIEW18_ARB = 34610;
   public static final int GL_MODELVIEW19_ARB = 34611;
   public static final int GL_MODELVIEW20_ARB = 34612;
   public static final int GL_MODELVIEW21_ARB = 34613;
   public static final int GL_MODELVIEW22_ARB = 34614;
   public static final int GL_MODELVIEW23_ARB = 34615;
   public static final int GL_MODELVIEW24_ARB = 34616;
   public static final int GL_MODELVIEW25_ARB = 34617;
   public static final int GL_MODELVIEW26_ARB = 34618;
   public static final int GL_MODELVIEW27_ARB = 34619;
   public static final int GL_MODELVIEW28_ARB = 34620;
   public static final int GL_MODELVIEW29_ARB = 34621;
   public static final int GL_MODELVIEW30_ARB = 34622;
   public static final int GL_MODELVIEW31_ARB = 34623;

   private ARBVertexBlend() {
   }

   public static void glWeightARB(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightbvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightbvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightbvARB(int var0, long var1, long var3);

   public static void glWeightARB(ShortBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightsvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightsvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightsvARB(int var0, long var1, long var3);

   public static void glWeightARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightivARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightivARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightivARB(int var0, long var1, long var3);

   public static void glWeightARB(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightfvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightfvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightfvARB(int var0, long var1, long var3);

   public static void glWeightARB(DoubleBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightdvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightdvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightdvARB(int var0, long var1, long var3);

   public static void glWeightuARB(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightubvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightubvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightubvARB(int var0, long var1, long var3);

   public static void glWeightuARB(ShortBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightusvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightusvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightusvARB(int var0, long var1, long var3);

   public static void glWeightuARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glWeightuivARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglWeightuivARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglWeightuivARB(int var0, long var1, long var3);

   public static void glWeightPointerARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWeightPointerARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).ARB_vertex_blend_glWeightPointerARB_pPointer = var2;
      }

      nglWeightPointerARB(var0, 5130, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glWeightPointerARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWeightPointerARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).ARB_vertex_blend_glWeightPointerARB_pPointer = var2;
      }

      nglWeightPointerARB(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glWeightPointerARB(int var0, boolean var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glWeightPointerARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).ARB_vertex_blend_glWeightPointerARB_pPointer = var3;
      }

      nglWeightPointerARB(var0, var1 ? 5121 : 5120, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glWeightPointerARB(int var0, boolean var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glWeightPointerARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).ARB_vertex_blend_glWeightPointerARB_pPointer = var3;
      }

      nglWeightPointerARB(var0, var1 ? 5125 : 5124, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glWeightPointerARB(int var0, boolean var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glWeightPointerARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).ARB_vertex_blend_glWeightPointerARB_pPointer = var3;
      }

      nglWeightPointerARB(var0, var1 ? 5123 : 5122, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglWeightPointerARB(int var0, int var1, int var2, long var3, long var5);

   public static void glWeightPointerARB(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glWeightPointerARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglWeightPointerARBBO(var0, var1, var2, var3, var6);
   }

   static native void nglWeightPointerARBBO(int var0, int var1, int var2, long var3, long var5);

   public static void glVertexBlendARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexBlendARB;
      BufferChecks.checkFunctionAddress(var2);
      nglVertexBlendARB(var0, var2);
   }

   static native void nglVertexBlendARB(int var0, long var1);
}
