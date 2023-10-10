package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTVertexShader {
   public static final int GL_VERTEX_SHADER_EXT = 34688;
   public static final int GL_VERTEX_SHADER_BINDING_EXT = 34689;
   public static final int GL_OP_INDEX_EXT = 34690;
   public static final int GL_OP_NEGATE_EXT = 34691;
   public static final int GL_OP_DOT3_EXT = 34692;
   public static final int GL_OP_DOT4_EXT = 34693;
   public static final int GL_OP_MUL_EXT = 34694;
   public static final int GL_OP_ADD_EXT = 34695;
   public static final int GL_OP_MADD_EXT = 34696;
   public static final int GL_OP_FRAC_EXT = 34697;
   public static final int GL_OP_MAX_EXT = 34698;
   public static final int GL_OP_MIN_EXT = 34699;
   public static final int GL_OP_SET_GE_EXT = 34700;
   public static final int GL_OP_SET_LT_EXT = 34701;
   public static final int GL_OP_CLAMP_EXT = 34702;
   public static final int GL_OP_FLOOR_EXT = 34703;
   public static final int GL_OP_ROUND_EXT = 34704;
   public static final int GL_OP_EXP_BASE_2_EXT = 34705;
   public static final int GL_OP_LOG_BASE_2_EXT = 34706;
   public static final int GL_OP_POWER_EXT = 34707;
   public static final int GL_OP_RECIP_EXT = 34708;
   public static final int GL_OP_RECIP_SQRT_EXT = 34709;
   public static final int GL_OP_SUB_EXT = 34710;
   public static final int GL_OP_CROSS_PRODUCT_EXT = 34711;
   public static final int GL_OP_MULTIPLY_MATRIX_EXT = 34712;
   public static final int GL_OP_MOV_EXT = 34713;
   public static final int GL_OUTPUT_VERTEX_EXT = 34714;
   public static final int GL_OUTPUT_COLOR0_EXT = 34715;
   public static final int GL_OUTPUT_COLOR1_EXT = 34716;
   public static final int GL_OUTPUT_TEXTURE_COORD0_EXT = 34717;
   public static final int GL_OUTPUT_TEXTURE_COORD1_EXT = 34718;
   public static final int GL_OUTPUT_TEXTURE_COORD2_EXT = 34719;
   public static final int GL_OUTPUT_TEXTURE_COORD3_EXT = 34720;
   public static final int GL_OUTPUT_TEXTURE_COORD4_EXT = 34721;
   public static final int GL_OUTPUT_TEXTURE_COORD5_EXT = 34722;
   public static final int GL_OUTPUT_TEXTURE_COORD6_EXT = 34723;
   public static final int GL_OUTPUT_TEXTURE_COORD7_EXT = 34724;
   public static final int GL_OUTPUT_TEXTURE_COORD8_EXT = 34725;
   public static final int GL_OUTPUT_TEXTURE_COORD9_EXT = 34726;
   public static final int GL_OUTPUT_TEXTURE_COORD10_EXT = 34727;
   public static final int GL_OUTPUT_TEXTURE_COORD11_EXT = 34728;
   public static final int GL_OUTPUT_TEXTURE_COORD12_EXT = 34729;
   public static final int GL_OUTPUT_TEXTURE_COORD13_EXT = 34730;
   public static final int GL_OUTPUT_TEXTURE_COORD14_EXT = 34731;
   public static final int GL_OUTPUT_TEXTURE_COORD15_EXT = 34732;
   public static final int GL_OUTPUT_TEXTURE_COORD16_EXT = 34733;
   public static final int GL_OUTPUT_TEXTURE_COORD17_EXT = 34734;
   public static final int GL_OUTPUT_TEXTURE_COORD18_EXT = 34735;
   public static final int GL_OUTPUT_TEXTURE_COORD19_EXT = 34736;
   public static final int GL_OUTPUT_TEXTURE_COORD20_EXT = 34737;
   public static final int GL_OUTPUT_TEXTURE_COORD21_EXT = 34738;
   public static final int GL_OUTPUT_TEXTURE_COORD22_EXT = 34739;
   public static final int GL_OUTPUT_TEXTURE_COORD23_EXT = 34740;
   public static final int GL_OUTPUT_TEXTURE_COORD24_EXT = 34741;
   public static final int GL_OUTPUT_TEXTURE_COORD25_EXT = 34742;
   public static final int GL_OUTPUT_TEXTURE_COORD26_EXT = 34743;
   public static final int GL_OUTPUT_TEXTURE_COORD27_EXT = 34744;
   public static final int GL_OUTPUT_TEXTURE_COORD28_EXT = 34745;
   public static final int GL_OUTPUT_TEXTURE_COORD29_EXT = 34746;
   public static final int GL_OUTPUT_TEXTURE_COORD30_EXT = 34747;
   public static final int GL_OUTPUT_TEXTURE_COORD31_EXT = 34748;
   public static final int GL_OUTPUT_FOG_EXT = 34749;
   public static final int GL_SCALAR_EXT = 34750;
   public static final int GL_VECTOR_EXT = 34751;
   public static final int GL_MATRIX_EXT = 34752;
   public static final int GL_VARIANT_EXT = 34753;
   public static final int GL_INVARIANT_EXT = 34754;
   public static final int GL_LOCAL_CONSTANT_EXT = 34755;
   public static final int GL_LOCAL_EXT = 34756;
   public static final int GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT = 34757;
   public static final int GL_MAX_VERTEX_SHADER_VARIANTS_EXT = 34758;
   public static final int GL_MAX_VERTEX_SHADER_INVARIANTS_EXT = 34759;
   public static final int GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 34760;
   public static final int GL_MAX_VERTEX_SHADER_LOCALS_EXT = 34761;
   public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT = 34762;
   public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT = 34763;
   public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT = 34764;
   public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 34765;
   public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT = 34766;
   public static final int GL_VERTEX_SHADER_INSTRUCTIONS_EXT = 34767;
   public static final int GL_VERTEX_SHADER_VARIANTS_EXT = 34768;
   public static final int GL_VERTEX_SHADER_INVARIANTS_EXT = 34769;
   public static final int GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 34770;
   public static final int GL_VERTEX_SHADER_LOCALS_EXT = 34771;
   public static final int GL_VERTEX_SHADER_OPTIMIZED_EXT = 34772;
   public static final int GL_X_EXT = 34773;
   public static final int GL_Y_EXT = 34774;
   public static final int GL_Z_EXT = 34775;
   public static final int GL_W_EXT = 34776;
   public static final int GL_NEGATIVE_X_EXT = 34777;
   public static final int GL_NEGATIVE_Y_EXT = 34778;
   public static final int GL_NEGATIVE_Z_EXT = 34779;
   public static final int GL_NEGATIVE_W_EXT = 34780;
   public static final int GL_ZERO_EXT = 34781;
   public static final int GL_ONE_EXT = 34782;
   public static final int GL_NEGATIVE_ONE_EXT = 34783;
   public static final int GL_NORMALIZED_RANGE_EXT = 34784;
   public static final int GL_FULL_RANGE_EXT = 34785;
   public static final int GL_CURRENT_VERTEX_EXT = 34786;
   public static final int GL_MVP_MATRIX_EXT = 34787;
   public static final int GL_VARIANT_VALUE_EXT = 34788;
   public static final int GL_VARIANT_DATATYPE_EXT = 34789;
   public static final int GL_VARIANT_ARRAY_STRIDE_EXT = 34790;
   public static final int GL_VARIANT_ARRAY_TYPE_EXT = 34791;
   public static final int GL_VARIANT_ARRAY_EXT = 34792;
   public static final int GL_VARIANT_ARRAY_POINTER_EXT = 34793;
   public static final int GL_INVARIANT_VALUE_EXT = 34794;
   public static final int GL_INVARIANT_DATATYPE_EXT = 34795;
   public static final int GL_LOCAL_CONSTANT_VALUE_EXT = 34796;
   public static final int GL_LOCAL_CONSTANT_DATATYPE_EXT = 34797;

   private EXTVertexShader() {
   }

   public static void glBeginVertexShaderEXT() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glBeginVertexShaderEXT;
      BufferChecks.checkFunctionAddress(var1);
      nglBeginVertexShaderEXT(var1);
   }

   static native void nglBeginVertexShaderEXT(long var0);

   public static void glEndVertexShaderEXT() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndVertexShaderEXT;
      BufferChecks.checkFunctionAddress(var1);
      nglEndVertexShaderEXT(var1);
   }

   static native void nglEndVertexShaderEXT(long var0);

   public static void glBindVertexShaderEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBindVertexShaderEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglBindVertexShaderEXT(var0, var2);
   }

   static native void nglBindVertexShaderEXT(int var0, long var1);

   public static int glGenVertexShadersEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenVertexShadersEXT;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglGenVertexShadersEXT(var0, var2);
      return var4;
   }

   static native int nglGenVertexShadersEXT(int var0, long var1);

   public static void glDeleteVertexShaderEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteVertexShaderEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteVertexShaderEXT(var0, var2);
   }

   static native void nglDeleteVertexShaderEXT(int var0, long var1);

   public static void glShaderOp1EXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glShaderOp1EXT;
      BufferChecks.checkFunctionAddress(var4);
      nglShaderOp1EXT(var0, var1, var2, var4);
   }

   static native void nglShaderOp1EXT(int var0, int var1, int var2, long var3);

   public static void glShaderOp2EXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glShaderOp2EXT;
      BufferChecks.checkFunctionAddress(var5);
      nglShaderOp2EXT(var0, var1, var2, var3, var5);
   }

   static native void nglShaderOp2EXT(int var0, int var1, int var2, int var3, long var4);

   public static void glShaderOp3EXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glShaderOp3EXT;
      BufferChecks.checkFunctionAddress(var6);
      nglShaderOp3EXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglShaderOp3EXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glSwizzleEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glSwizzleEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglSwizzleEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglSwizzleEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glWriteMaskEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glWriteMaskEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglWriteMaskEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglWriteMaskEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glInsertComponentEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glInsertComponentEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglInsertComponentEXT(var0, var1, var2, var4);
   }

   static native void nglInsertComponentEXT(int var0, int var1, int var2, long var3);

   public static void glExtractComponentEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glExtractComponentEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglExtractComponentEXT(var0, var1, var2, var4);
   }

   static native void nglExtractComponentEXT(int var0, int var1, int var2, long var3);

   public static int glGenSymbolsEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGenSymbolsEXT;
      BufferChecks.checkFunctionAddress(var5);
      int var7 = nglGenSymbolsEXT(var0, var1, var2, var3, var5);
      return var7;
   }

   static native int nglGenSymbolsEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glSetInvariantEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSetInvariantEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 4);
      nglSetInvariantEXT(var0, 5130, MemoryUtil.getAddress(var1), var3);
   }

   public static void glSetInvariantEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSetInvariantEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglSetInvariantEXT(var0, 5126, MemoryUtil.getAddress(var1), var3);
   }

   public static void glSetInvariantEXT(int var0, boolean var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSetInvariantEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ByteBuffer)var2, 4);
      nglSetInvariantEXT(var0, var1 ? 5121 : 5120, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSetInvariantEXT(int var0, boolean var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSetInvariantEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglSetInvariantEXT(var0, var1 ? 5125 : 5124, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSetInvariantEXT(int var0, boolean var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSetInvariantEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ShortBuffer)var2, 4);
      nglSetInvariantEXT(var0, var1 ? 5123 : 5122, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglSetInvariantEXT(int var0, int var1, long var2, long var4);

   public static void glSetLocalConstantEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSetLocalConstantEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 4);
      nglSetLocalConstantEXT(var0, 5130, MemoryUtil.getAddress(var1), var3);
   }

   public static void glSetLocalConstantEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSetLocalConstantEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglSetLocalConstantEXT(var0, 5126, MemoryUtil.getAddress(var1), var3);
   }

   public static void glSetLocalConstantEXT(int var0, boolean var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSetLocalConstantEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ByteBuffer)var2, 4);
      nglSetLocalConstantEXT(var0, var1 ? 5121 : 5120, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSetLocalConstantEXT(int var0, boolean var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSetLocalConstantEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglSetLocalConstantEXT(var0, var1 ? 5125 : 5124, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSetLocalConstantEXT(int var0, boolean var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSetLocalConstantEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ShortBuffer)var2, 4);
      nglSetLocalConstantEXT(var0, var1 ? 5123 : 5122, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglSetLocalConstantEXT(int var0, int var1, long var2, long var4);

   public static void glVariantEXT(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantbvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 4);
      nglVariantbvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantbvEXT(int var0, long var1, long var3);

   public static void glVariantEXT(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantsvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ShortBuffer)var1, 4);
      nglVariantsvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantsvEXT(int var0, long var1, long var3);

   public static void glVariantEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglVariantivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantivEXT(int var0, long var1, long var3);

   public static void glVariantEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantfvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglVariantfvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantfvEXT(int var0, long var1, long var3);

   public static void glVariantEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantdvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 4);
      nglVariantdvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantdvEXT(int var0, long var1, long var3);

   public static void glVariantuEXT(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantubvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 4);
      nglVariantubvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantubvEXT(int var0, long var1, long var3);

   public static void glVariantuEXT(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantusvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ShortBuffer)var1, 4);
      nglVariantusvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantusvEXT(int var0, long var1, long var3);

   public static void glVariantuEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVariantuivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglVariantuivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVariantuivEXT(int var0, long var1, long var3);

   public static void glVariantPointerEXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVariantPointerEXT;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).EXT_vertex_shader_glVariantPointerEXT_pAddr = var2;
      }

      nglVariantPointerEXT(var0, 5130, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glVariantPointerEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVariantPointerEXT;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).EXT_vertex_shader_glVariantPointerEXT_pAddr = var2;
      }

      nglVariantPointerEXT(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glVariantPointerEXT(int var0, boolean var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVariantPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).EXT_vertex_shader_glVariantPointerEXT_pAddr = var3;
      }

      nglVariantPointerEXT(var0, var1 ? 5121 : 5120, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glVariantPointerEXT(int var0, boolean var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVariantPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).EXT_vertex_shader_glVariantPointerEXT_pAddr = var3;
      }

      nglVariantPointerEXT(var0, var1 ? 5125 : 5124, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glVariantPointerEXT(int var0, boolean var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVariantPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).EXT_vertex_shader_glVariantPointerEXT_pAddr = var3;
      }

      nglVariantPointerEXT(var0, var1 ? 5123 : 5122, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVariantPointerEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glVariantPointerEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVariantPointerEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglVariantPointerEXTBO(var0, var1, var2, var3, var6);
   }

   static native void nglVariantPointerEXTBO(int var0, int var1, int var2, long var3, long var5);

   public static void glEnableVariantClientStateEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEnableVariantClientStateEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglEnableVariantClientStateEXT(var0, var2);
   }

   static native void nglEnableVariantClientStateEXT(int var0, long var1);

   public static void glDisableVariantClientStateEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDisableVariantClientStateEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglDisableVariantClientStateEXT(var0, var2);
   }

   static native void nglDisableVariantClientStateEXT(int var0, long var1);

   public static int glBindLightParameterEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindLightParameterEXT;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglBindLightParameterEXT(var0, var1, var3);
      return var5;
   }

   static native int nglBindLightParameterEXT(int var0, int var1, long var2);

   public static int glBindMaterialParameterEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindMaterialParameterEXT;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglBindMaterialParameterEXT(var0, var1, var3);
      return var5;
   }

   static native int nglBindMaterialParameterEXT(int var0, int var1, long var2);

   public static int glBindTexGenParameterEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindTexGenParameterEXT;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglBindTexGenParameterEXT(var0, var1, var2, var4);
      return var6;
   }

   static native int nglBindTexGenParameterEXT(int var0, int var1, int var2, long var3);

   public static int glBindTextureUnitParameterEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindTextureUnitParameterEXT;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglBindTextureUnitParameterEXT(var0, var1, var3);
      return var5;
   }

   static native int nglBindTextureUnitParameterEXT(int var0, int var1, long var2);

   public static int glBindParameterEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBindParameterEXT;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglBindParameterEXT(var0, var2);
      return var4;
   }

   static native int nglBindParameterEXT(int var0, long var1);

   public static boolean glIsVariantEnabledEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsVariantEnabledEXT;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsVariantEnabledEXT(var0, var1, var3);
      return var5;
   }

   static native boolean nglIsVariantEnabledEXT(int var0, int var1, long var2);

   public static void glGetVariantBooleanEXT(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVariantBooleanvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ByteBuffer)var2, 4);
      nglGetVariantBooleanvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVariantBooleanvEXT(int var0, int var1, long var2, long var4);

   public static void glGetVariantIntegerEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVariantIntegervEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVariantIntegervEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVariantIntegervEXT(int var0, int var1, long var2, long var4);

   public static void glGetVariantFloatEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVariantFloatvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetVariantFloatvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVariantFloatvEXT(int var0, int var1, long var2, long var4);

   public static ByteBuffer glGetVariantPointerEXT(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVariantPointervEXT;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglGetVariantPointervEXT(var0, var1, var2, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetVariantPointervEXT(int var0, int var1, long var2, long var4);

   public static void glGetInvariantBooleanEXT(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetInvariantBooleanvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ByteBuffer)var2, 4);
      nglGetInvariantBooleanvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetInvariantBooleanvEXT(int var0, int var1, long var2, long var4);

   public static void glGetInvariantIntegerEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetInvariantIntegervEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetInvariantIntegervEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetInvariantIntegervEXT(int var0, int var1, long var2, long var4);

   public static void glGetInvariantFloatEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetInvariantFloatvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetInvariantFloatvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetInvariantFloatvEXT(int var0, int var1, long var2, long var4);

   public static void glGetLocalConstantBooleanEXT(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetLocalConstantBooleanvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ByteBuffer)var2, 4);
      nglGetLocalConstantBooleanvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetLocalConstantBooleanvEXT(int var0, int var1, long var2, long var4);

   public static void glGetLocalConstantIntegerEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetLocalConstantIntegervEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetLocalConstantIntegervEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetLocalConstantIntegervEXT(int var0, int var1, long var2, long var4);

   public static void glGetLocalConstantFloatEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetLocalConstantFloatvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetLocalConstantFloatvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetLocalConstantFloatvEXT(int var0, int var1, long var2, long var4);
}
