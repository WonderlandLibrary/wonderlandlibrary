package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class AbstractSequentialIterator extends UnmodifiableIterator {
   private Object nextOrNull;

   protected AbstractSequentialIterator(@Nullable Object var1) {
      this.nextOrNull = var1;
   }

   protected abstract Object computeNext(Object var1);

   public final Object next() {
      if (this != null) {
         throw new NoSuchElementException();
      } else {
         Object var1 = this.nextOrNull;
         this.nextOrNull = this.computeNext(this.nextOrNull);
         return var1;
      }
   }
}
