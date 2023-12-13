package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class AMDDebugOutput {
   public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_AMD = 37187;
   public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_AMD = 37188;
   public static final int GL_DEBUG_LOGGED_MESSAGES_AMD = 37189;
   public static final int GL_DEBUG_SEVERITY_HIGH_AMD = 37190;
   public static final int GL_DEBUG_SEVERITY_MEDIUM_AMD = 37191;
   public static final int GL_DEBUG_SEVERITY_LOW_AMD = 37192;
   public static final int GL_DEBUG_CATEGORY_API_ERROR_AMD = 37193;
   public static final int GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD = 37194;
   public static final int GL_DEBUG_CATEGORY_DEPRECATION_AMD = 37195;
   public static final int GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD = 37196;
   public static final int GL_DEBUG_CATEGORY_PERFORMANCE_AMD = 37197;
   public static final int GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD = 37198;
   public static final int GL_DEBUG_CATEGORY_APPLICATION_AMD = 37199;
   public static final int GL_DEBUG_CATEGORY_OTHER_AMD = 37200;

   private AMDDebugOutput() {
   }

   public static void glDebugMessageEnableAMD(int var0, int var1, IntBuffer var2, boolean var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDebugMessageEnableAMD;
      BufferChecks.checkFunctionAddress(var5);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      nglDebugMessageEnableAMD(var0, var1, var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), var3, var5);
   }

   static native void nglDebugMessageEnableAMD(int var0, int var1, int var2, long var3, boolean var5, long var6);

   public static void glDebugMessageInsertAMD(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDebugMessageInsertAMD;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglDebugMessageInsertAMD(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglDebugMessageInsertAMD(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glDebugMessageInsertAMD(int var0, int var1, int var2, CharSequence var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDebugMessageInsertAMD;
      BufferChecks.checkFunctionAddress(var5);
      nglDebugMessageInsertAMD(var0, var1, var2, var3.length(), APIUtil.getBuffer(var4, var3), var5);
   }

   public static void glDebugMessageCallbackAMD(AMDDebugOutputCallback var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDebugMessageCallbackAMD;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = var0 == null ? 0L : CallbackUtil.createGlobalRef(var0.getHandler());
      CallbackUtil.registerContextCallbackAMD(var4);
      nglDebugMessageCallbackAMD(var0 == null ? 0L : var0.getPointer(), var4, var2);
   }

   static native void nglDebugMessageCallbackAMD(long var0, long var2, long var4);

   public static int glGetDebugMessageLogAMD(int var0, IntBuffer var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetDebugMessageLogAMD;
      BufferChecks.checkFunctionAddress(var7);
      if (var1 != null) {
         BufferChecks.checkBuffer(var1, var0);
      }

      if (var2 != null) {
         BufferChecks.checkBuffer(var2, var0);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer(var3, var0);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer(var4, var0);
      }

      if (var5 != null) {
         BufferChecks.checkDirect(var5);
      }

      int var9 = nglGetDebugMessageLogAMD(var0, var5 == null ? 0 : var5.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var7);
      return var9;
   }

   static native int nglGetDebugMessageLogAMD(int var0, int var1, long var2, long var4, long var6, long var8, long var10, long var12);
}
