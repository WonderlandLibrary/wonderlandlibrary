package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;

public interface Drawable {
  boolean isCurrent() throws LWJGLException;
  
  void makeCurrent() throws LWJGLException;
  
  void releaseContext() throws LWJGLException;
  
  void destroy();
  
  void setCLSharingProperties(PointerBuffer paramPointerBuffer) throws LWJGLException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\Drawable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */