package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class CL11 {
   public static final int CL_MISALIGNED_SUB_BUFFER_OFFSET = -13;
   public static final int CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST = -14;
   public static final int CL_INVALID_PROPERTY = -64;
   public static final int CL_VERSION_1_1 = 1;
   public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF = 4148;
   public static final int CL_DEVICE_HOST_UNIFIED_MEMORY = 4149;
   public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR = 4150;
   public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT = 4151;
   public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_INT = 4152;
   public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG = 4153;
   public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT = 4154;
   public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE = 4155;
   public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF = 4156;
   public static final int CL_DEVICE_OPENCL_C_VERSION = 4157;
   public static final int CL_FP_SOFT_FLOAT = 64;
   public static final int CL_CONTEXT_NUM_DEVICES = 4227;
   public static final int CL_Rx = 4282;
   public static final int CL_RGx = 4283;
   public static final int CL_RGBx = 4284;
   public static final int CL_MEM_ASSOCIATED_MEMOBJECT = 4359;
   public static final int CL_MEM_OFFSET = 4360;
   public static final int CL_ADDRESS_MIRRORED_REPEAT = 4404;
   public static final int CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE = 4531;
   public static final int CL_KERNEL_PRIVATE_MEM_SIZE = 4532;
   public static final int CL_EVENT_CONTEXT = 4564;
   public static final int CL_COMMAND_READ_BUFFER_RECT = 4609;
   public static final int CL_COMMAND_WRITE_BUFFER_RECT = 4610;
   public static final int CL_COMMAND_COPY_BUFFER_RECT = 4611;
   public static final int CL_COMMAND_USER = 4612;
   public static final int CL_BUFFER_CREATE_TYPE_REGION = 4640;

   private CL11() {
   }

   public static CLMem clCreateSubBuffer(CLMem var0, long var1, int var3, ByteBuffer var4, IntBuffer var5) {
      long var6 = CLCapabilities.clCreateSubBuffer;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer(var4, 2 * PointerBuffer.getPointerSize());
      if (var5 != null) {
         BufferChecks.checkBuffer((IntBuffer)var5, 1);
      }

      CLMem var8 = CLMem.create(nclCreateSubBuffer(var0.getPointer(), var1, var3, MemoryUtil.getAddress(var4), MemoryUtil.getAddressSafe(var5), var6), (CLContext)var0.getParent());
      return var8;
   }

   static native long nclCreateSubBuffer(long var0, long var2, int var4, long var5, long var7, long var9);

   public static int clSetMemObjectDestructorCallback(CLMem var0, CLMemObjectDestructorCallback var1) {
      long var2 = CLCapabilities.clSetMemObjectDestructorCallback;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = CallbackUtil.createGlobalRef(var1);
      boolean var6 = false;
      int var9 = nclSetMemObjectDestructorCallback(var0.getPointer(), var1.getPointer(), var4, var2);
      CallbackUtil.checkCallback(var9, var4);
      return var9;
   }

   static native int nclSetMemObjectDestructorCallback(long var0, long var2, long var4, long var6);

   public static int clEnqueueReadBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, ByteBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueReadBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueReadBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueReadBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, DoubleBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueReadBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueReadBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueReadBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, FloatBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueReadBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueReadBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueReadBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, IntBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueReadBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueReadBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueReadBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, LongBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueReadBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueReadBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueReadBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, ShortBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueReadBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueReadBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   static native int nclEnqueueReadBufferRect(long var0, long var2, int var4, long var5, long var7, long var9, long var11, long var13, long var15, long var17, long var19, int var21, long var22, long var24, long var26);

   public static int clEnqueueWriteBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, ByteBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueWriteBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueWriteBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueWriteBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, DoubleBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueWriteBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueWriteBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueWriteBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, FloatBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueWriteBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueWriteBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueWriteBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, IntBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueWriteBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueWriteBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueWriteBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, LongBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueWriteBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueWriteBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   public static int clEnqueueWriteBufferRect(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, ShortBuffer var14, PointerBuffer var15, PointerBuffer var16) {
      long var17 = CLCapabilities.clEnqueueWriteBufferRect;
      BufferChecks.checkFunctionAddress(var17);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer(var14, CLChecks.calculateBufferRectSize(var4, var5, var10, var12));
      if (var15 != null) {
         BufferChecks.checkDirect(var15);
      }

      if (var16 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var16, 1);
      }

      int var19 = nclEnqueueWriteBufferRect(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, MemoryUtil.getAddress(var14), var15 == null ? 0 : var15.remaining(), MemoryUtil.getAddressSafe(var15), MemoryUtil.getAddressSafe(var16), var17);
      if (var19 == 0) {
         var0.registerCLEvent(var16);
      }

      return var19;
   }

   static native int nclEnqueueWriteBufferRect(long var0, long var2, int var4, long var5, long var7, long var9, long var11, long var13, long var15, long var17, long var19, int var21, long var22, long var24, long var26);

   public static int clEnqueueCopyBufferRect(CLCommandQueue var0, CLMem var1, CLMem var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, long var6, long var8, long var10, long var12, PointerBuffer var14, PointerBuffer var15) {
      long var16 = CLCapabilities.clEnqueueCopyBufferRect;
      BufferChecks.checkFunctionAddress(var16);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      if (var14 != null) {
         BufferChecks.checkDirect(var14);
      }

      if (var15 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var15, 1);
      }

      int var18 = nclEnqueueCopyBufferRect(var0.getPointer(), var1.getPointer(), var2.getPointer(), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6, var8, var10, var12, var14 == null ? 0 : var14.remaining(), MemoryUtil.getAddressSafe(var14), MemoryUtil.getAddressSafe(var15), var16);
      if (var18 == 0) {
         var0.registerCLEvent(var15);
      }

      return var18;
   }

   static native int nclEnqueueCopyBufferRect(long var0, long var2, long var4, long var6, long var8, long var10, long var12, long var14, long var16, long var18, int var20, long var21, long var23, long var25);

   public static CLEvent clCreateUserEvent(CLContext var0, IntBuffer var1) {
      long var2 = CLCapabilities.clCreateUserEvent;
      BufferChecks.checkFunctionAddress(var2);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      CLEvent var4 = new CLEvent(nclCreateUserEvent(var0.getPointer(), MemoryUtil.getAddressSafe(var1), var2), var0);
      return var4;
   }

   static native long nclCreateUserEvent(long var0, long var2, long var4);

   public static int clSetUserEventStatus(CLEvent var0, int var1) {
      long var2 = CLCapabilities.clSetUserEventStatus;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nclSetUserEventStatus(var0.getPointer(), var1, var2);
      return var4;
   }

   static native int nclSetUserEventStatus(long var0, int var2, long var3);

   public static int clSetEventCallback(CLEvent var0, int var1, CLEventCallback var2) {
      long var3 = CLCapabilities.clSetEventCallback;
      BufferChecks.checkFunctionAddress(var3);
      long var5 = CallbackUtil.createGlobalRef(var2);
      var2.setRegistry(var0.getParentRegistry());
      boolean var7 = false;
      int var10 = nclSetEventCallback(var0.getPointer(), var1, var2.getPointer(), var5, var3);
      CallbackUtil.checkCallback(var10, var5);
      return var10;
   }

   static native int nclSetEventCallback(long var0, int var2, long var3, long var5, long var7);
}
