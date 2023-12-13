package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;

public final class NVPresentVideoUtil {
   private NVPresentVideoUtil() {
   }

   private static void checkExtension() {
      if (LWJGLUtil.CHECKS && !GLContext.getCapabilities().GL_NV_present_video) {
         throw new IllegalStateException("NV_present_video is not supported");
      }
   }

   private static ByteBuffer getPeerInfo() {
      return ContextGL.getCurrentContext().getPeerInfo().getHandle();
   }

   public static int glEnumerateVideoDevicesNV(LongBuffer var0) {
      checkExtension();
      if (var0 != null) {
         BufferChecks.checkBuffer((LongBuffer)var0, 1);
      }

      return nglEnumerateVideoDevicesNV(getPeerInfo(), var0, var0 == null ? 0 : var0.position());
   }

   private static native int nglEnumerateVideoDevicesNV(ByteBuffer var0, LongBuffer var1, int var2);

   public static boolean glBindVideoDeviceNV(int var0, long var1, IntBuffer var3) {
      checkExtension();
      if (var3 != null) {
         BufferChecks.checkNullTerminated(var3);
      }

      return nglBindVideoDeviceNV(getPeerInfo(), var0, var1, var3, var3 == null ? 0 : var3.position());
   }

   private static native boolean nglBindVideoDeviceNV(ByteBuffer var0, int var1, long var2, IntBuffer var4, int var5);

   public static boolean glQueryContextNV(int var0, IntBuffer var1) {
      checkExtension();
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      ContextGL var2 = ContextGL.getCurrentContext();
      return nglQueryContextNV(var2.getPeerInfo().getHandle(), var2.getHandle(), var0, var1, var1.position());
   }

   private static native boolean nglQueryContextNV(ByteBuffer var0, ByteBuffer var1, int var2, IntBuffer var3, int var4);
}
