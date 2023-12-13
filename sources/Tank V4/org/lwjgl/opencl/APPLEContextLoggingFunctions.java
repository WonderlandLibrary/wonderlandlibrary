package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

final class APPLEContextLoggingFunctions {
   private APPLEContextLoggingFunctions() {
   }

   static void clLogMessagesToSystemLogAPPLE(ByteBuffer var0, ByteBuffer var1, ByteBuffer var2) {
      long var3 = CLCapabilities.clLogMessagesToSystemLogAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkDirect(var2);
      nclLogMessagesToSystemLogAPPLE(MemoryUtil.getAddress(var0), MemoryUtil.getAddress(var1), (long)var1.remaining(), MemoryUtil.getAddress(var2), var3);
   }

   static native void nclLogMessagesToSystemLogAPPLE(long var0, long var2, long var4, long var6, long var8);

   static void clLogMessagesToStdoutAPPLE(ByteBuffer var0, ByteBuffer var1, ByteBuffer var2) {
      long var3 = CLCapabilities.clLogMessagesToStdoutAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkDirect(var2);
      nclLogMessagesToStdoutAPPLE(MemoryUtil.getAddress(var0), MemoryUtil.getAddress(var1), (long)var1.remaining(), MemoryUtil.getAddress(var2), var3);
   }

   static native void nclLogMessagesToStdoutAPPLE(long var0, long var2, long var4, long var6, long var8);

   static void clLogMessagesToStderrAPPLE(ByteBuffer var0, ByteBuffer var1, ByteBuffer var2) {
      long var3 = CLCapabilities.clLogMessagesToStderrAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkDirect(var2);
      nclLogMessagesToStderrAPPLE(MemoryUtil.getAddress(var0), MemoryUtil.getAddress(var1), (long)var1.remaining(), MemoryUtil.getAddress(var2), var3);
   }

   static native void nclLogMessagesToStderrAPPLE(long var0, long var2, long var4, long var6, long var8);
}
