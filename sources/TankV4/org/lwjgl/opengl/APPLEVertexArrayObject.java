package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEVertexArrayObject {
   public static final int GL_VERTEX_ARRAY_BINDING_APPLE = 34229;

   private APPLEVertexArrayObject() {
   }

   public static void glBindVertexArrayAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBindVertexArrayAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      nglBindVertexArrayAPPLE(var0, var2);
   }

   static native void nglBindVertexArrayAPPLE(int var0, long var1);

   public static void glDeleteVertexArraysAPPLE(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteVertexArraysAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteVertexArraysAPPLE(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteVertexArraysAPPLE(int var0, long var1, long var3);

   public static void glDeleteVertexArraysAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteVertexArraysAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteVertexArraysAPPLE(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenVertexArraysAPPLE(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenVertexArraysAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenVertexArraysAPPLE(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenVertexArraysAPPLE(int var0, long var1, long var3);

   public static int glGenVertexArraysAPPLE() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenVertexArraysAPPLE;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenVertexArraysAPPLE(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static boolean glIsVertexArrayAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsVertexArrayAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsVertexArrayAPPLE(var0, var2);
      return var4;
   }

   static native boolean nglIsVertexArrayAPPLE(int var0, long var1);
}
