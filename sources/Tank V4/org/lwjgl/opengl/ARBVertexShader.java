package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class ARBVertexShader {
   public static final int GL_VERTEX_SHADER_ARB = 35633;
   public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS_ARB = 35658;
   public static final int GL_MAX_VARYING_FLOATS_ARB = 35659;
   public static final int GL_MAX_VERTEX_ATTRIBS_ARB = 34921;
   public static final int GL_MAX_TEXTURE_IMAGE_UNITS_ARB = 34930;
   public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS_ARB = 35660;
   public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS_ARB = 35661;
   public static final int GL_MAX_TEXTURE_COORDS_ARB = 34929;
   public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 34370;
   public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 34371;
   public static final int GL_OBJECT_ACTIVE_ATTRIBUTES_ARB = 35721;
   public static final int GL_OBJECT_ACTIVE_ATTRIBUTE_MAX_LENGTH_ARB = 35722;
   public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 34338;
   public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 34339;
   public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 34340;
   public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 34341;
   public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 34922;
   public static final int GL_CURRENT_VERTEX_ATTRIB_ARB = 34342;
   public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 34373;
   public static final int GL_FLOAT_VEC2_ARB = 35664;
   public static final int GL_FLOAT_VEC3_ARB = 35665;
   public static final int GL_FLOAT_VEC4_ARB = 35666;
   public static final int GL_FLOAT_MAT2_ARB = 35674;
   public static final int GL_FLOAT_MAT3_ARB = 35675;
   public static final int GL_FLOAT_MAT4_ARB = 35676;

   private ARBVertexShader() {
   }

   public static void glVertexAttrib1sARB(int var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttrib1sARB;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttrib1sARB(var0, var1, var3);
   }

   static native void nglVertexAttrib1sARB(int var0, short var1, long var2);

   public static void glVertexAttrib1fARB(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttrib1fARB;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttrib1fARB(var0, var1, var3);
   }

   static native void nglVertexAttrib1fARB(int var0, float var1, long var2);

   public static void glVertexAttrib1dARB(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib1dARB;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib1dARB(var0, var1, var4);
   }

   static native void nglVertexAttrib1dARB(int var0, double var1, long var3);

   public static void glVertexAttrib2sARB(int var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib2sARB;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib2sARB(var0, var1, var2, var4);
   }

   static native void nglVertexAttrib2sARB(int var0, short var1, short var2, long var3);

   public static void glVertexAttrib2fARB(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib2fARB;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib2fARB(var0, var1, var2, var4);
   }

   static native void nglVertexAttrib2fARB(int var0, float var1, float var2, long var3);

   public static void glVertexAttrib2dARB(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib2dARB;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib2dARB(var0, var1, var3, var6);
   }

   static native void nglVertexAttrib2dARB(int var0, double var1, double var3, long var5);

   public static void glVertexAttrib3sARB(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttrib3sARB;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttrib3sARB(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttrib3sARB(int var0, short var1, short var2, short var3, long var4);

   public static void glVertexAttrib3fARB(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttrib3fARB;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttrib3fARB(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttrib3fARB(int var0, float var1, float var2, float var3, long var4);

   public static void glVertexAttrib3dARB(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttrib3dARB;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttrib3dARB(var0, var1, var3, var5, var8);
   }

   static native void nglVertexAttrib3dARB(int var0, double var1, double var3, double var5, long var7);

   public static void glVertexAttrib4sARB(int var0, short var1, short var2, short var3, short var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4sARB;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4sARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4sARB(int var0, short var1, short var2, short var3, short var4, long var5);

   public static void glVertexAttrib4fARB(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4fARB;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4fARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4fARB(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glVertexAttrib4dARB(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexAttrib4dARB;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexAttrib4dARB(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexAttrib4dARB(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glVertexAttrib4NubARB(int var0, byte var1, byte var2, byte var3, byte var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4NubARB;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4NubARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4NubARB(int var0, byte var1, byte var2, byte var3, byte var4, long var5);

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointerARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointerARB(var0, var1, 5130, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointerARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointerARB(var0, var1, 5126, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, boolean var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointerARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointerARB(var0, var1, var2 ? 5121 : 5120, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, boolean var3, int var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointerARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointerARB(var0, var1, var2 ? 5125 : 5124, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, boolean var3, int var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointerARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointerARB(var0, var1, var2 ? 5123 : 5122, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglVertexAttribPointerARB(int var0, int var1, int var2, boolean var3, int var4, long var5, long var7);

   public static void glVertexAttribPointerARB(int var0, int var1, int var2, boolean var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttribPointerARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureArrayVBOenabled(var7);
      nglVertexAttribPointerARBBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglVertexAttribPointerARBBO(int var0, int var1, int var2, boolean var3, int var4, long var5, long var7);

   public static void glVertexAttribPointerARB(int var0, int var1, int var2, boolean var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointerARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointerARB(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glEnableVertexAttribArrayARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEnableVertexAttribArrayARB;
      BufferChecks.checkFunctionAddress(var2);
      nglEnableVertexAttribArrayARB(var0, var2);
   }

   static native void nglEnableVertexAttribArrayARB(int var0, long var1);

   public static void glDisableVertexAttribArrayARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDisableVertexAttribArrayARB;
      BufferChecks.checkFunctionAddress(var2);
      nglDisableVertexAttribArrayARB(var0, var2);
   }

   static native void nglDisableVertexAttribArrayARB(int var0, long var1);

   public static void glBindAttribLocationARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindAttribLocationARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      nglBindAttribLocationARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglBindAttribLocationARB(int var0, int var1, long var2, long var4);

   public static void glBindAttribLocationARB(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindAttribLocationARB;
      BufferChecks.checkFunctionAddress(var4);
      nglBindAttribLocationARB(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
   }

   public static void glGetActiveAttribARB(int var0, int var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetActiveAttribARB;
      BufferChecks.checkFunctionAddress(var7);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      BufferChecks.checkDirect(var5);
      nglGetActiveAttribARB(var0, var1, var5.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetActiveAttribARB(int var0, int var1, int var2, long var3, long var5, long var7, long var9, long var11);

   public static String glGetActiveAttribARB(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveAttribARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 2);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var2);
      nglGetActiveAttribARB(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var3, var3.position() + 1), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static String glGetActiveAttribARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveAttribARB;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetActiveAttribARB(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress0((Buffer)APIUtil.getBufferInt(var3)), MemoryUtil.getAddress((IntBuffer)APIUtil.getBufferInt(var3), 1), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static int glGetActiveAttribSizeARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveAttribARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveAttribARB(var0, var1, 0, 0L, MemoryUtil.getAddress(var5), MemoryUtil.getAddress((IntBuffer)var5, 1), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static int glGetActiveAttribTypeARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveAttribARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveAttribARB(var0, var1, 0, 0L, MemoryUtil.getAddress((IntBuffer)var5, 1), MemoryUtil.getAddress(var5), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static int glGetAttribLocationARB(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetAttribLocationARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetAttribLocationARB(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetAttribLocationARB(int var0, long var1, long var3);

   public static int glGetAttribLocationARB(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetAttribLocationARB;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetAttribLocationARB(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glGetVertexAttribARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribfvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetVertexAttribfvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribfvARB(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribdvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetVertexAttribdvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribdvARB(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribivARB(int var0, int var1, long var2, long var4);

   public static ByteBuffer glGetVertexAttribPointerARB(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVertexAttribPointervARB;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglGetVertexAttribPointervARB(var0, var1, var2, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetVertexAttribPointervARB(int var0, int var1, long var2, long var4);
}
