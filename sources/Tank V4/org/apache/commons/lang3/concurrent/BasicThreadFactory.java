package org.apache.commons.lang3.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class BasicThreadFactory implements ThreadFactory {
   private final AtomicLong threadCounter;
   private final ThreadFactory wrappedFactory;
   private final UncaughtExceptionHandler uncaughtExceptionHandler;
   private final String namingPattern;
   private final Integer priority;
   private final Boolean daemonFlag;

   private BasicThreadFactory(BasicThreadFactory.Builder var1) {
      if (BasicThreadFactory.Builder.access$000(var1) == null) {
         this.wrappedFactory = Executors.defaultThreadFactory();
      } else {
         this.wrappedFactory = BasicThreadFactory.Builder.access$000(var1);
      }

      this.namingPattern = BasicThreadFactory.Builder.access$100(var1);
      this.priority = BasicThreadFactory.Builder.access$200(var1);
      this.daemonFlag = BasicThreadFactory.Builder.access$300(var1);
      this.uncaughtExceptionHandler = BasicThreadFactory.Builder.access$400(var1);
      this.threadCounter = new AtomicLong();
   }

   public final ThreadFactory getWrappedFactory() {
      return this.wrappedFactory;
   }

   public final String getNamingPattern() {
      return this.namingPattern;
   }

   public final Boolean getDaemonFlag() {
      return this.daemonFlag;
   }

   public final Integer getPriority() {
      return this.priority;
   }

   public final UncaughtExceptionHandler getUncaughtExceptionHandler() {
      return this.uncaughtExceptionHandler;
   }

   public long getThreadCount() {
      return this.threadCounter.get();
   }

   public Thread newThread(Runnable var1) {
      Thread var2 = this.getWrappedFactory().newThread(var1);
      this.initializeThread(var2);
      return var2;
   }

   private void initializeThread(Thread var1) {
      if (this.getNamingPattern() != null) {
         Long var2 = this.threadCounter.incrementAndGet();
         var1.setName(String.format(this.getNamingPattern(), var2));
      }

      if (this.getUncaughtExceptionHandler() != null) {
         var1.setUncaughtExceptionHandler(this.getUncaughtExceptionHandler());
      }

      if (this.getPriority() != null) {
         var1.setPriority(this.getPriority());
      }

      if (this.getDaemonFlag() != null) {
         var1.setDaemon(this.getDaemonFlag());
      }

   }

   BasicThreadFactory(BasicThreadFactory.Builder var1, Object var2) {
      this(var1);
   }

   public static class Builder implements org.apache.commons.lang3.builder.Builder {
      private ThreadFactory wrappedFactory;
      private UncaughtExceptionHandler exceptionHandler;
      private String namingPattern;
      private Integer priority;
      private Boolean daemonFlag;

      public BasicThreadFactory.Builder wrappedFactory(ThreadFactory var1) {
         if (var1 == null) {
            throw new NullPointerException("Wrapped ThreadFactory must not be null!");
         } else {
            this.wrappedFactory = var1;
            return this;
         }
      }

      public BasicThreadFactory.Builder namingPattern(String var1) {
         if (var1 == null) {
            throw new NullPointerException("Naming pattern must not be null!");
         } else {
            this.namingPattern = var1;
            return this;
         }
      }

      public BasicThreadFactory.Builder daemon(boolean var1) {
         this.daemonFlag = var1;
         return this;
      }

      public BasicThreadFactory.Builder priority(int var1) {
         this.priority = var1;
         return this;
      }

      public BasicThreadFactory.Builder uncaughtExceptionHandler(UncaughtExceptionHandler var1) {
         if (var1 == null) {
            throw new NullPointerException("Uncaught exception handler must not be null!");
         } else {
            this.exceptionHandler = var1;
            return this;
         }
      }

      public void reset() {
         this.wrappedFactory = null;
         this.exceptionHandler = null;
         this.namingPattern = null;
         this.priority = null;
         this.daemonFlag = null;
      }

      public BasicThreadFactory build() {
         BasicThreadFactory var1 = new BasicThreadFactory(this);
         this.reset();
         return var1;
      }

      public Object build() {
         return this.build();
      }

      static ThreadFactory access$000(BasicThreadFactory.Builder var0) {
         return var0.wrappedFactory;
      }

      static String access$100(BasicThreadFactory.Builder var0) {
         return var0.namingPattern;
      }

      static Integer access$200(BasicThreadFactory.Builder var0) {
         return var0.priority;
      }

      static Boolean access$300(BasicThreadFactory.Builder var0) {
         return var0.daemonFlag;
      }

      static UncaughtExceptionHandler access$400(BasicThreadFactory.Builder var0) {
         return var0.exceptionHandler;
      }
   }
}
