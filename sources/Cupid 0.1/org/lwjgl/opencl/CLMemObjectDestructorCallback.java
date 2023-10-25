package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLMemObjectDestructorCallback extends PointerWrapperAbstract {
  protected CLMemObjectDestructorCallback() {
    super(CallbackUtil.getMemObjectDestructorCallback());
  }
  
  protected abstract void handleMessage(long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLMemObjectDestructorCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */