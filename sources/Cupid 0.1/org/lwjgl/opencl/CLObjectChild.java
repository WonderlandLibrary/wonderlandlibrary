package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;

abstract class CLObjectChild<P extends CLObject> extends CLObjectRetainable {
  private final P parent;
  
  protected CLObjectChild(long pointer, P parent) {
    super(pointer);
    if (LWJGLUtil.DEBUG && parent != null && !parent.isValid())
      throw new IllegalStateException("The parent specified is not a valid CL object."); 
    this.parent = parent;
  }
  
  public P getParent() {
    return this.parent;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLObjectChild.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */