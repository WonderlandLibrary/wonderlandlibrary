package com.sun.javafx.runtime.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BackgroundExecutor {
   private static ExecutorService instance;
   private static ScheduledExecutorService timerInstance;

   private BackgroundExecutor() {
   }

   public static synchronized ExecutorService getExecutor() {
      if (instance == null) {
         instance = Executors.newCachedThreadPool((var0) -> {
            Thread var1 = new Thread(var0);
            var1.setPriority(1);
            return var1;
         });
         ((ThreadPoolExecutor)instance).setKeepAliveTime(1L, TimeUnit.SECONDS);
      }

      return instance;
   }

   public static synchronized ScheduledExecutorService getTimer() {
      if (timerInstance == null) {
         timerInstance = new ScheduledThreadPoolExecutor(1, (var0) -> {
            Thread var1 = new Thread(var0);
            var1.setDaemon(true);
            return var1;
         });
      }

      return timerInstance;
   }

   private static synchronized void shutdown() {
      if (instance != null) {
         instance.shutdown();
         instance = null;
      }

      if (timerInstance != null) {
         timerInstance.shutdown();
         timerInstance = null;
      }

   }
}
