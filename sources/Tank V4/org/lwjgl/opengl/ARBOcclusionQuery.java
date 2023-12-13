package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBOcclusionQuery {
   public static final int GL_SAMPLES_PASSED_ARB = 35092;
   public static final int GL_QUERY_COUNTER_BITS_ARB = 34916;
   public static final int GL_CURRENT_QUERY_ARB = 34917;
   public static final int GL_QUERY_RESULT_ARB = 34918;
   public static final int GL_QUERY_RESULT_AVAILABLE_ARB = 34919;

   private ARBOcclusionQuery() {
   }

   public static void glGenQueriesARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenQueriesARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenQueriesARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenQueriesARB(int var0, long var1, long var3);

   public static int glGenQueriesARB() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenQueriesARB;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenQueriesARB(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glDeleteQueriesARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteQueriesARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteQueriesARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteQueriesARB(int var0, long var1, long var3);

   public static void glDeleteQueriesARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteQueriesARB;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteQueriesARB(1, APIUtil.getInt(var1, var0), var2);
   }

   public static boolean glIsQueryARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsQueryARB;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsQueryARB(var0, var2);
      return var4;
   }

   static native boolean nglIsQueryARB(int var0, long var1);

   public static void glBeginQueryARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBeginQueryARB;
      BufferChecks.checkFunctionAddress(var3);
      nglBeginQueryARB(var0, var1, var3);
   }

   static native void nglBeginQueryARB(int var0, int var1, long var2);

   public static void glEndQueryARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEndQueryARB;
      BufferChecks.checkFunctionAddress(var2);
      nglEndQueryARB(var0, var2);
   }

   static native void nglEndQueryARB(int var0, long var1);

   public static void glGetQueryARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetQueryivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryivARB(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetQueryARB(int var0, int var1) {
      return glGetQueryiARB(var0, var1);
   }

   public static int glGetQueryiARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryivARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetQueryivARB(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetQueryObjectARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjectivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetQueryObjectivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjectivARB(int var0, int var1, long var2, long var4);

   public static int glGetQueryObjectiARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjectivARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetQueryObjectivARB(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetQueryObjectuARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjectuivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetQueryObjectuivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjectuivARB(int var0, int var1, long var2, long var4);

   public static int glGetQueryObjectuiARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjectuivARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetQueryObjectuivARB(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
