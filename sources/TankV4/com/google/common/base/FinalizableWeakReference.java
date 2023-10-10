package com.google.common.base;

import java.lang.ref.WeakReference;

public abstract class FinalizableWeakReference extends WeakReference implements FinalizableReference {
   protected FinalizableWeakReference(Object var1, FinalizableReferenceQueue var2) {
      super(var1, var2.queue);
      var2.cleanUp();
   }
}
