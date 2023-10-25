package com.google.common.base;

import java.lang.ref.WeakReference;

public abstract class FinalizableWeakReference<T> extends WeakReference<T> implements FinalizableReference {
  protected FinalizableWeakReference(T referent, FinalizableReferenceQueue queue) {
    super(referent, queue.queue);
    queue.cleanUp();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\FinalizableWeakReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */