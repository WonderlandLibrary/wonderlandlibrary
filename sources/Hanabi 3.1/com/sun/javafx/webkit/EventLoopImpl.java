package com.sun.javafx.webkit;

import com.sun.javafx.tk.Toolkit;
import com.sun.webkit.EventLoop;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

public final class EventLoopImpl extends EventLoop {
   private static final long DELAY = 20L;
   private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

   protected void cycle() {
      Object var1 = new Object();
      executor.schedule(() -> {
         Platform.runLater(new Runnable() {
            public void run() {
               Toolkit.getToolkit().exitNestedEventLoop(var1, (Object)null);
            }
         });
      }, 20L, TimeUnit.MILLISECONDS);
      Toolkit.getToolkit().enterNestedEventLoop(var1);
   }
}
