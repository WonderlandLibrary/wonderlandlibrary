package org.lwjgl.opencl;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class EXTDeviceFission {
   public static final int CL_DEVICE_PARTITION_EQUALLY_EXT = 16464;
   public static final int CL_DEVICE_PARTITION_BY_COUNTS_EXT = 16465;
   public static final int CL_DEVICE_PARTITION_BY_NAMES_EXT = 16466;
   public static final int CL_DEVICE_PARTITION_BY_AFFINITY_DOMAIN_EXT = 16467;
   public static final int CL_AFFINITY_DOMAIN_L1_CACHE_EXT = 1;
   public static final int CL_AFFINITY_DOMAIN_L2_CACHE_EXT = 2;
   public static final int CL_AFFINITY_DOMAIN_L3_CACHE_EXT = 3;
   public static final int CL_AFFINITY_DOMAIN_L4_CACHE_EXT = 4;
   public static final int CL_AFFINITY_DOMAIN_NUMA_EXT = 16;
   public static final int CL_AFFINITY_DOMAIN_NEXT_FISSIONABLE_EXT = 256;
   public static final int CL_DEVICE_PARENT_DEVICE_EXT = 16468;
   public static final int CL_DEVICE_PARITION_TYPES_EXT = 16469;
   public static final int CL_DEVICE_AFFINITY_DOMAINS_EXT = 16470;
   public static final int CL_DEVICE_REFERENCE_COUNT_EXT = 16471;
   public static final int CL_DEVICE_PARTITION_STYLE_EXT = 16472;
   public static final int CL_PROPERTIES_LIST_END_EXT = 0;
   public static final int CL_PARTITION_BY_COUNTS_LIST_END_EXT = 0;
   public static final int CL_PARTITION_BY_NAMES_LIST_END_EXT = -1;
   public static final int CL_DEVICE_PARTITION_FAILED_EXT = -1057;
   public static final int CL_INVALID_PARTITION_COUNT_EXT = -1058;
   public static final int CL_INVALID_PARTITION_NAME_EXT = -1059;

   private EXTDeviceFission() {
   }

   public static int clRetainDeviceEXT(CLDevice var0) {
      long var1 = CLCapabilities.clRetainDeviceEXT;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainDeviceEXT(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainDeviceEXT(long var0, long var2);

   public static int clReleaseDeviceEXT(CLDevice var0) {
      long var1 = CLCapabilities.clReleaseDeviceEXT;
      BufferChecks.checkFunctionAddress(var1);
      APIUtil.releaseObjects(var0);
      int var3 = nclReleaseDeviceEXT(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseDeviceEXT(long var0, long var2);

   public static int clCreateSubDevicesEXT(CLDevice var0, LongBuffer var1, PointerBuffer var2, IntBuffer var3) {
      long var4 = CLCapabilities.clCreateSubDevicesEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      int var6 = nclCreateSubDevicesEXT(var0.getPointer(), MemoryUtil.getAddress(var1), var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0 && var2 != null) {
         var0.registerSubCLDevices(var2);
      }

      return var6;
   }

   static native int nclCreateSubDevicesEXT(long var0, long var2, int var4, long var5, long var7, long var9);
}
