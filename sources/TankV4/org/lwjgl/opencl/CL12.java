package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class CL12 {
   public static final int CL_COMPILE_PROGRAM_FAILURE = -15;
   public static final int CL_LINKER_NOT_AVAILABLE = -16;
   public static final int CL_LINK_PROGRAM_FAILURE = -17;
   public static final int CL_DEVICE_PARTITION_FAILED = -18;
   public static final int CL_KERNEL_ARG_INFO_NOT_AVAILABLE = -19;
   public static final int CL_INVALID_IMAGE_DESCRIPTOR = -65;
   public static final int CL_INVALID_COMPILER_OPTIONS = -66;
   public static final int CL_INVALID_LINKER_OPTIONS = -67;
   public static final int CL_INVALID_DEVICE_PARTITION_COUNT = -68;
   public static final int CL_VERSION_1_2 = 1;
   public static final int CL_BLOCKING = 1;
   public static final int CL_NON_BLOCKING = 0;
   public static final int CL_DEVICE_TYPE_CUSTOM = 16;
   public static final int CL_DEVICE_DOUBLE_FP_CONFIG = 4146;
   public static final int CL_DEVICE_LINKER_AVAILABLE = 4158;
   public static final int CL_DEVICE_BUILT_IN_KERNELS = 4159;
   public static final int CL_DEVICE_IMAGE_MAX_BUFFER_SIZE = 4160;
   public static final int CL_DEVICE_IMAGE_MAX_ARRAY_SIZE = 4161;
   public static final int CL_DEVICE_PARENT_DEVICE = 4162;
   public static final int CL_DEVICE_PARTITION_MAX_SUB_DEVICES = 4163;
   public static final int CL_DEVICE_PARTITION_PROPERTIES = 4164;
   public static final int CL_DEVICE_PARTITION_AFFINITY_DOMAIN = 4165;
   public static final int CL_DEVICE_PARTITION_TYPE = 4166;
   public static final int CL_DEVICE_REFERENCE_COUNT = 4167;
   public static final int CL_DEVICE_PREFERRED_INTEROP_USER_SYNC = 4168;
   public static final int CL_DEVICE_PRINTF_BUFFER_SIZE = 4169;
   public static final int CL_FP_CORRECTLY_ROUNDED_DIVIDE_SQRT = 128;
   public static final int CL_CONTEXT_INTEROP_USER_SYNC = 4229;
   public static final int CL_DEVICE_PARTITION_EQUALLY = 4230;
   public static final int CL_DEVICE_PARTITION_BY_COUNTS = 4231;
   public static final int CL_DEVICE_PARTITION_BY_COUNTS_LIST_END = 0;
   public static final int CL_DEVICE_PARTITION_BY_AFFINITY_DOMAIN = 4232;
   public static final int CL_DEVICE_AFFINITY_DOMAIN_NUMA = 1;
   public static final int CL_DEVICE_AFFINITY_DOMAIN_L4_CACHE = 2;
   public static final int CL_DEVICE_AFFINITY_DOMAIN_L3_CACHE = 4;
   public static final int CL_DEVICE_AFFINITY_DOMAIN_L2_CACHE = 8;
   public static final int CL_DEVICE_AFFINITY_DOMAIN_L1_CACHE = 16;
   public static final int CL_DEVICE_AFFINITY_DOMAIN_NEXT_PARTITIONABLE = 32;
   public static final int CL_MEM_HOST_WRITE_ONLY = 128;
   public static final int CL_MEM_HOST_READ_ONLY = 256;
   public static final int CL_MEM_HOST_NO_ACCESS = 512;
   public static final int CL_MIGRATE_MEM_OBJECT_HOST = 1;
   public static final int CL_MIGRATE_MEM_OBJECT_CONTENT_UNDEFINED = 2;
   public static final int CL_MEM_OBJECT_IMAGE2D_ARRAY = 4339;
   public static final int CL_MEM_OBJECT_IMAGE1D = 4340;
   public static final int CL_MEM_OBJECT_IMAGE1D_ARRAY = 4341;
   public static final int CL_MEM_OBJECT_IMAGE1D_BUFFER = 4342;
   public static final int CL_IMAGE_ARRAY_SIZE = 4375;
   public static final int CL_IMAGE_BUFFER = 4376;
   public static final int CL_IMAGE_NUM_MIP_LEVELS = 4377;
   public static final int CL_IMAGE_NUM_SAMPLES = 4378;
   public static final int CL_MAP_WRITE_INVALIDATE_REGION = 4;
   public static final int CL_PROGRAM_NUM_KERNELS = 4455;
   public static final int CL_PROGRAM_KERNEL_NAMES = 4456;
   public static final int CL_PROGRAM_BINARY_TYPE = 4484;
   public static final int CL_PROGRAM_BINARY_TYPE_NONE = 0;
   public static final int CL_PROGRAM_BINARY_TYPE_COMPILED_OBJECT = 1;
   public static final int CL_PROGRAM_BINARY_TYPE_LIBRARY = 2;
   public static final int CL_PROGRAM_BINARY_TYPE_EXECUTABLE = 4;
   public static final int CL_KERNEL_ATTRIBUTES = 4501;
   public static final int CL_KERNEL_ARG_ADDRESS_QUALIFIER = 4502;
   public static final int CL_KERNEL_ARG_ACCESS_QUALIFIER = 4503;
   public static final int CL_KERNEL_ARG_TYPE_NAME = 4504;
   public static final int CL_KERNEL_ARG_TYPE_QUALIFIER = 4505;
   public static final int CL_KERNEL_ARG_NAME = 4506;
   public static final int CL_KERNEL_ARG_ADDRESS_GLOBAL = 4506;
   public static final int CL_KERNEL_ARG_ADDRESS_LOCAL = 4507;
   public static final int CL_KERNEL_ARG_ADDRESS_CONSTANT = 4508;
   public static final int CL_KERNEL_ARG_ADDRESS_PRIVATE = 4509;
   public static final int CL_KERNEL_ARG_ACCESS_READ_ONLY = 4512;
   public static final int CL_KERNEL_ARG_ACCESS_WRITE_ONLY = 4513;
   public static final int CL_KERNEL_ARG_ACCESS_READ_WRITE = 4514;
   public static final int CL_KERNEL_ARG_ACCESS_NONE = 4515;
   public static final int CL_KERNEL_ARG_TYPE_NONE = 0;
   public static final int CL_KERNEL_ARG_TYPE_CONST = 1;
   public static final int CL_KERNEL_ARG_TYPE_RESTRICT = 2;
   public static final int CL_KERNEL_ARG_TYPE_VOLATILE = 4;
   public static final int CL_KERNEL_GLOBAL_WORK_SIZE = 4533;
   public static final int CL_COMMAND_BARRIER = 4613;
   public static final int CL_COMMAND_MIGRATE_MEM_OBJECTS = 4614;
   public static final int CL_COMMAND_FILL_BUFFER = 4615;
   public static final int CL_COMMAND_FILL_IMAGE = 4616;

   private CL12() {
   }

   public static int clRetainDevice(CLDevice var0) {
      long var1 = CLCapabilities.clRetainDevice;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainDevice(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainDevice(long var0, long var2);

   public static int clReleaseDevice(CLDevice var0) {
      long var1 = CLCapabilities.clReleaseDevice;
      BufferChecks.checkFunctionAddress(var1);
      APIUtil.releaseObjects(var0);
      int var3 = nclReleaseDevice(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseDevice(long var0, long var2);

   public static int clCreateSubDevices(CLDevice var0, LongBuffer var1, PointerBuffer var2, IntBuffer var3) {
      long var4 = CLCapabilities.clCreateSubDevices;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      int var6 = nclCreateSubDevices(var0.getPointer(), MemoryUtil.getAddress(var1), var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0 && var2 != null) {
         var0.registerSubCLDevices(var2);
      }

      return var6;
   }

   static native int nclCreateSubDevices(long var0, long var2, int var4, long var5, long var7, long var9);

   public static CLMem clCreateImage(CLContext var0, long var1, ByteBuffer var3, ByteBuffer var4, ByteBuffer var5, IntBuffer var6) {
      long var7 = CLCapabilities.clCreateImage;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      BufferChecks.checkBuffer(var4, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
      if (var5 != null) {
         BufferChecks.checkDirect(var5);
      }

      if (var6 != null) {
         BufferChecks.checkBuffer((IntBuffer)var6, 1);
      }

      CLMem var9 = new CLMem(nclCreateImage(var0.getPointer(), var1, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), var7), var0);
      return var9;
   }

   public static CLMem clCreateImage(CLContext var0, long var1, ByteBuffer var3, ByteBuffer var4, FloatBuffer var5, IntBuffer var6) {
      long var7 = CLCapabilities.clCreateImage;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      BufferChecks.checkBuffer(var4, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
      if (var5 != null) {
         BufferChecks.checkDirect(var5);
      }

      if (var6 != null) {
         BufferChecks.checkBuffer((IntBuffer)var6, 1);
      }

      CLMem var9 = new CLMem(nclCreateImage(var0.getPointer(), var1, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), var7), var0);
      return var9;
   }

   public static CLMem clCreateImage(CLContext var0, long var1, ByteBuffer var3, ByteBuffer var4, IntBuffer var5, IntBuffer var6) {
      long var7 = CLCapabilities.clCreateImage;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      BufferChecks.checkBuffer(var4, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
      if (var5 != null) {
         BufferChecks.checkDirect(var5);
      }

      if (var6 != null) {
         BufferChecks.checkBuffer((IntBuffer)var6, 1);
      }

      CLMem var9 = new CLMem(nclCreateImage(var0.getPointer(), var1, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), var7), var0);
      return var9;
   }

   public static CLMem clCreateImage(CLContext var0, long var1, ByteBuffer var3, ByteBuffer var4, ShortBuffer var5, IntBuffer var6) {
      long var7 = CLCapabilities.clCreateImage;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      BufferChecks.checkBuffer(var4, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
      if (var5 != null) {
         BufferChecks.checkDirect(var5);
      }

      if (var6 != null) {
         BufferChecks.checkBuffer((IntBuffer)var6, 1);
      }

      CLMem var9 = new CLMem(nclCreateImage(var0.getPointer(), var1, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), var7), var0);
      return var9;
   }

   static native long nclCreateImage(long var0, long var2, long var4, long var6, long var8, long var10, long var12);

   public static CLProgram clCreateProgramWithBuiltInKernels(CLContext var0, PointerBuffer var1, ByteBuffer var2, IntBuffer var3) {
      long var4 = CLCapabilities.clCreateProgramWithBuiltInKernels;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      BufferChecks.checkDirect(var2);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      CLProgram var6 = new CLProgram(nclCreateProgramWithBuiltInKernels(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), MemoryUtil.getAddressSafe(var3), var4), var0);
      return var6;
   }

   static native long nclCreateProgramWithBuiltInKernels(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLProgram clCreateProgramWithBuiltInKernels(CLContext var0, PointerBuffer var1, CharSequence var2, IntBuffer var3) {
      long var4 = CLCapabilities.clCreateProgramWithBuiltInKernels;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      CLProgram var6 = new CLProgram(nclCreateProgramWithBuiltInKernels(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), APIUtil.getBuffer(var2), MemoryUtil.getAddressSafe(var3), var4), var0);
      return var6;
   }

   public static int clCompileProgram(CLProgram var0, PointerBuffer var1, ByteBuffer var2, PointerBuffer var3, ByteBuffer var4, CLCompileProgramCallback var5) {
      long var6 = CLCapabilities.clCompileProgram;
      BufferChecks.checkFunctionAddress(var6);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkNullTerminated(var4);
      long var8 = CallbackUtil.createGlobalRef(var5);
      if (var5 != null) {
         var5.setContext((CLContext)var0.getParent());
      }

      boolean var10 = false;
      int var13 = nclCompileProgram(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), 1, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5 == null ? 0L : var5.getPointer(), var8, var6);
      CallbackUtil.checkCallback(var13, var8);
      return var13;
   }

   static native int nclCompileProgram(long var0, int var2, long var3, long var5, int var7, long var8, long var10, long var12, long var14, long var16);

   public static int clCompileProgramMulti(CLProgram var0, PointerBuffer var1, ByteBuffer var2, PointerBuffer var3, ByteBuffer var4, CLCompileProgramCallback var5) {
      long var6 = CLCapabilities.clCompileProgram;
      BufferChecks.checkFunctionAddress(var6);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkNullTerminated(var4, var3.remaining());
      long var8 = CallbackUtil.createGlobalRef(var5);
      if (var5 != null) {
         var5.setContext((CLContext)var0.getParent());
      }

      boolean var10 = false;
      int var13 = nclCompileProgramMulti(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var3.remaining(), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5 == null ? 0L : var5.getPointer(), var8, var6);
      CallbackUtil.checkCallback(var13, var8);
      return var13;
   }

   static native int nclCompileProgramMulti(long var0, int var2, long var3, long var5, int var7, long var8, long var10, long var12, long var14, long var16);

   public static int clCompileProgram(CLProgram var0, PointerBuffer var1, ByteBuffer var2, PointerBuffer var3, ByteBuffer[] var4, CLCompileProgramCallback var5) {
      long var6 = CLCapabilities.clCompileProgram;
      BufferChecks.checkFunctionAddress(var6);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      BufferChecks.checkBuffer(var3, var4.length);
      BufferChecks.checkArray((Object[])var4, 1);
      long var8 = CallbackUtil.createGlobalRef(var5);
      if (var5 != null) {
         var5.setContext((CLContext)var0.getParent());
      }

      boolean var10 = false;
      int var13 = nclCompileProgram3(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4.length, MemoryUtil.getAddress(var3), var4, var5 == null ? 0L : var5.getPointer(), var8, var6);
      CallbackUtil.checkCallback(var13, var8);
      return var13;
   }

   static native int nclCompileProgram3(long var0, int var2, long var3, long var5, int var7, long var8, ByteBuffer[] var10, long var11, long var13, long var15);

   public static int clCompileProgram(CLProgram var0, PointerBuffer var1, CharSequence var2, PointerBuffer var3, CharSequence var4, CLCompileProgramCallback var5) {
      long var6 = CLCapabilities.clCompileProgram;
      BufferChecks.checkFunctionAddress(var6);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      long var8 = CallbackUtil.createGlobalRef(var5);
      if (var5 != null) {
         var5.setContext((CLContext)var0.getParent());
      }

      boolean var10 = false;
      int var13 = nclCompileProgram(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), APIUtil.getBufferNT(var2), 1, MemoryUtil.getAddress(var3), APIUtil.getBufferNT(var4), var5 == null ? 0L : var5.getPointer(), var8, var6);
      CallbackUtil.checkCallback(var13, var8);
      return var13;
   }

   public static int clCompileProgram(CLProgram var0, PointerBuffer var1, CharSequence var2, PointerBuffer var3, CharSequence[] var4, CLCompileProgramCallback var5) {
      long var6 = CLCapabilities.clCompileProgram;
      BufferChecks.checkFunctionAddress(var6);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      BufferChecks.checkArray(var4);
      long var8 = CallbackUtil.createGlobalRef(var5);
      if (var5 != null) {
         var5.setContext((CLContext)var0.getParent());
      }

      boolean var10 = false;
      int var13 = nclCompileProgramMulti(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), APIUtil.getBufferNT(var2), var3.remaining(), MemoryUtil.getAddress(var3), APIUtil.getBufferNT(var4), var5 == null ? 0L : var5.getPointer(), var8, var6);
      CallbackUtil.checkCallback(var13, var8);
      return var13;
   }

   public static CLProgram clLinkProgram(CLContext var0, PointerBuffer var1, ByteBuffer var2, PointerBuffer var3, CLLinkProgramCallback var4, IntBuffer var5) {
      long var6 = CLCapabilities.clLinkProgram;
      BufferChecks.checkFunctionAddress(var6);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkBuffer((IntBuffer)var5, 1);
      long var8 = CallbackUtil.createGlobalRef(var4);
      if (var4 != null) {
         var4.setContext(var0);
      }

      CLProgram var10 = null;
      var10 = new CLProgram(nclLinkProgram(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var3.remaining(), MemoryUtil.getAddress(var3), var4 == null ? 0L : var4.getPointer(), var8, MemoryUtil.getAddress(var5), var6), var0);
      CallbackUtil.checkCallback(var5.get(var5.position()), var8);
      return var10;
   }

   static native long nclLinkProgram(long var0, int var2, long var3, long var5, int var7, long var8, long var10, long var12, long var14, long var16);

   public static CLProgram clLinkProgram(CLContext var0, PointerBuffer var1, CharSequence var2, PointerBuffer var3, CLLinkProgramCallback var4, IntBuffer var5) {
      long var6 = CLCapabilities.clLinkProgram;
      BufferChecks.checkFunctionAddress(var6);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkDirect(var3);
      BufferChecks.checkBuffer((IntBuffer)var5, 1);
      long var8 = CallbackUtil.createGlobalRef(var4);
      if (var4 != null) {
         var4.setContext(var0);
      }

      CLProgram var10 = null;
      var10 = new CLProgram(nclLinkProgram(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), APIUtil.getBufferNT(var2), var3.remaining(), MemoryUtil.getAddress(var3), var4 == null ? 0L : var4.getPointer(), var8, MemoryUtil.getAddress(var5), var6), var0);
      CallbackUtil.checkCallback(var5.get(var5.position()), var8);
      return var10;
   }

   public static int clUnloadPlatformCompiler(CLPlatform var0) {
      long var1 = CLCapabilities.clUnloadPlatformCompiler;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclUnloadPlatformCompiler(var0.getPointer(), var1);
      return var3;
   }

   static native int nclUnloadPlatformCompiler(long var0, long var2);

   public static int clGetKernelArgInfo(CLKernel var0, int var1, int var2, ByteBuffer var3, PointerBuffer var4) {
      long var5 = CLCapabilities.clGetKernelArgInfo;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var4, 1);
      }

      int var7 = nclGetKernelArgInfo(var0.getPointer(), var1, var2, (long)(var3 == null ? 0 : var3.remaining()), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var5);
      return var7;
   }

   static native int nclGetKernelArgInfo(long var0, int var2, int var3, long var4, long var6, long var8, long var10);

   public static int clEnqueueFillBuffer(CLCommandQueue var0, CLMem var1, ByteBuffer var2, long var3, long var5, PointerBuffer var7, PointerBuffer var8) {
      long var9 = CLCapabilities.clEnqueueFillBuffer;
      BufferChecks.checkFunctionAddress(var9);
      BufferChecks.checkDirect(var2);
      if (var7 != null) {
         BufferChecks.checkDirect(var7);
      }

      if (var8 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var8, 1);
      }

      int var11 = nclEnqueueFillBuffer(var0.getPointer(), var1.getPointer(), MemoryUtil.getAddress(var2), (long)var2.remaining(), var3, var5, var7 == null ? 0 : var7.remaining(), MemoryUtil.getAddressSafe(var7), MemoryUtil.getAddressSafe(var8), var9);
      return var11;
   }

   static native int nclEnqueueFillBuffer(long var0, long var2, long var4, long var6, long var8, long var10, int var12, long var13, long var15, long var17);

   public static int clEnqueueFillImage(CLCommandQueue var0, CLMem var1, ByteBuffer var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, PointerBuffer var6) {
      long var7 = CLCapabilities.clEnqueueFillImage;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer((ByteBuffer)var2, 16);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      if (var5 != null) {
         BufferChecks.checkDirect(var5);
      }

      if (var6 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var6, 1);
      }

      int var9 = nclEnqueueFillImage(var0.getPointer(), var1.getPointer(), MemoryUtil.getAddress(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5 == null ? 0 : var5.remaining(), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), var7);
      return var9;
   }

   static native int nclEnqueueFillImage(long var0, long var2, long var4, long var6, long var8, int var10, long var11, long var13, long var15);

   public static int clEnqueueMigrateMemObjects(CLCommandQueue var0, PointerBuffer var1, long var2, PointerBuffer var4, PointerBuffer var5) {
      long var6 = CLCapabilities.clEnqueueMigrateMemObjects;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkDirect(var1);
      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var5, 1);
      }

      int var8 = nclEnqueueMigrateMemObjects(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), var2, var4 == null ? 0 : var4.remaining(), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var6);
      return var8;
   }

   static native int nclEnqueueMigrateMemObjects(long var0, int var2, long var3, long var5, int var7, long var8, long var10, long var12);

   public static int clEnqueueMarkerWithWaitList(CLCommandQueue var0, PointerBuffer var1, PointerBuffer var2) {
      long var3 = CLCapabilities.clEnqueueMarkerWithWaitList;
      BufferChecks.checkFunctionAddress(var3);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      if (var2 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var2, 1);
      }

      int var5 = nclEnqueueMarkerWithWaitList(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), var3);
      return var5;
   }

   static native int nclEnqueueMarkerWithWaitList(long var0, int var2, long var3, long var5, long var7);

   public static int clEnqueueBarrierWithWaitList(CLCommandQueue var0, PointerBuffer var1, PointerBuffer var2) {
      long var3 = CLCapabilities.clEnqueueBarrierWithWaitList;
      BufferChecks.checkFunctionAddress(var3);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      if (var2 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var2, 1);
      }

      int var5 = nclEnqueueBarrierWithWaitList(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), var3);
      return var5;
   }

   static native int nclEnqueueBarrierWithWaitList(long var0, int var2, long var3, long var5, long var7);

   public static int clSetPrintfCallback(CLContext var0, CLPrintfCallback var1) {
      long var2 = CLCapabilities.clSetPrintfCallback;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = CallbackUtil.createGlobalRef(var1);
      boolean var6 = false;
      int var9 = nclSetPrintfCallback(var0.getPointer(), var1.getPointer(), var4, var2);
      var0.setPrintfCallback(var4, var9);
      return var9;
   }

   static native int nclSetPrintfCallback(long var0, long var2, long var4, long var6);

   static CLFunctionAddress clGetExtensionFunctionAddressForPlatform(CLPlatform var0, ByteBuffer var1) {
      long var2 = CLCapabilities.clGetExtensionFunctionAddressForPlatform;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      CLFunctionAddress var4 = new CLFunctionAddress(nclGetExtensionFunctionAddressForPlatform(var0.getPointer(), MemoryUtil.getAddress(var1), var2));
      return var4;
   }

   static native long nclGetExtensionFunctionAddressForPlatform(long var0, long var2, long var4);

   static CLFunctionAddress clGetExtensionFunctionAddressForPlatform(CLPlatform var0, CharSequence var1) {
      long var2 = CLCapabilities.clGetExtensionFunctionAddressForPlatform;
      BufferChecks.checkFunctionAddress(var2);
      CLFunctionAddress var4 = new CLFunctionAddress(nclGetExtensionFunctionAddressForPlatform(var0.getPointer(), APIUtil.getBufferNT(var1), var2));
      return var4;
   }
}
