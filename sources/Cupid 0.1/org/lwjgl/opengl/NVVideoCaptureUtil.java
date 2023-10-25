package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;

public final class NVVideoCaptureUtil {
  private static void checkExtension() {
    if (LWJGLUtil.CHECKS && !(GLContext.getCapabilities()).GL_NV_video_capture)
      throw new IllegalStateException("NV_video_capture is not supported"); 
  }
  
  private static ByteBuffer getPeerInfo() {
    return ContextGL.getCurrentContext().getPeerInfo().getHandle();
  }
  
  public static boolean glBindVideoCaptureDeviceNV(int video_slot, long device) {
    checkExtension();
    return nglBindVideoCaptureDeviceNV(getPeerInfo(), video_slot, device);
  }
  
  private static native boolean nglBindVideoCaptureDeviceNV(ByteBuffer paramByteBuffer, int paramInt, long paramLong);
  
  public static int glEnumerateVideoCaptureDevicesNV(LongBuffer devices) {
    checkExtension();
    if (devices != null)
      BufferChecks.checkBuffer(devices, 1); 
    return nglEnumerateVideoCaptureDevicesNV(getPeerInfo(), devices, (devices == null) ? 0 : devices.position());
  }
  
  private static native int nglEnumerateVideoCaptureDevicesNV(ByteBuffer paramByteBuffer, LongBuffer paramLongBuffer, int paramInt);
  
  public static boolean glLockVideoCaptureDeviceNV(long device) {
    checkExtension();
    return nglLockVideoCaptureDeviceNV(getPeerInfo(), device);
  }
  
  private static native boolean nglLockVideoCaptureDeviceNV(ByteBuffer paramByteBuffer, long paramLong);
  
  public static boolean glQueryVideoCaptureDeviceNV(long device, int attribute, IntBuffer value) {
    checkExtension();
    BufferChecks.checkBuffer(value, 1);
    return nglQueryVideoCaptureDeviceNV(getPeerInfo(), device, attribute, value, value.position());
  }
  
  private static native boolean nglQueryVideoCaptureDeviceNV(ByteBuffer paramByteBuffer, long paramLong, int paramInt1, IntBuffer paramIntBuffer, int paramInt2);
  
  public static boolean glReleaseVideoCaptureDeviceNV(long device) {
    checkExtension();
    return nglReleaseVideoCaptureDeviceNV(getPeerInfo(), device);
  }
  
  private static native boolean nglReleaseVideoCaptureDeviceNV(ByteBuffer paramByteBuffer, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVVideoCaptureUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */