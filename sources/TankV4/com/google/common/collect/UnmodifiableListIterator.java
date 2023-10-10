package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@GwtCompatible
public abstract class UnmodifiableListIterator extends UnmodifiableIterator implements ListIterator {
   protected UnmodifiableListIterator() {
   }

   /** @deprecated */
   @Deprecated
   public final void add(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void set(Object var1) {
      throw new UnsupportedOperationException();
   }
}
