package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVVideoCapture {
   public static final int GL_VIDEO_BUFFER_NV = 36896;
   public static final int GL_VIDEO_BUFFER_BINDING_NV = 36897;
   public static final int GL_FIELD_UPPER_NV = 36898;
   public static final int GL_FIELD_LOWER_NV = 36899;
   public static final int GL_NUM_VIDEO_CAPTURE_STREAMS_NV = 36900;
   public static final int GL_NEXT_VIDEO_CAPTURE_BUFFER_STATUS_NV = 36901;
   public static final int GL_LAST_VIDEO_CAPTURE_STATUS_NV = 36903;
   public static final int GL_VIDEO_BUFFER_PITCH_NV = 36904;
   public static final int GL_VIDEO_CAPTURE_FRAME_WIDTH_NV = 36920;
   public static final int GL_VIDEO_CAPTURE_FRAME_HEIGHT_NV = 36921;
   public static final int GL_VIDEO_CAPTURE_FIELD_UPPER_HEIGHT_NV = 36922;
   public static final int GL_VIDEO_CAPTURE_FIELD_LOWER_HEIGHT_NV = 36923;
   public static final int GL_VIDEO_CAPTURE_TO_422_SUPPORTED_NV = 36902;
   public static final int GL_VIDEO_COLOR_CONVERSION_MATRIX_NV = 36905;
   public static final int GL_VIDEO_COLOR_CONVERSION_MAX_NV = 36906;
   public static final int GL_VIDEO_COLOR_CONVERSION_MIN_NV = 36907;
   public static final int GL_VIDEO_COLOR_CONVERSION_OFFSET_NV = 36908;
   public static final int GL_VIDEO_BUFFER_INTERNAL_FORMAT_NV = 36909;
   public static final int GL_VIDEO_CAPTURE_SURFACE_ORIGIN_NV = 36924;
   public static final int GL_PARTIAL_SUCCESS_NV = 36910;
   public static final int GL_SUCCESS_NV = 36911;
   public static final int GL_FAILURE_NV = 36912;
   public static final int GL_YCBYCR8_422_NV = 36913;
   public static final int GL_YCBAYCR8A_4224_NV = 36914;
   public static final int GL_Z6Y10Z6CB10Z6Y10Z6CR10_422_NV = 36915;
   public static final int GL_Z6Y10Z6CB10Z6A10Z6Y10Z6CR10Z6A10_4224_NV = 36916;
   public static final int GL_Z4Y12Z4CB12Z4Y12Z4CR12_422_NV = 36917;
   public static final int GL_Z4Y12Z4CB12Z4A12Z4Y12Z4CR12Z4A12_4224_NV = 36918;
   public static final int GL_Z4Y12Z4CB12Z4CR12_444_NV = 36919;
   public static final int GL_NUM_VIDEO_CAPTURE_SLOTS_NV = 8399;
   public static final int GL_UNIQUE_ID_NV = 8398;

   private NVVideoCapture() {
   }

   public static void glBeginVideoCaptureNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBeginVideoCaptureNV;
      BufferChecks.checkFunctionAddress(var2);
      nglBeginVideoCaptureNV(var0, var2);
   }

   static native void nglBeginVideoCaptureNV(int var0, long var1);

   public static void glBindVideoCaptureStreamBufferNV(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBindVideoCaptureStreamBufferNV;
      BufferChecks.checkFunctionAddress(var6);
      nglBindVideoCaptureStreamBufferNV(var0, var1, var2, var3, var6);
   }

   static native void nglBindVideoCaptureStreamBufferNV(int var0, int var1, int var2, long var3, long var5);

   public static void glBindVideoCaptureStreamTextureNV(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBindVideoCaptureStreamTextureNV;
      BufferChecks.checkFunctionAddress(var6);
      nglBindVideoCaptureStreamTextureNV(var0, var1, var2, var3, var4, var6);
   }

   static native void nglBindVideoCaptureStreamTextureNV(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glEndVideoCaptureNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEndVideoCaptureNV;
      BufferChecks.checkFunctionAddress(var2);
      nglEndVideoCaptureNV(var0, var2);
   }

   static native void nglEndVideoCaptureNV(int var0, long var1);

   public static void glGetVideoCaptureNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideoCaptureivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetVideoCaptureivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVideoCaptureivNV(int var0, int var1, long var2, long var4);

   public static int glGetVideoCaptureiNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVideoCaptureivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetVideoCaptureivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetVideoCaptureStreamNV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVideoCaptureStreamivNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglGetVideoCaptureStreamivNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetVideoCaptureStreamivNV(int var0, int var1, int var2, long var3, long var5);

   public static int glGetVideoCaptureStreamiNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideoCaptureStreamivNV;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetVideoCaptureStreamivNV(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetVideoCaptureStreamNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVideoCaptureStreamfvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 1);
      nglGetVideoCaptureStreamfvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetVideoCaptureStreamfvNV(int var0, int var1, int var2, long var3, long var5);

   public static float glGetVideoCaptureStreamfNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideoCaptureStreamfvNV;
      BufferChecks.checkFunctionAddress(var4);
      FloatBuffer var6 = APIUtil.getBufferFloat(var3);
      nglGetVideoCaptureStreamfvNV(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetVideoCaptureStreamNV(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVideoCaptureStreamdvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 1);
      nglGetVideoCaptureStreamdvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetVideoCaptureStreamdvNV(int var0, int var1, int var2, long var3, long var5);

   public static double glGetVideoCaptureStreamdNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideoCaptureStreamdvNV;
      BufferChecks.checkFunctionAddress(var4);
      DoubleBuffer var6 = APIUtil.getBufferDouble(var3);
      nglGetVideoCaptureStreamdvNV(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static int glVideoCaptureNV(int var0, IntBuffer var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVideoCaptureNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      int var6 = nglVideoCaptureNV(var0, MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var4);
      return var6;
   }

   static native int nglVideoCaptureNV(int var0, long var1, long var3, long var5);

   public static void glVideoCaptureStreamParameterNV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVideoCaptureStreamParameterivNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 16);
      nglVideoCaptureStreamParameterivNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVideoCaptureStreamParameterivNV(int var0, int var1, int var2, long var3, long var5);

   public static void glVideoCaptureStreamParameterNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVideoCaptureStreamParameterfvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 16);
      nglVideoCaptureStreamParameterfvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVideoCaptureStreamParameterfvNV(int var0, int var1, int var2, long var3, long var5);

   public static void glVideoCaptureStreamParameterNV(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVideoCaptureStreamParameterdvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 16);
      nglVideoCaptureStreamParameterdvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVideoCaptureStreamParameterdvNV(int var0, int var1, int var2, long var3, long var5);
}
