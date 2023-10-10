package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicInitializer implements ConcurrentInitializer {
   private final AtomicReference reference = new AtomicReference();

   public Object get() throws ConcurrentException {
      Object var1 = this.reference.get();
      if (var1 == null) {
         var1 = this.initialize();
         if (!this.reference.compareAndSet((Object)null, var1)) {
            var1 = this.reference.get();
         }
      }

      return var1;
   }

   protected abstract Object initialize() throws ConcurrentException;
}
