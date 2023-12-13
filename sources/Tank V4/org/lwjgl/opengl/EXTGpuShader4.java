package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTGpuShader4 {
   public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER_EXT = 35069;
   public static final int GL_SAMPLER_1D_ARRAY_EXT = 36288;
   public static final int GL_SAMPLER_2D_ARRAY_EXT = 36289;
   public static final int GL_SAMPLER_BUFFER_EXT = 36290;
   public static final int GL_SAMPLER_1D_ARRAY_SHADOW_EXT = 36291;
   public static final int GL_SAMPLER_2D_ARRAY_SHADOW_EXT = 36292;
   public static final int GL_SAMPLER_CUBE_SHADOW_EXT = 36293;
   public static final int GL_UNSIGNED_INT_VEC2_EXT = 36294;
   public static final int GL_UNSIGNED_INT_VEC3_EXT = 36295;
   public static final int GL_UNSIGNED_INT_VEC4_EXT = 36296;
   public static final int GL_INT_SAMPLER_1D_EXT = 36297;
   public static final int GL_INT_SAMPLER_2D_EXT = 36298;
   public static final int GL_INT_SAMPLER_3D_EXT = 36299;
   public static final int GL_INT_SAMPLER_CUBE_EXT = 36300;
   public static final int GL_INT_SAMPLER_2D_RECT_EXT = 36301;
   public static final int GL_INT_SAMPLER_1D_ARRAY_EXT = 36302;
   public static final int GL_INT_SAMPLER_2D_ARRAY_EXT = 36303;
   public static final int GL_INT_SAMPLER_BUFFER_EXT = 36304;
   public static final int GL_UNSIGNED_INT_SAMPLER_1D_EXT = 36305;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_EXT = 36306;
   public static final int GL_UNSIGNED_INT_SAMPLER_3D_EXT = 36307;
   public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_EXT = 36308;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT_EXT = 36309;
   public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY_EXT = 36310;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY_EXT = 36311;
   public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER_EXT = 36312;
   public static final int GL_MIN_PROGRAM_TEXEL_OFFSET_EXT = 35076;
   public static final int GL_MAX_PROGRAM_TEXEL_OFFSET_EXT = 35077;

   private EXTGpuShader4() {
   }

   public static void glVertexAttribI1iEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1iEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttribI1iEXT(var0, var1, var3);
   }

   static native void nglVertexAttribI1iEXT(int var0, int var1, long var2);

   public static void glVertexAttribI2iEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribI2iEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribI2iEXT(var0, var1, var2, var4);
   }

   static native void nglVertexAttribI2iEXT(int var0, int var1, int var2, long var3);

   public static void glVertexAttribI3iEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribI3iEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribI3iEXT(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribI3iEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexAttribI4iEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribI4iEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribI4iEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttribI4iEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glVertexAttribI1uiEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1uiEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttribI1uiEXT(var0, var1, var3);
   }

   static native void nglVertexAttribI1uiEXT(int var0, int var1, long var2);

   public static void glVertexAttribI2uiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribI2uiEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribI2uiEXT(var0, var1, var2, var4);
   }

   static native void nglVertexAttribI2uiEXT(int var0, int var1, int var2, long var3);

   public static void glVertexAttribI3uiEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribI3uiEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribI3uiEXT(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribI3uiEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexAttribI4uiEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribI4uiEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribI4uiEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttribI4uiEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glVertexAttribI1EXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1ivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      nglVertexAttribI1ivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI1ivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI2EXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI2ivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 2);
      nglVertexAttribI2ivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI2ivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI3EXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI3ivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglVertexAttribI3ivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI3ivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI4EXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4ivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglVertexAttribI4ivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4ivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI1uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      nglVertexAttribI1uivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI1uivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI2uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI2uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 2);
      nglVertexAttribI2uivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI2uivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI3uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI3uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglVertexAttribI3uivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI3uivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI4uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglVertexAttribI4uivEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4uivEXT(int var0, long var1, long var3);

   public static void glVertexAttribI4EXT(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4bvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 4);
      nglVertexAttribI4bvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4bvEXT(int var0, long var1, long var3);

   public static void glVertexAttribI4EXT(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4svEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ShortBuffer)var1, 4);
      nglVertexAttribI4svEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4svEXT(int var0, long var1, long var3);

   public static void glVertexAttribI4uEXT(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4ubvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 4);
      nglVertexAttribI4ubvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4ubvEXT(int var0, long var1, long var3);

   public static void glVertexAttribI4uEXT(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4usvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ShortBuffer)var1, 4);
      nglVertexAttribI4usvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4usvEXT(int var0, long var1, long var3);

   public static void glVertexAttribIPointerEXT(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribIPointerEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribIPointerEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribIPointerEXT(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribIPointerEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribIPointerEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribIPointerEXT(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribIPointerEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribIPointerEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglVertexAttribIPointerEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexAttribIPointerEXT(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribIPointerEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOenabled(var6);
      nglVertexAttribIPointerEXTBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglVertexAttribIPointerEXTBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetVertexAttribIEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribIivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribIivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribIivEXT(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribIuEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribIuivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribIuivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribIuivEXT(int var0, int var1, long var2, long var4);

   public static void glUniform1uiEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1uiEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglUniform1uiEXT(var0, var1, var3);
   }

   static native void nglUniform1uiEXT(int var0, int var1, long var2);

   public static void glUniform2uiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform2uiEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform2uiEXT(var0, var1, var2, var4);
   }

   static native void nglUniform2uiEXT(int var0, int var1, int var2, long var3);

   public static void glUniform3uiEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUniform3uiEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglUniform3uiEXT(var0, var1, var2, var3, var5);
   }

   static native void nglUniform3uiEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glUniform4uiEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform4uiEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform4uiEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglUniform4uiEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glUniform1uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1uivEXT(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1uivEXT(int var0, int var1, long var2, long var4);

   public static void glUniform2uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2uivEXT(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2uivEXT(int var0, int var1, long var2, long var4);

   public static void glUniform3uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3uivEXT(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3uivEXT(int var0, int var1, long var2, long var4);

   public static void glUniform4uEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4uivEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4uivEXT(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4uivEXT(int var0, int var1, long var2, long var4);

   public static void glGetUniformuEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformuivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetUniformuivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformuivEXT(int var0, int var1, long var2, long var4);

   public static void glBindFragDataLocationEXT(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindFragDataLocationEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      nglBindFragDataLocationEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglBindFragDataLocationEXT(int var0, int var1, long var2, long var4);

   public static void glBindFragDataLocationEXT(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindFragDataLocationEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglBindFragDataLocationEXT(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
   }

   public static int glGetFragDataLocationEXT(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFragDataLocationEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetFragDataLocationEXT(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetFragDataLocationEXT(int var0, long var1, long var3);

   public static int glGetFragDataLocationEXT(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFragDataLocationEXT;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetFragDataLocationEXT(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }
}
