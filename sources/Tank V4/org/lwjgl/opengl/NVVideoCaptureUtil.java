package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;

public final class NVVideoCaptureUtil {
   private NVVideoCaptureUtil() {
   }

   private static void checkExtension() {
      if (LWJGLUtil.CHECKS && !GLContext.getCapabilities().GL_NV_video_capture) {
         throw new IllegalStateException("NV_video_capture is not supported");
      }
   }

   private static ByteBuffer getPeerInfo() {
      return ContextGL.getCurrentContext().getPeerInfo().getHandle();
   }

   public static boolean glBindVideoCaptureDeviceNV(int var0, long var1) {
      checkExtension();
      return nglBindVideoCaptureDeviceNV(getPeerInfo(), var0, var1);
   }

   private static native boolean nglBindVideoCaptureDeviceNV(ByteBuffer var0, int var1, long var2);

   public static int glEnumerateVideoCaptureDevicesNV(LongBuffer var0) {
      checkExtension();
      if (var0 != null) {
         BufferChecks.checkBuffer((LongBuffer)var0, 1);
      }

      return nglEnumerateVideoCaptureDevicesNV(getPeerInfo(), var0, var0 == null ? 0 : var0.position());
   }

   private static native int nglEnumerateVideoCaptureDevicesNV(ByteBuffer var0, LongBuffer var1, int var2);

   public static boolean glLockVideoCaptureDeviceNV(long var0) {
      checkExtension();
      return nglLockVideoCaptureDeviceNV(getPeerInfo(), var0);
   }

   private static native boolean nglLockVideoCaptureDeviceNV(ByteBuffer var0, long var1);

   public static boolean glQueryVideoCaptureDeviceNV(long var0, int var2, IntBuffer var3) {
      checkExtension();
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      return nglQueryVideoCaptureDeviceNV(getPeerInfo(), var0, var2, var3, var3.position());
   }

   private static native boolean nglQueryVideoCaptureDeviceNV(ByteBuffer var0, long var1, int var3, IntBuffer var4, int var5);

   public static boolean glReleaseVideoCaptureDeviceNV(long var0) {
      checkExtension();
      return nglReleaseVideoCaptureDeviceNV(getPeerInfo(), var0);
   }

   private static native boolean nglReleaseVideoCaptureDeviceNV(ByteBuffer var0, long var1);
}
