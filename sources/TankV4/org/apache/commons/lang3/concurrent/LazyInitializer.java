package org.apache.commons.lang3.concurrent;

public abstract class LazyInitializer implements ConcurrentInitializer {
   private volatile Object object;

   public Object get() throws ConcurrentException {
      Object var1 = this.object;
      if (var1 == null) {
         synchronized(this){}
         var1 = this.object;
         if (var1 == null) {
            this.object = var1 = this.initialize();
         }
      }

      return var1;
   }

   protected abstract Object initialize() throws ConcurrentException;
}
