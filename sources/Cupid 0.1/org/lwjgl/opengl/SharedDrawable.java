package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;

public final class SharedDrawable extends DrawableGL {
  public SharedDrawable(Drawable drawable) throws LWJGLException {
    this.context = (ContextGL)((DrawableLWJGL)drawable).createSharedContext();
  }
  
  public ContextGL createSharedContext() {
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\SharedDrawable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */