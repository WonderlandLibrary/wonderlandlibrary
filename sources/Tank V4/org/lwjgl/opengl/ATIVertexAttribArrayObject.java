package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ATIVertexAttribArrayObject {
   private ATIVertexAttribArrayObject() {
   }

   public static void glVertexAttribArrayObjectATI(int var0, int var1, int var2, boolean var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttribArrayObjectATI;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttribArrayObjectATI(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglVertexAttribArrayObjectATI(int var0, int var1, int var2, boolean var3, int var4, int var5, int var6, long var7);

   public static void glGetVertexAttribArrayObjectATI(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribArrayObjectfvATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetVertexAttribArrayObjectfvATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribArrayObjectfvATI(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribArrayObjectATI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribArrayObjectivATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribArrayObjectivATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribArrayObjectivATI(int var0, int var1, long var2, long var4);
}
