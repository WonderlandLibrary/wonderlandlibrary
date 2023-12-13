package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class GL30 {
   public static final int GL_MAJOR_VERSION = 33307;
   public static final int GL_MINOR_VERSION = 33308;
   public static final int GL_NUM_EXTENSIONS = 33309;
   public static final int GL_CONTEXT_FLAGS = 33310;
   public static final int GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT = 1;
   public static final int GL_DEPTH_BUFFER = 33315;
   public static final int GL_STENCIL_BUFFER = 33316;
   public static final int GL_COMPRESSED_RED = 33317;
   public static final int GL_COMPRESSED_RG = 33318;
   public static final int GL_COMPARE_REF_TO_TEXTURE = 34894;
   public static final int GL_CLIP_DISTANCE0 = 12288;
   public static final int GL_CLIP_DISTANCE1 = 12289;
   public static final int GL_CLIP_DISTANCE2 = 12290;
   public static final int GL_CLIP_DISTANCE3 = 12291;
   public static final int GL_CLIP_DISTANCE4 = 12292;
   public static final int GL_CLIP_DISTANCE5 = 12293;
   public static final int GL_CLIP_DISTANCE6 = 12294;
   public static final int GL_CLIP_DISTANCE7 = 12295;
   public static final int GL_MAX_CLIP_DISTANCES = 3378;
   public static final int GL_MAX_VARYING_COMPONENTS = 35659;
   public static final int GL_BUFFER_ACCESS_FLAGS = 37151;
   public static final int GL_BUFFER_MAP_LENGTH = 37152;
   public static final int GL_BUFFER_MAP_OFFSET = 37153;
   public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER = 35069;
   public static final int GL_SAMPLER_BUFFER = 36290;
   public static final int GL_SAMPLER_CUBE_SHADOW = 36293;
   public static final int GL_UNSIGNED_INT_VEC2 = 36294;
   public static final int GL_UNSIGNED_INT_VEC3 = 36295;
   public static final int GL_UNSIGNED_INT_VEC4 = 36296;
   public static final int GL_INT_SAMPLER_1D = 36297;
   public static final int GL_INT_SAMPLER_2D = 36298;
   public static final int GL_INT_SAMPLER_3D = 36299;
   public static final int GL_INT_SAMPLER_CUBE = 36300;
   public static final int GL_INT_SAMPLER_2D_RECT = 36301;
   public static final int GL_INT_SAMPLER_1D_ARRAY = 36302;
   public static final int GL_INT_SAMPLER_2D_ARRAY = 36303;
   public static final int GL_INT_SAMPLER_BUFFER = 36304;
   public static final int GL_UNSIGNED_INT_SAMPLER_1D = 36305;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D = 36306;
   public static final int GL_UNSIGNED_INT_SAMPLER_3D = 36307;
   public static final int GL_UNSIGNED_INT_SAMPLER_CUBE = 36308;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT = 36309;
   public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY = 36310;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 36311;
   public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER = 36312;
   public static final int GL_MIN_PROGRAM_TEXEL_OFFSET = 35076;
   public static final int GL_MAX_PROGRAM_TEXEL_OFFSET = 35077;
   public static final int GL_QUERY_WAIT = 36371;
   public static final int GL_QUERY_NO_WAIT = 36372;
   public static final int GL_QUERY_BY_REGION_WAIT = 36373;
   public static final int GL_QUERY_BY_REGION_NO_WAIT = 36374;
   public static final int GL_MAP_READ_BIT = 1;
   public static final int GL_MAP_WRITE_BIT = 2;
   public static final int GL_MAP_INVALIDATE_RANGE_BIT = 4;
   public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 8;
   public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 16;
   public static final int GL_MAP_UNSYNCHRONIZED_BIT = 32;
   public static final int GL_CLAMP_VERTEX_COLOR = 35098;
   public static final int GL_CLAMP_FRAGMENT_COLOR = 35099;
   public static final int GL_CLAMP_READ_COLOR = 35100;
   public static final int GL_FIXED_ONLY = 35101;
   public static final int GL_DEPTH_COMPONENT32F = 36012;
   public static final int GL_DEPTH32F_STENCIL8 = 36013;
   public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 36269;
   public static final int GL_TEXTURE_RED_TYPE = 35856;
   public static final int GL_TEXTURE_GREEN_TYPE = 35857;
   public static final int GL_TEXTURE_BLUE_TYPE = 35858;
   public static final int GL_TEXTURE_ALPHA_TYPE = 35859;
   public static final int GL_TEXTURE_LUMINANCE_TYPE = 35860;
   public static final int GL_TEXTURE_INTENSITY_TYPE = 35861;
   public static final int GL_TEXTURE_DEPTH_TYPE = 35862;
   public static final int GL_UNSIGNED_NORMALIZED = 35863;
   public static final int GL_RGBA32F = 34836;
   public static final int GL_RGB32F = 34837;
   public static final int GL_ALPHA32F = 34838;
   public static final int GL_RGBA16F = 34842;
   public static final int GL_RGB16F = 34843;
   public static final int GL_ALPHA16F = 34844;
   public static final int GL_R11F_G11F_B10F = 35898;
   public static final int GL_UNSIGNED_INT_10F_11F_11F_REV = 35899;
   public static final int GL_RGB9_E5 = 35901;
   public static final int GL_UNSIGNED_INT_5_9_9_9_REV = 35902;
   public static final int GL_TEXTURE_SHARED_SIZE = 35903;
   public static final int GL_FRAMEBUFFER = 36160;
   public static final int GL_READ_FRAMEBUFFER = 36008;
   public static final int GL_DRAW_FRAMEBUFFER = 36009;
   public static final int GL_RENDERBUFFER = 36161;
   public static final int GL_STENCIL_INDEX1 = 36166;
   public static final int GL_STENCIL_INDEX4 = 36167;
   public static final int GL_STENCIL_INDEX8 = 36168;
   public static final int GL_STENCIL_INDEX16 = 36169;
   public static final int GL_RENDERBUFFER_WIDTH = 36162;
   public static final int GL_RENDERBUFFER_HEIGHT = 36163;
   public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 36164;
   public static final int GL_RENDERBUFFER_RED_SIZE = 36176;
   public static final int GL_RENDERBUFFER_GREEN_SIZE = 36177;
   public static final int GL_RENDERBUFFER_BLUE_SIZE = 36178;
   public static final int GL_RENDERBUFFER_ALPHA_SIZE = 36179;
   public static final int GL_RENDERBUFFER_DEPTH_SIZE = 36180;
   public static final int GL_RENDERBUFFER_STENCIL_SIZE = 36181;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 33296;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 33297;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 33298;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 33299;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 33300;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 33301;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 33302;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 33303;
   public static final int GL_FRAMEBUFFER_DEFAULT = 33304;
   public static final int GL_INDEX = 33314;
   public static final int GL_COLOR_ATTACHMENT0 = 36064;
   public static final int GL_COLOR_ATTACHMENT1 = 36065;
   public static final int GL_COLOR_ATTACHMENT2 = 36066;
   public static final int GL_COLOR_ATTACHMENT3 = 36067;
   public static final int GL_COLOR_ATTACHMENT4 = 36068;
   public static final int GL_COLOR_ATTACHMENT5 = 36069;
   public static final int GL_COLOR_ATTACHMENT6 = 36070;
   public static final int GL_COLOR_ATTACHMENT7 = 36071;
   public static final int GL_COLOR_ATTACHMENT8 = 36072;
   public static final int GL_COLOR_ATTACHMENT9 = 36073;
   public static final int GL_COLOR_ATTACHMENT10 = 36074;
   public static final int GL_COLOR_ATTACHMENT11 = 36075;
   public static final int GL_COLOR_ATTACHMENT12 = 36076;
   public static final int GL_COLOR_ATTACHMENT13 = 36077;
   public static final int GL_COLOR_ATTACHMENT14 = 36078;
   public static final int GL_COLOR_ATTACHMENT15 = 36079;
   public static final int GL_DEPTH_ATTACHMENT = 36096;
   public static final int GL_STENCIL_ATTACHMENT = 36128;
   public static final int GL_DEPTH_STENCIL_ATTACHMENT = 33306;
   public static final int GL_FRAMEBUFFER_COMPLETE = 36053;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 36059;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 36060;
   public static final int GL_FRAMEBUFFER_UNSUPPORTED = 36061;
   public static final int GL_FRAMEBUFFER_UNDEFINED = 33305;
   public static final int GL_FRAMEBUFFER_BINDING = 36006;
   public static final int GL_RENDERBUFFER_BINDING = 36007;
   public static final int GL_MAX_COLOR_ATTACHMENTS = 36063;
   public static final int GL_MAX_RENDERBUFFER_SIZE = 34024;
   public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 1286;
   public static final int GL_HALF_FLOAT = 5131;
   public static final int GL_RENDERBUFFER_SAMPLES = 36011;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 36182;
   public static final int GL_MAX_SAMPLES = 36183;
   public static final int GL_DRAW_FRAMEBUFFER_BINDING = 36006;
   public static final int GL_READ_FRAMEBUFFER_BINDING = 36010;
   public static final int GL_RGBA_INTEGER_MODE = 36254;
   public static final int GL_RGBA32UI = 36208;
   public static final int GL_RGB32UI = 36209;
   public static final int GL_ALPHA32UI = 36210;
   public static final int GL_RGBA16UI = 36214;
   public static final int GL_RGB16UI = 36215;
   public static final int GL_ALPHA16UI = 36216;
   public static final int GL_RGBA8UI = 36220;
   public static final int GL_RGB8UI = 36221;
   public static final int GL_ALPHA8UI = 36222;
   public static final int GL_RGBA32I = 36226;
   public static final int GL_RGB32I = 36227;
   public static final int GL_ALPHA32I = 36228;
   public static final int GL_RGBA16I = 36232;
   public static final int GL_RGB16I = 36233;
   public static final int GL_ALPHA16I = 36234;
   public static final int GL_RGBA8I = 36238;
   public static final int GL_RGB8I = 36239;
   public static final int GL_ALPHA8I = 36240;
   public static final int GL_RED_INTEGER = 36244;
   public static final int GL_GREEN_INTEGER = 36245;
   public static final int GL_BLUE_INTEGER = 36246;
   public static final int GL_ALPHA_INTEGER = 36247;
   public static final int GL_RGB_INTEGER = 36248;
   public static final int GL_RGBA_INTEGER = 36249;
   public static final int GL_BGR_INTEGER = 36250;
   public static final int GL_BGRA_INTEGER = 36251;
   public static final int GL_TEXTURE_1D_ARRAY = 35864;
   public static final int GL_TEXTURE_2D_ARRAY = 35866;
   public static final int GL_PROXY_TEXTURE_2D_ARRAY = 35867;
   public static final int GL_PROXY_TEXTURE_1D_ARRAY = 35865;
   public static final int GL_TEXTURE_BINDING_1D_ARRAY = 35868;
   public static final int GL_TEXTURE_BINDING_2D_ARRAY = 35869;
   public static final int GL_MAX_ARRAY_TEXTURE_LAYERS = 35071;
   public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE = 34894;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 36052;
   public static final int GL_SAMPLER_1D_ARRAY = 36288;
   public static final int GL_SAMPLER_2D_ARRAY = 36289;
   public static final int GL_SAMPLER_1D_ARRAY_SHADOW = 36291;
   public static final int GL_SAMPLER_2D_ARRAY_SHADOW = 36292;
   public static final int GL_DEPTH_STENCIL = 34041;
   public static final int GL_UNSIGNED_INT_24_8 = 34042;
   public static final int GL_DEPTH24_STENCIL8 = 35056;
   public static final int GL_TEXTURE_STENCIL_SIZE = 35057;
   public static final int GL_COMPRESSED_RED_RGTC1 = 36283;
   public static final int GL_COMPRESSED_SIGNED_RED_RGTC1 = 36284;
   public static final int GL_COMPRESSED_RG_RGTC2 = 36285;
   public static final int GL_COMPRESSED_SIGNED_RG_RGTC2 = 36286;
   public static final int GL_R8 = 33321;
   public static final int GL_R16 = 33322;
   public static final int GL_RG8 = 33323;
   public static final int GL_RG16 = 33324;
   public static final int GL_R16F = 33325;
   public static final int GL_R32F = 33326;
   public static final int GL_RG16F = 33327;
   public static final int GL_RG32F = 33328;
   public static final int GL_R8I = 33329;
   public static final int GL_R8UI = 33330;
   public static final int GL_R16I = 33331;
   public static final int GL_R16UI = 33332;
   public static final int GL_R32I = 33333;
   public static final int GL_R32UI = 33334;
   public static final int GL_RG8I = 33335;
   public static final int GL_RG8UI = 33336;
   public static final int GL_RG16I = 33337;
   public static final int GL_RG16UI = 33338;
   public static final int GL_RG32I = 33339;
   public static final int GL_RG32UI = 33340;
   public static final int GL_RG = 33319;
   public static final int GL_RG_INTEGER = 33320;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 35982;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START = 35972;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 35973;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 35983;
   public static final int GL_INTERLEAVED_ATTRIBS = 35980;
   public static final int GL_SEPARATE_ATTRIBS = 35981;
   public static final int GL_PRIMITIVES_GENERATED = 35975;
   public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 35976;
   public static final int GL_RASTERIZER_DISCARD = 35977;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 35978;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 35979;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 35968;
   public static final int GL_TRANSFORM_FEEDBACK_VARYINGS = 35971;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 35967;
   public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 35958;
   public static final int GL_VERTEX_ARRAY_BINDING = 34229;
   public static final int GL_FRAMEBUFFER_SRGB = 36281;
   public static final int GL_FRAMEBUFFER_SRGB_CAPABLE = 36282;

   private GL30() {
   }

   public static String glGetStringi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetStringi;
      BufferChecks.checkFunctionAddress(var3);
      String var5 = nglGetStringi(var0, var1, var3);
      return var5;
   }

   static native String nglGetStringi(int var0, int var1, long var2);

   public static void glClearBuffer(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glClearBufferfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglClearBufferfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglClearBufferfv(int var0, int var1, long var2, long var4);

   public static void glClearBuffer(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glClearBufferiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglClearBufferiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglClearBufferiv(int var0, int var1, long var2, long var4);

   public static void glClearBufferu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glClearBufferuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglClearBufferuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglClearBufferuiv(int var0, int var1, long var2, long var4);

   public static void glClearBufferfi(int var0, int var1, float var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glClearBufferfi;
      BufferChecks.checkFunctionAddress(var5);
      nglClearBufferfi(var0, var1, var2, var3, var5);
   }

   static native void nglClearBufferfi(int var0, int var1, float var2, int var3, long var4);

   public static void glVertexAttribI1i(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1i;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttribI1i(var0, var1, var3);
   }

   static native void nglVertexAttribI1i(int var0, int var1, long var2);

   public static void glVertexAttribI2i(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribI2i;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribI2i(var0, var1, var2, var4);
   }

   static native void nglVertexAttribI2i(int var0, int var1, int var2, long var3);

   public static void glVertexAttribI3i(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribI3i;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribI3i(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribI3i(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexAttribI4i(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribI4i;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribI4i(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttribI4i(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glVertexAttribI1ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1ui;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttribI1ui(var0, var1, var3);
   }

   static native void nglVertexAttribI1ui(int var0, int var1, long var2);

   public static void glVertexAttribI2ui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribI2ui;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribI2ui(var0, var1, var2, var4);
   }

   static native void nglVertexAttribI2ui(int var0, int var1, int var2, long var3);

   public static void glVertexAttribI3ui(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribI3ui;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribI3ui(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribI3ui(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexAttribI4ui(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribI4ui;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribI4ui(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttribI4ui(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glVertexAttribI1(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      nglVertexAttribI1iv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI1iv(int var0, long var1, long var3);

   public static void glVertexAttribI2(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI2iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 2);
      nglVertexAttribI2iv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI2iv(int var0, long var1, long var3);

   public static void glVertexAttribI3(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI3iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglVertexAttribI3iv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI3iv(int var0, long var1, long var3);

   public static void glVertexAttribI4(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglVertexAttribI4iv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4iv(int var0, long var1, long var3);

   public static void glVertexAttribI1u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI1uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      nglVertexAttribI1uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI1uiv(int var0, long var1, long var3);

   public static void glVertexAttribI2u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI2uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 2);
      nglVertexAttribI2uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI2uiv(int var0, long var1, long var3);

   public static void glVertexAttribI3u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI3uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglVertexAttribI3uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI3uiv(int var0, long var1, long var3);

   public static void glVertexAttribI4u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglVertexAttribI4uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4uiv(int var0, long var1, long var3);

   public static void glVertexAttribI4(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4bv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 4);
      nglVertexAttribI4bv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4bv(int var0, long var1, long var3);

   public static void glVertexAttribI4(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4sv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ShortBuffer)var1, 4);
      nglVertexAttribI4sv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4sv(int var0, long var1, long var3);

   public static void glVertexAttribI4u(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4ubv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 4);
      nglVertexAttribI4ubv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4ubv(int var0, long var1, long var3);

   public static void glVertexAttribI4u(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribI4usv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ShortBuffer)var1, 4);
      nglVertexAttribI4usv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribI4usv(int var0, long var1, long var3);

   public static void glVertexAttribIPointer(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribIPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribIPointer(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribIPointer(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribIPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribIPointer(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribIPointer(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribIPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribIPointer(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglVertexAttribIPointer(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexAttribIPointer(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribIPointer;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOenabled(var6);
      nglVertexAttribIPointerBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglVertexAttribIPointerBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetVertexAttribI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribIiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribIiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribIiv(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribIu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribIuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribIuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribIuiv(int var0, int var1, long var2, long var4);

   public static void glUniform1ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1ui;
      BufferChecks.checkFunctionAddress(var3);
      nglUniform1ui(var0, var1, var3);
   }

   static native void nglUniform1ui(int var0, int var1, long var2);

   public static void glUniform2ui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform2ui;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform2ui(var0, var1, var2, var4);
   }

   static native void nglUniform2ui(int var0, int var1, int var2, long var3);

   public static void glUniform3ui(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUniform3ui;
      BufferChecks.checkFunctionAddress(var5);
      nglUniform3ui(var0, var1, var2, var3, var5);
   }

   static native void nglUniform3ui(int var0, int var1, int var2, int var3, long var4);

   public static void glUniform4ui(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform4ui;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform4ui(var0, var1, var2, var3, var4, var6);
   }

   static native void nglUniform4ui(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glUniform1u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1uiv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1uiv(int var0, int var1, long var2, long var4);

   public static void glUniform2u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2uiv(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2uiv(int var0, int var1, long var2, long var4);

   public static void glUniform3u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3uiv(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3uiv(int var0, int var1, long var2, long var4);

   public static void glUniform4u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4uiv(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4uiv(int var0, int var1, long var2, long var4);

   public static void glGetUniformu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetUniformuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformuiv(int var0, int var1, long var2, long var4);

   public static void glBindFragDataLocation(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindFragDataLocation;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      nglBindFragDataLocation(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglBindFragDataLocation(int var0, int var1, long var2, long var4);

   public static void glBindFragDataLocation(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindFragDataLocation;
      BufferChecks.checkFunctionAddress(var4);
      nglBindFragDataLocation(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
   }

   public static int glGetFragDataLocation(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFragDataLocation;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetFragDataLocation(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetFragDataLocation(int var0, long var1, long var3);

   public static int glGetFragDataLocation(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFragDataLocation;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetFragDataLocation(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glBeginConditionalRender(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBeginConditionalRender;
      BufferChecks.checkFunctionAddress(var3);
      nglBeginConditionalRender(var0, var1, var3);
   }

   static native void nglBeginConditionalRender(int var0, int var1, long var2);

   public static void glEndConditionalRender() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndConditionalRender;
      BufferChecks.checkFunctionAddress(var1);
      nglEndConditionalRender(var1);
   }

   static native void nglEndConditionalRender(long var0);

   public static ByteBuffer glMapBufferRange(int var0, long var1, long var3, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMapBufferRange;
      BufferChecks.checkFunctionAddress(var8);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      ByteBuffer var10 = nglMapBufferRange(var0, var1, var3, var5, var6, var8);
      return LWJGLUtil.CHECKS && var10 == null ? null : var10.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglMapBufferRange(int var0, long var1, long var3, int var5, ByteBuffer var6, long var7);

   public static void glFlushMappedBufferRange(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFlushMappedBufferRange;
      BufferChecks.checkFunctionAddress(var6);
      nglFlushMappedBufferRange(var0, var1, var3, var6);
   }

   static native void nglFlushMappedBufferRange(int var0, long var1, long var3, long var5);

   public static void glClampColor(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glClampColor;
      BufferChecks.checkFunctionAddress(var3);
      nglClampColor(var0, var1, var3);
   }

   static native void nglClampColor(int var0, int var1, long var2);

   public static boolean glIsRenderbuffer(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsRenderbuffer;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsRenderbuffer(var0, var2);
      return var4;
   }

   static native boolean nglIsRenderbuffer(int var0, long var1);

   public static void glBindRenderbuffer(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindRenderbuffer;
      BufferChecks.checkFunctionAddress(var3);
      nglBindRenderbuffer(var0, var1, var3);
   }

   static native void nglBindRenderbuffer(int var0, int var1, long var2);

   public static void glDeleteRenderbuffers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteRenderbuffers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteRenderbuffers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteRenderbuffers(int var0, long var1, long var3);

   public static void glDeleteRenderbuffers(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteRenderbuffers;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteRenderbuffers(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenRenderbuffers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenRenderbuffers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenRenderbuffers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenRenderbuffers(int var0, long var1, long var3);

   public static int glGenRenderbuffers() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenRenderbuffers;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenRenderbuffers(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glRenderbufferStorage(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRenderbufferStorage;
      BufferChecks.checkFunctionAddress(var5);
      nglRenderbufferStorage(var0, var1, var2, var3, var5);
   }

   static native void nglRenderbufferStorage(int var0, int var1, int var2, int var3, long var4);

   public static void glGetRenderbufferParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetRenderbufferParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetRenderbufferParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetRenderbufferParameteriv(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetRenderbufferParameter(int var0, int var1) {
      return glGetRenderbufferParameteri(var0, var1);
   }

   public static int glGetRenderbufferParameteri(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetRenderbufferParameteriv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetRenderbufferParameteriv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static boolean glIsFramebuffer(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsFramebuffer;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsFramebuffer(var0, var2);
      return var4;
   }

   static native boolean nglIsFramebuffer(int var0, long var1);

   public static void glBindFramebuffer(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindFramebuffer;
      BufferChecks.checkFunctionAddress(var3);
      nglBindFramebuffer(var0, var1, var3);
   }

   static native void nglBindFramebuffer(int var0, int var1, long var2);

   public static void glDeleteFramebuffers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFramebuffers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteFramebuffers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteFramebuffers(int var0, long var1, long var3);

   public static void glDeleteFramebuffers(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFramebuffers;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteFramebuffers(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenFramebuffers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenFramebuffers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenFramebuffers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenFramebuffers(int var0, long var1, long var3);

   public static int glGenFramebuffers() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenFramebuffers;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenFramebuffers(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static int glCheckFramebufferStatus(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCheckFramebufferStatus;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglCheckFramebufferStatus(var0, var2);
      return var4;
   }

   static native int nglCheckFramebufferStatus(int var0, long var1);

   public static void glFramebufferTexture1D(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFramebufferTexture1D;
      BufferChecks.checkFunctionAddress(var6);
      nglFramebufferTexture1D(var0, var1, var2, var3, var4, var6);
   }

   static native void nglFramebufferTexture1D(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glFramebufferTexture2D(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFramebufferTexture2D;
      BufferChecks.checkFunctionAddress(var6);
      nglFramebufferTexture2D(var0, var1, var2, var3, var4, var6);
   }

   static native void nglFramebufferTexture2D(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glFramebufferTexture3D(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glFramebufferTexture3D;
      BufferChecks.checkFunctionAddress(var7);
      nglFramebufferTexture3D(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglFramebufferTexture3D(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glFramebufferRenderbuffer(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glFramebufferRenderbuffer;
      BufferChecks.checkFunctionAddress(var5);
      nglFramebufferRenderbuffer(var0, var1, var2, var3, var5);
   }

   static native void nglFramebufferRenderbuffer(int var0, int var1, int var2, int var3, long var4);

   public static void glGetFramebufferAttachmentParameter(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetFramebufferAttachmentParameteriv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetFramebufferAttachmentParameteriv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetFramebufferAttachmentParameteriv(int var0, int var1, int var2, long var3, long var5);

   /** @deprecated */
   @Deprecated
   public static int glGetFramebufferAttachmentParameter(int var0, int var1, int var2) {
      return glGetFramebufferAttachmentParameteri(var0, var1, var2);
   }

   public static int glGetFramebufferAttachmentParameteri(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFramebufferAttachmentParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetFramebufferAttachmentParameteriv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGenerateMipmap(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenerateMipmap;
      BufferChecks.checkFunctionAddress(var2);
      nglGenerateMipmap(var0, var2);
   }

   static native void nglGenerateMipmap(int var0, long var1);

   public static void glRenderbufferStorageMultisample(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glRenderbufferStorageMultisample;
      BufferChecks.checkFunctionAddress(var6);
      nglRenderbufferStorageMultisample(var0, var1, var2, var3, var4, var6);
   }

   static native void nglRenderbufferStorageMultisample(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glBlitFramebuffer(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glBlitFramebuffer;
      BufferChecks.checkFunctionAddress(var11);
      nglBlitFramebuffer(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   static native void nglBlitFramebuffer(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10);

   public static void glTexParameterI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglTexParameterIiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexParameterIiv(int var0, int var1, long var2, long var4);

   public static void glTexParameterIi(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIiv;
      BufferChecks.checkFunctionAddress(var4);
      nglTexParameterIiv(var0, var1, APIUtil.getInt(var3, var2), var4);
   }

   public static void glTexParameterIu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglTexParameterIuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexParameterIuiv(int var0, int var1, long var2, long var4);

   public static void glTexParameterIui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIuiv;
      BufferChecks.checkFunctionAddress(var4);
      nglTexParameterIuiv(var0, var1, APIUtil.getInt(var3, var2), var4);
   }

   public static void glGetTexParameterI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexParameterIiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetTexParameterIiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexParameterIiv(int var0, int var1, long var2, long var4);

   public static int glGetTexParameterIi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexParameterIiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTexParameterIiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexParameterIu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexParameterIuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetTexParameterIuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexParameterIuiv(int var0, int var1, long var2, long var4);

   public static int glGetTexParameterIui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexParameterIuiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTexParameterIuiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glFramebufferTextureLayer(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFramebufferTextureLayer;
      BufferChecks.checkFunctionAddress(var6);
      nglFramebufferTextureLayer(var0, var1, var2, var3, var4, var6);
   }

   static native void nglFramebufferTextureLayer(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glColorMaski(int var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glColorMaski;
      BufferChecks.checkFunctionAddress(var6);
      nglColorMaski(var0, var1, var2, var3, var4, var6);
   }

   static native void nglColorMaski(int var0, boolean var1, boolean var2, boolean var3, boolean var4, long var5);

   public static void glGetBoolean(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetBooleani_v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ByteBuffer)var2, 4);
      nglGetBooleani_v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetBooleani_v(int var0, int var1, long var2, long var4);

   public static boolean glGetBoolean(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBooleani_v;
      BufferChecks.checkFunctionAddress(var3);
      ByteBuffer var5 = APIUtil.getBufferByte(var2, 1);
      nglGetBooleani_v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0) == 1;
   }

   public static void glGetInteger(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetIntegeri_v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetIntegeri_v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetIntegeri_v(int var0, int var1, long var2, long var4);

   public static int glGetInteger(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetIntegeri_v;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetIntegeri_v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glEnablei(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEnablei;
      BufferChecks.checkFunctionAddress(var3);
      nglEnablei(var0, var1, var3);
   }

   static native void nglEnablei(int var0, int var1, long var2);

   public static void glDisablei(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDisablei;
      BufferChecks.checkFunctionAddress(var3);
      nglDisablei(var0, var1, var3);
   }

   static native void nglDisablei(int var0, int var1, long var2);

   public static boolean glIsEnabledi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsEnabledi;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsEnabledi(var0, var1, var3);
      return var5;
   }

   static native boolean nglIsEnabledi(int var0, int var1, long var2);

   public static void glBindBufferRange(int var0, int var1, int var2, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glBindBufferRange;
      BufferChecks.checkFunctionAddress(var8);
      nglBindBufferRange(var0, var1, var2, var3, var5, var8);
   }

   static native void nglBindBufferRange(int var0, int var1, int var2, long var3, long var5, long var7);

   public static void glBindBufferBase(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindBufferBase;
      BufferChecks.checkFunctionAddress(var4);
      nglBindBufferBase(var0, var1, var2, var4);
   }

   static native void nglBindBufferBase(int var0, int var1, int var2, long var3);

   public static void glBeginTransformFeedback(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBeginTransformFeedback;
      BufferChecks.checkFunctionAddress(var2);
      nglBeginTransformFeedback(var0, var2);
   }

   static native void nglBeginTransformFeedback(int var0, long var1);

   public static void glEndTransformFeedback() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndTransformFeedback;
      BufferChecks.checkFunctionAddress(var1);
      nglEndTransformFeedback(var1);
   }

   static native void nglEndTransformFeedback(long var0);

   public static void glTransformFeedbackVaryings(int var0, int var1, ByteBuffer var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTransformFeedbackVaryings;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2, var1);
      nglTransformFeedbackVaryings(var0, var1, MemoryUtil.getAddress(var2), var3, var5);
   }

   static native void nglTransformFeedbackVaryings(int var0, int var1, long var2, int var4, long var5);

   public static void glTransformFeedbackVaryings(int var0, CharSequence[] var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTransformFeedbackVaryings;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkArray(var1);
      nglTransformFeedbackVaryings(var0, var1.length, APIUtil.getBufferNT(var3, var1), var2, var4);
   }

   public static void glGetTransformFeedbackVarying(int var0, int var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTransformFeedbackVarying;
      BufferChecks.checkFunctionAddress(var7);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      BufferChecks.checkDirect(var5);
      nglGetTransformFeedbackVarying(var0, var1, var5.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetTransformFeedbackVarying(int var0, int var1, int var2, long var3, long var5, long var7, long var9, long var11);

   public static String glGetTransformFeedbackVarying(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTransformFeedbackVarying;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      IntBuffer var8 = APIUtil.getLengths(var5);
      ByteBuffer var9 = APIUtil.getBufferByte(var5, var2);
      nglGetTransformFeedbackVarying(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var8), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var9), var6);
      var9.limit(var8.get(0));
      return APIUtil.getString(var5, var9);
   }

   public static void glBindVertexArray(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBindVertexArray;
      BufferChecks.checkFunctionAddress(var2);
      StateTracker.bindVAO(var1, var0);
      nglBindVertexArray(var0, var2);
   }

   static native void nglBindVertexArray(int var0, long var1);

   public static void glDeleteVertexArrays(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteVertexArrays;
      BufferChecks.checkFunctionAddress(var2);
      StateTracker.deleteVAO(var1, var0);
      BufferChecks.checkDirect(var0);
      nglDeleteVertexArrays(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteVertexArrays(int var0, long var1, long var3);

   public static void glDeleteVertexArrays(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteVertexArrays;
      BufferChecks.checkFunctionAddress(var2);
      StateTracker.deleteVAO(var1, var0);
      nglDeleteVertexArrays(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenVertexArrays(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenVertexArrays;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenVertexArrays(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenVertexArrays(int var0, long var1, long var3);

   public static int glGenVertexArrays() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenVertexArrays;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenVertexArrays(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static boolean glIsVertexArray(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsVertexArray;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsVertexArray(var0, var2);
      return var4;
   }

   static native boolean nglIsVertexArray(int var0, long var1);
}
