package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

public interface InputImplementation {
  boolean hasWheel();
  
  int getButtonCount();
  
  void createMouse() throws LWJGLException;
  
  void destroyMouse();
  
  void pollMouse(IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer);
  
  void readMouse(ByteBuffer paramByteBuffer);
  
  void grabMouse(boolean paramBoolean);
  
  int getNativeCursorCapabilities();
  
  void setCursorPosition(int paramInt1, int paramInt2);
  
  void setNativeCursor(Object paramObject) throws LWJGLException;
  
  int getMinCursorSize();
  
  int getMaxCursorSize();
  
  void createKeyboard() throws LWJGLException;
  
  void destroyKeyboard();
  
  void pollKeyboard(ByteBuffer paramByteBuffer);
  
  void readKeyboard(ByteBuffer paramByteBuffer);
  
  Object createCursor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2) throws LWJGLException;
  
  void destroyCursor(Object paramObject);
  
  int getWidth();
  
  int getHeight();
  
  boolean isInsideWindow();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\InputImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */