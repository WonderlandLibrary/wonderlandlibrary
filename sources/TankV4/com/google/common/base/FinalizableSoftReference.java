package com.google.common.base;

import java.lang.ref.SoftReference;

public abstract class FinalizableSoftReference extends SoftReference implements FinalizableReference {
   protected FinalizableSoftReference(Object var1, FinalizableReferenceQueue var2) {
      super(var1, var2.queue);
      var2.cleanUp();
   }
}
