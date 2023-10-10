package io.netty.util.concurrent;

import java.util.concurrent.Executor;

public final class ImmediateExecutor implements Executor {
   public static final ImmediateExecutor INSTANCE = new ImmediateExecutor();

   private ImmediateExecutor() {
   }

   public void execute(Runnable var1) {
      if (var1 == null) {
         throw new NullPointerException("command");
      } else {
         var1.run();
      }
   }
}
