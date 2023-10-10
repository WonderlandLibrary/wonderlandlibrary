package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVExplicitMultisample {
   public static final int GL_SAMPLE_POSITION_NV = 36432;
   public static final int GL_SAMPLE_MASK_NV = 36433;
   public static final int GL_SAMPLE_MASK_VALUE_NV = 36434;
   public static final int GL_TEXTURE_BINDING_RENDERBUFFER_NV = 36435;
   public static final int GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV = 36436;
   public static final int GL_MAX_SAMPLE_MASK_WORDS_NV = 36441;
   public static final int GL_TEXTURE_RENDERBUFFER_NV = 36437;
   public static final int GL_SAMPLER_RENDERBUFFER_NV = 36438;
   public static final int GL_INT_SAMPLER_RENDERBUFFER_NV = 36439;
   public static final int GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV = 36440;

   private NVExplicitMultisample() {
   }

   public static void glGetBooleanIndexedEXT(int var0, int var1, ByteBuffer var2) {
      EXTDrawBuffers2.glGetBooleanIndexedEXT(var0, var1, var2);
   }

   public static boolean glGetBooleanIndexedEXT(int var0, int var1) {
      return EXTDrawBuffers2.glGetBooleanIndexedEXT(var0, var1);
   }

   public static void glGetIntegerIndexedEXT(int var0, int var1, IntBuffer var2) {
      EXTDrawBuffers2.glGetIntegerIndexedEXT(var0, var1, var2);
   }

   public static int glGetIntegerIndexedEXT(int var0, int var1) {
      return EXTDrawBuffers2.glGetIntegerIndexedEXT(var0, var1);
   }

   public static void glGetMultisampleNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMultisamplefvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 2);
      nglGetMultisamplefvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMultisamplefvNV(int var0, int var1, long var2, long var4);

   public static void glSampleMaskIndexedNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSampleMaskIndexedNV;
      BufferChecks.checkFunctionAddress(var3);
      nglSampleMaskIndexedNV(var0, var1, var3);
   }

   static native void nglSampleMaskIndexedNV(int var0, int var1, long var2);

   public static void glTexRenderbufferNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexRenderbufferNV;
      BufferChecks.checkFunctionAddress(var3);
      nglTexRenderbufferNV(var0, var1, var3);
   }

   static native void nglTexRenderbufferNV(int var0, int var1, long var2);
}
