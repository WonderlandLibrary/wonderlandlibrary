package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVRegisterCombiners {
   public static final int GL_REGISTER_COMBINERS_NV = 34082;
   public static final int GL_COMBINER0_NV = 34128;
   public static final int GL_COMBINER1_NV = 34129;
   public static final int GL_COMBINER2_NV = 34130;
   public static final int GL_COMBINER3_NV = 34131;
   public static final int GL_COMBINER4_NV = 34132;
   public static final int GL_COMBINER5_NV = 34133;
   public static final int GL_COMBINER6_NV = 34134;
   public static final int GL_COMBINER7_NV = 34135;
   public static final int GL_VARIABLE_A_NV = 34083;
   public static final int GL_VARIABLE_B_NV = 34084;
   public static final int GL_VARIABLE_C_NV = 34085;
   public static final int GL_VARIABLE_D_NV = 34086;
   public static final int GL_VARIABLE_E_NV = 34087;
   public static final int GL_VARIABLE_F_NV = 34088;
   public static final int GL_VARIABLE_G_NV = 34089;
   public static final int GL_CONSTANT_COLOR0_NV = 34090;
   public static final int GL_CONSTANT_COLOR1_NV = 34091;
   public static final int GL_PRIMARY_COLOR_NV = 34092;
   public static final int GL_SECONDARY_COLOR_NV = 34093;
   public static final int GL_SPARE0_NV = 34094;
   public static final int GL_SPARE1_NV = 34095;
   public static final int GL_UNSIGNED_IDENTITY_NV = 34102;
   public static final int GL_UNSIGNED_INVERT_NV = 34103;
   public static final int GL_EXPAND_NORMAL_NV = 34104;
   public static final int GL_EXPAND_NEGATE_NV = 34105;
   public static final int GL_HALF_BIAS_NORMAL_NV = 34106;
   public static final int GL_HALF_BIAS_NEGATE_NV = 34107;
   public static final int GL_SIGNED_IDENTITY_NV = 34108;
   public static final int GL_SIGNED_NEGATE_NV = 34109;
   public static final int GL_E_TIMES_F_NV = 34097;
   public static final int GL_SPARE0_PLUS_SECONDARY_COLOR_NV = 34098;
   public static final int GL_SCALE_BY_TWO_NV = 34110;
   public static final int GL_SCALE_BY_FOUR_NV = 34111;
   public static final int GL_SCALE_BY_ONE_HALF_NV = 34112;
   public static final int GL_BIAS_BY_NEGATIVE_ONE_HALF_NV = 34113;
   public static final int GL_DISCARD_NV = 34096;
   public static final int GL_COMBINER_INPUT_NV = 34114;
   public static final int GL_COMBINER_MAPPING_NV = 34115;
   public static final int GL_COMBINER_COMPONENT_USAGE_NV = 34116;
   public static final int GL_COMBINER_AB_DOT_PRODUCT_NV = 34117;
   public static final int GL_COMBINER_CD_DOT_PRODUCT_NV = 34118;
   public static final int GL_COMBINER_MUX_SUM_NV = 34119;
   public static final int GL_COMBINER_SCALE_NV = 34120;
   public static final int GL_COMBINER_BIAS_NV = 34121;
   public static final int GL_COMBINER_AB_OUTPUT_NV = 34122;
   public static final int GL_COMBINER_CD_OUTPUT_NV = 34123;
   public static final int GL_COMBINER_SUM_OUTPUT_NV = 34124;
   public static final int GL_NUM_GENERAL_COMBINERS_NV = 34126;
   public static final int GL_COLOR_SUM_CLAMP_NV = 34127;
   public static final int GL_MAX_GENERAL_COMBINERS_NV = 34125;

   private NVRegisterCombiners() {
   }

   public static void glCombinerParameterfNV(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCombinerParameterfNV;
      BufferChecks.checkFunctionAddress(var3);
      nglCombinerParameterfNV(var0, var1, var3);
   }

   static native void nglCombinerParameterfNV(int var0, float var1, long var2);

   public static void glCombinerParameterNV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCombinerParameterfvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglCombinerParameterfvNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglCombinerParameterfvNV(int var0, long var1, long var3);

   public static void glCombinerParameteriNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCombinerParameteriNV;
      BufferChecks.checkFunctionAddress(var3);
      nglCombinerParameteriNV(var0, var1, var3);
   }

   static native void nglCombinerParameteriNV(int var0, int var1, long var2);

   public static void glCombinerParameterNV(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCombinerParameterivNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglCombinerParameterivNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglCombinerParameterivNV(int var0, long var1, long var3);

   public static void glCombinerInputNV(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCombinerInputNV;
      BufferChecks.checkFunctionAddress(var7);
      nglCombinerInputNV(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglCombinerInputNV(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glCombinerOutputNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, boolean var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCombinerOutputNV;
      BufferChecks.checkFunctionAddress(var11);
      nglCombinerOutputNV(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   static native void nglCombinerOutputNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, boolean var9, long var10);

   public static void glFinalCombinerInputNV(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glFinalCombinerInputNV;
      BufferChecks.checkFunctionAddress(var5);
      nglFinalCombinerInputNV(var0, var1, var2, var3, var5);
   }

   static native void nglFinalCombinerInputNV(int var0, int var1, int var2, int var3, long var4);

   public static void glGetCombinerInputParameterNV(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetCombinerInputParameterfvNV;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((FloatBuffer)var4, 4);
      nglGetCombinerInputParameterfvNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetCombinerInputParameterfvNV(int var0, int var1, int var2, int var3, long var4, long var6);

   public static float glGetCombinerInputParameterfNV(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCombinerInputParameterfvNV;
      BufferChecks.checkFunctionAddress(var5);
      FloatBuffer var7 = APIUtil.getBufferFloat(var4);
      nglGetCombinerInputParameterfvNV(var0, var1, var2, var3, MemoryUtil.getAddress(var7), var5);
      return var7.get(0);
   }

   public static void glGetCombinerInputParameterNV(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetCombinerInputParameterivNV;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((IntBuffer)var4, 4);
      nglGetCombinerInputParameterivNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetCombinerInputParameterivNV(int var0, int var1, int var2, int var3, long var4, long var6);

   public static int glGetCombinerInputParameteriNV(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCombinerInputParameterivNV;
      BufferChecks.checkFunctionAddress(var5);
      IntBuffer var7 = APIUtil.getBufferInt(var4);
      nglGetCombinerInputParameterivNV(var0, var1, var2, var3, MemoryUtil.getAddress(var7), var5);
      return var7.get(0);
   }

   public static void glGetCombinerOutputParameterNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCombinerOutputParameterfvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetCombinerOutputParameterfvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetCombinerOutputParameterfvNV(int var0, int var1, int var2, long var3, long var5);

   public static float glGetCombinerOutputParameterfNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetCombinerOutputParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      FloatBuffer var6 = APIUtil.getBufferFloat(var3);
      nglGetCombinerOutputParameterfvNV(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetCombinerOutputParameterNV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCombinerOutputParameterivNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetCombinerOutputParameterivNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetCombinerOutputParameterivNV(int var0, int var1, int var2, long var3, long var5);

   public static int glGetCombinerOutputParameteriNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetCombinerOutputParameterivNV;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetCombinerOutputParameterivNV(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetFinalCombinerInputParameterNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFinalCombinerInputParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetFinalCombinerInputParameterfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFinalCombinerInputParameterfvNV(int var0, int var1, long var2, long var4);

   public static float glGetFinalCombinerInputParameterfNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFinalCombinerInputParameterfvNV;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetFinalCombinerInputParameterfvNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetFinalCombinerInputParameterNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFinalCombinerInputParameterivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetFinalCombinerInputParameterivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFinalCombinerInputParameterivNV(int var0, int var1, long var2, long var4);

   public static int glGetFinalCombinerInputParameteriNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFinalCombinerInputParameterivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetFinalCombinerInputParameterivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
