package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MoreExecutors {
   private MoreExecutors() {
   }

   @Beta
   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor var0, long var1, TimeUnit var3) {
      return (new MoreExecutors.Application()).getExitingExecutorService(var0, var1, var3);
   }

   @Beta
   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor var0, long var1, TimeUnit var3) {
      return (new MoreExecutors.Application()).getExitingScheduledExecutorService(var0, var1, var3);
   }

   @Beta
   public static void addDelayedShutdownHook(ExecutorService var0, long var1, TimeUnit var3) {
      (new MoreExecutors.Application()).addDelayedShutdownHook(var0, var1, var3);
   }

   @Beta
   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor var0) {
      return (new MoreExecutors.Application()).getExitingExecutorService(var0);
   }

   @Beta
   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor var0) {
      return (new MoreExecutors.Application()).getExitingScheduledExecutorService(var0);
   }

   private static void useDaemonThreadFactory(ThreadPoolExecutor var0) {
      var0.setThreadFactory((new ThreadFactoryBuilder()).setDaemon(true).setThreadFactory(var0.getThreadFactory()).build());
   }

   public static ListeningExecutorService sameThreadExecutor() {
      return new MoreExecutors.SameThreadExecutorService();
   }

   public static ListeningExecutorService listeningDecorator(ExecutorService var0) {
      return (ListeningExecutorService)(var0 instanceof ListeningExecutorService ? (ListeningExecutorService)var0 : (var0 instanceof ScheduledExecutorService ? new MoreExecutors.ScheduledListeningDecorator((ScheduledExecutorService)var0) : new MoreExecutors.ListeningDecorator(var0)));
   }

   public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService var0) {
      return (ListeningScheduledExecutorService)(var0 instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService)var0 : new MoreExecutors.ScheduledListeningDecorator(var0));
   }

   static Object invokeAnyImpl(ListeningExecutorService var0, Collection var1, boolean var2, long var3) throws InterruptedException, ExecutionException, TimeoutException {
      Preconditions.checkNotNull(var0);
      int var5 = var1.size();
      Preconditions.checkArgument(var5 > 0);
      ArrayList var6 = Lists.newArrayListWithCapacity(var5);
      LinkedBlockingQueue var7 = Queues.newLinkedBlockingQueue();
      ExecutionException var8 = null;
      long var9 = var2 ? System.nanoTime() : 0L;
      Iterator var11 = var1.iterator();
      var6.add(submitAndAddQueueListener(var0, (Callable)var11.next(), var7));
      --var5;
      int var12 = 1;

      Object var22;
      while(true) {
         Future var13;
         do {
            var13 = (Future)var7.poll();
            if (var13 == null) {
               if (var5 > 0) {
                  --var5;
                  var6.add(submitAndAddQueueListener(var0, (Callable)var11.next(), var7));
                  ++var12;
               } else {
                  if (var12 == 0) {
                     if (var8 == null) {
                        var8 = new ExecutionException((Throwable)null);
                     }

                     throw var8;
                  }

                  if (var2) {
                     var13 = (Future)var7.poll(var3, TimeUnit.NANOSECONDS);
                     if (var13 == null) {
                        throw new TimeoutException();
                     }

                     long var14 = System.nanoTime();
                     var3 -= var14 - var9;
                     var9 = var14;
                  } else {
                     var13 = (Future)var7.take();
                  }
               }
            }
         } while(var13 == null);

         --var12;

         try {
            var22 = var13.get();
            break;
         } catch (ExecutionException var20) {
            var8 = var20;
         } catch (RuntimeException var21) {
            var8 = new ExecutionException(var21);
         }
      }

      Iterator var15 = var6.iterator();

      while(var15.hasNext()) {
         Future var16 = (Future)var15.next();
         var16.cancel(true);
      }

      return var22;
   }

   private static ListenableFuture submitAndAddQueueListener(ListeningExecutorService var0, Callable var1, BlockingQueue var2) {
      ListenableFuture var3 = var0.submit(var1);
      var3.addListener(new Runnable(var2, var3) {
         final BlockingQueue val$queue;
         final ListenableFuture val$future;

         {
            this.val$queue = var1;
            this.val$future = var2;
         }

         public void run() {
            this.val$queue.add(this.val$future);
         }
      }, sameThreadExecutor());
      return var3;
   }

   @Beta
   public static ThreadFactory platformThreadFactory() {
      // $FF: Couldn't be decompiled
   }

   static Thread newThread(String var0, Runnable var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Thread var2 = platformThreadFactory().newThread(var1);

      try {
         var2.setName(var0);
      } catch (SecurityException var4) {
      }

      return var2;
   }

   static Executor renamingDecorator(Executor param0, Supplier param1) {
      // $FF: Couldn't be decompiled
   }

   static ExecutorService renamingDecorator(ExecutorService param0, Supplier param1) {
      // $FF: Couldn't be decompiled
   }

   static ScheduledExecutorService renamingDecorator(ScheduledExecutorService param0, Supplier param1) {
      // $FF: Couldn't be decompiled
   }

   @Beta
   public static boolean shutdownAndAwaitTermination(ExecutorService var0, long var1, TimeUnit var3) {
      Preconditions.checkNotNull(var3);
      var0.shutdown();

      try {
         long var4 = TimeUnit.NANOSECONDS.convert(var1, var3) / 2L;
         if (!var0.awaitTermination(var4, TimeUnit.NANOSECONDS)) {
            var0.shutdownNow();
            var0.awaitTermination(var4, TimeUnit.NANOSECONDS);
         }
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
         var0.shutdownNow();
      }

      return var0.isTerminated();
   }

   static void access$000(ThreadPoolExecutor var0) {
      useDaemonThreadFactory(var0);
   }

   private static class ScheduledListeningDecorator extends MoreExecutors.ListeningDecorator implements ListeningScheduledExecutorService {
      final ScheduledExecutorService delegate;

      ScheduledListeningDecorator(ScheduledExecutorService var1) {
         super(var1);
         this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(var1);
      }

      public ListenableScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
         ListenableFutureTask var5 = ListenableFutureTask.create(var1, (Object)null);
         ScheduledFuture var6 = this.delegate.schedule(var5, var2, var4);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(var5, var6);
      }

      public ListenableScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
         ListenableFutureTask var5 = ListenableFutureTask.create(var1);
         ScheduledFuture var6 = this.delegate.schedule(var5, var2, var4);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(var5, var6);
      }

      public ListenableScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
         MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask var7 = new MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask(var1);
         ScheduledFuture var8 = this.delegate.scheduleAtFixedRate(var7, var2, var4, var6);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(var7, var8);
      }

      public ListenableScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
         MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask var7 = new MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask(var1);
         ScheduledFuture var8 = this.delegate.scheduleWithFixedDelay(var7, var2, var4, var6);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(var7, var8);
      }

      public ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
         return this.scheduleWithFixedDelay(var1, var2, var4, var6);
      }

      public ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
         return this.scheduleAtFixedRate(var1, var2, var4, var6);
      }

      public ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
         return this.schedule(var1, var2, var4);
      }

      public ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
         return this.schedule(var1, var2, var4);
      }

      private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture implements Runnable {
         private final Runnable delegate;

         public NeverSuccessfulListenableFutureTask(Runnable var1) {
            this.delegate = (Runnable)Preconditions.checkNotNull(var1);
         }

         public void run() {
            try {
               this.delegate.run();
            } catch (Throwable var2) {
               this.setException(var2);
               throw Throwables.propagate(var2);
            }
         }
      }

      private static final class ListenableScheduledTask extends ForwardingListenableFuture.SimpleForwardingListenableFuture implements ListenableScheduledFuture {
         private final ScheduledFuture scheduledDelegate;

         public ListenableScheduledTask(ListenableFuture var1, ScheduledFuture var2) {
            super(var1);
            this.scheduledDelegate = var2;
         }

         public boolean cancel(boolean var1) {
            boolean var2 = super.cancel(var1);
            if (var2) {
               this.scheduledDelegate.cancel(var1);
            }

            return var2;
         }

         public long getDelay(TimeUnit var1) {
            return this.scheduledDelegate.getDelay(var1);
         }

         public int compareTo(Delayed var1) {
            return this.scheduledDelegate.compareTo(var1);
         }

         public int compareTo(Object var1) {
            return this.compareTo((Delayed)var1);
         }
      }
   }

   private static class ListeningDecorator extends AbstractListeningExecutorService {
      private final ExecutorService delegate;

      ListeningDecorator(ExecutorService var1) {
         this.delegate = (ExecutorService)Preconditions.checkNotNull(var1);
      }

      public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
         return this.delegate.awaitTermination(var1, var3);
      }

      public boolean isShutdown() {
         return this.delegate.isShutdown();
      }

      public boolean isTerminated() {
         return this.delegate.isTerminated();
      }

      public void shutdown() {
         this.delegate.shutdown();
      }

      public List shutdownNow() {
         return this.delegate.shutdownNow();
      }

      public void execute(Runnable var1) {
         this.delegate.execute(var1);
      }
   }

   private static class SameThreadExecutorService extends AbstractListeningExecutorService {
      private final Lock lock;
      private final Condition termination;
      private int runningTasks;
      private boolean shutdown;

      private SameThreadExecutorService() {
         this.lock = new ReentrantLock();
         this.termination = this.lock.newCondition();
         this.runningTasks = 0;
         this.shutdown = false;
      }

      public void execute(Runnable var1) {
         this.startTask();
         var1.run();
         this.endTask();
      }

      public boolean isShutdown() {
         this.lock.lock();
         boolean var1 = this.shutdown;
         this.lock.unlock();
         return var1;
      }

      public void shutdown() {
         this.lock.lock();
         this.shutdown = true;
         this.lock.unlock();
      }

      public List shutdownNow() {
         this.shutdown();
         return Collections.emptyList();
      }

      public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
         long var4 = var3.toNanos(var1);
         this.lock.lock();

         boolean var6;
         while(this == false) {
            if (var4 <= 0L) {
               var6 = false;
               this.lock.unlock();
               return var6;
            }

            var4 = this.termination.awaitNanos(var4);
         }

         var6 = true;
         this.lock.unlock();
         return var6;
      }

      private void startTask() {
         this.lock.lock();
         if (this.isShutdown()) {
            throw new RejectedExecutionException("Executor already shutdown");
         } else {
            ++this.runningTasks;
            this.lock.unlock();
         }
      }

      private void endTask() {
         this.lock.lock();
         --this.runningTasks;
         if (this != false) {
            this.termination.signalAll();
         }

         this.lock.unlock();
      }

      SameThreadExecutorService(Object var1) {
         this();
      }
   }

   @VisibleForTesting
   static class Application {
      final ExecutorService getExitingExecutorService(ThreadPoolExecutor var1, long var2, TimeUnit var4) {
         MoreExecutors.access$000(var1);
         ExecutorService var5 = Executors.unconfigurableExecutorService(var1);
         this.addDelayedShutdownHook(var5, var2, var4);
         return var5;
      }

      final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor var1, long var2, TimeUnit var4) {
         MoreExecutors.access$000(var1);
         ScheduledExecutorService var5 = Executors.unconfigurableScheduledExecutorService(var1);
         this.addDelayedShutdownHook(var5, var2, var4);
         return var5;
      }

      final void addDelayedShutdownHook(ExecutorService var1, long var2, TimeUnit var4) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var4);
         this.addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + var1, new Runnable(this, var1, var2, var4) {
            final ExecutorService val$service;
            final long val$terminationTimeout;
            final TimeUnit val$timeUnit;
            final MoreExecutors.Application this$0;

            {
               this.this$0 = var1;
               this.val$service = var2;
               this.val$terminationTimeout = var3;
               this.val$timeUnit = var5;
            }

            public void run() {
               try {
                  this.val$service.shutdown();
                  this.val$service.awaitTermination(this.val$terminationTimeout, this.val$timeUnit);
               } catch (InterruptedException var2) {
               }

            }
         }));
      }

      final ExecutorService getExitingExecutorService(ThreadPoolExecutor var1) {
         return this.getExitingExecutorService(var1, 120L, TimeUnit.SECONDS);
      }

      final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor var1) {
         return this.getExitingScheduledExecutorService(var1, 120L, TimeUnit.SECONDS);
      }

      @VisibleForTesting
      void addShutdownHook(Thread var1) {
         Runtime.getRuntime().addShutdownHook(var1);
      }
   }
}
