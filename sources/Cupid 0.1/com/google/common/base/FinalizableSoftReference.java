package com.google.common.base;

import java.lang.ref.SoftReference;

public abstract class FinalizableSoftReference<T> extends SoftReference<T> implements FinalizableReference {
  protected FinalizableSoftReference(T referent, FinalizableReferenceQueue queue) {
    super(referent, queue.queue);
    queue.cleanUp();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\FinalizableSoftReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */