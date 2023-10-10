package org.lwjgl.opencl;

import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class EXTMigrateMemobject {
   public static final int CL_MIGRATE_MEM_OBJECT_HOST_EXT = 1;
   public static final int CL_COMMAND_MIGRATE_MEM_OBJECT_EXT = 16448;

   private EXTMigrateMemobject() {
   }

   public static int clEnqueueMigrateMemObjectEXT(CLCommandQueue var0, PointerBuffer var1, long var2, PointerBuffer var4, PointerBuffer var5) {
      long var6 = CLCapabilities.clEnqueueMigrateMemObjectEXT;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var5, 1);
      }

      int var8 = nclEnqueueMigrateMemObjectEXT(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), var2, var4 == null ? 0 : var4.remaining(), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var6);
      if (var8 == 0) {
         var0.registerCLEvent(var5);
      }

      return var8;
   }

   static native int nclEnqueueMigrateMemObjectEXT(long var0, int var2, long var3, long var5, int var7, long var8, long var10, long var12);

   public static int clEnqueueMigrateMemObjectEXT(CLCommandQueue var0, CLMem var1, long var2, PointerBuffer var4, PointerBuffer var5) {
      long var6 = CLCapabilities.clEnqueueMigrateMemObjectEXT;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var5, 1);
      }

      int var8 = nclEnqueueMigrateMemObjectEXT(var0.getPointer(), 1, APIUtil.getPointer(var1), var2, var4 == null ? 0 : var4.remaining(), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var6);
      if (var8 == 0) {
         var0.registerCLEvent(var5);
      }

      return var8;
   }
}
