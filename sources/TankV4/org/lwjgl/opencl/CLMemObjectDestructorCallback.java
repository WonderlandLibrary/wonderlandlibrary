package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLMemObjectDestructorCallback extends PointerWrapperAbstract {
   protected CLMemObjectDestructorCallback() {
      super(CallbackUtil.getMemObjectDestructorCallback());
   }

   protected abstract void handleMessage(long var1);
}
