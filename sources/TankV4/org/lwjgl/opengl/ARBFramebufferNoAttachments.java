package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBFramebufferNoAttachments {
   public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 37648;
   public static final int GL_FRAMEBUFFER_DEFAULT_HEIGHT = 37649;
   public static final int GL_FRAMEBUFFER_DEFAULT_LAYERS = 37650;
   public static final int GL_FRAMEBUFFER_DEFAULT_SAMPLES = 37651;
   public static final int GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 37652;
   public static final int GL_MAX_FRAMEBUFFER_WIDTH = 37653;
   public static final int GL_MAX_FRAMEBUFFER_HEIGHT = 37654;
   public static final int GL_MAX_FRAMEBUFFER_LAYERS = 37655;
   public static final int GL_MAX_FRAMEBUFFER_SAMPLES = 37656;

   private ARBFramebufferNoAttachments() {
   }

   public static void glFramebufferParameteri(int var0, int var1, int var2) {
      GL43.glFramebufferParameteri(var0, var1, var2);
   }

   public static void glGetFramebufferParameter(int var0, int var1, IntBuffer var2) {
      GL43.glGetFramebufferParameter(var0, var1, var2);
   }

   public static int glGetFramebufferParameteri(int var0, int var1) {
      return GL43.glGetFramebufferParameteri(var0, var1);
   }

   public static void glNamedFramebufferParameteriEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedFramebufferParameteriEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglNamedFramebufferParameteriEXT(var0, var1, var2, var4);
   }

   static native void nglNamedFramebufferParameteriEXT(int var0, int var1, int var2, long var3);

   public static void glGetNamedFramebufferParameterEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedFramebufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetNamedFramebufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetNamedFramebufferParameterivEXT(int var0, int var1, long var2, long var4);

   public static int glGetNamedFramebufferParameterEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetNamedFramebufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetNamedFramebufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
