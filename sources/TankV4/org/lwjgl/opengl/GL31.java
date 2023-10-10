package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GL31 {
   public static final int GL_RED_SNORM = 36752;
   public static final int GL_RG_SNORM = 36753;
   public static final int GL_RGB_SNORM = 36754;
   public static final int GL_RGBA_SNORM = 36755;
   public static final int GL_R8_SNORM = 36756;
   public static final int GL_RG8_SNORM = 36757;
   public static final int GL_RGB8_SNORM = 36758;
   public static final int GL_RGBA8_SNORM = 36759;
   public static final int GL_R16_SNORM = 36760;
   public static final int GL_RG16_SNORM = 36761;
   public static final int GL_RGB16_SNORM = 36762;
   public static final int GL_RGBA16_SNORM = 36763;
   public static final int GL_SIGNED_NORMALIZED = 36764;
   public static final int GL_COPY_READ_BUFFER_BINDING = 36662;
   public static final int GL_COPY_WRITE_BUFFER_BINDING = 36663;
   public static final int GL_COPY_READ_BUFFER = 36662;
   public static final int GL_COPY_WRITE_BUFFER = 36663;
   public static final int GL_PRIMITIVE_RESTART = 36765;
   public static final int GL_PRIMITIVE_RESTART_INDEX = 36766;
   public static final int GL_TEXTURE_BUFFER = 35882;
   public static final int GL_MAX_TEXTURE_BUFFER_SIZE = 35883;
   public static final int GL_TEXTURE_BINDING_BUFFER = 35884;
   public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING = 35885;
   public static final int GL_TEXTURE_BUFFER_FORMAT = 35886;
   public static final int GL_TEXTURE_RECTANGLE = 34037;
   public static final int GL_TEXTURE_BINDING_RECTANGLE = 34038;
   public static final int GL_PROXY_TEXTURE_RECTANGLE = 34039;
   public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE = 34040;
   public static final int GL_SAMPLER_2D_RECT = 35683;
   public static final int GL_SAMPLER_2D_RECT_SHADOW = 35684;
   public static final int GL_UNIFORM_BUFFER = 35345;
   public static final int GL_UNIFORM_BUFFER_BINDING = 35368;
   public static final int GL_UNIFORM_BUFFER_START = 35369;
   public static final int GL_UNIFORM_BUFFER_SIZE = 35370;
   public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 35371;
   public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 35372;
   public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 35373;
   public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 35374;
   public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 35375;
   public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 35376;
   public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 35377;
   public static final int GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 35378;
   public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 35379;
   public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 35380;
   public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 35381;
   public static final int GL_ACTIVE_UNIFORM_BLOCKS = 35382;
   public static final int GL_UNIFORM_TYPE = 35383;
   public static final int GL_UNIFORM_SIZE = 35384;
   public static final int GL_UNIFORM_NAME_LENGTH = 35385;
   public static final int GL_UNIFORM_BLOCK_INDEX = 35386;
   public static final int GL_UNIFORM_OFFSET = 35387;
   public static final int GL_UNIFORM_ARRAY_STRIDE = 35388;
   public static final int GL_UNIFORM_MATRIX_STRIDE = 35389;
   public static final int GL_UNIFORM_IS_ROW_MAJOR = 35390;
   public static final int GL_UNIFORM_BLOCK_BINDING = 35391;
   public static final int GL_UNIFORM_BLOCK_DATA_SIZE = 35392;
   public static final int GL_UNIFORM_BLOCK_NAME_LENGTH = 35393;
   public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 35394;
   public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 35395;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 35396;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 35397;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 35398;
   public static final int GL_INVALID_INDEX = -1;

   private GL31() {
   }

   public static void glDrawArraysInstanced(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawArraysInstanced;
      BufferChecks.checkFunctionAddress(var5);
      nglDrawArraysInstanced(var0, var1, var2, var3, var5);
   }

   static native void nglDrawArraysInstanced(int var0, int var1, int var2, int var3, long var4);

   public static void glDrawElementsInstanced(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsInstanced;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstanced(var0, var1.remaining(), 5121, MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glDrawElementsInstanced(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsInstanced;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstanced(var0, var1.remaining(), 5125, MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glDrawElementsInstanced(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsInstanced;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstanced(var0, var1.remaining(), 5123, MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglDrawElementsInstanced(int var0, int var1, int var2, long var3, int var5, long var6);

   public static void glDrawElementsInstanced(int var0, int var1, int var2, long var3, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glDrawElementsInstanced;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureElementVBOenabled(var6);
      nglDrawElementsInstancedBO(var0, var1, var2, var3, var5, var7);
   }

   static native void nglDrawElementsInstancedBO(int var0, int var1, int var2, long var3, int var5, long var6);

   public static void glCopyBufferSubData(int var0, int var1, long var2, long var4, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCopyBufferSubData;
      BufferChecks.checkFunctionAddress(var9);
      nglCopyBufferSubData(var0, var1, var2, var4, var6, var9);
   }

   static native void nglCopyBufferSubData(int var0, int var1, long var2, long var4, long var6, long var8);

   public static void glPrimitiveRestartIndex(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPrimitiveRestartIndex;
      BufferChecks.checkFunctionAddress(var2);
      nglPrimitiveRestartIndex(var0, var2);
   }

   static native void nglPrimitiveRestartIndex(int var0, long var1);

   public static void glTexBuffer(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexBuffer;
      BufferChecks.checkFunctionAddress(var4);
      nglTexBuffer(var0, var1, var2, var4);
   }

   static native void nglTexBuffer(int var0, int var1, int var2, long var3);

   public static void glGetUniformIndices(int var0, ByteBuffer var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformIndices;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1, var2.remaining());
      BufferChecks.checkDirect(var2);
      nglGetUniformIndices(var0, var2.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformIndices(int var0, int var1, long var2, long var4, long var6);

   public static void glGetUniformIndices(int var0, CharSequence[] var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformIndices;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkArray(var1);
      BufferChecks.checkBuffer(var2, var1.length);
      nglGetUniformIndices(var0, var1.length, APIUtil.getBufferNT(var3, var1), MemoryUtil.getAddress(var2), var4);
   }

   public static void glGetActiveUniforms(int var0, IntBuffer var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveUniformsiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkBuffer(var3, var1.remaining());
      nglGetActiveUniformsiv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetActiveUniformsiv(int var0, int var1, long var2, int var4, long var5, long var7);

   /** @deprecated */
   @Deprecated
   public static int glGetActiveUniforms(int var0, int var1, int var2) {
      return glGetActiveUniformsi(var0, var1, var2);
   }

   public static int glGetActiveUniformsi(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveUniformsiv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetActiveUniformsiv(var0, 1, MemoryUtil.getAddress((IntBuffer)var6.put(1, var1), 1), var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetActiveUniformName(int var0, int var1, IntBuffer var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveUniformName;
      BufferChecks.checkFunctionAddress(var5);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkDirect(var3);
      nglGetActiveUniformName(var0, var1, var3.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetActiveUniformName(int var0, int var1, int var2, long var3, long var5, long var7);

   public static String glGetActiveUniformName(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveUniformName;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetActiveUniformName(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static int glGetUniformBlockIndex(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformBlockIndex;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetUniformBlockIndex(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetUniformBlockIndex(int var0, long var1, long var3);

   public static int glGetUniformBlockIndex(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformBlockIndex;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetUniformBlockIndex(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glGetActiveUniformBlock(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveUniformBlockiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 16);
      nglGetActiveUniformBlockiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetActiveUniformBlockiv(int var0, int var1, int var2, long var3, long var5);

   /** @deprecated */
   @Deprecated
   public static int glGetActiveUniformBlock(int var0, int var1, int var2) {
      return glGetActiveUniformBlocki(var0, var1, var2);
   }

   public static int glGetActiveUniformBlocki(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveUniformBlockiv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetActiveUniformBlockiv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetActiveUniformBlockName(int var0, int var1, IntBuffer var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveUniformBlockName;
      BufferChecks.checkFunctionAddress(var5);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkDirect(var3);
      nglGetActiveUniformBlockName(var0, var1, var3.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetActiveUniformBlockName(int var0, int var1, int var2, long var3, long var5, long var7);

   public static String glGetActiveUniformBlockName(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveUniformBlockName;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetActiveUniformBlockName(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static void glUniformBlockBinding(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformBlockBinding;
      BufferChecks.checkFunctionAddress(var4);
      nglUniformBlockBinding(var0, var1, var2, var4);
   }

   static native void nglUniformBlockBinding(int var0, int var1, int var2, long var3);
}
