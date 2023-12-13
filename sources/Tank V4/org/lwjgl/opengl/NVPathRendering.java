package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVPathRendering {
   public static final int GL_CLOSE_PATH_NV = 0;
   public static final int GL_MOVE_TO_NV = 2;
   public static final int GL_RELATIVE_MOVE_TO_NV = 3;
   public static final int GL_LINE_TO_NV = 4;
   public static final int GL_RELATIVE_LINE_TO_NV = 5;
   public static final int GL_HORIZONTAL_LINE_TO_NV = 6;
   public static final int GL_RELATIVE_HORIZONTAL_LINE_TO_NV = 7;
   public static final int GL_VERTICAL_LINE_TO_NV = 8;
   public static final int GL_RELATIVE_VERTICAL_LINE_TO_NV = 9;
   public static final int GL_QUADRATIC_CURVE_TO_NV = 10;
   public static final int GL_RELATIVE_QUADRATIC_CURVE_TO_NV = 11;
   public static final int GL_CUBIC_CURVE_TO_NV = 12;
   public static final int GL_RELATIVE_CUBIC_CURVE_TO_NV = 13;
   public static final int GL_SMOOTH_QUADRATIC_CURVE_TO_NV = 14;
   public static final int GL_RELATIVE_SMOOTH_QUADRATIC_CURVE_TO_NV = 15;
   public static final int GL_SMOOTH_CUBIC_CURVE_TO_NV = 16;
   public static final int GL_RELATIVE_SMOOTH_CUBIC_CURVE_TO_NV = 17;
   public static final int GL_SMALL_CCW_ARC_TO_NV = 18;
   public static final int GL_RELATIVE_SMALL_CCW_ARC_TO_NV = 19;
   public static final int GL_SMALL_CW_ARC_TO_NV = 20;
   public static final int GL_RELATIVE_SMALL_CW_ARC_TO_NV = 21;
   public static final int GL_LARGE_CCW_ARC_TO_NV = 22;
   public static final int GL_RELATIVE_LARGE_CCW_ARC_TO_NV = 23;
   public static final int GL_LARGE_CW_ARC_TO_NV = 24;
   public static final int GL_RELATIVE_LARGE_CW_ARC_TO_NV = 25;
   public static final int GL_CIRCULAR_CCW_ARC_TO_NV = 248;
   public static final int GL_CIRCULAR_CW_ARC_TO_NV = 250;
   public static final int GL_CIRCULAR_TANGENT_ARC_TO_NV = 252;
   public static final int GL_ARC_TO_NV = 254;
   public static final int GL_RELATIVE_ARC_TO_NV = 255;
   public static final int GL_PATH_FORMAT_SVG_NV = 36976;
   public static final int GL_PATH_FORMAT_PS_NV = 36977;
   public static final int GL_STANDARD_FONT_NAME_NV = 36978;
   public static final int GL_SYSTEM_FONT_NAME_NV = 36979;
   public static final int GL_FILE_NAME_NV = 36980;
   public static final int GL_SKIP_MISSING_GLYPH_NV = 37033;
   public static final int GL_USE_MISSING_GLYPH_NV = 37034;
   public static final int GL_PATH_STROKE_WIDTH_NV = 36981;
   public static final int GL_PATH_INITIAL_END_CAP_NV = 36983;
   public static final int GL_PATH_TERMINAL_END_CAP_NV = 36984;
   public static final int GL_PATH_JOIN_STYLE_NV = 36985;
   public static final int GL_PATH_MITER_LIMIT_NV = 36986;
   public static final int GL_PATH_INITIAL_DASH_CAP_NV = 36988;
   public static final int GL_PATH_TERMINAL_DASH_CAP_NV = 36989;
   public static final int GL_PATH_DASH_OFFSET_NV = 36990;
   public static final int GL_PATH_CLIENT_LENGTH_NV = 36991;
   public static final int GL_PATH_DASH_OFFSET_RESET_NV = 37044;
   public static final int GL_PATH_FILL_MODE_NV = 36992;
   public static final int GL_PATH_FILL_MASK_NV = 36993;
   public static final int GL_PATH_FILL_COVER_MODE_NV = 36994;
   public static final int GL_PATH_STROKE_COVER_MODE_NV = 36995;
   public static final int GL_PATH_STROKE_MASK_NV = 36996;
   public static final int GL_PATH_END_CAPS_NV = 36982;
   public static final int GL_PATH_DASH_CAPS_NV = 36987;
   public static final int GL_COUNT_UP_NV = 37000;
   public static final int GL_COUNT_DOWN_NV = 37001;
   public static final int GL_PRIMARY_COLOR = 34167;
   public static final int GL_PRIMARY_COLOR_NV = 34092;
   public static final int GL_SECONDARY_COLOR_NV = 34093;
   public static final int GL_PATH_OBJECT_BOUNDING_BOX_NV = 37002;
   public static final int GL_CONVEX_HULL_NV = 37003;
   public static final int GL_BOUNDING_BOX_NV = 37005;
   public static final int GL_TRANSLATE_X_NV = 37006;
   public static final int GL_TRANSLATE_Y_NV = 37007;
   public static final int GL_TRANSLATE_2D_NV = 37008;
   public static final int GL_TRANSLATE_3D_NV = 37009;
   public static final int GL_AFFINE_2D_NV = 37010;
   public static final int GL_AFFINE_3D_NV = 37012;
   public static final int GL_TRANSPOSE_AFFINE_2D_NV = 37014;
   public static final int GL_TRANSPOSE_AFFINE_3D_NV = 37016;
   public static final int GL_UTF8_NV = 37018;
   public static final int GL_UTF16_NV = 37019;
   public static final int GL_BOUNDING_BOX_OF_BOUNDING_BOXES_NV = 37020;
   public static final int GL_PATH_COMMAND_COUNT_NV = 37021;
   public static final int GL_PATH_COORD_COUNT_NV = 37022;
   public static final int GL_PATH_DASH_ARRAY_COUNT_NV = 37023;
   public static final int GL_PATH_COMPUTED_LENGTH_NV = 37024;
   public static final int GL_PATH_FILL_BOUNDING_BOX_NV = 37025;
   public static final int GL_PATH_STROKE_BOUNDING_BOX_NV = 37026;
   public static final int GL_SQUARE_NV = 37027;
   public static final int GL_ROUND_NV = 37028;
   public static final int GL_TRIANGULAR_NV = 37029;
   public static final int GL_BEVEL_NV = 37030;
   public static final int GL_MITER_REVERT_NV = 37031;
   public static final int GL_MITER_TRUNCATE_NV = 37032;
   public static final int GL_MOVE_TO_RESETS_NV = 37045;
   public static final int GL_MOVE_TO_CONTINUES_NV = 37046;
   public static final int GL_BOLD_BIT_NV = 1;
   public static final int GL_ITALIC_BIT_NV = 2;
   public static final int GL_PATH_ERROR_POSITION_NV = 37035;
   public static final int GL_PATH_FOG_GEN_MODE_NV = 37036;
   public static final int GL_PATH_STENCIL_FUNC_NV = 37047;
   public static final int GL_PATH_STENCIL_REF_NV = 37048;
   public static final int GL_PATH_STENCIL_VALUE_MASK_NV = 37049;
   public static final int GL_PATH_STENCIL_DEPTH_OFFSET_FACTOR_NV = 37053;
   public static final int GL_PATH_STENCIL_DEPTH_OFFSET_UNITS_NV = 37054;
   public static final int GL_PATH_COVER_DEPTH_FUNC_NV = 37055;
   public static final int GL_GLYPH_WIDTH_BIT_NV = 1;
   public static final int GL_GLYPH_HEIGHT_BIT_NV = 2;
   public static final int GL_GLYPH_HORIZONTAL_BEARING_X_BIT_NV = 4;
   public static final int GL_GLYPH_HORIZONTAL_BEARING_Y_BIT_NV = 8;
   public static final int GL_GLYPH_HORIZONTAL_BEARING_ADVANCE_BIT_NV = 16;
   public static final int GL_GLYPH_VERTICAL_BEARING_X_BIT_NV = 32;
   public static final int GL_GLYPH_VERTICAL_BEARING_Y_BIT_NV = 64;
   public static final int GL_GLYPH_VERTICAL_BEARING_ADVANCE_BIT_NV = 128;
   public static final int GL_GLYPH_HAS_KERNING_NV = 256;
   public static final int GL_FONT_X_MIN_BOUNDS_NV = 65536;
   public static final int GL_FONT_Y_MIN_BOUNDS_NV = 131072;
   public static final int GL_FONT_X_MAX_BOUNDS_NV = 262144;
   public static final int GL_FONT_Y_MAX_BOUNDS_NV = 524288;
   public static final int GL_FONT_UNITS_PER_EM_NV = 1048576;
   public static final int GL_FONT_ASCENDER_NV = 2097152;
   public static final int GL_FONT_DESCENDER_NV = 4194304;
   public static final int GL_FONT_HEIGHT_NV = 8388608;
   public static final int GL_FONT_MAX_ADVANCE_WIDTH_NV = 16777216;
   public static final int GL_FONT_MAX_ADVANCE_HEIGHT_NV = 33554432;
   public static final int GL_FONT_UNDERLINE_POSITION_NV = 67108864;
   public static final int GL_FONT_UNDERLINE_THICKNESS_NV = 134217728;
   public static final int GL_FONT_HAS_KERNING_NV = 268435456;
   public static final int GL_ACCUM_ADJACENT_PAIRS_NV = 37037;
   public static final int GL_ADJACENT_PAIRS_NV = 37038;
   public static final int GL_FIRST_TO_REST_NV = 37039;
   public static final int GL_PATH_GEN_MODE_NV = 37040;
   public static final int GL_PATH_GEN_COEFF_NV = 37041;
   public static final int GL_PATH_GEN_COLOR_FORMAT_NV = 37042;
   public static final int GL_PATH_GEN_COMPONENTS_NV = 37043;

   private NVPathRendering() {
   }

   public static void glPathCommandsNV(int var0, ByteBuffer var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glPathCommandsNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkDirect(var3);
      nglPathCommandsNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3.remaining(), var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglPathCommandsNV(int var0, int var1, long var2, int var4, int var5, long var6, long var8);

   public static void glPathCoordsNV(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathCoordsNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglPathCoordsNV(var0, var2.remaining(), var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglPathCoordsNV(int var0, int var1, int var2, long var3, long var5);

   public static void glPathSubCommandsNV(int var0, int var1, int var2, ByteBuffer var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glPathSubCommandsNV;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var5);
      nglPathSubCommandsNV(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5.remaining(), var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglPathSubCommandsNV(int var0, int var1, int var2, int var3, long var4, int var6, int var7, long var8, long var10);

   public static void glPathSubCoordsNV(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glPathSubCoordsNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglPathSubCoordsNV(var0, var1, var3.remaining(), var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglPathSubCoordsNV(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glPathStringNV(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathStringNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglPathStringNV(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglPathStringNV(int var0, int var1, int var2, long var3, long var5);

   public static void glPathGlyphsNV(int var0, int var1, ByteBuffer var2, int var3, int var4, ByteBuffer var5, int var6, int var7, float var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glPathGlyphsNV;
      BufferChecks.checkFunctionAddress(var10);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      BufferChecks.checkDirect(var5);
      nglPathGlyphsNV(var0, var1, MemoryUtil.getAddress(var2), var3, var5.remaining() / GLChecks.calculateBytesPerCharCode(var4), var4, MemoryUtil.getAddress(var5), var6, var7, var8, var10);
   }

   static native void nglPathGlyphsNV(int var0, int var1, long var2, int var4, int var5, int var6, long var7, int var9, int var10, float var11, long var12);

   public static void glPathGlyphRangeNV(int var0, int var1, ByteBuffer var2, int var3, int var4, int var5, int var6, int var7, float var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glPathGlyphRangeNV;
      BufferChecks.checkFunctionAddress(var10);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      nglPathGlyphRangeNV(var0, var1, MemoryUtil.getAddress(var2), var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglPathGlyphRangeNV(int var0, int var1, long var2, int var4, int var5, int var6, int var7, int var8, float var9, long var10);

   public static void glWeightPathsNV(int var0, IntBuffer var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWeightPathsNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkBuffer(var2, var1.remaining());
      nglWeightPathsNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglWeightPathsNV(int var0, int var1, long var2, long var4, long var6);

   public static void glCopyPathNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCopyPathNV;
      BufferChecks.checkFunctionAddress(var3);
      nglCopyPathNV(var0, var1, var3);
   }

   static native void nglCopyPathNV(int var0, int var1, long var2);

   public static void glInterpolatePathsNV(int var0, int var1, int var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glInterpolatePathsNV;
      BufferChecks.checkFunctionAddress(var5);
      nglInterpolatePathsNV(var0, var1, var2, var3, var5);
   }

   static native void nglInterpolatePathsNV(int var0, int var1, int var2, float var3, long var4);

   public static void glTransformPathNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTransformPathNV;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkBuffer(var3, GLChecks.calculateTransformPathValues(var2));
      }

      nglTransformPathNV(var0, var1, var2, MemoryUtil.getAddressSafe(var3), var5);
   }

   static native void nglTransformPathNV(int var0, int var1, int var2, long var3, long var5);

   public static void glPathParameterNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathParameterivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglPathParameterivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglPathParameterivNV(int var0, int var1, long var2, long var4);

   public static void glPathParameteriNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathParameteriNV;
      BufferChecks.checkFunctionAddress(var4);
      nglPathParameteriNV(var0, var1, var2, var4);
   }

   static native void nglPathParameteriNV(int var0, int var1, int var2, long var3);

   public static void glPathParameterfNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglPathParameterfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglPathParameterfvNV(int var0, int var1, long var2, long var4);

   public static void glPathParameterfNV(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathParameterfNV;
      BufferChecks.checkFunctionAddress(var4);
      nglPathParameterfNV(var0, var1, var2, var4);
   }

   static native void nglPathParameterfNV(int var0, int var1, float var2, long var3);

   public static void glPathDashArrayNV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPathDashArrayNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglPathDashArrayNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPathDashArrayNV(int var0, int var1, long var2, long var4);

   public static int glGenPathsNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenPathsNV;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglGenPathsNV(var0, var2);
      return var4;
   }

   static native int nglGenPathsNV(int var0, long var1);

   public static void glDeletePathsNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDeletePathsNV;
      BufferChecks.checkFunctionAddress(var3);
      nglDeletePathsNV(var0, var1, var3);
   }

   static native void nglDeletePathsNV(int var0, int var1, long var2);

   public static boolean glIsPathNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsPathNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsPathNV(var0, var2);
      return var4;
   }

   static native boolean nglIsPathNV(int var0, long var1);

   public static void glPathStencilFuncNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathStencilFuncNV;
      BufferChecks.checkFunctionAddress(var4);
      nglPathStencilFuncNV(var0, var1, var2, var4);
   }

   static native void nglPathStencilFuncNV(int var0, int var1, int var2, long var3);

   public static void glPathStencilDepthOffsetNV(float var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPathStencilDepthOffsetNV;
      BufferChecks.checkFunctionAddress(var3);
      nglPathStencilDepthOffsetNV(var0, var1, var3);
   }

   static native void nglPathStencilDepthOffsetNV(float var0, int var1, long var2);

   public static void glStencilFillPathNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glStencilFillPathNV;
      BufferChecks.checkFunctionAddress(var4);
      nglStencilFillPathNV(var0, var1, var2, var4);
   }

   static native void nglStencilFillPathNV(int var0, int var1, int var2, long var3);

   public static void glStencilStrokePathNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glStencilStrokePathNV;
      BufferChecks.checkFunctionAddress(var4);
      nglStencilStrokePathNV(var0, var1, var2, var4);
   }

   static native void nglStencilStrokePathNV(int var0, int var1, int var2, long var3);

   public static void glStencilFillPathInstancedNV(int var0, ByteBuffer var1, int var2, int var3, int var4, int var5, FloatBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glStencilFillPathInstancedNV;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var1);
      if (var6 != null) {
         BufferChecks.checkBuffer(var6, GLChecks.calculateTransformPathValues(var5));
      }

      nglStencilFillPathInstancedNV(var1.remaining() / GLChecks.calculateBytesPerPathName(var0), var0, MemoryUtil.getAddress(var1), var2, var3, var4, var5, MemoryUtil.getAddressSafe(var6), var8);
   }

   static native void nglStencilFillPathInstancedNV(int var0, int var1, long var2, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glStencilStrokePathInstancedNV(int var0, ByteBuffer var1, int var2, int var3, int var4, int var5, FloatBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glStencilStrokePathInstancedNV;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var1);
      if (var6 != null) {
         BufferChecks.checkBuffer(var6, GLChecks.calculateTransformPathValues(var5));
      }

      nglStencilStrokePathInstancedNV(var1.remaining() / GLChecks.calculateBytesPerPathName(var0), var0, MemoryUtil.getAddress(var1), var2, var3, var4, var5, MemoryUtil.getAddressSafe(var6), var8);
   }

   static native void nglStencilStrokePathInstancedNV(int var0, int var1, long var2, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glPathCoverDepthFuncNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPathCoverDepthFuncNV;
      BufferChecks.checkFunctionAddress(var2);
      nglPathCoverDepthFuncNV(var0, var2);
   }

   static native void nglPathCoverDepthFuncNV(int var0, long var1);

   public static void glPathColorGenNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glPathColorGenNV;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkBuffer(var3, GLChecks.calculatePathColorGenCoeffsCount(var1, var2));
      }

      nglPathColorGenNV(var0, var1, var2, MemoryUtil.getAddressSafe(var3), var5);
   }

   static native void nglPathColorGenNV(int var0, int var1, int var2, long var3, long var5);

   public static void glPathTexGenNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPathTexGenNV;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      nglPathTexGenNV(var0, var1, GLChecks.calculatePathTextGenCoeffsPerComponent(var2, var1), MemoryUtil.getAddressSafe(var2), var4);
   }

   static native void nglPathTexGenNV(int var0, int var1, int var2, long var3, long var5);

   public static void glPathFogGenNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPathFogGenNV;
      BufferChecks.checkFunctionAddress(var2);
      nglPathFogGenNV(var0, var2);
   }

   static native void nglPathFogGenNV(int var0, long var1);

   public static void glCoverFillPathNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCoverFillPathNV;
      BufferChecks.checkFunctionAddress(var3);
      nglCoverFillPathNV(var0, var1, var3);
   }

   static native void nglCoverFillPathNV(int var0, int var1, long var2);

   public static void glCoverStrokePathNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCoverStrokePathNV;
      BufferChecks.checkFunctionAddress(var3);
      nglCoverStrokePathNV(var0, var1, var3);
   }

   static native void nglCoverStrokePathNV(int var0, int var1, long var2);

   public static void glCoverFillPathInstancedNV(int var0, ByteBuffer var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCoverFillPathInstancedNV;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var1);
      if (var5 != null) {
         BufferChecks.checkBuffer(var5, GLChecks.calculateTransformPathValues(var4));
      }

      nglCoverFillPathInstancedNV(var1.remaining() / GLChecks.calculateBytesPerPathName(var0), var0, MemoryUtil.getAddress(var1), var2, var3, var4, MemoryUtil.getAddressSafe(var5), var7);
   }

   static native void nglCoverFillPathInstancedNV(int var0, int var1, long var2, int var4, int var5, int var6, long var7, long var9);

   public static void glCoverStrokePathInstancedNV(int var0, ByteBuffer var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCoverStrokePathInstancedNV;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var1);
      if (var5 != null) {
         BufferChecks.checkBuffer(var5, GLChecks.calculateTransformPathValues(var4));
      }

      nglCoverStrokePathInstancedNV(var1.remaining() / GLChecks.calculateBytesPerPathName(var0), var0, MemoryUtil.getAddress(var1), var2, var3, var4, MemoryUtil.getAddressSafe(var5), var7);
   }

   static native void nglCoverStrokePathInstancedNV(int var0, int var1, long var2, int var4, int var5, int var6, long var7, long var9);

   public static void glGetPathParameterNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPathParameterivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetPathParameterivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetPathParameterivNV(int var0, int var1, long var2, long var4);

   public static int glGetPathParameteriNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathParameterivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetPathParameterivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetPathParameterfvNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPathParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetPathParameterfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetPathParameterfvNV(int var0, int var1, long var2, long var4);

   public static float glGetPathParameterfNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathParameterfvNV;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetPathParameterfvNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetPathCommandsNV(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathCommandsNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglGetPathCommandsNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetPathCommandsNV(int var0, long var1, long var3);

   public static void glGetPathCoordsNV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathCoordsNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglGetPathCoordsNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetPathCoordsNV(int var0, long var1, long var3);

   public static void glGetPathDashArrayNV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathDashArrayNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglGetPathDashArrayNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetPathDashArrayNV(int var0, long var1, long var3);

   public static void glGetPathMetricsNV(int var0, int var1, ByteBuffer var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetPathMetricsNV;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkBuffer(var5, GLChecks.calculateMetricsSize(var0, var4));
      nglGetPathMetricsNV(var0, var2.remaining() / GLChecks.calculateBytesPerPathName(var1), var1, MemoryUtil.getAddress(var2), var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetPathMetricsNV(int var0, int var1, int var2, long var3, int var5, int var6, long var7, long var9);

   public static void glGetPathMetricRangeNV(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetPathMetricRangeNV;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer(var4, GLChecks.calculateMetricsSize(var0, var3));
      nglGetPathMetricRangeNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetPathMetricRangeNV(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetPathSpacingNV(int var0, int var1, ByteBuffer var2, int var3, float var4, float var5, int var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glGetPathSpacingNV;
      BufferChecks.checkFunctionAddress(var9);
      int var11 = var2.remaining() / GLChecks.calculateBytesPerPathName(var1);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkBuffer(var7, var11 - 1);
      nglGetPathSpacingNV(var0, var11, var1, MemoryUtil.getAddress(var2), var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   static native void nglGetPathSpacingNV(int var0, int var1, int var2, long var3, int var5, float var6, float var7, int var8, long var9, long var11);

   public static void glGetPathColorGenNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPathColorGenivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 16);
      nglGetPathColorGenivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetPathColorGenivNV(int var0, int var1, long var2, long var4);

   public static int glGetPathColorGeniNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathColorGenivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetPathColorGenivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetPathColorGenNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPathColorGenfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 16);
      nglGetPathColorGenfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetPathColorGenfvNV(int var0, int var1, long var2, long var4);

   public static float glGetPathColorGenfNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathColorGenfvNV;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetPathColorGenfvNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetPathTexGenNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPathTexGenivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 16);
      nglGetPathTexGenivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetPathTexGenivNV(int var0, int var1, long var2, long var4);

   public static int glGetPathTexGeniNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathTexGenivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetPathTexGenivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetPathTexGenNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPathTexGenfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 16);
      nglGetPathTexGenfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetPathTexGenfvNV(int var0, int var1, long var2, long var4);

   public static float glGetPathTexGenfNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPathTexGenfvNV;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetPathTexGenfvNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static boolean glIsPointInFillPathNV(int var0, int var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glIsPointInFillPathNV;
      BufferChecks.checkFunctionAddress(var5);
      boolean var7 = nglIsPointInFillPathNV(var0, var1, var2, var3, var5);
      return var7;
   }

   static native boolean nglIsPointInFillPathNV(int var0, int var1, float var2, float var3, long var4);

   public static boolean glIsPointInStrokePathNV(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glIsPointInStrokePathNV;
      BufferChecks.checkFunctionAddress(var4);
      boolean var6 = nglIsPointInStrokePathNV(var0, var1, var2, var4);
      return var6;
   }

   static native boolean nglIsPointInStrokePathNV(int var0, float var1, float var2, long var3);

   public static float glGetPathLengthNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPathLengthNV;
      BufferChecks.checkFunctionAddress(var4);
      float var6 = nglGetPathLengthNV(var0, var1, var2, var4);
      return var6;
   }

   static native float nglGetPathLengthNV(int var0, int var1, int var2, long var3);

   public static boolean glPointAlongPathNV(int var0, int var1, int var2, float var3, FloatBuffer var4, FloatBuffer var5, FloatBuffer var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glPointAlongPathNV;
      BufferChecks.checkFunctionAddress(var9);
      if (var4 != null) {
         BufferChecks.checkBuffer((FloatBuffer)var4, 1);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer((FloatBuffer)var5, 1);
      }

      if (var6 != null) {
         BufferChecks.checkBuffer((FloatBuffer)var6, 1);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((FloatBuffer)var7, 1);
      }

      boolean var11 = nglPointAlongPathNV(var0, var1, var2, var3, MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var9);
      return var11;
   }

   static native boolean nglPointAlongPathNV(int var0, int var1, int var2, float var3, long var4, long var6, long var8, long var10, long var12);
}
