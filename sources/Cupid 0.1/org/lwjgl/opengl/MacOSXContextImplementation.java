package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

final class MacOSXContextImplementation implements ContextImplementation {
  public ByteBuffer create(PeerInfo peer_info, IntBuffer attribs, ByteBuffer shared_context_handle) throws LWJGLException {
    ByteBuffer peer_handle = peer_info.lockAndGetHandle();
    try {
      return nCreate(peer_handle, shared_context_handle);
    } finally {
      peer_info.unlock();
    } 
  }
  
  private static native ByteBuffer nCreate(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) throws LWJGLException;
  
  public void swapBuffers() throws LWJGLException {
    ContextGL current_context = ContextGL.getCurrentContext();
    if (current_context == null)
      throw new IllegalStateException("No context is current"); 
    synchronized (current_context) {
      nSwapBuffers(current_context.getHandle());
    } 
  }
  
  native long getCGLShareGroup(ByteBuffer paramByteBuffer);
  
  private static native void nSwapBuffers(ByteBuffer paramByteBuffer) throws LWJGLException;
  
  public void update(ByteBuffer context_handle) {
    nUpdate(context_handle);
  }
  
  private static native void nUpdate(ByteBuffer paramByteBuffer);
  
  public void releaseCurrentContext() throws LWJGLException {
    nReleaseCurrentContext();
  }
  
  private static native void nReleaseCurrentContext() throws LWJGLException;
  
  public void releaseDrawable(ByteBuffer context_handle) throws LWJGLException {
    clearDrawable(context_handle);
  }
  
  private static native void clearDrawable(ByteBuffer paramByteBuffer) throws LWJGLException;
  
  static void resetView(PeerInfo peer_info, ContextGL context) throws LWJGLException {
    ByteBuffer peer_handle = peer_info.lockAndGetHandle();
    try {
      synchronized (context) {
        clearDrawable(context.getHandle());
        setView(peer_handle, context.getHandle());
      } 
    } finally {
      peer_info.unlock();
    } 
  }
  
  public void makeCurrent(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
    ByteBuffer peer_handle = peer_info.lockAndGetHandle();
    try {
      setView(peer_handle, handle);
      nMakeCurrent(handle);
    } finally {
      peer_info.unlock();
    } 
  }
  
  private static native void setView(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) throws LWJGLException;
  
  private static native void nMakeCurrent(ByteBuffer paramByteBuffer) throws LWJGLException;
  
  public boolean isCurrent(ByteBuffer handle) throws LWJGLException {
    boolean result = nIsCurrent(handle);
    return result;
  }
  
  private static native boolean nIsCurrent(ByteBuffer paramByteBuffer) throws LWJGLException;
  
  public void setSwapInterval(int value) {
    ContextGL current_context = ContextGL.getCurrentContext();
    synchronized (current_context) {
      nSetSwapInterval(current_context.getHandle(), value);
    } 
  }
  
  private static native void nSetSwapInterval(ByteBuffer paramByteBuffer, int paramInt);
  
  public void destroy(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
    nDestroy(handle);
  }
  
  private static native void nDestroy(ByteBuffer paramByteBuffer) throws LWJGLException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\MacOSXContextImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */