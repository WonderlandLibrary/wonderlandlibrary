package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBDebugOutput {
   public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB = 33346;
   public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_ARB = 37187;
   public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_ARB = 37188;
   public static final int GL_DEBUG_LOGGED_MESSAGES_ARB = 37189;
   public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB = 33347;
   public static final int GL_DEBUG_CALLBACK_FUNCTION_ARB = 33348;
   public static final int GL_DEBUG_CALLBACK_USER_PARAM_ARB = 33349;
   public static final int GL_DEBUG_SOURCE_API_ARB = 33350;
   public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB = 33351;
   public static final int GL_DEBUG_SOURCE_SHADER_COMPILER_ARB = 33352;
   public static final int GL_DEBUG_SOURCE_THIRD_PARTY_ARB = 33353;
   public static final int GL_DEBUG_SOURCE_APPLICATION_ARB = 33354;
   public static final int GL_DEBUG_SOURCE_OTHER_ARB = 33355;
   public static final int GL_DEBUG_TYPE_ERROR_ARB = 33356;
   public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB = 33357;
   public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB = 33358;
   public static final int GL_DEBUG_TYPE_PORTABILITY_ARB = 33359;
   public static final int GL_DEBUG_TYPE_PERFORMANCE_ARB = 33360;
   public static final int GL_DEBUG_TYPE_OTHER_ARB = 33361;
   public static final int GL_DEBUG_SEVERITY_HIGH_ARB = 37190;
   public static final int GL_DEBUG_SEVERITY_MEDIUM_ARB = 37191;
   public static final int GL_DEBUG_SEVERITY_LOW_ARB = 37192;

   private ARBDebugOutput() {
   }

   public static void glDebugMessageControlARB(int var0, int var1, int var2, IntBuffer var3, boolean var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDebugMessageControlARB;
      BufferChecks.checkFunctionAddress(var6);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      nglDebugMessageControlARB(var0, var1, var2, var3 == null ? 0 : var3.remaining(), MemoryUtil.getAddressSafe(var3), var4, var6);
   }

   static native void nglDebugMessageControlARB(int var0, int var1, int var2, int var3, long var4, boolean var6, long var7);

   public static void glDebugMessageInsertARB(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDebugMessageInsertARB;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkDirect(var4);
      nglDebugMessageInsertARB(var0, var1, var2, var3, var4.remaining(), MemoryUtil.getAddress(var4), var6);
   }

   static native void nglDebugMessageInsertARB(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glDebugMessageInsertARB(int var0, int var1, int var2, int var3, CharSequence var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDebugMessageInsertARB;
      BufferChecks.checkFunctionAddress(var6);
      nglDebugMessageInsertARB(var0, var1, var2, var3, var4.length(), APIUtil.getBuffer(var5, var4), var6);
   }

   public static void glDebugMessageCallbackARB(ARBDebugOutputCallback var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDebugMessageCallbackARB;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = var0 == null ? 0L : CallbackUtil.createGlobalRef(var0.getHandler());
      CallbackUtil.registerContextCallbackARB(var4);
      nglDebugMessageCallbackARB(var0 == null ? 0L : var0.getPointer(), var4, var2);
   }

   static native void nglDebugMessageCallbackARB(long var0, long var2, long var4);

   public static int glGetDebugMessageLogARB(int var0, IntBuffer var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, IntBuffer var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetDebugMessageLogARB;
      BufferChecks.checkFunctionAddress(var8);
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
         BufferChecks.checkBuffer(var5, var0);
      }

      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      int var10 = nglGetDebugMessageLogARB(var0, var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), var8);
      return var10;
   }

   static native int nglGetDebugMessageLogARB(int var0, int var1, long var2, long var4, long var6, long var8, long var10, long var12, long var14);
}
