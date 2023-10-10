package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryBuilder {
   private String nameFormat = null;
   private Boolean daemon = null;
   private Integer priority = null;
   private UncaughtExceptionHandler uncaughtExceptionHandler = null;
   private ThreadFactory backingThreadFactory = null;

   public ThreadFactoryBuilder setNameFormat(String var1) {
      String.format(var1, 0);
      this.nameFormat = var1;
      return this;
   }

   public ThreadFactoryBuilder setDaemon(boolean var1) {
      this.daemon = var1;
      return this;
   }

   public ThreadFactoryBuilder setPriority(int var1) {
      Preconditions.checkArgument(var1 >= 1, "Thread priority (%s) must be >= %s", var1, 1);
      Preconditions.checkArgument(var1 <= 10, "Thread priority (%s) must be <= %s", var1, 10);
      this.priority = var1;
      return this;
   }

   public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler var1) {
      this.uncaughtExceptionHandler = (UncaughtExceptionHandler)Preconditions.checkNotNull(var1);
      return this;
   }

   public ThreadFactoryBuilder setThreadFactory(ThreadFactory var1) {
      this.backingThreadFactory = (ThreadFactory)Preconditions.checkNotNull(var1);
      return this;
   }

   public ThreadFactory build() {
      return build(this);
   }

   private static ThreadFactory build(ThreadFactoryBuilder var0) {
      String var1 = var0.nameFormat;
      Boolean var2 = var0.daemon;
      Integer var3 = var0.priority;
      UncaughtExceptionHandler var4 = var0.uncaughtExceptionHandler;
      ThreadFactory var5 = var0.backingThreadFactory != null ? var0.backingThreadFactory : Executors.defaultThreadFactory();
      AtomicLong var6 = var1 != null ? new AtomicLong(0L) : null;
      return new ThreadFactory(var5, var1, var6, var2, var3, var4) {
         final ThreadFactory val$backingThreadFactory;
         final String val$nameFormat;
         final AtomicLong val$count;
         final Boolean val$daemon;
         final Integer val$priority;
         final UncaughtExceptionHandler val$uncaughtExceptionHandler;

         {
            this.val$backingThreadFactory = var1;
            this.val$nameFormat = var2;
            this.val$count = var3;
            this.val$daemon = var4;
            this.val$priority = var5;
            this.val$uncaughtExceptionHandler = var6;
         }

         public Thread newThread(Runnable var1) {
            Thread var2 = this.val$backingThreadFactory.newThread(var1);
            if (this.val$nameFormat != null) {
               var2.setName(String.format(this.val$nameFormat, this.val$count.getAndIncrement()));
            }

            if (this.val$daemon != null) {
               var2.setDaemon(this.val$daemon);
            }

            if (this.val$priority != null) {
               var2.setPriority(this.val$priority);
            }

            if (this.val$uncaughtExceptionHandler != null) {
               var2.setUncaughtExceptionHandler(this.val$uncaughtExceptionHandler);
            }

            return var2;
         }
      };
   }
}
