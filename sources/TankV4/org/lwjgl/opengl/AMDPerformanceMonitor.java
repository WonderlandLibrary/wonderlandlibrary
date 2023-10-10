package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class AMDPerformanceMonitor {
   public static final int GL_COUNTER_TYPE_AMD = 35776;
   public static final int GL_COUNTER_RANGE_AMD = 35777;
   public static final int GL_UNSIGNED_INT64_AMD = 35778;
   public static final int GL_PERCENTAGE_AMD = 35779;
   public static final int GL_PERFMON_RESULT_AVAILABLE_AMD = 35780;
   public static final int GL_PERFMON_RESULT_SIZE_AMD = 35781;
   public static final int GL_PERFMON_RESULT_AMD = 35782;

   private AMDPerformanceMonitor() {
   }

   public static void glGetPerfMonitorGroupsAMD(IntBuffer var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPerfMonitorGroupsAMD;
      BufferChecks.checkFunctionAddress(var3);
      if (var0 != null) {
         BufferChecks.checkBuffer((IntBuffer)var0, 1);
      }

      BufferChecks.checkDirect(var1);
      nglGetPerfMonitorGroupsAMD(MemoryUtil.getAddressSafe(var0), var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetPerfMonitorGroupsAMD(long var0, int var2, long var3, long var5);

   public static void glGetPerfMonitorCountersAMD(int var0, IntBuffer var1, IntBuffer var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetPerfMonitorCountersAMD;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      nglGetPerfMonitorCountersAMD(var0, MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var3 == null ? 0 : var3.remaining(), MemoryUtil.getAddressSafe(var3), var5);
   }

   static native void nglGetPerfMonitorCountersAMD(int var0, long var1, long var3, int var5, long var6, long var8);

   public static void glGetPerfMonitorGroupStringAMD(int var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPerfMonitorGroupStringAMD;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      nglGetPerfMonitorGroupStringAMD(var0, var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), var4);
   }

   static native void nglGetPerfMonitorGroupStringAMD(int var0, int var1, long var2, long var4, long var6);

   public static String glGetPerfMonitorGroupStringAMD(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPerfMonitorGroupStringAMD;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetPerfMonitorGroupStringAMD(var0, var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glGetPerfMonitorCounterStringAMD(int var0, int var1, IntBuffer var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetPerfMonitorCounterStringAMD;
      BufferChecks.checkFunctionAddress(var5);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      nglGetPerfMonitorCounterStringAMD(var0, var1, var3 == null ? 0 : var3.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var5);
   }

   static native void nglGetPerfMonitorCounterStringAMD(int var0, int var1, int var2, long var3, long var5, long var7);

   public static String glGetPerfMonitorCounterStringAMD(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPerfMonitorCounterStringAMD;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetPerfMonitorCounterStringAMD(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static void glGetPerfMonitorCounterInfoAMD(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetPerfMonitorCounterInfoAMD;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((ByteBuffer)var3, 16);
      nglGetPerfMonitorCounterInfoAMD(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetPerfMonitorCounterInfoAMD(int var0, int var1, int var2, long var3, long var5);

   public static void glGenPerfMonitorsAMD(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenPerfMonitorsAMD;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenPerfMonitorsAMD(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenPerfMonitorsAMD(int var0, long var1, long var3);

   public static int glGenPerfMonitorsAMD() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenPerfMonitorsAMD;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenPerfMonitorsAMD(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glDeletePerfMonitorsAMD(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeletePerfMonitorsAMD;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeletePerfMonitorsAMD(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeletePerfMonitorsAMD(int var0, long var1, long var3);

   public static void glDeletePerfMonitorsAMD(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeletePerfMonitorsAMD;
      BufferChecks.checkFunctionAddress(var2);
      nglDeletePerfMonitorsAMD(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glSelectPerfMonitorCountersAMD(int var0, boolean var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glSelectPerfMonitorCountersAMD;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglSelectPerfMonitorCountersAMD(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglSelectPerfMonitorCountersAMD(int var0, boolean var1, int var2, int var3, long var4, long var6);

   public static void glSelectPerfMonitorCountersAMD(int var0, boolean var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glSelectPerfMonitorCountersAMD;
      BufferChecks.checkFunctionAddress(var5);
      nglSelectPerfMonitorCountersAMD(var0, var1, var2, 1, APIUtil.getInt(var4, var3), var5);
   }

   public static void glBeginPerfMonitorAMD(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBeginPerfMonitorAMD;
      BufferChecks.checkFunctionAddress(var2);
      nglBeginPerfMonitorAMD(var0, var2);
   }

   static native void nglBeginPerfMonitorAMD(int var0, long var1);

   public static void glEndPerfMonitorAMD(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEndPerfMonitorAMD;
      BufferChecks.checkFunctionAddress(var2);
      nglEndPerfMonitorAMD(var0, var2);
   }

   static native void nglEndPerfMonitorAMD(int var0, long var1);

   public static void glGetPerfMonitorCounterDataAMD(int var0, int var1, IntBuffer var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetPerfMonitorCounterDataAMD;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      nglGetPerfMonitorCounterDataAMD(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), MemoryUtil.getAddressSafe(var3), var5);
   }

   static native void nglGetPerfMonitorCounterDataAMD(int var0, int var1, int var2, long var3, long var5, long var7);

   public static int glGetPerfMonitorCounterDataAMD(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPerfMonitorCounterDataAMD;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetPerfMonitorCounterDataAMD(var0, var1, 4, MemoryUtil.getAddress(var5), 0L, var3);
      return var5.get(0);
   }
}
