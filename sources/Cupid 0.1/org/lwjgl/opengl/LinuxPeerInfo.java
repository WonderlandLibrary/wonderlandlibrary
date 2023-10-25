package org.lwjgl.opengl;

import java.nio.ByteBuffer;

abstract class LinuxPeerInfo extends PeerInfo {
  LinuxPeerInfo() {
    super(createHandle());
  }
  
  private static native ByteBuffer createHandle();
  
  public final long getDisplay() {
    return nGetDisplay(getHandle());
  }
  
  private static native long nGetDisplay(ByteBuffer paramByteBuffer);
  
  public final long getDrawable() {
    return nGetDrawable(getHandle());
  }
  
  private static native long nGetDrawable(ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\LinuxPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */