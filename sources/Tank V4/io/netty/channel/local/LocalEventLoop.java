package io.netty.channel.local;

import io.netty.channel.SingleThreadEventLoop;
import java.util.concurrent.ThreadFactory;

final class LocalEventLoop extends SingleThreadEventLoop {
   LocalEventLoop(LocalEventLoopGroup var1, ThreadFactory var2) {
      super(var1, var2, true);
   }

   protected void run() {
      do {
         Runnable var1 = this.takeTask();
         if (var1 != null) {
            var1.run();
            this.updateLastExecutionTime();
         }
      } while(!this.confirmShutdown());

   }
}
