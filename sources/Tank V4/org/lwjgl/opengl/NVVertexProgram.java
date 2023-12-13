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

public final class NVVertexProgram extends NVProgram {
   public static final int GL_VERTEX_PROGRAM_NV = 34336;
   public static final int GL_VERTEX_PROGRAM_POINT_SIZE_NV = 34370;
   public static final int GL_VERTEX_PROGRAM_TWO_SIDE_NV = 34371;
   public static final int GL_VERTEX_STATE_PROGRAM_NV = 34337;
   public static final int GL_ATTRIB_ARRAY_SIZE_NV = 34339;
   public static final int GL_ATTRIB_ARRAY_STRIDE_NV = 34340;
   public static final int GL_ATTRIB_ARRAY_TYPE_NV = 34341;
   public static final int GL_CURRENT_ATTRIB_NV = 34342;
   public static final int GL_PROGRAM_PARAMETER_NV = 34372;
   public static final int GL_ATTRIB_ARRAY_POINTER_NV = 34373;
   public static final int GL_TRACK_MATRIX_NV = 34376;
   public static final int GL_TRACK_MATRIX_TRANSFORM_NV = 34377;
   public static final int GL_MAX_TRACK_MATRIX_STACK_DEPTH_NV = 34350;
   public static final int GL_MAX_TRACK_MATRICES_NV = 34351;
   public static final int GL_CURRENT_MATRIX_STACK_DEPTH_NV = 34368;
   public static final int GL_CURRENT_MATRIX_NV = 34369;
   public static final int GL_VERTEX_PROGRAM_BINDING_NV = 34378;
   public static final int GL_MODELVIEW_PROJECTION_NV = 34345;
   public static final int GL_MATRIX0_NV = 34352;
   public static final int GL_MATRIX1_NV = 34353;
   public static final int GL_MATRIX2_NV = 34354;
   public static final int GL_MATRIX3_NV = 34355;
   public static final int GL_MATRIX4_NV = 34356;
   public static final int GL_MATRIX5_NV = 34357;
   public static final int GL_MATRIX6_NV = 34358;
   public static final int GL_MATRIX7_NV = 34359;
   public static final int GL_IDENTITY_NV = 34346;
   public static final int GL_INVERSE_NV = 34347;
   public static final int GL_TRANSPOSE_NV = 34348;
   public static final int GL_INVERSE_TRANSPOSE_NV = 34349;
   public static final int GL_VERTEX_ATTRIB_ARRAY0_NV = 34384;
   public static final int GL_VERTEX_ATTRIB_ARRAY1_NV = 34385;
   public static final int GL_VERTEX_ATTRIB_ARRAY2_NV = 34386;
   public static final int GL_VERTEX_ATTRIB_ARRAY3_NV = 34387;
   public static final int GL_VERTEX_ATTRIB_ARRAY4_NV = 34388;
   public static final int GL_VERTEX_ATTRIB_ARRAY5_NV = 34389;
   public static final int GL_VERTEX_ATTRIB_ARRAY6_NV = 34390;
   public static final int GL_VERTEX_ATTRIB_ARRAY7_NV = 34391;
   public static final int GL_VERTEX_ATTRIB_ARRAY8_NV = 34392;
   public static final int GL_VERTEX_ATTRIB_ARRAY9_NV = 34393;
   public static final int GL_VERTEX_ATTRIB_ARRAY10_NV = 34394;
   public static final int GL_VERTEX_ATTRIB_ARRAY11_NV = 34395;
   public static final int GL_VERTEX_ATTRIB_ARRAY12_NV = 34396;
   public static final int GL_VERTEX_ATTRIB_ARRAY13_NV = 34397;
   public static final int GL_VERTEX_ATTRIB_ARRAY14_NV = 34398;
   public static final int GL_VERTEX_ATTRIB_ARRAY15_NV = 34399;
   public static final int GL_MAP1_VERTEX_ATTRIB0_4_NV = 34400;
   public static final int GL_MAP1_VERTEX_ATTRIB1_4_NV = 34401;
   public static final int GL_MAP1_VERTEX_ATTRIB2_4_NV = 34402;
   public static final int GL_MAP1_VERTEX_ATTRIB3_4_NV = 34403;
   public static final int GL_MAP1_VERTEX_ATTRIB4_4_NV = 34404;
   public static final int GL_MAP1_VERTEX_ATTRIB5_4_NV = 34405;
   public static final int GL_MAP1_VERTEX_ATTRIB6_4_NV = 34406;
   public static final int GL_MAP1_VERTEX_ATTRIB7_4_NV = 34407;
   public static final int GL_MAP1_VERTEX_ATTRIB8_4_NV = 34408;
   public static final int GL_MAP1_VERTEX_ATTRIB9_4_NV = 34409;
   public static final int GL_MAP1_VERTEX_ATTRIB10_4_NV = 34410;
   public static final int GL_MAP1_VERTEX_ATTRIB11_4_NV = 34411;
   public static final int GL_MAP1_VERTEX_ATTRIB12_4_NV = 34412;
   public static final int GL_MAP1_VERTEX_ATTRIB13_4_NV = 34413;
   public static final int GL_MAP1_VERTEX_ATTRIB14_4_NV = 34414;
   public static final int GL_MAP1_VERTEX_ATTRIB15_4_NV = 34415;
   public static final int GL_MAP2_VERTEX_ATTRIB0_4_NV = 34416;
   public static final int GL_MAP2_VERTEX_ATTRIB1_4_NV = 34417;
   public static final int GL_MAP2_VERTEX_ATTRIB2_4_NV = 34418;
   public static final int GL_MAP2_VERTEX_ATTRIB3_4_NV = 34419;
   public static final int GL_MAP2_VERTEX_ATTRIB4_4_NV = 34420;
   public static final int GL_MAP2_VERTEX_ATTRIB5_4_NV = 34421;
   public static final int GL_MAP2_VERTEX_ATTRIB6_4_NV = 34422;
   public static final int GL_MAP2_VERTEX_ATTRIB7_4_NV = 34423;
   public static final int GL_MAP2_VERTEX_ATTRIB8_4_NV = 34424;
   public static final int GL_MAP2_VERTEX_ATTRIB9_4_NV = 34425;
   public static final int GL_MAP2_VERTEX_ATTRIB10_4_NV = 34426;
   public static final int GL_MAP2_VERTEX_ATTRIB11_4_NV = 34427;
   public static final int GL_MAP2_VERTEX_ATTRIB12_4_NV = 34428;
   public static final int GL_MAP2_VERTEX_ATTRIB13_4_NV = 34429;
   public static final int GL_MAP2_VERTEX_ATTRIB14_4_NV = 34430;
   public static final int GL_MAP2_VERTEX_ATTRIB15_4_NV = 34431;

   private NVVertexProgram() {
   }

   public static void glExecuteProgramNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glExecuteProgramNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglExecuteProgramNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglExecuteProgramNV(int var0, int var1, long var2, long var4);

   public static void glGetProgramParameterNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetProgramParameterfvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetProgramParameterfvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetProgramParameterfvNV(int var0, int var1, int var2, long var3, long var5);

   public static void glGetProgramParameterNV(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetProgramParameterdvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 4);
      nglGetProgramParameterdvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetProgramParameterdvNV(int var0, int var1, int var2, long var3, long var5);

   public static void glGetTrackMatrixNV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTrackMatrixivNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetTrackMatrixivNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetTrackMatrixivNV(int var0, int var1, int var2, long var3, long var5);

   public static void glGetVertexAttribNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetVertexAttribfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribfvNV(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribNV(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribdvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetVertexAttribdvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribdvNV(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribivNV(int var0, int var1, long var2, long var4);

   public static ByteBuffer glGetVertexAttribPointerNV(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVertexAttribPointervNV;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglGetVertexAttribPointervNV(var0, var1, var2, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetVertexAttribPointervNV(int var0, int var1, long var2, long var4);

   public static void glProgramParameter4fNV(int var0, int var1, float var2, float var3, float var4, float var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramParameter4fNV;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramParameter4fNV(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramParameter4fNV(int var0, int var1, float var2, float var3, float var4, float var5, long var6);

   public static void glProgramParameter4dNV(int var0, int var1, double var2, double var4, double var6, double var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramParameter4dNV;
      BufferChecks.checkFunctionAddress(var11);
      nglProgramParameter4dNV(var0, var1, var2, var4, var6, var8, var11);
   }

   static native void nglProgramParameter4dNV(int var0, int var1, double var2, double var4, double var6, double var8, long var10);

   public static void glProgramParameters4NV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramParameters4fvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramParameters4fvNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramParameters4fvNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramParameters4NV(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramParameters4dvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramParameters4dvNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramParameters4dvNV(int var0, int var1, int var2, long var3, long var5);

   public static void glTrackMatrixNV(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTrackMatrixNV;
      BufferChecks.checkFunctionAddress(var5);
      nglTrackMatrixNV(var0, var1, var2, var3, var5);
   }

   static native void nglTrackMatrixNV(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexAttribPointerNV(int var0, int var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointerNV;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointerNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointerNV(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointerNV;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointerNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointerNV(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointerNV;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointerNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointerNV(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointerNV;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointerNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointerNV(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointerNV;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointerNV(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglVertexAttribPointerNV(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexAttribPointerNV(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointerNV;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOenabled(var6);
      nglVertexAttribPointerNVBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglVertexAttribPointerNVBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexAttrib1sNV(int var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttrib1sNV;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttrib1sNV(var0, var1, var3);
   }

   static native void nglVertexAttrib1sNV(int var0, short var1, long var2);

   public static void glVertexAttrib1fNV(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttrib1fNV;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttrib1fNV(var0, var1, var3);
   }

   static native void nglVertexAttrib1fNV(int var0, float var1, long var2);

   public static void glVertexAttrib1dNV(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib1dNV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib1dNV(var0, var1, var4);
   }

   static native void nglVertexAttrib1dNV(int var0, double var1, long var3);

   public static void glVertexAttrib2sNV(int var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib2sNV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib2sNV(var0, var1, var2, var4);
   }

   static native void nglVertexAttrib2sNV(int var0, short var1, short var2, long var3);

   public static void glVertexAttrib2fNV(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib2fNV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib2fNV(var0, var1, var2, var4);
   }

   static native void nglVertexAttrib2fNV(int var0, float var1, float var2, long var3);

   public static void glVertexAttrib2dNV(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib2dNV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib2dNV(var0, var1, var3, var6);
   }

   static native void nglVertexAttrib2dNV(int var0, double var1, double var3, long var5);

   public static void glVertexAttrib3sNV(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttrib3sNV;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttrib3sNV(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttrib3sNV(int var0, short var1, short var2, short var3, long var4);

   public static void glVertexAttrib3fNV(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttrib3fNV;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttrib3fNV(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttrib3fNV(int var0, float var1, float var2, float var3, long var4);

   public static void glVertexAttrib3dNV(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttrib3dNV;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttrib3dNV(var0, var1, var3, var5, var8);
   }

   static native void nglVertexAttrib3dNV(int var0, double var1, double var3, double var5, long var7);

   public static void glVertexAttrib4sNV(int var0, short var1, short var2, short var3, short var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4sNV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4sNV(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4sNV(int var0, short var1, short var2, short var3, short var4, long var5);

   public static void glVertexAttrib4fNV(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4fNV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4fNV(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4fNV(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glVertexAttrib4dNV(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexAttrib4dNV;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexAttrib4dNV(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexAttrib4dNV(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glVertexAttrib4ubNV(int var0, byte var1, byte var2, byte var3, byte var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4ubNV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4ubNV(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4ubNV(int var0, byte var1, byte var2, byte var3, byte var4, long var5);

   public static void glVertexAttribs1NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs1svNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs1svNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs1svNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs1NV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs1fvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs1fvNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs1fvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs1NV(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs1dvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs1dvNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs1dvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs2NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs2svNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs2svNV(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs2svNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs2NV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs2fvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs2fvNV(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs2fvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs2NV(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs2dvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs2dvNV(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs2dvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs3NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs3svNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs3svNV(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs3svNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs3NV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs3fvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs3fvNV(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs3fvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs3NV(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs3dvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs3dvNV(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs3dvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs4NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs4svNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs4svNV(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs4svNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs4NV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs4fvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs4fvNV(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs4fvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs4NV(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs4dvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs4dvNV(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs4dvNV(int var0, int var1, long var2, long var4);
}
