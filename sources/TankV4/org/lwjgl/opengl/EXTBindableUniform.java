package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBindableUniform {
   public static final int GL_MAX_VERTEX_BINDABLE_UNIFORMS_EXT = 36322;
   public static final int GL_MAX_FRAGMENT_BINDABLE_UNIFORMS_EXT = 36323;
   public static final int GL_MAX_GEOMETRY_BINDABLE_UNIFORMS_EXT = 36324;
   public static final int GL_MAX_BINDABLE_UNIFORM_SIZE_EXT = 36333;
   public static final int GL_UNIFORM_BUFFER_BINDING_EXT = 36335;
   public static final int GL_UNIFORM_BUFFER_EXT = 36334;

   private EXTBindableUniform() {
   }

   public static void glUniformBufferEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformBufferEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglUniformBufferEXT(var0, var1, var2, var4);
   }

   static native void nglUniformBufferEXT(int var0, int var1, int var2, long var3);

   public static int glGetUniformBufferSizeEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformBufferSizeEXT;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetUniformBufferSizeEXT(var0, var1, var3);
      return var5;
   }

   static native int nglGetUniformBufferSizeEXT(int var0, int var1, long var2);

   public static long glGetUniformOffsetEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformOffsetEXT;
      BufferChecks.checkFunctionAddress(var3);
      long var5 = nglGetUniformOffsetEXT(var0, var1, var3);
      return var5;
   }

   static native long nglGetUniformOffsetEXT(int var0, int var1, long var2);
}
