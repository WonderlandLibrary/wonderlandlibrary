package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;

public final class NVPresentVideoUtil {
  private static void checkExtension() {
    if (LWJGLUtil.CHECKS && !(GLContext.getCapabilities()).GL_NV_present_video)
      throw new IllegalStateException("NV_present_video is not supported"); 
  }
  
  private static ByteBuffer getPeerInfo() {
    return ContextGL.getCurrentContext().getPeerInfo().getHandle();
  }
  
  public static int glEnumerateVideoDevicesNV(LongBuffer devices) {
    checkExtension();
    if (devices != null)
      BufferChecks.checkBuffer(devices, 1); 
    return nglEnumerateVideoDevicesNV(getPeerInfo(), devices, (devices == null) ? 0 : devices.position());
  }
  
  private static native int nglEnumerateVideoDevicesNV(ByteBuffer paramByteBuffer, LongBuffer paramLongBuffer, int paramInt);
  
  public static boolean glBindVideoDeviceNV(int video_slot, long video_device, IntBuffer attrib_list) {
    checkExtension();
    if (attrib_list != null)
      BufferChecks.checkNullTerminated(attrib_list); 
    return nglBindVideoDeviceNV(getPeerInfo(), video_slot, video_device, attrib_list, (attrib_list == null) ? 0 : attrib_list.position());
  }
  
  private static native boolean nglBindVideoDeviceNV(ByteBuffer paramByteBuffer, int paramInt1, long paramLong, IntBuffer paramIntBuffer, int paramInt2);
  
  public static boolean glQueryContextNV(int attrib, IntBuffer value) {
    checkExtension();
    BufferChecks.checkBuffer(value, 1);
    ContextGL ctx = ContextGL.getCurrentContext();
    return nglQueryContextNV(ctx.getPeerInfo().getHandle(), ctx.getHandle(), attrib, value, value.position());
  }
  
  private static native boolean nglQueryContextNV(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, int paramInt1, IntBuffer paramIntBuffer, int paramInt2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVPresentVideoUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */