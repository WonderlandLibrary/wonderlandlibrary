package org.apache.logging.log4j.core.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DaemonThreadFactory implements ThreadFactory {
   final ThreadGroup group;
   final AtomicInteger threadNumber = new AtomicInteger(1);
   final String threadNamePrefix;

   public DaemonThreadFactory(String var1) {
      this.threadNamePrefix = var1;
      SecurityManager var2 = System.getSecurityManager();
      this.group = var2 != null ? var2.getThreadGroup() : Thread.currentThread().getThreadGroup();
   }

   public Thread newThread(Runnable var1) {
      Thread var2 = new Thread(this.group, var1, this.threadNamePrefix + this.threadNumber.getAndIncrement(), 0L);
      if (!var2.isDaemon()) {
         var2.setDaemon(true);
      }

      if (var2.getPriority() != 5) {
         var2.setPriority(5);
      }

      return var2;
   }
}
