package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class CallableBackgroundInitializer extends BackgroundInitializer {
   private final Callable callable;

   public CallableBackgroundInitializer(Callable var1) {
      this.checkCallable(var1);
      this.callable = var1;
   }

   public CallableBackgroundInitializer(Callable var1, ExecutorService var2) {
      super(var2);
      this.checkCallable(var1);
      this.callable = var1;
   }

   protected Object initialize() throws Exception {
      return this.callable.call();
   }

   private void checkCallable(Callable var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Callable must not be null!");
      }
   }
}
