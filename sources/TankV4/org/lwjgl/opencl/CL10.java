package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class CL10 {
   public static final int CL_SUCCESS = 0;
   public static final int CL_DEVICE_NOT_FOUND = -1;
   public static final int CL_DEVICE_NOT_AVAILABLE = -2;
   public static final int CL_COMPILER_NOT_AVAILABLE = -3;
   public static final int CL_MEM_OBJECT_ALLOCATION_FAILURE = -4;
   public static final int CL_OUT_OF_RESOURCES = -5;
   public static final int CL_OUT_OF_HOST_MEMORY = -6;
   public static final int CL_PROFILING_INFO_NOT_AVAILABLE = -7;
   public static final int CL_MEM_COPY_OVERLAP = -8;
   public static final int CL_IMAGE_FORMAT_MISMATCH = -9;
   public static final int CL_IMAGE_FORMAT_NOT_SUPPORTED = -10;
   public static final int CL_BUILD_PROGRAM_FAILURE = -11;
   public static final int CL_MAP_FAILURE = -12;
   public static final int CL_INVALID_VALUE = -30;
   public static final int CL_INVALID_DEVICE_TYPE = -31;
   public static final int CL_INVALID_PLATFORM = -32;
   public static final int CL_INVALID_DEVICE = -33;
   public static final int CL_INVALID_CONTEXT = -34;
   public static final int CL_INVALID_QUEUE_PROPERTIES = -35;
   public static final int CL_INVALID_COMMAND_QUEUE = -36;
   public static final int CL_INVALID_HOST_PTR = -37;
   public static final int CL_INVALID_MEM_OBJECT = -38;
   public static final int CL_INVALID_IMAGE_FORMAT_DESCRIPTOR = -39;
   public static final int CL_INVALID_IMAGE_SIZE = -40;
   public static final int CL_INVALID_SAMPLER = -41;
   public static final int CL_INVALID_BINARY = -42;
   public static final int CL_INVALID_BUILD_OPTIONS = -43;
   public static final int CL_INVALID_PROGRAM = -44;
   public static final int CL_INVALID_PROGRAM_EXECUTABLE = -45;
   public static final int CL_INVALID_KERNEL_NAME = -46;
   public static final int CL_INVALID_KERNEL_DEFINITION = -47;
   public static final int CL_INVALID_KERNEL = -48;
   public static final int CL_INVALID_ARG_INDEX = -49;
   public static final int CL_INVALID_ARG_VALUE = -50;
   public static final int CL_INVALID_ARG_SIZE = -51;
   public static final int CL_INVALID_KERNEL_ARGS = -52;
   public static final int CL_INVALID_WORK_DIMENSION = -53;
   public static final int CL_INVALID_WORK_GROUP_SIZE = -54;
   public static final int CL_INVALID_WORK_ITEM_SIZE = -55;
   public static final int CL_INVALID_GLOBAL_OFFSET = -56;
   public static final int CL_INVALID_EVENT_WAIT_LIST = -57;
   public static final int CL_INVALID_EVENT = -58;
   public static final int CL_INVALID_OPERATION = -59;
   public static final int CL_INVALID_GL_OBJECT = -60;
   public static final int CL_INVALID_BUFFER_SIZE = -61;
   public static final int CL_INVALID_MIP_LEVEL = -62;
   public static final int CL_INVALID_GLOBAL_WORK_SIZE = -63;
   public static final int CL_VERSION_1_0 = 1;
   public static final int CL_FALSE = 0;
   public static final int CL_TRUE = 1;
   public static final int CL_PLATFORM_PROFILE = 2304;
   public static final int CL_PLATFORM_VERSION = 2305;
   public static final int CL_PLATFORM_NAME = 2306;
   public static final int CL_PLATFORM_VENDOR = 2307;
   public static final int CL_PLATFORM_EXTENSIONS = 2308;
   public static final int CL_DEVICE_TYPE_DEFAULT = 1;
   public static final int CL_DEVICE_TYPE_CPU = 2;
   public static final int CL_DEVICE_TYPE_GPU = 4;
   public static final int CL_DEVICE_TYPE_ACCELERATOR = 8;
   public static final int CL_DEVICE_TYPE_ALL = -1;
   public static final int CL_DEVICE_TYPE = 4096;
   public static final int CL_DEVICE_VENDOR_ID = 4097;
   public static final int CL_DEVICE_MAX_COMPUTE_UNITS = 4098;
   public static final int CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS = 4099;
   public static final int CL_DEVICE_MAX_WORK_GROUP_SIZE = 4100;
   public static final int CL_DEVICE_MAX_WORK_ITEM_SIZES = 4101;
   public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR = 4102;
   public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT = 4103;
   public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_ = 4104;
   public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG = 4105;
   public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT = 4106;
   public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE = 4107;
   public static final int CL_DEVICE_MAX_CLOCK_FREQUENCY = 4108;
   public static final int CL_DEVICE_ADDRESS_BITS = 4109;
   public static final int CL_DEVICE_MAX_READ_IMAGE_ARGS = 4110;
   public static final int CL_DEVICE_MAX_WRITE_IMAGE_ARGS = 4111;
   public static final int CL_DEVICE_MAX_MEM_ALLOC_SIZE = 4112;
   public static final int CL_DEVICE_IMAGE2D_MAX_WIDTH = 4113;
   public static final int CL_DEVICE_IMAGE2D_MAX_HEIGHT = 4114;
   public static final int CL_DEVICE_IMAGE3D_MAX_WIDTH = 4115;
   public static final int CL_DEVICE_IMAGE3D_MAX_HEIGHT = 4116;
   public static final int CL_DEVICE_IMAGE3D_MAX_DEPTH = 4117;
   public static final int CL_DEVICE_IMAGE_SUPPORT = 4118;
   public static final int CL_DEVICE_MAX_PARAMETER_SIZE = 4119;
   public static final int CL_DEVICE_MAX_SAMPLERS = 4120;
   public static final int CL_DEVICE_MEM_BASE_ADDR_ALIGN = 4121;
   public static final int CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE = 4122;
   public static final int CL_DEVICE_SINGLE_FP_CONFIG = 4123;
   public static final int CL_DEVICE_GLOBAL_MEM_CACHE_TYPE = 4124;
   public static final int CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE = 4125;
   public static final int CL_DEVICE_GLOBAL_MEM_CACHE_SIZE = 4126;
   public static final int CL_DEVICE_GLOBAL_MEM_SIZE = 4127;
   public static final int CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE = 4128;
   public static final int CL_DEVICE_MAX_CONSTANT_ARGS = 4129;
   public static final int CL_DEVICE_LOCAL_MEM_TYPE = 4130;
   public static final int CL_DEVICE_LOCAL_MEM_SIZE = 4131;
   public static final int CL_DEVICE_ERROR_CORRECTION_SUPPORT = 4132;
   public static final int CL_DEVICE_PROFILING_TIMER_RESOLUTION = 4133;
   public static final int CL_DEVICE_ENDIAN_LITTLE = 4134;
   public static final int CL_DEVICE_AVAILABLE = 4135;
   public static final int CL_DEVICE_COMPILER_AVAILABLE = 4136;
   public static final int CL_DEVICE_EXECUTION_CAPABILITIES = 4137;
   public static final int CL_DEVICE_QUEUE_PROPERTIES = 4138;
   public static final int CL_DEVICE_NAME = 4139;
   public static final int CL_DEVICE_VENDOR = 4140;
   public static final int CL_DRIVER_VERSION = 4141;
   public static final int CL_DEVICE_PROFILE = 4142;
   public static final int CL_DEVICE_VERSION = 4143;
   public static final int CL_DEVICE_EXTENSIONS = 4144;
   public static final int CL_DEVICE_PLATFORM = 4145;
   public static final int CL_FP_DENORM = 1;
   public static final int CL_FP_INF_NAN = 2;
   public static final int CL_FP_ROUND_TO_NEAREST = 4;
   public static final int CL_FP_ROUND_TO_ZERO = 8;
   public static final int CL_FP_ROUND_TO_INF = 16;
   public static final int CL_FP_FMA = 32;
   public static final int CL_NONE = 0;
   public static final int CL_READ_ONLY_CACHE = 1;
   public static final int CL_READ_WRITE_CACHE = 2;
   public static final int CL_LOCAL = 1;
   public static final int CL_GLOBAL = 2;
   public static final int CL_EXEC_KERNEL = 1;
   public static final int CL_EXEC_NATIVE_KERNEL = 2;
   public static final int CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE = 1;
   public static final int CL_QUEUE_PROFILING_ENABLE = 2;
   public static final int CL_CONTEXT_REFERENCE_COUNT = 4224;
   public static final int CL_CONTEXT_DEVICES = 4225;
   public static final int CL_CONTEXT_PROPERTIES = 4226;
   public static final int CL_CONTEXT_PLATFORM = 4228;
   public static final int CL_QUEUE_CONTEXT = 4240;
   public static final int CL_QUEUE_DEVICE = 4241;
   public static final int CL_QUEUE_REFERENCE_COUNT = 4242;
   public static final int CL_QUEUE_PROPERTIES = 4243;
   public static final int CL_MEM_READ_WRITE = 1;
   public static final int CL_MEM_WRITE_ONLY = 2;
   public static final int CL_MEM_READ_ONLY = 4;
   public static final int CL_MEM_USE_HOST_PTR = 8;
   public static final int CL_MEM_ALLOC_HOST_PTR = 16;
   public static final int CL_MEM_COPY_HOST_PTR = 32;
   public static final int CL_R = 4272;
   public static final int CL_A = 4273;
   public static final int CL_RG = 4274;
   public static final int CL_RA = 4275;
   public static final int CL_RGB = 4276;
   public static final int CL_RGBA = 4277;
   public static final int CL_BGRA = 4278;
   public static final int CL_ARGB = 4279;
   public static final int CL_INTENSITY = 4280;
   public static final int CL_LUMINANCE = 4281;
   public static final int CL_SNORM_INT8 = 4304;
   public static final int CL_SNORM_INT16 = 4305;
   public static final int CL_UNORM_INT8 = 4306;
   public static final int CL_UNORM_INT16 = 4307;
   public static final int CL_UNORM_SHORT_565 = 4308;
   public static final int CL_UNORM_SHORT_555 = 4309;
   public static final int CL_UNORM_INT_101010 = 4310;
   public static final int CL_SIGNED_INT8 = 4311;
   public static final int CL_SIGNED_INT16 = 4312;
   public static final int CL_SIGNED_INT32 = 4313;
   public static final int CL_UNSIGNED_INT8 = 4314;
   public static final int CL_UNSIGNED_INT16 = 4315;
   public static final int CL_UNSIGNED_INT32 = 4316;
   public static final int CL_HALF_FLOAT = 4317;
   public static final int CL_FLOAT = 4318;
   public static final int CL_MEM_OBJECT_BUFFER = 4336;
   public static final int CL_MEM_OBJECT_IMAGE2D = 4337;
   public static final int CL_MEM_OBJECT_IMAGE3D = 4338;
   public static final int CL_MEM_TYPE = 4352;
   public static final int CL_MEM_FLAGS = 4353;
   public static final int CL_MEM_SIZE = 4354;
   public static final int CL_MEM_HOST_PTR = 4355;
   public static final int CL_MEM_MAP_COUNT = 4356;
   public static final int CL_MEM_REFERENCE_COUNT = 4357;
   public static final int CL_MEM_CONTEXT = 4358;
   public static final int CL_IMAGE_FORMAT = 4368;
   public static final int CL_IMAGE_ELEMENT_SIZE = 4369;
   public static final int CL_IMAGE_ROW_PITCH = 4370;
   public static final int CL_IMAGE_SLICE_PITCH = 4371;
   public static final int CL_IMAGE_WIDTH = 4372;
   public static final int CL_IMAGE_HEIGHT = 4373;
   public static final int CL_IMAGE_DEPTH = 4374;
   public static final int CL_ADDRESS_NONE = 4400;
   public static final int CL_ADDRESS_CLAMP_TO_EDGE = 4401;
   public static final int CL_ADDRESS_CLAMP = 4402;
   public static final int CL_ADDRESS_REPEAT = 4403;
   public static final int CL_FILTER_NEAREST = 4416;
   public static final int CL_FILTER_LINEAR = 4417;
   public static final int CL_SAMPLER_REFERENCE_COUNT = 4432;
   public static final int CL_SAMPLER_CONTEXT = 4433;
   public static final int CL_SAMPLER_NORMALIZED_COORDS = 4434;
   public static final int CL_SAMPLER_ADDRESSING_MODE = 4435;
   public static final int CL_SAMPLER_FILTER_MODE = 4436;
   public static final int CL_MAP_READ = 1;
   public static final int CL_MAP_WRITE = 2;
   public static final int CL_PROGRAM_REFERENCE_COUNT = 4448;
   public static final int CL_PROGRAM_CONTEXT = 4449;
   public static final int CL_PROGRAM_NUM_DEVICES = 4450;
   public static final int CL_PROGRAM_DEVICES = 4451;
   public static final int CL_PROGRAM_SOURCE = 4452;
   public static final int CL_PROGRAM_BINARY_SIZES = 4453;
   public static final int CL_PROGRAM_BINARIES = 4454;
   public static final int CL_PROGRAM_BUILD_STATUS = 4481;
   public static final int CL_PROGRAM_BUILD_OPTIONS = 4482;
   public static final int CL_PROGRAM_BUILD_LOG = 4483;
   public static final int CL_BUILD_SUCCESS = 0;
   public static final int CL_BUILD_NONE = -1;
   public static final int CL_BUILD_ERROR = -2;
   public static final int CL_BUILD_IN_PROGRESS = -3;
   public static final int CL_KERNEL_FUNCTION_NAME = 4496;
   public static final int CL_KERNEL_NUM_ARGS = 4497;
   public static final int CL_KERNEL_REFERENCE_COUNT = 4498;
   public static final int CL_KERNEL_CONTEXT = 4499;
   public static final int CL_KERNEL_PROGRAM = 4500;
   public static final int CL_KERNEL_WORK_GROUP_SIZE = 4528;
   public static final int CL_KERNEL_COMPILE_WORK_GROUP_SIZE = 4529;
   public static final int CL_KERNEL_LOCAL_MEM_SIZE = 4530;
   public static final int CL_EVENT_COMMAND_QUEUE = 4560;
   public static final int CL_EVENT_COMMAND_TYPE = 4561;
   public static final int CL_EVENT_REFERENCE_COUNT = 4562;
   public static final int CL_EVENT_COMMAND_EXECUTION_STATUS = 4563;
   public static final int CL_COMMAND_NDRANGE_KERNEL = 4592;
   public static final int CL_COMMAND_TASK = 4593;
   public static final int CL_COMMAND_NATIVE_KERNEL = 4594;
   public static final int CL_COMMAND_READ_BUFFER = 4595;
   public static final int CL_COMMAND_WRITE_BUFFER = 4596;
   public static final int CL_COMMAND_COPY_BUFFER = 4597;
   public static final int CL_COMMAND_READ_IMAGE = 4598;
   public static final int CL_COMMAND_WRITE_IMAGE = 4599;
   public static final int CL_COMMAND_COPY_IMAGE = 4600;
   public static final int CL_COMMAND_COPY_IMAGE_TO_BUFFER = 4601;
   public static final int CL_COMMAND_COPY_BUFFER_TO_IMAGE = 4602;
   public static final int CL_COMMAND_MAP_BUFFER = 4603;
   public static final int CL_COMMAND_MAP_IMAGE = 4604;
   public static final int CL_COMMAND_UNMAP_MEM_OBJECT = 4605;
   public static final int CL_COMMAND_MARKER = 4606;
   public static final int CL_COMMAND_ACQUIRE_GL_OBJECTS = 4607;
   public static final int CL_COMMAND_RELEASE_GL_OBJECTS = 4608;
   public static final int CL_COMPLETE = 0;
   public static final int CL_RUNNING = 1;
   public static final int CL_SUBMITTED = 2;
   public static final int CL_QUEUED = 3;
   public static final int CL_PROFILING_COMMAND_QUEUED = 4736;
   public static final int CL_PROFILING_COMMAND_SUBMIT = 4737;
   public static final int CL_PROFILING_COMMAND_START = 4738;
   public static final int CL_PROFILING_COMMAND_END = 4739;

   private CL10() {
   }

   public static int clGetPlatformIDs(PointerBuffer var0, IntBuffer var1) {
      long var2 = CLCapabilities.clGetPlatformIDs;
      BufferChecks.checkFunctionAddress(var2);
      if (var0 != null) {
         BufferChecks.checkDirect(var0);
      }

      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      if (var1 == null) {
         var1 = APIUtil.getBufferInt();
      }

      int var4 = nclGetPlatformIDs(var0 == null ? 0 : var0.remaining(), MemoryUtil.getAddressSafe(var0), MemoryUtil.getAddressSafe(var1), var2);
      if (var4 == 0 && var0 != null) {
         CLPlatform.registerCLPlatforms(var0, var1);
      }

      return var4;
   }

   static native int nclGetPlatformIDs(int var0, long var1, long var3, long var5);

   public static int clGetPlatformInfo(CLPlatform var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetPlatformInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetPlatformInfo(var0 == null ? 0L : var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetPlatformInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static int clGetDeviceIDs(CLPlatform var0, long var1, PointerBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clGetDeviceIDs;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      } else {
         var4 = APIUtil.getBufferInt();
      }

      int var7 = nclGetDeviceIDs(var0.getPointer(), var1, var3 == null ? 0 : var3.remaining(), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var5);
      if (var7 == 0 && var3 != null) {
         var0.registerCLDevices(var3, var4);
      }

      return var7;
   }

   static native int nclGetDeviceIDs(long var0, long var2, int var4, long var5, long var7, long var9);

   public static int clGetDeviceInfo(CLDevice var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetDeviceInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetDeviceInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetDeviceInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLContext clCreateContext(PointerBuffer var0, PointerBuffer var1, CLContextCallback var2, IntBuffer var3) {
      long var4 = CLCapabilities.clCreateContext;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((PointerBuffer)var0, 3);
      BufferChecks.checkNullTerminated(var0);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      long var6 = var2 != null && !var2.isCustom() ? CallbackUtil.createGlobalRef(var2) : 0L;
      CLContext var8 = null;
      var8 = new CLContext(nclCreateContext(MemoryUtil.getAddress(var0), var1.remaining(), MemoryUtil.getAddress(var1), var2 == null ? 0L : var2.getPointer(), var6, MemoryUtil.getAddressSafe(var3), var4), APIUtil.getCLPlatform(var0));
      if (var8 != null) {
         var8.setContextCallback(var6);
      }

      return var8;
   }

   static native long nclCreateContext(long var0, int var2, long var3, long var5, long var7, long var9, long var11);

   public static CLContext clCreateContext(PointerBuffer var0, CLDevice var1, CLContextCallback var2, IntBuffer var3) {
      long var4 = CLCapabilities.clCreateContext;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((PointerBuffer)var0, 3);
      BufferChecks.checkNullTerminated(var0);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      long var6 = var2 != null && !var2.isCustom() ? CallbackUtil.createGlobalRef(var2) : 0L;
      CLContext var8 = null;
      var8 = new CLContext(nclCreateContext(MemoryUtil.getAddress(var0), 1, APIUtil.getPointer(var1), var2 == null ? 0L : var2.getPointer(), var6, MemoryUtil.getAddressSafe(var3), var4), APIUtil.getCLPlatform(var0));
      if (var8 != null) {
         var8.setContextCallback(var6);
      }

      return var8;
   }

   public static CLContext clCreateContextFromType(PointerBuffer var0, long var1, CLContextCallback var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateContextFromType;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((PointerBuffer)var0, 3);
      BufferChecks.checkNullTerminated(var0);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      long var7 = var3 != null && !var3.isCustom() ? CallbackUtil.createGlobalRef(var3) : 0L;
      CLContext var9 = null;
      var9 = new CLContext(nclCreateContextFromType(MemoryUtil.getAddress(var0), var1, var3 == null ? 0L : var3.getPointer(), var7, MemoryUtil.getAddressSafe(var4), var5), APIUtil.getCLPlatform(var0));
      if (var9 != null) {
         var9.setContextCallback(var7);
      }

      return var9;
   }

   static native long nclCreateContextFromType(long var0, long var2, long var4, long var6, long var8, long var10);

   public static int clRetainContext(CLContext var0) {
      long var1 = CLCapabilities.clRetainContext;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainContext(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainContext(long var0, long var2);

   public static int clReleaseContext(CLContext var0) {
      long var1 = CLCapabilities.clReleaseContext;
      BufferChecks.checkFunctionAddress(var1);
      APIUtil.releaseObjects(var0);
      int var3 = nclReleaseContext(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.releaseImpl();
      }

      return var3;
   }

   static native int nclReleaseContext(long var0, long var2);

   public static int clGetContextInfo(CLContext var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetContextInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      if (var3 == null && APIUtil.isDevicesParam(var1)) {
         var3 = APIUtil.getBufferPointer();
      }

      int var6 = nclGetContextInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0 && var2 != null && APIUtil.isDevicesParam(var1)) {
         ((CLPlatform)var0.getParent()).registerCLDevices(var2, var3);
      }

      return var6;
   }

   static native int nclGetContextInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLCommandQueue clCreateCommandQueue(CLContext var0, CLDevice var1, long var2, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateCommandQueue;
      BufferChecks.checkFunctionAddress(var5);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLCommandQueue var7 = new CLCommandQueue(nclCreateCommandQueue(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddressSafe(var4), var5), var0, var1);
      return var7;
   }

   static native long nclCreateCommandQueue(long var0, long var2, long var4, long var6, long var8);

   public static int clRetainCommandQueue(CLCommandQueue var0) {
      long var1 = CLCapabilities.clRetainCommandQueue;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainCommandQueue(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainCommandQueue(long var0, long var2);

   public static int clReleaseCommandQueue(CLCommandQueue var0) {
      long var1 = CLCapabilities.clReleaseCommandQueue;
      BufferChecks.checkFunctionAddress(var1);
      APIUtil.releaseObjects(var0);
      int var3 = nclReleaseCommandQueue(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseCommandQueue(long var0, long var2);

   public static int clGetCommandQueueInfo(CLCommandQueue var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetCommandQueueInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetCommandQueueInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetCommandQueueInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLMem clCreateBuffer(CLContext var0, long var1, long var3, IntBuffer var5) {
      long var6 = CLCapabilities.clCreateBuffer;
      BufferChecks.checkFunctionAddress(var6);
      if (var5 != null) {
         BufferChecks.checkBuffer((IntBuffer)var5, 1);
      }

      CLMem var8 = new CLMem(nclCreateBuffer(var0.getPointer(), var1, var3, 0L, MemoryUtil.getAddressSafe(var5), var6), var0);
      return var8;
   }

   public static CLMem clCreateBuffer(CLContext var0, long var1, ByteBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateBuffer;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateBuffer(var0.getPointer(), var1, (long)var3.remaining(), MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   public static CLMem clCreateBuffer(CLContext var0, long var1, DoubleBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateBuffer;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateBuffer(var0.getPointer(), var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   public static CLMem clCreateBuffer(CLContext var0, long var1, FloatBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateBuffer;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateBuffer(var0.getPointer(), var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   public static CLMem clCreateBuffer(CLContext var0, long var1, IntBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateBuffer;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateBuffer(var0.getPointer(), var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   public static CLMem clCreateBuffer(CLContext var0, long var1, LongBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateBuffer;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateBuffer(var0.getPointer(), var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   public static CLMem clCreateBuffer(CLContext var0, long var1, ShortBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateBuffer;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateBuffer(var0.getPointer(), var1, (long)(var3.remaining() << 1), MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   static native long nclCreateBuffer(long var0, long var2, long var4, long var6, long var8, long var10);

   public static int clEnqueueReadBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, ByteBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueReadBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueReadBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)var5.remaining(), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueReadBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, DoubleBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueReadBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueReadBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 3), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueReadBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, FloatBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueReadBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueReadBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 2), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueReadBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, IntBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueReadBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueReadBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 2), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueReadBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, LongBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueReadBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueReadBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 3), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueReadBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, ShortBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueReadBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueReadBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 1), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   static native int nclEnqueueReadBuffer(long var0, long var2, int var4, long var5, long var7, long var9, int var11, long var12, long var14, long var16);

   public static int clEnqueueWriteBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, ByteBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueWriteBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueWriteBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)var5.remaining(), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueWriteBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, DoubleBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueWriteBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueWriteBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 3), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueWriteBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, FloatBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueWriteBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueWriteBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 2), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueWriteBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, IntBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueWriteBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueWriteBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 2), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueWriteBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, LongBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueWriteBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueWriteBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 3), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   public static int clEnqueueWriteBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, ShortBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueWriteBuffer;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var5);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueWriteBuffer(var0.getPointer(), var1.getPointer(), var2, var3, (long)(var5.remaining() << 1), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   static native int nclEnqueueWriteBuffer(long var0, long var2, int var4, long var5, long var7, long var9, int var11, long var12, long var14, long var16);

   public static int clEnqueueCopyBuffer(CLCommandQueue var0, CLMem var1, CLMem var2, long var3, long var5, long var7, PointerBuffer var9, PointerBuffer var10) {
      long var11 = CLCapabilities.clEnqueueCopyBuffer;
      BufferChecks.checkFunctionAddress(var11);
      if (var9 != null) {
         BufferChecks.checkDirect(var9);
      }

      if (var10 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var10, 1);
      }

      int var13 = nclEnqueueCopyBuffer(var0.getPointer(), var1.getPointer(), var2.getPointer(), var3, var5, var7, var9 == null ? 0 : var9.remaining(), MemoryUtil.getAddressSafe(var9), MemoryUtil.getAddressSafe(var10), var11);
      if (var13 == 0) {
         var0.registerCLEvent(var10);
      }

      return var13;
   }

   static native int nclEnqueueCopyBuffer(long var0, long var2, long var4, long var6, long var8, long var10, int var12, long var13, long var15, long var17);

   public static ByteBuffer clEnqueueMapBuffer(CLCommandQueue var0, CLMem var1, int var2, long var3, long var5, long var7, PointerBuffer var9, PointerBuffer var10, IntBuffer var11) {
      long var12 = CLCapabilities.clEnqueueMapBuffer;
      BufferChecks.checkFunctionAddress(var12);
      if (var9 != null) {
         BufferChecks.checkDirect(var9);
      }

      if (var10 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var10, 1);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((IntBuffer)var11, 1);
      }

      ByteBuffer var14 = nclEnqueueMapBuffer(var0.getPointer(), var1.getPointer(), var2, var3, var5, var7, var9 == null ? 0 : var9.remaining(), MemoryUtil.getAddressSafe(var9), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var7, var12);
      if (var14 != null) {
         var0.registerCLEvent(var10);
      }

      return LWJGLUtil.CHECKS && var14 == null ? null : var14.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nclEnqueueMapBuffer(long var0, long var2, int var4, long var5, long var7, long var9, int var11, long var12, long var14, long var16, long var18, long var20);

   public static CLMem clCreateImage2D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, ByteBuffer var10, IntBuffer var11) {
      long var12 = CLCapabilities.clCreateImage2D;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, CLChecks.calculateImage2DSize(var3, var4, var6, var8));
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((IntBuffer)var11, 1);
      }

      CLMem var14 = new CLMem(nclCreateImage2D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12), var0);
      return var14;
   }

   public static CLMem clCreateImage2D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, FloatBuffer var10, IntBuffer var11) {
      long var12 = CLCapabilities.clCreateImage2D;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, CLChecks.calculateImage2DSize(var3, var4, var6, var8));
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((IntBuffer)var11, 1);
      }

      CLMem var14 = new CLMem(nclCreateImage2D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12), var0);
      return var14;
   }

   public static CLMem clCreateImage2D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, IntBuffer var10, IntBuffer var11) {
      long var12 = CLCapabilities.clCreateImage2D;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, CLChecks.calculateImage2DSize(var3, var4, var6, var8));
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((IntBuffer)var11, 1);
      }

      CLMem var14 = new CLMem(nclCreateImage2D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12), var0);
      return var14;
   }

   public static CLMem clCreateImage2D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, ShortBuffer var10, IntBuffer var11) {
      long var12 = CLCapabilities.clCreateImage2D;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, CLChecks.calculateImage2DSize(var3, var4, var6, var8));
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((IntBuffer)var11, 1);
      }

      CLMem var14 = new CLMem(nclCreateImage2D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12), var0);
      return var14;
   }

   static native long nclCreateImage2D(long var0, long var2, long var4, long var6, long var8, long var10, long var12, long var14, long var16);

   public static CLMem clCreateImage3D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, long var10, long var12, ByteBuffer var14, IntBuffer var15) {
      long var16 = CLCapabilities.clCreateImage3D;
      BufferChecks.checkFunctionAddress(var16);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var14 != null) {
         BufferChecks.checkBuffer(var14, CLChecks.calculateImage3DSize(var3, var4, var6, var6, var10, var12));
      }

      if (var15 != null) {
         BufferChecks.checkBuffer((IntBuffer)var15, 1);
      }

      CLMem var18 = new CLMem(nclCreateImage3D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, var10, var12, MemoryUtil.getAddressSafe(var14), MemoryUtil.getAddressSafe(var15), var16), var0);
      return var18;
   }

   public static CLMem clCreateImage3D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, long var10, long var12, FloatBuffer var14, IntBuffer var15) {
      long var16 = CLCapabilities.clCreateImage3D;
      BufferChecks.checkFunctionAddress(var16);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var14 != null) {
         BufferChecks.checkBuffer(var14, CLChecks.calculateImage3DSize(var3, var4, var6, var6, var10, var12));
      }

      if (var15 != null) {
         BufferChecks.checkBuffer((IntBuffer)var15, 1);
      }

      CLMem var18 = new CLMem(nclCreateImage3D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, var10, var12, MemoryUtil.getAddressSafe(var14), MemoryUtil.getAddressSafe(var15), var16), var0);
      return var18;
   }

   public static CLMem clCreateImage3D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, long var10, long var12, IntBuffer var14, IntBuffer var15) {
      long var16 = CLCapabilities.clCreateImage3D;
      BufferChecks.checkFunctionAddress(var16);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var14 != null) {
         BufferChecks.checkBuffer(var14, CLChecks.calculateImage3DSize(var3, var4, var6, var6, var10, var12));
      }

      if (var15 != null) {
         BufferChecks.checkBuffer((IntBuffer)var15, 1);
      }

      CLMem var18 = new CLMem(nclCreateImage3D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, var10, var12, MemoryUtil.getAddressSafe(var14), MemoryUtil.getAddressSafe(var15), var16), var0);
      return var18;
   }

   public static CLMem clCreateImage3D(CLContext var0, long var1, ByteBuffer var3, long var4, long var6, long var8, long var10, long var12, ShortBuffer var14, IntBuffer var15) {
      long var16 = CLCapabilities.clCreateImage3D;
      BufferChecks.checkFunctionAddress(var16);
      BufferChecks.checkBuffer((ByteBuffer)var3, 8);
      if (var14 != null) {
         BufferChecks.checkBuffer(var14, CLChecks.calculateImage3DSize(var3, var4, var6, var6, var10, var12));
      }

      if (var15 != null) {
         BufferChecks.checkBuffer((IntBuffer)var15, 1);
      }

      CLMem var18 = new CLMem(nclCreateImage3D(var0.getPointer(), var1, MemoryUtil.getAddress(var3), var4, var6, var8, var10, var12, MemoryUtil.getAddressSafe(var14), MemoryUtil.getAddressSafe(var15), var16), var0);
      return var18;
   }

   static native long nclCreateImage3D(long var0, long var2, long var4, long var6, long var8, long var10, long var12, long var14, long var16, long var18, long var20);

   public static int clGetSupportedImageFormats(CLContext var0, long var1, int var3, ByteBuffer var4, IntBuffer var5) {
      long var6 = CLCapabilities.clGetSupportedImageFormats;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer((IntBuffer)var5, 1);
      }

      int var8 = nclGetSupportedImageFormats(var0.getPointer(), var1, var3, (var4 == null ? 0 : var4.remaining()) / 8, MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var6);
      return var8;
   }

   static native int nclGetSupportedImageFormats(long var0, long var2, int var4, int var5, long var6, long var8, long var10);

   public static int clEnqueueReadImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, ByteBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueReadImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueReadImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   public static int clEnqueueReadImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, FloatBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueReadImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueReadImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   public static int clEnqueueReadImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, IntBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueReadImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueReadImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   public static int clEnqueueReadImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, ShortBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueReadImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueReadImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   static native int nclEnqueueReadImage(long var0, long var2, int var4, long var5, long var7, long var9, long var11, long var13, int var15, long var16, long var18, long var20);

   public static int clEnqueueWriteImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, ByteBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueWriteImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueWriteImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   public static int clEnqueueWriteImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, FloatBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueWriteImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueWriteImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   public static int clEnqueueWriteImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, IntBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueWriteImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueWriteImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   public static int clEnqueueWriteImage(CLCommandQueue var0, CLMem var1, int var2, PointerBuffer var3, PointerBuffer var4, long var5, long var7, ShortBuffer var9, PointerBuffer var10, PointerBuffer var11) {
      long var12 = CLCapabilities.clEnqueueWriteImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer(var9, CLChecks.calculateImageSize(var4, var5, var7));
      if (var10 != null) {
         BufferChecks.checkDirect(var10);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var11, 1);
      }

      int var14 = nclEnqueueWriteImage(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7, MemoryUtil.getAddress(var9), var10 == null ? 0 : var10.remaining(), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 == 0) {
         var0.registerCLEvent(var11);
      }

      return var14;
   }

   static native int nclEnqueueWriteImage(long var0, long var2, int var4, long var5, long var7, long var9, long var11, long var13, int var15, long var16, long var18, long var20);

   public static int clEnqueueCopyImage(CLCommandQueue var0, CLMem var1, CLMem var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueCopyImage;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueCopyImage(var0.getPointer(), var1.getPointer(), var2.getPointer(), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   static native int nclEnqueueCopyImage(long var0, long var2, long var4, long var6, long var8, long var10, int var12, long var13, long var15, long var17);

   public static int clEnqueueCopyImageToBuffer(CLCommandQueue var0, CLMem var1, CLMem var2, PointerBuffer var3, PointerBuffer var4, long var5, PointerBuffer var7, PointerBuffer var8) {
      long var9 = CLCapabilities.clEnqueueCopyImageToBuffer;
      BufferChecks.checkFunctionAddress(var9);
      BufferChecks.checkBuffer((PointerBuffer)var3, 3);
      BufferChecks.checkBuffer((PointerBuffer)var4, 3);
      if (var7 != null) {
         BufferChecks.checkDirect(var7);
      }

      if (var8 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var8, 1);
      }

      int var11 = nclEnqueueCopyImageToBuffer(var0.getPointer(), var1.getPointer(), var2.getPointer(), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var5, var7 == null ? 0 : var7.remaining(), MemoryUtil.getAddressSafe(var7), MemoryUtil.getAddressSafe(var8), var9);
      if (var11 == 0) {
         var0.registerCLEvent(var8);
      }

      return var11;
   }

   static native int nclEnqueueCopyImageToBuffer(long var0, long var2, long var4, long var6, long var8, long var10, int var12, long var13, long var15, long var17);

   public static int clEnqueueCopyBufferToImage(CLCommandQueue var0, CLMem var1, CLMem var2, long var3, PointerBuffer var5, PointerBuffer var6, PointerBuffer var7, PointerBuffer var8) {
      long var9 = CLCapabilities.clEnqueueCopyBufferToImage;
      BufferChecks.checkFunctionAddress(var9);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer((PointerBuffer)var6, 3);
      if (var7 != null) {
         BufferChecks.checkDirect(var7);
      }

      if (var8 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var8, 1);
      }

      int var11 = nclEnqueueCopyBufferToImage(var0.getPointer(), var1.getPointer(), var2.getPointer(), var3, MemoryUtil.getAddress(var5), MemoryUtil.getAddress(var6), var7 == null ? 0 : var7.remaining(), MemoryUtil.getAddressSafe(var7), MemoryUtil.getAddressSafe(var8), var9);
      if (var11 == 0) {
         var0.registerCLEvent(var8);
      }

      return var11;
   }

   static native int nclEnqueueCopyBufferToImage(long var0, long var2, long var4, long var6, long var8, long var10, int var12, long var13, long var15, long var17);

   public static ByteBuffer clEnqueueMapImage(CLCommandQueue var0, CLMem var1, int var2, long var3, PointerBuffer var5, PointerBuffer var6, PointerBuffer var7, PointerBuffer var8, PointerBuffer var9, PointerBuffer var10, IntBuffer var11) {
      long var12 = CLCapabilities.clEnqueueMapImage;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkBuffer((PointerBuffer)var5, 3);
      BufferChecks.checkBuffer((PointerBuffer)var6, 3);
      BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      if (var8 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var8, 1);
      }

      if (var9 != null) {
         BufferChecks.checkDirect(var9);
      }

      if (var10 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var10, 1);
      }

      if (var11 != null) {
         BufferChecks.checkBuffer((IntBuffer)var11, 1);
      }

      ByteBuffer var14 = nclEnqueueMapImage(var0.getPointer(), var1.getPointer(), var2, var3, MemoryUtil.getAddress(var5), MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), MemoryUtil.getAddressSafe(var8), var9 == null ? 0 : var9.remaining(), MemoryUtil.getAddressSafe(var9), MemoryUtil.getAddressSafe(var10), MemoryUtil.getAddressSafe(var11), var12);
      if (var14 != null) {
         var0.registerCLEvent(var10);
      }

      return LWJGLUtil.CHECKS && var14 == null ? null : var14.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nclEnqueueMapImage(long var0, long var2, int var4, long var5, long var7, long var9, long var11, long var13, int var15, long var16, long var18, long var20, long var22);

   public static int clGetImageInfo(CLMem var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetImageInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetImageInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetImageInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static int clRetainMemObject(CLMem var0) {
      long var1 = CLCapabilities.clRetainMemObject;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainMemObject(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainMemObject(long var0, long var2);

   public static int clReleaseMemObject(CLMem var0) {
      long var1 = CLCapabilities.clReleaseMemObject;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclReleaseMemObject(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseMemObject(long var0, long var2);

   public static int clEnqueueUnmapMemObject(CLCommandQueue var0, CLMem var1, ByteBuffer var2, PointerBuffer var3, PointerBuffer var4) {
      long var5 = CLCapabilities.clEnqueueUnmapMemObject;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var4, 1);
      }

      int var7 = nclEnqueueUnmapMemObject(var0.getPointer(), var1.getPointer(), MemoryUtil.getAddress(var2), var3 == null ? 0 : var3.remaining(), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var5);
      if (var7 == 0) {
         var0.registerCLEvent(var4);
      }

      return var7;
   }

   static native int nclEnqueueUnmapMemObject(long var0, long var2, long var4, int var6, long var7, long var9, long var11);

   public static int clGetMemObjectInfo(CLMem var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetMemObjectInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetMemObjectInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetMemObjectInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLSampler clCreateSampler(CLContext var0, int var1, int var2, int var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateSampler;
      BufferChecks.checkFunctionAddress(var5);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLSampler var7 = new CLSampler(nclCreateSampler(var0.getPointer(), var1, var2, var3, MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   static native long nclCreateSampler(long var0, int var2, int var3, int var4, long var5, long var7);

   public static int clRetainSampler(CLSampler var0) {
      long var1 = CLCapabilities.clRetainSampler;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainSampler(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainSampler(long var0, long var2);

   public static int clReleaseSampler(CLSampler var0) {
      long var1 = CLCapabilities.clReleaseSampler;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclReleaseSampler(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseSampler(long var0, long var2);

   public static int clGetSamplerInfo(CLSampler var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetSamplerInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetSamplerInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetSamplerInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLProgram clCreateProgramWithSource(CLContext var0, ByteBuffer var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateProgramWithSource;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      CLProgram var5 = new CLProgram(nclCreateProgramWithSource(var0.getPointer(), 1, MemoryUtil.getAddress(var1), (long)var1.remaining(), MemoryUtil.getAddressSafe(var2), var3), var0);
      return var5;
   }

   static native long nclCreateProgramWithSource(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLProgram clCreateProgramWithSource(CLContext var0, ByteBuffer var1, PointerBuffer var2, IntBuffer var3) {
      long var4 = CLCapabilities.clCreateProgramWithSource;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer(var1, APIUtil.getSize(var2));
      BufferChecks.checkBuffer((PointerBuffer)var2, 1);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      CLProgram var6 = new CLProgram(nclCreateProgramWithSource2(var0.getPointer(), var2.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), MemoryUtil.getAddressSafe(var3), var4), var0);
      return var6;
   }

   static native long nclCreateProgramWithSource2(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLProgram clCreateProgramWithSource(CLContext var0, ByteBuffer[] var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateProgramWithSource;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray((Object[])var1, 1);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      CLProgram var5 = new CLProgram(nclCreateProgramWithSource3(var0.getPointer(), var1.length, var1, APIUtil.getLengths(var1), MemoryUtil.getAddressSafe(var2), var3), var0);
      return var5;
   }

   static native long nclCreateProgramWithSource3(long var0, int var2, ByteBuffer[] var3, long var4, long var6, long var8);

   public static CLProgram clCreateProgramWithSource(CLContext var0, CharSequence var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateProgramWithSource;
      BufferChecks.checkFunctionAddress(var3);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      CLProgram var5 = new CLProgram(nclCreateProgramWithSource(var0.getPointer(), 1, APIUtil.getBuffer(var1), (long)var1.length(), MemoryUtil.getAddressSafe(var2), var3), var0);
      return var5;
   }

   public static CLProgram clCreateProgramWithSource(CLContext var0, CharSequence[] var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateProgramWithSource;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray(var1);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      CLProgram var5 = new CLProgram(nclCreateProgramWithSource4(var0.getPointer(), var1.length, APIUtil.getBuffer(var1), APIUtil.getLengths(var1), MemoryUtil.getAddressSafe(var2), var3), var0);
      return var5;
   }

   static native long nclCreateProgramWithSource4(long var0, int var2, long var3, long var5, long var7, long var9);

   public static CLProgram clCreateProgramWithBinary(CLContext var0, CLDevice var1, ByteBuffer var2, IntBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateProgramWithBinary;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLProgram var7 = new CLProgram(nclCreateProgramWithBinary(var0.getPointer(), 1, var1.getPointer(), (long)var2.remaining(), MemoryUtil.getAddress(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   static native long nclCreateProgramWithBinary(long var0, int var2, long var3, long var5, long var7, long var9, long var11, long var13);

   public static CLProgram clCreateProgramWithBinary(CLContext var0, PointerBuffer var1, PointerBuffer var2, ByteBuffer var3, IntBuffer var4, IntBuffer var5) {
      long var6 = CLCapabilities.clCreateProgramWithBinary;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      BufferChecks.checkBuffer(var2, var1.remaining());
      BufferChecks.checkBuffer(var3, APIUtil.getSize(var2));
      BufferChecks.checkBuffer(var4, var1.remaining());
      if (var5 != null) {
         BufferChecks.checkBuffer((IntBuffer)var5, 1);
      }

      CLProgram var8 = new CLProgram(nclCreateProgramWithBinary2(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddressSafe(var5), var6), var0);
      return var8;
   }

   static native long nclCreateProgramWithBinary2(long var0, int var2, long var3, long var5, long var7, long var9, long var11, long var13);

   public static CLProgram clCreateProgramWithBinary(CLContext var0, PointerBuffer var1, ByteBuffer[] var2, IntBuffer var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateProgramWithBinary;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer(var1, var2.length);
      BufferChecks.checkArray((Object[])var2, 1);
      BufferChecks.checkBuffer(var3, var2.length);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLProgram var7 = new CLProgram(nclCreateProgramWithBinary3(var0.getPointer(), var2.length, MemoryUtil.getAddress(var1), APIUtil.getLengths(var2), var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   static native long nclCreateProgramWithBinary3(long var0, int var2, long var3, long var5, ByteBuffer[] var7, long var8, long var10, long var12);

   public static int clRetainProgram(CLProgram var0) {
      long var1 = CLCapabilities.clRetainProgram;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainProgram(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainProgram(long var0, long var2);

   public static int clReleaseProgram(CLProgram var0) {
      long var1 = CLCapabilities.clReleaseProgram;
      BufferChecks.checkFunctionAddress(var1);
      APIUtil.releaseObjects(var0);
      int var3 = nclReleaseProgram(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseProgram(long var0, long var2);

   public static int clBuildProgram(CLProgram var0, PointerBuffer var1, ByteBuffer var2, CLBuildProgramCallback var3) {
      long var4 = CLCapabilities.clBuildProgram;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      long var6 = CallbackUtil.createGlobalRef(var3);
      if (var3 != null) {
         var3.setContext((CLContext)var0.getParent());
      }

      boolean var8 = false;
      int var11 = nclBuildProgram(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var3 == null ? 0L : var3.getPointer(), var6, var4);
      CallbackUtil.checkCallback(var11, var6);
      return var11;
   }

   static native int nclBuildProgram(long var0, int var2, long var3, long var5, long var7, long var9, long var11);

   public static int clBuildProgram(CLProgram var0, PointerBuffer var1, CharSequence var2, CLBuildProgramCallback var3) {
      long var4 = CLCapabilities.clBuildProgram;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      long var6 = CallbackUtil.createGlobalRef(var3);
      if (var3 != null) {
         var3.setContext((CLContext)var0.getParent());
      }

      boolean var8 = false;
      int var11 = nclBuildProgram(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), APIUtil.getBufferNT(var2), var3 == null ? 0L : var3.getPointer(), var6, var4);
      CallbackUtil.checkCallback(var11, var6);
      return var11;
   }

   public static int clBuildProgram(CLProgram var0, CLDevice var1, CharSequence var2, CLBuildProgramCallback var3) {
      long var4 = CLCapabilities.clBuildProgram;
      BufferChecks.checkFunctionAddress(var4);
      long var6 = CallbackUtil.createGlobalRef(var3);
      if (var3 != null) {
         var3.setContext((CLContext)var0.getParent());
      }

      boolean var8 = false;
      int var11 = nclBuildProgram(var0.getPointer(), 1, APIUtil.getPointer(var1), APIUtil.getBufferNT(var2), var3 == null ? 0L : var3.getPointer(), var6, var4);
      CallbackUtil.checkCallback(var11, var6);
      return var11;
   }

   public static int clUnloadCompiler() {
      long var0 = CLCapabilities.clUnloadCompiler;
      BufferChecks.checkFunctionAddress(var0);
      int var2 = nclUnloadCompiler(var0);
      return var2;
   }

   static native int nclUnloadCompiler(long var0);

   public static int clGetProgramInfo(CLProgram var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetProgramInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetProgramInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetProgramInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static int clGetProgramInfo(CLProgram var0, PointerBuffer var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetProgramInfo;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      BufferChecks.checkBuffer(var2, APIUtil.getSize(var1));
      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetProgramInfo2(var0.getPointer(), 4454, (long)var1.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetProgramInfo2(long var0, int var2, long var3, long var5, long var7, long var9, long var11);

   public static int clGetProgramInfo(CLProgram var0, ByteBuffer[] var1, PointerBuffer var2) {
      long var3 = CLCapabilities.clGetProgramInfo;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray(var1);
      if (var2 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var2, 1);
      }

      int var5 = nclGetProgramInfo3(var0.getPointer(), 4454, (long)var1.length, var1, MemoryUtil.getAddressSafe(var2), var3);
      return var5;
   }

   static native int nclGetProgramInfo3(long var0, int var2, long var3, ByteBuffer[] var5, long var6, long var8);

   public static int clGetProgramBuildInfo(CLProgram var0, CLDevice var1, int var2, ByteBuffer var3, PointerBuffer var4) {
      long var5 = CLCapabilities.clGetProgramBuildInfo;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var4, 1);
      }

      int var7 = nclGetProgramBuildInfo(var0.getPointer(), var1.getPointer(), var2, (long)(var3 == null ? 0 : var3.remaining()), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var5);
      return var7;
   }

   static native int nclGetProgramBuildInfo(long var0, long var2, int var4, long var5, long var7, long var9, long var11);

   public static CLKernel clCreateKernel(CLProgram var0, ByteBuffer var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateKernel;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      CLKernel var5 = new CLKernel(nclCreateKernel(var0.getPointer(), MemoryUtil.getAddress(var1), MemoryUtil.getAddressSafe(var2), var3), var0);
      return var5;
   }

   static native long nclCreateKernel(long var0, long var2, long var4, long var6);

   public static CLKernel clCreateKernel(CLProgram var0, CharSequence var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateKernel;
      BufferChecks.checkFunctionAddress(var3);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      CLKernel var5 = new CLKernel(nclCreateKernel(var0.getPointer(), APIUtil.getBufferNT(var1), MemoryUtil.getAddressSafe(var2), var3), var0);
      return var5;
   }

   public static int clCreateKernelsInProgram(CLProgram var0, PointerBuffer var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateKernelsInProgram;
      BufferChecks.checkFunctionAddress(var3);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      int var5 = nclCreateKernelsInProgram(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), var3);
      if (var5 == 0 && var1 != null) {
         var0.registerCLKernels(var1);
      }

      return var5;
   }

   static native int nclCreateKernelsInProgram(long var0, int var2, long var3, long var5, long var7);

   public static int clRetainKernel(CLKernel var0) {
      long var1 = CLCapabilities.clRetainKernel;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainKernel(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainKernel(long var0, long var2);

   public static int clReleaseKernel(CLKernel var0) {
      long var1 = CLCapabilities.clReleaseKernel;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclReleaseKernel(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseKernel(long var0, long var2);

   public static int clSetKernelArg(CLKernel var0, int var1, long var2) {
      long var4 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nclSetKernelArg(var0.getPointer(), var1, var2, 0L, var4);
      return var6;
   }

   public static int clSetKernelArg(CLKernel var0, int var1, ByteBuffer var2) {
      long var3 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var2);
      int var5 = nclSetKernelArg(var0.getPointer(), var1, (long)var2.remaining(), MemoryUtil.getAddress(var2), var3);
      return var5;
   }

   public static int clSetKernelArg(CLKernel var0, int var1, DoubleBuffer var2) {
      long var3 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var2);
      int var5 = nclSetKernelArg(var0.getPointer(), var1, (long)(var2.remaining() << 3), MemoryUtil.getAddress(var2), var3);
      return var5;
   }

   public static int clSetKernelArg(CLKernel var0, int var1, FloatBuffer var2) {
      long var3 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var2);
      int var5 = nclSetKernelArg(var0.getPointer(), var1, (long)(var2.remaining() << 2), MemoryUtil.getAddress(var2), var3);
      return var5;
   }

   public static int clSetKernelArg(CLKernel var0, int var1, IntBuffer var2) {
      long var3 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var2);
      int var5 = nclSetKernelArg(var0.getPointer(), var1, (long)(var2.remaining() << 2), MemoryUtil.getAddress(var2), var3);
      return var5;
   }

   public static int clSetKernelArg(CLKernel var0, int var1, LongBuffer var2) {
      long var3 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var2);
      int var5 = nclSetKernelArg(var0.getPointer(), var1, (long)(var2.remaining() << 3), MemoryUtil.getAddress(var2), var3);
      return var5;
   }

   public static int clSetKernelArg(CLKernel var0, int var1, ShortBuffer var2) {
      long var3 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var2);
      int var5 = nclSetKernelArg(var0.getPointer(), var1, (long)(var2.remaining() << 1), MemoryUtil.getAddress(var2), var3);
      return var5;
   }

   static native int nclSetKernelArg(long var0, int var2, long var3, long var5, long var7);

   public static int clSetKernelArg(CLKernel var0, int var1, CLObject var2) {
      long var3 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nclSetKernelArg(var0.getPointer(), var1, (long)PointerBuffer.getPointerSize(), APIUtil.getPointerSafe(var2), var3);
      return var5;
   }

   static int clSetKernelArg(CLKernel var0, int var1, long var2, Buffer var4) {
      long var5 = CLCapabilities.clSetKernelArg;
      BufferChecks.checkFunctionAddress(var5);
      int var7 = nclSetKernelArg(var0.getPointer(), var1, var2, MemoryUtil.getAddress0(var4), var5);
      return var7;
   }

   public static int clGetKernelInfo(CLKernel var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetKernelInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetKernelInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetKernelInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static int clGetKernelWorkGroupInfo(CLKernel var0, CLDevice var1, int var2, ByteBuffer var3, PointerBuffer var4) {
      long var5 = CLCapabilities.clGetKernelWorkGroupInfo;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var4, 1);
      }

      int var7 = nclGetKernelWorkGroupInfo(var0.getPointer(), var1 == null ? 0L : var1.getPointer(), var2, (long)(var3 == null ? 0 : var3.remaining()), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var5);
      return var7;
   }

   static native int nclGetKernelWorkGroupInfo(long var0, long var2, int var4, long var5, long var7, long var9, long var11);

   public static int clEnqueueNDRangeKernel(CLCommandQueue var0, CLKernel var1, int var2, PointerBuffer var3, PointerBuffer var4, PointerBuffer var5, PointerBuffer var6, PointerBuffer var7) {
      long var8 = CLCapabilities.clEnqueueNDRangeKernel;
      BufferChecks.checkFunctionAddress(var8);
      if (var3 != null) {
         BufferChecks.checkBuffer(var3, var2);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer(var4, var2);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer(var5, var2);
      }

      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      if (var7 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var7, 1);
      }

      int var10 = nclEnqueueNDRangeKernel(var0.getPointer(), var1.getPointer(), var2, MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var6), MemoryUtil.getAddressSafe(var7), var8);
      if (var10 == 0) {
         var0.registerCLEvent(var7);
      }

      return var10;
   }

   static native int nclEnqueueNDRangeKernel(long var0, long var2, int var4, long var5, long var7, long var9, int var11, long var12, long var14, long var16);

   public static int clEnqueueTask(CLCommandQueue var0, CLKernel var1, PointerBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clEnqueueTask;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclEnqueueTask(var0.getPointer(), var1.getPointer(), var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0) {
         var0.registerCLEvent(var3);
      }

      return var6;
   }

   static native int nclEnqueueTask(long var0, long var2, int var4, long var5, long var7, long var9);

   public static int clEnqueueNativeKernel(CLCommandQueue var0, CLNativeKernel var1, CLMem[] var2, long[] var3, PointerBuffer var4, PointerBuffer var5) {
      long var6 = CLCapabilities.clEnqueueNativeKernel;
      BufferChecks.checkFunctionAddress(var6);
      if (var2 != null) {
         BufferChecks.checkArray((Object[])var2, 1);
      }

      if (var3 != null) {
         BufferChecks.checkArray(var3, var2.length);
      }

      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var5, 1);
      }

      long var8 = CallbackUtil.createGlobalRef(var1);
      ByteBuffer var10 = APIUtil.getNativeKernelArgs(var8, var2, var3);
      boolean var11 = false;
      int var14 = nclEnqueueNativeKernel(var0.getPointer(), var1.getPointer(), MemoryUtil.getAddress0((Buffer)var10), (long)var10.remaining(), var2 == null ? 0 : var2.length, var2, var4 == null ? 0 : var4.remaining(), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var6);
      if (var14 == 0) {
         var0.registerCLEvent(var5);
      }

      CallbackUtil.checkCallback(var14, var8);
      return var14;
   }

   static native int nclEnqueueNativeKernel(long var0, long var2, long var4, long var6, int var8, CLMem[] var9, int var10, long var11, long var13, long var15);

   public static int clWaitForEvents(PointerBuffer var0) {
      long var1 = CLCapabilities.clWaitForEvents;
      BufferChecks.checkFunctionAddress(var1);
      BufferChecks.checkBuffer((PointerBuffer)var0, 1);
      int var3 = nclWaitForEvents(var0.remaining(), MemoryUtil.getAddress(var0), var1);
      return var3;
   }

   static native int nclWaitForEvents(int var0, long var1, long var3);

   public static int clWaitForEvents(CLEvent var0) {
      long var1 = CLCapabilities.clWaitForEvents;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclWaitForEvents(1, APIUtil.getPointer(var0), var1);
      return var3;
   }

   public static int clGetEventInfo(CLEvent var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetEventInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetEventInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetEventInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static int clRetainEvent(CLEvent var0) {
      long var1 = CLCapabilities.clRetainEvent;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclRetainEvent(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.retain();
      }

      return var3;
   }

   static native int nclRetainEvent(long var0, long var2);

   public static int clReleaseEvent(CLEvent var0) {
      long var1 = CLCapabilities.clReleaseEvent;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclReleaseEvent(var0.getPointer(), var1);
      if (var3 == 0) {
         var0.release();
      }

      return var3;
   }

   static native int nclReleaseEvent(long var0, long var2);

   public static int clEnqueueMarker(CLCommandQueue var0, PointerBuffer var1) {
      long var2 = CLCapabilities.clEnqueueMarker;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      int var4 = nclEnqueueMarker(var0.getPointer(), MemoryUtil.getAddress(var1), var2);
      if (var4 == 0) {
         var0.registerCLEvent(var1);
      }

      return var4;
   }

   static native int nclEnqueueMarker(long var0, long var2, long var4);

   public static int clEnqueueBarrier(CLCommandQueue var0) {
      long var1 = CLCapabilities.clEnqueueBarrier;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclEnqueueBarrier(var0.getPointer(), var1);
      return var3;
   }

   static native int nclEnqueueBarrier(long var0, long var2);

   public static int clEnqueueWaitForEvents(CLCommandQueue var0, PointerBuffer var1) {
      long var2 = CLCapabilities.clEnqueueWaitForEvents;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      int var4 = nclEnqueueWaitForEvents(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), var2);
      return var4;
   }

   static native int nclEnqueueWaitForEvents(long var0, int var2, long var3, long var5);

   public static int clEnqueueWaitForEvents(CLCommandQueue var0, CLEvent var1) {
      long var2 = CLCapabilities.clEnqueueWaitForEvents;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nclEnqueueWaitForEvents(var0.getPointer(), 1, APIUtil.getPointer(var1), var2);
      return var4;
   }

   public static int clGetEventProfilingInfo(CLEvent var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetEventProfilingInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetEventProfilingInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetEventProfilingInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static int clFlush(CLCommandQueue var0) {
      long var1 = CLCapabilities.clFlush;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclFlush(var0.getPointer(), var1);
      return var3;
   }

   static native int nclFlush(long var0, long var2);

   public static int clFinish(CLCommandQueue var0) {
      long var1 = CLCapabilities.clFinish;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclFinish(var0.getPointer(), var1);
      return var3;
   }

   static native int nclFinish(long var0, long var2);

   static CLFunctionAddress clGetExtensionFunctionAddress(ByteBuffer var0) {
      long var1 = CLCapabilities.clGetExtensionFunctionAddress;
      BufferChecks.checkFunctionAddress(var1);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkNullTerminated(var0);
      CLFunctionAddress var3 = new CLFunctionAddress(nclGetExtensionFunctionAddress(MemoryUtil.getAddress(var0), var1));
      return var3;
   }

   static native long nclGetExtensionFunctionAddress(long var0, long var2);

   static CLFunctionAddress clGetExtensionFunctionAddress(CharSequence var0) {
      long var1 = CLCapabilities.clGetExtensionFunctionAddress;
      BufferChecks.checkFunctionAddress(var1);
      CLFunctionAddress var3 = new CLFunctionAddress(nclGetExtensionFunctionAddress(APIUtil.getBufferNT(var0), var1));
      return var3;
   }
}
