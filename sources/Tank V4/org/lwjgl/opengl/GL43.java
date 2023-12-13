package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerWrapper;

public final class GL43 {
   public static final int GL_NUM_SHADING_LANGUAGE_VERSIONS = 33513;
   public static final int GL_VERTEX_ATTRIB_ARRAY_LONG = 34638;
   public static final int GL_COMPRESSED_RGB8_ETC2 = 37492;
   public static final int GL_COMPRESSED_SRGB8_ETC2 = 37493;
   public static final int GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 37494;
   public static final int GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 37495;
   public static final int GL_COMPRESSED_RGBA8_ETC2_EAC = 37496;
   public static final int GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC = 37497;
   public static final int GL_COMPRESSED_R11_EAC = 37488;
   public static final int GL_COMPRESSED_SIGNED_R11_EAC = 37489;
   public static final int GL_COMPRESSED_RG11_EAC = 37490;
   public static final int GL_COMPRESSED_SIGNED_RG11_EAC = 37491;
   public static final int GL_PRIMITIVE_RESTART_FIXED_INDEX = 36201;
   public static final int GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 36202;
   public static final int GL_MAX_ELEMENT_INDEX = 36203;
   public static final int GL_COMPUTE_SHADER = 37305;
   public static final int GL_MAX_COMPUTE_UNIFORM_BLOCKS = 37307;
   public static final int GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 37308;
   public static final int GL_MAX_COMPUTE_IMAGE_UNIFORMS = 37309;
   public static final int GL_MAX_COMPUTE_SHARED_MEMORY_SIZE = 33378;
   public static final int GL_MAX_COMPUTE_UNIFORM_COMPONENTS = 33379;
   public static final int GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 33380;
   public static final int GL_MAX_COMPUTE_ATOMIC_COUNTERS = 33381;
   public static final int GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 33382;
   public static final int GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = 37099;
   public static final int GL_MAX_COMPUTE_WORK_GROUP_COUNT = 37310;
   public static final int GL_MAX_COMPUTE_WORK_GROUP_SIZE = 37311;
   public static final int GL_COMPUTE_WORK_GROUP_SIZE = 33383;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 37100;
   public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 37101;
   public static final int GL_DISPATCH_INDIRECT_BUFFER = 37102;
   public static final int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 37103;
   public static final int GL_COMPUTE_SHADER_BIT = 32;
   public static final int GL_DEBUG_OUTPUT = 37600;
   public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 33346;
   public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 2;
   public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 37187;
   public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 37188;
   public static final int GL_DEBUG_LOGGED_MESSAGES = 37189;
   public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 33347;
   public static final int GL_MAX_DEBUG_GROUP_STACK_DEPTH = 33388;
   public static final int GL_DEBUG_GROUP_STACK_DEPTH = 33389;
   public static final int GL_MAX_LABEL_LENGTH = 33512;
   public static final int GL_DEBUG_CALLBACK_FUNCTION = 33348;
   public static final int GL_DEBUG_CALLBACK_USER_PARAM = 33349;
   public static final int GL_DEBUG_SOURCE_API = 33350;
   public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 33351;
   public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 33352;
   public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 33353;
   public static final int GL_DEBUG_SOURCE_APPLICATION = 33354;
   public static final int GL_DEBUG_SOURCE_OTHER = 33355;
   public static final int GL_DEBUG_TYPE_ERROR = 33356;
   public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 33357;
   public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 33358;
   public static final int GL_DEBUG_TYPE_PORTABILITY = 33359;
   public static final int GL_DEBUG_TYPE_PERFORMANCE = 33360;
   public static final int GL_DEBUG_TYPE_OTHER = 33361;
   public static final int GL_DEBUG_TYPE_MARKER = 33384;
   public static final int GL_DEBUG_TYPE_PUSH_GROUP = 33385;
   public static final int GL_DEBUG_TYPE_POP_GROUP = 33386;
   public static final int GL_DEBUG_SEVERITY_HIGH = 37190;
   public static final int GL_DEBUG_SEVERITY_MEDIUM = 37191;
   public static final int GL_DEBUG_SEVERITY_LOW = 37192;
   public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 33387;
   public static final int GL_STACK_UNDERFLOW = 1284;
   public static final int GL_STACK_OVERFLOW = 1283;
   public static final int GL_BUFFER = 33504;
   public static final int GL_SHADER = 33505;
   public static final int GL_PROGRAM = 33506;
   public static final int GL_QUERY = 33507;
   public static final int GL_PROGRAM_PIPELINE = 33508;
   public static final int GL_SAMPLER = 33510;
   public static final int GL_DISPLAY_LIST = 33511;
   public static final int GL_MAX_UNIFORM_LOCATIONS = 33390;
   public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 37648;
   public static final int GL_FRAMEBUFFER_DEFAULT_HEIGHT = 37649;
   public static final int GL_FRAMEBUFFER_DEFAULT_LAYERS = 37650;
   public static final int GL_FRAMEBUFFER_DEFAULT_SAMPLES = 37651;
   public static final int GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 37652;
   public static final int GL_MAX_FRAMEBUFFER_WIDTH = 37653;
   public static final int GL_MAX_FRAMEBUFFER_HEIGHT = 37654;
   public static final int GL_MAX_FRAMEBUFFER_LAYERS = 37655;
   public static final int GL_MAX_FRAMEBUFFER_SAMPLES = 37656;
   public static final int GL_TEXTURE_1D = 3552;
   public static final int GL_TEXTURE_1D_ARRAY = 35864;
   public static final int GL_TEXTURE_2D = 3553;
   public static final int GL_TEXTURE_2D_ARRAY = 35866;
   public static final int GL_TEXTURE_3D = 32879;
   public static final int GL_TEXTURE_CUBE_MAP = 34067;
   public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 36873;
   public static final int GL_TEXTURE_RECTANGLE = 34037;
   public static final int GL_TEXTURE_BUFFER = 35882;
   public static final int GL_RENDERBUFFER = 36161;
   public static final int GL_TEXTURE_2D_MULTISAMPLE = 37120;
   public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 37122;
   public static final int GL_SAMPLES = 32937;
   public static final int GL_NUM_SAMPLE_COUNTS = 37760;
   public static final int GL_INTERNALFORMAT_SUPPORTED = 33391;
   public static final int GL_INTERNALFORMAT_PREFERRED = 33392;
   public static final int GL_INTERNALFORMAT_RED_SIZE = 33393;
   public static final int GL_INTERNALFORMAT_GREEN_SIZE = 33394;
   public static final int GL_INTERNALFORMAT_BLUE_SIZE = 33395;
   public static final int GL_INTERNALFORMAT_ALPHA_SIZE = 33396;
   public static final int GL_INTERNALFORMAT_DEPTH_SIZE = 33397;
   public static final int GL_INTERNALFORMAT_STENCIL_SIZE = 33398;
   public static final int GL_INTERNALFORMAT_SHARED_SIZE = 33399;
   public static final int GL_INTERNALFORMAT_RED_TYPE = 33400;
   public static final int GL_INTERNALFORMAT_GREEN_TYPE = 33401;
   public static final int GL_INTERNALFORMAT_BLUE_TYPE = 33402;
   public static final int GL_INTERNALFORMAT_ALPHA_TYPE = 33403;
   public static final int GL_INTERNALFORMAT_DEPTH_TYPE = 33404;
   public static final int GL_INTERNALFORMAT_STENCIL_TYPE = 33405;
   public static final int GL_MAX_WIDTH = 33406;
   public static final int GL_MAX_HEIGHT = 33407;
   public static final int GL_MAX_DEPTH = 33408;
   public static final int GL_MAX_LAYERS = 33409;
   public static final int GL_MAX_COMBINED_DIMENSIONS = 33410;
   public static final int GL_COLOR_COMPONENTS = 33411;
   public static final int GL_DEPTH_COMPONENTS = 33412;
   public static final int GL_STENCIL_COMPONENTS = 33413;
   public static final int GL_COLOR_RENDERABLE = 33414;
   public static final int GL_DEPTH_RENDERABLE = 33415;
   public static final int GL_STENCIL_RENDERABLE = 33416;
   public static final int GL_FRAMEBUFFER_RENDERABLE = 33417;
   public static final int GL_FRAMEBUFFER_RENDERABLE_LAYERED = 33418;
   public static final int GL_FRAMEBUFFER_BLEND = 33419;
   public static final int GL_READ_PIXELS = 33420;
   public static final int GL_READ_PIXELS_FORMAT = 33421;
   public static final int GL_READ_PIXELS_TYPE = 33422;
   public static final int GL_TEXTURE_IMAGE_FORMAT = 33423;
   public static final int GL_TEXTURE_IMAGE_TYPE = 33424;
   public static final int GL_GET_TEXTURE_IMAGE_FORMAT = 33425;
   public static final int GL_GET_TEXTURE_IMAGE_TYPE = 33426;
   public static final int GL_MIPMAP = 33427;
   public static final int GL_MANUAL_GENERATE_MIPMAP = 33428;
   public static final int GL_AUTO_GENERATE_MIPMAP = 33429;
   public static final int GL_COLOR_ENCODING = 33430;
   public static final int GL_SRGB_READ = 33431;
   public static final int GL_SRGB_WRITE = 33432;
   public static final int GL_SRGB_DECODE_ARB = 33433;
   public static final int GL_FILTER = 33434;
   public static final int GL_VERTEX_TEXTURE = 33435;
   public static final int GL_TESS_CONTROL_TEXTURE = 33436;
   public static final int GL_TESS_EVALUATION_TEXTURE = 33437;
   public static final int GL_GEOMETRY_TEXTURE = 33438;
   public static final int GL_FRAGMENT_TEXTURE = 33439;
   public static final int GL_COMPUTE_TEXTURE = 33440;
   public static final int GL_TEXTURE_SHADOW = 33441;
   public static final int GL_TEXTURE_GATHER = 33442;
   public static final int GL_TEXTURE_GATHER_SHADOW = 33443;
   public static final int GL_SHADER_IMAGE_LOAD = 33444;
   public static final int GL_SHADER_IMAGE_STORE = 33445;
   public static final int GL_SHADER_IMAGE_ATOMIC = 33446;
   public static final int GL_IMAGE_TEXEL_SIZE = 33447;
   public static final int GL_IMAGE_COMPATIBILITY_CLASS = 33448;
   public static final int GL_IMAGE_PIXEL_FORMAT = 33449;
   public static final int GL_IMAGE_PIXEL_TYPE = 33450;
   public static final int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE = 37063;
   public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST = 33452;
   public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST = 33453;
   public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE = 33454;
   public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 33455;
   public static final int GL_TEXTURE_COMPRESSED = 34465;
   public static final int GL_TEXTURE_COMPRESSED_BLOCK_WIDTH = 33457;
   public static final int GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT = 33458;
   public static final int GL_TEXTURE_COMPRESSED_BLOCK_SIZE = 33459;
   public static final int GL_CLEAR_BUFFER = 33460;
   public static final int GL_TEXTURE_VIEW = 33461;
   public static final int GL_VIEW_COMPATIBILITY_CLASS = 33462;
   public static final int GL_FULL_SUPPORT = 33463;
   public static final int GL_CAVEAT_SUPPORT = 33464;
   public static final int GL_IMAGE_CLASS_4_X_32 = 33465;
   public static final int GL_IMAGE_CLASS_2_X_32 = 33466;
   public static final int GL_IMAGE_CLASS_1_X_32 = 33467;
   public static final int GL_IMAGE_CLASS_4_X_16 = 33468;
   public static final int GL_IMAGE_CLASS_2_X_16 = 33469;
   public static final int GL_IMAGE_CLASS_1_X_16 = 33470;
   public static final int GL_IMAGE_CLASS_4_X_8 = 33471;
   public static final int GL_IMAGE_CLASS_2_X_8 = 33472;
   public static final int GL_IMAGE_CLASS_1_X_8 = 33473;
   public static final int GL_IMAGE_CLASS_11_11_10 = 33474;
   public static final int GL_IMAGE_CLASS_10_10_10_2 = 33475;
   public static final int GL_VIEW_CLASS_128_BITS = 33476;
   public static final int GL_VIEW_CLASS_96_BITS = 33477;
   public static final int GL_VIEW_CLASS_64_BITS = 33478;
   public static final int GL_VIEW_CLASS_48_BITS = 33479;
   public static final int GL_VIEW_CLASS_32_BITS = 33480;
   public static final int GL_VIEW_CLASS_24_BITS = 33481;
   public static final int GL_VIEW_CLASS_16_BITS = 33482;
   public static final int GL_VIEW_CLASS_8_BITS = 33483;
   public static final int GL_VIEW_CLASS_S3TC_DXT1_RGB = 33484;
   public static final int GL_VIEW_CLASS_S3TC_DXT1_RGBA = 33485;
   public static final int GL_VIEW_CLASS_S3TC_DXT3_RGBA = 33486;
   public static final int GL_VIEW_CLASS_S3TC_DXT5_RGBA = 33487;
   public static final int GL_VIEW_CLASS_RGTC1_RED = 33488;
   public static final int GL_VIEW_CLASS_RGTC2_RG = 33489;
   public static final int GL_VIEW_CLASS_BPTC_UNORM = 33490;
   public static final int GL_VIEW_CLASS_BPTC_FLOAT = 33491;
   public static final int GL_UNIFORM = 37601;
   public static final int GL_UNIFORM_BLOCK = 37602;
   public static final int GL_PROGRAM_INPUT = 37603;
   public static final int GL_PROGRAM_OUTPUT = 37604;
   public static final int GL_BUFFER_VARIABLE = 37605;
   public static final int GL_SHADER_STORAGE_BLOCK = 37606;
   public static final int GL_VERTEX_SUBROUTINE = 37608;
   public static final int GL_TESS_CONTROL_SUBROUTINE = 37609;
   public static final int GL_TESS_EVALUATION_SUBROUTINE = 37610;
   public static final int GL_GEOMETRY_SUBROUTINE = 37611;
   public static final int GL_FRAGMENT_SUBROUTINE = 37612;
   public static final int GL_COMPUTE_SUBROUTINE = 37613;
   public static final int GL_VERTEX_SUBROUTINE_UNIFORM = 37614;
   public static final int GL_TESS_CONTROL_SUBROUTINE_UNIFORM = 37615;
   public static final int GL_TESS_EVALUATION_SUBROUTINE_UNIFORM = 37616;
   public static final int GL_GEOMETRY_SUBROUTINE_UNIFORM = 37617;
   public static final int GL_FRAGMENT_SUBROUTINE_UNIFORM = 37618;
   public static final int GL_COMPUTE_SUBROUTINE_UNIFORM = 37619;
   public static final int GL_TRANSFORM_FEEDBACK_VARYING = 37620;
   public static final int GL_ACTIVE_RESOURCES = 37621;
   public static final int GL_MAX_NAME_LENGTH = 37622;
   public static final int GL_MAX_NUM_ACTIVE_VARIABLES = 37623;
   public static final int GL_MAX_NUM_COMPATIBLE_SUBROUTINES = 37624;
   public static final int GL_NAME_LENGTH = 37625;
   public static final int GL_TYPE = 37626;
   public static final int GL_ARRAY_SIZE = 37627;
   public static final int GL_OFFSET = 37628;
   public static final int GL_BLOCK_INDEX = 37629;
   public static final int GL_ARRAY_STRIDE = 37630;
   public static final int GL_MATRIX_STRIDE = 37631;
   public static final int GL_IS_ROW_MAJOR = 37632;
   public static final int GL_ATOMIC_COUNTER_BUFFER_INDEX = 37633;
   public static final int GL_BUFFER_BINDING = 37634;
   public static final int GL_BUFFER_DATA_SIZE = 37635;
   public static final int GL_NUM_ACTIVE_VARIABLES = 37636;
   public static final int GL_ACTIVE_VARIABLES = 37637;
   public static final int GL_REFERENCED_BY_VERTEX_SHADER = 37638;
   public static final int GL_REFERENCED_BY_TESS_CONTROL_SHADER = 37639;
   public static final int GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 37640;
   public static final int GL_REFERENCED_BY_GEOMETRY_SHADER = 37641;
   public static final int GL_REFERENCED_BY_FRAGMENT_SHADER = 37642;
   public static final int GL_REFERENCED_BY_COMPUTE_SHADER = 37643;
   public static final int GL_TOP_LEVEL_ARRAY_SIZE = 37644;
   public static final int GL_TOP_LEVEL_ARRAY_STRIDE = 37645;
   public static final int GL_LOCATION = 37646;
   public static final int GL_LOCATION_INDEX = 37647;
   public static final int GL_IS_PER_PATCH = 37607;
   public static final int GL_SHADER_STORAGE_BUFFER = 37074;
   public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 37075;
   public static final int GL_SHADER_STORAGE_BUFFER_START = 37076;
   public static final int GL_SHADER_STORAGE_BUFFER_SIZE = 37077;
   public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 37078;
   public static final int GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 37079;
   public static final int GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 37080;
   public static final int GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 37081;
   public static final int GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 37082;
   public static final int GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 37083;
   public static final int GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 37084;
   public static final int GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 37085;
   public static final int GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 37086;
   public static final int GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 37087;
   public static final int GL_SHADER_STORAGE_BARRIER_BIT = 8192;
   public static final int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 36665;
   public static final int GL_DEPTH_STENCIL_TEXTURE_MODE = 37098;
   public static final int GL_TEXTURE_BUFFER_OFFSET = 37277;
   public static final int GL_TEXTURE_BUFFER_SIZE = 37278;
   public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 37279;
   public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 33499;
   public static final int GL_TEXTURE_VIEW_NUM_LEVELS = 33500;
   public static final int GL_TEXTURE_VIEW_MIN_LAYER = 33501;
   public static final int GL_TEXTURE_VIEW_NUM_LAYERS = 33502;
   public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 33503;
   public static final int GL_VERTEX_ATTRIB_BINDING = 33492;
   public static final int GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 33493;
   public static final int GL_VERTEX_BINDING_DIVISOR = 33494;
   public static final int GL_VERTEX_BINDING_OFFSET = 33495;
   public static final int GL_VERTEX_BINDING_STRIDE = 33496;
   public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 33497;
   public static final int GL_MAX_VERTEX_ATTRIB_BINDINGS = 33498;

   private GL43() {
   }

   public static void glClearBufferData(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearBufferData;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((ByteBuffer)var4, 1);
      nglClearBufferData(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglClearBufferData(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glClearBufferSubData(int var0, int var1, long var2, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glClearBufferSubData;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var6);
      nglClearBufferSubData(var0, var1, var2, (long)var6.remaining(), var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglClearBufferSubData(int var0, int var1, long var2, long var4, int var6, int var7, long var8, long var10);

   public static void glDispatchCompute(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDispatchCompute;
      BufferChecks.checkFunctionAddress(var4);
      nglDispatchCompute(var0, var1, var2, var4);
   }

   static native void nglDispatchCompute(int var0, int var1, int var2, long var3);

   public static void glDispatchComputeIndirect(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDispatchComputeIndirect;
      BufferChecks.checkFunctionAddress(var3);
      nglDispatchComputeIndirect(var0, var3);
   }

   static native void nglDispatchComputeIndirect(long var0, long var2);

   public static void glCopyImageSubData(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14) {
      ContextCapabilities var15 = GLContext.getCapabilities();
      long var16 = var15.glCopyImageSubData;
      BufferChecks.checkFunctionAddress(var16);
      nglCopyImageSubData(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var16);
   }

   static native void nglCopyImageSubData(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, long var15);

   public static void glDebugMessageControl(int var0, int var1, int var2, IntBuffer var3, boolean var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDebugMessageControl;
      BufferChecks.checkFunctionAddress(var6);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      nglDebugMessageControl(var0, var1, var2, var3 == null ? 0 : var3.remaining(), MemoryUtil.getAddressSafe(var3), var4, var6);
   }

   static native void nglDebugMessageControl(int var0, int var1, int var2, int var3, long var4, boolean var6, long var7);

   public static void glDebugMessageInsert(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDebugMessageInsert;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkDirect(var4);
      nglDebugMessageInsert(var0, var1, var2, var3, var4.remaining(), MemoryUtil.getAddress(var4), var6);
   }

   static native void nglDebugMessageInsert(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glDebugMessageInsert(int var0, int var1, int var2, int var3, CharSequence var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDebugMessageInsert;
      BufferChecks.checkFunctionAddress(var6);
      nglDebugMessageInsert(var0, var1, var2, var3, var4.length(), APIUtil.getBuffer(var5, var4), var6);
   }

   public static void glDebugMessageCallback(KHRDebugCallback var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDebugMessageCallback;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = var0 == null ? 0L : CallbackUtil.createGlobalRef(var0.getHandler());
      CallbackUtil.registerContextCallbackKHR(var4);
      nglDebugMessageCallback(var0 == null ? 0L : var0.getPointer(), var4, var2);
   }

   static native void nglDebugMessageCallback(long var0, long var2, long var4);

   public static int glGetDebugMessageLog(int var0, IntBuffer var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, IntBuffer var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetDebugMessageLog;
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

      int var10 = nglGetDebugMessageLog(var0, var6 == null ? 0 : var6.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), MemoryUtil.getAddressSafe(var6), var8);
      return var10;
   }

   static native int nglGetDebugMessageLog(int var0, int var1, long var2, long var4, long var6, long var8, long var10, long var12, long var14);

   public static void glPushDebugGroup(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPushDebugGroup;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglPushDebugGroup(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglPushDebugGroup(int var0, int var1, int var2, long var3, long var5);

   public static void glPushDebugGroup(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glPushDebugGroup;
      BufferChecks.checkFunctionAddress(var4);
      nglPushDebugGroup(var0, var1, var2.length(), APIUtil.getBuffer(var3, var2), var4);
   }

   public static void glPopDebugGroup() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPopDebugGroup;
      BufferChecks.checkFunctionAddress(var1);
      nglPopDebugGroup(var1);
   }

   static native void nglPopDebugGroup(long var0);

   public static void glObjectLabel(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glObjectLabel;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      nglObjectLabel(var0, var1, var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), var4);
   }

   static native void nglObjectLabel(int var0, int var1, int var2, long var3, long var5);

   public static void glObjectLabel(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glObjectLabel;
      BufferChecks.checkFunctionAddress(var4);
      nglObjectLabel(var0, var1, var2.length(), APIUtil.getBuffer(var3, var2), var4);
   }

   public static void glGetObjectLabel(int var0, int var1, IntBuffer var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetObjectLabel;
      BufferChecks.checkFunctionAddress(var5);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkDirect(var3);
      nglGetObjectLabel(var0, var1, var3.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetObjectLabel(int var0, int var1, int var2, long var3, long var5, long var7);

   public static String glGetObjectLabel(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetObjectLabel;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetObjectLabel(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static void glObjectPtrLabel(PointerWrapper var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glObjectPtrLabel;
      BufferChecks.checkFunctionAddress(var3);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      nglObjectPtrLabel(var0.getPointer(), var1 == null ? 0 : var1.remaining(), MemoryUtil.getAddressSafe(var1), var3);
   }

   static native void nglObjectPtrLabel(long var0, int var2, long var3, long var5);

   public static void glObjectPtrLabel(PointerWrapper var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glObjectPtrLabel;
      BufferChecks.checkFunctionAddress(var3);
      nglObjectPtrLabel(var0.getPointer(), var1.length(), APIUtil.getBuffer(var2, var1), var3);
   }

   public static void glGetObjectPtrLabel(PointerWrapper var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetObjectPtrLabel;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetObjectPtrLabel(var0.getPointer(), var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetObjectPtrLabel(long var0, int var2, long var3, long var5, long var7);

   public static String glGetObjectPtrLabel(PointerWrapper var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetObjectPtrLabel;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetObjectPtrLabel(var0.getPointer(), var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glFramebufferParameteri(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glFramebufferParameteri;
      BufferChecks.checkFunctionAddress(var4);
      nglFramebufferParameteri(var0, var1, var2, var4);
   }

   static native void nglFramebufferParameteri(int var0, int var1, int var2, long var3);

   public static void glGetFramebufferParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFramebufferParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetFramebufferParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFramebufferParameteriv(int var0, int var1, long var2, long var4);

   public static int glGetFramebufferParameteri(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFramebufferParameteriv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetFramebufferParameteriv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetInternalformat(int var0, int var1, int var2, LongBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetInternalformati64v;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetInternalformati64v(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetInternalformati64v(int var0, int var1, int var2, int var3, long var4, long var6);

   public static long glGetInternalformati64(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetInternalformati64v;
      BufferChecks.checkFunctionAddress(var4);
      LongBuffer var6 = APIUtil.getBufferLong(var3);
      nglGetInternalformati64v(var0, var1, var2, 1, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glInvalidateTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glInvalidateTexSubImage;
      BufferChecks.checkFunctionAddress(var9);
      nglInvalidateTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglInvalidateTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8);

   public static void glInvalidateTexImage(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glInvalidateTexImage;
      BufferChecks.checkFunctionAddress(var3);
      nglInvalidateTexImage(var0, var1, var3);
   }

   static native void nglInvalidateTexImage(int var0, int var1, long var2);

   public static void glInvalidateBufferSubData(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glInvalidateBufferSubData;
      BufferChecks.checkFunctionAddress(var6);
      nglInvalidateBufferSubData(var0, var1, var3, var6);
   }

   static native void nglInvalidateBufferSubData(int var0, long var1, long var3, long var5);

   public static void glInvalidateBufferData(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glInvalidateBufferData;
      BufferChecks.checkFunctionAddress(var2);
      nglInvalidateBufferData(var0, var2);
   }

   static native void nglInvalidateBufferData(int var0, long var1);

   public static void glInvalidateFramebuffer(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glInvalidateFramebuffer;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglInvalidateFramebuffer(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglInvalidateFramebuffer(int var0, int var1, long var2, long var4);

   public static void glInvalidateSubFramebuffer(int var0, IntBuffer var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glInvalidateSubFramebuffer;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var1);
      nglInvalidateSubFramebuffer(var0, var1.remaining(), MemoryUtil.getAddress(var1), var2, var3, var4, var5, var7);
   }

   static native void nglInvalidateSubFramebuffer(int var0, int var1, long var2, int var4, int var5, int var6, int var7, long var8);

   public static void glMultiDrawArraysIndirect(int var0, ByteBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiDrawArraysIndirect;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureIndirectBOdisabled(var4);
      BufferChecks.checkBuffer(var1, (var3 == 0 ? 16 : var3) * var2);
      nglMultiDrawArraysIndirect(var0, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   static native void nglMultiDrawArraysIndirect(int var0, long var1, int var3, int var4, long var5);

   public static void glMultiDrawArraysIndirect(int var0, long var1, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawArraysIndirect;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureIndirectBOenabled(var5);
      nglMultiDrawArraysIndirectBO(var0, var1, var3, var4, var6);
   }

   static native void nglMultiDrawArraysIndirectBO(int var0, long var1, int var3, int var4, long var5);

   public static void glMultiDrawArraysIndirect(int var0, IntBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiDrawArraysIndirect;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureIndirectBOdisabled(var4);
      BufferChecks.checkBuffer(var1, (var3 == 0 ? 4 : var3 >> 2) * var2);
      nglMultiDrawArraysIndirect(var0, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   public static void glMultiDrawElementsIndirect(int var0, int var1, ByteBuffer var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawElementsIndirect;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureIndirectBOdisabled(var5);
      BufferChecks.checkBuffer(var2, (var4 == 0 ? 20 : var4) * var3);
      nglMultiDrawElementsIndirect(var0, var1, MemoryUtil.getAddress(var2), var3, var4, var6);
   }

   static native void nglMultiDrawElementsIndirect(int var0, int var1, long var2, int var4, int var5, long var6);

   public static void glMultiDrawElementsIndirect(int var0, int var1, long var2, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMultiDrawElementsIndirect;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureIndirectBOenabled(var6);
      nglMultiDrawElementsIndirectBO(var0, var1, var2, var4, var5, var7);
   }

   static native void nglMultiDrawElementsIndirectBO(int var0, int var1, long var2, int var4, int var5, long var6);

   public static void glMultiDrawElementsIndirect(int var0, int var1, IntBuffer var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawElementsIndirect;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureIndirectBOdisabled(var5);
      BufferChecks.checkBuffer(var2, (var4 == 0 ? 5 : var4 >> 2) * var3);
      nglMultiDrawElementsIndirect(var0, var1, MemoryUtil.getAddress(var2), var3, var4, var6);
   }

   public static void glGetProgramInterface(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetProgramInterfaceiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglGetProgramInterfaceiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetProgramInterfaceiv(int var0, int var1, int var2, long var3, long var5);

   public static int glGetProgramInterfacei(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramInterfaceiv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetProgramInterfaceiv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static int glGetProgramResourceIndex(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramResourceIndex;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      int var6 = nglGetProgramResourceIndex(var0, var1, MemoryUtil.getAddress(var2), var4);
      return var6;
   }

   static native int nglGetProgramResourceIndex(int var0, int var1, long var2, long var4);

   public static int glGetProgramResourceIndex(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramResourceIndex;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglGetProgramResourceIndex(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
      return var6;
   }

   public static void glGetProgramResourceName(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetProgramResourceName;
      BufferChecks.checkFunctionAddress(var6);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      nglGetProgramResourceName(var0, var1, var2, var4 == null ? 0 : var4.remaining(), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var6);
   }

   static native void nglGetProgramResourceName(int var0, int var1, int var2, int var3, long var4, long var6, long var8);

   public static String glGetProgramResourceName(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetProgramResourceName;
      BufferChecks.checkFunctionAddress(var5);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var3);
      nglGetProgramResourceName(var0, var1, var2, var3, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static void glGetProgramResource(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetProgramResourceiv;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var3);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      BufferChecks.checkDirect(var5);
      nglGetProgramResourceiv(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5.remaining(), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetProgramResourceiv(int var0, int var1, int var2, int var3, long var4, int var6, long var7, long var9, long var11);

   public static int glGetProgramResourceLocation(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramResourceLocation;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      int var6 = nglGetProgramResourceLocation(var0, var1, MemoryUtil.getAddress(var2), var4);
      return var6;
   }

   static native int nglGetProgramResourceLocation(int var0, int var1, long var2, long var4);

   public static int glGetProgramResourceLocation(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramResourceLocation;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglGetProgramResourceLocation(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
      return var6;
   }

   public static int glGetProgramResourceLocationIndex(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramResourceLocationIndex;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      int var6 = nglGetProgramResourceLocationIndex(var0, var1, MemoryUtil.getAddress(var2), var4);
      return var6;
   }

   static native int nglGetProgramResourceLocationIndex(int var0, int var1, long var2, long var4);

   public static int glGetProgramResourceLocationIndex(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramResourceLocationIndex;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglGetProgramResourceLocationIndex(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
      return var6;
   }

   public static void glShaderStorageBlockBinding(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glShaderStorageBlockBinding;
      BufferChecks.checkFunctionAddress(var4);
      nglShaderStorageBlockBinding(var0, var1, var2, var4);
   }

   static native void nglShaderStorageBlockBinding(int var0, int var1, int var2, long var3);

   public static void glTexBufferRange(int var0, int var1, int var2, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexBufferRange;
      BufferChecks.checkFunctionAddress(var8);
      nglTexBufferRange(var0, var1, var2, var3, var5, var8);
   }

   static native void nglTexBufferRange(int var0, int var1, int var2, long var3, long var5, long var7);

   public static void glTexStorage2DMultisample(int var0, int var1, int var2, int var3, int var4, boolean var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glTexStorage2DMultisample;
      BufferChecks.checkFunctionAddress(var7);
      nglTexStorage2DMultisample(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglTexStorage2DMultisample(int var0, int var1, int var2, int var3, int var4, boolean var5, long var6);

   public static void glTexStorage3DMultisample(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexStorage3DMultisample;
      BufferChecks.checkFunctionAddress(var8);
      nglTexStorage3DMultisample(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglTexStorage3DMultisample(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, long var7);

   public static void glTextureView(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureView;
      BufferChecks.checkFunctionAddress(var9);
      nglTextureView(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglTextureView(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8);

   public static void glBindVertexBuffer(int var0, int var1, long var2, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBindVertexBuffer;
      BufferChecks.checkFunctionAddress(var6);
      nglBindVertexBuffer(var0, var1, var2, var4, var6);
   }

   static native void nglBindVertexBuffer(int var0, int var1, long var2, int var4, long var5);

   public static void glVertexAttribFormat(int var0, int var1, int var2, boolean var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribFormat;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribFormat(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttribFormat(int var0, int var1, int var2, boolean var3, int var4, long var5);

   public static void glVertexAttribIFormat(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribIFormat;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribIFormat(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribIFormat(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexAttribLFormat(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribLFormat;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribLFormat(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribLFormat(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexAttribBinding(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribBinding;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttribBinding(var0, var1, var3);
   }

   static native void nglVertexAttribBinding(int var0, int var1, long var2);

   public static void glVertexBindingDivisor(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexBindingDivisor;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexBindingDivisor(var0, var1, var3);
   }

   static native void nglVertexBindingDivisor(int var0, int var1, long var2);
}
