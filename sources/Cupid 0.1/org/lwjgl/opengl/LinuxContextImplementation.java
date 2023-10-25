package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

final class LinuxContextImplementation implements ContextImplementation {
  public ByteBuffer create(PeerInfo peer_info, IntBuffer attribs, ByteBuffer shared_context_handle) throws LWJGLException {
    LinuxDisplay.lockAWT();
    try {
      ByteBuffer peer_handle = peer_info.lockAndGetHandle();
    } finally {
      LinuxDisplay.unlockAWT();
    } 
  }
  
  private static native ByteBuffer nCreate(ByteBuffer paramByteBuffer1, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer2) throws LWJGLException;
  
  native long getGLXContext(ByteBuffer paramByteBuffer);
  
  native long getDisplay(ByteBuffer paramByteBuffer);
  
  public void releaseDrawable(ByteBuffer context_handle) throws LWJGLException {}
  
  public void swapBuffers() throws LWJGLException {
    ContextGL current_context = ContextGL.getCurrentContext();
    if (current_context == null)
      throw new IllegalStateException("No context is current"); 
    synchronized (current_context) {
      PeerInfo current_peer_info = current_context.getPeerInfo();
      LinuxDisplay.lockAWT();
      try {
        ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
        try {
          nSwapBuffers(peer_handle);
        } finally {
          current_peer_info.unlock();
        } 
      } finally {
        LinuxDisplay.unlockAWT();
      } 
    } 
  }
  
  private static native void nSwapBuffers(ByteBuffer paramByteBuffer) throws LWJGLException;
  
  public void releaseCurrentContext() throws LWJGLException {
    ContextGL current_context = ContextGL.getCurrentContext();
    if (current_context == null)
      throw new IllegalStateException("No context is current"); 
    synchronized (current_context) {
      PeerInfo current_peer_info = current_context.getPeerInfo();
      LinuxDisplay.lockAWT();
      try {
        ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
        try {
          nReleaseCurrentContext(peer_handle);
        } finally {
          current_peer_info.unlock();
        } 
      } finally {
        LinuxDisplay.unlockAWT();
      } 
    } 
  }
  
  private static native void nReleaseCurrentContext(ByteBuffer paramByteBuffer) throws LWJGLException;
  
  public void update(ByteBuffer context_handle) {}
  
  public void makeCurrent(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
    LinuxDisplay.lockAWT();
    try {
      ByteBuffer peer_handle = peer_info.lockAndGetHandle();
      try {
        nMakeCurrent(peer_handle, handle);
      } finally {
        peer_info.unlock();
      } 
    } finally {
      LinuxDisplay.unlockAWT();
    } 
  }
  
  private static native void nMakeCurrent(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) throws LWJGLException;
  
  public boolean isCurrent(ByteBuffer handle) throws LWJGLException {
    LinuxDisplay.lockAWT();
    try {
      boolean result = nIsCurrent(handle);
      return result;
    } finally {
      LinuxDisplay.unlockAWT();
    } 
  }
  
  private static native boolean nIsCurrent(ByteBuffer paramByteBuffer) throws LWJGLException;
  
  public void setSwapInterval(int value) {
    ContextGL current_context = ContextGL.getCurrentContext();
    PeerInfo peer_info = current_context.getPeerInfo();
    if (current_context == null)
      throw new IllegalStateException("No context is current"); 
    synchronized (current_context) {
      LinuxDisplay.lockAWT();
      try {
        ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
          nSetSwapInterval(peer_handle, current_context.getHandle(), value);
        } finally {
          peer_info.unlock();
        } 
      } catch (LWJGLException e) {
        e.printStackTrace();
      } finally {
        LinuxDisplay.unlockAWT();
      } 
    } 
  }
  
  private static native void nSetSwapInterval(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, int paramInt);
  
  public void destroy(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
    LinuxDisplay.lockAWT();
    try {
      ByteBuffer peer_handle = peer_info.lockAndGetHandle();
      try {
        nDestroy(peer_handle, handle);
      } finally {
        peer_info.unlock();
      } 
    } finally {
      LinuxDisplay.unlockAWT();
    } 
  }
  
  private static native void nDestroy(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) throws LWJGLException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\LinuxContextImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */