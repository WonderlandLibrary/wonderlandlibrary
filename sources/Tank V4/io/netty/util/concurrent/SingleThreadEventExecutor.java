package io.netty.util.concurrent;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class SingleThreadEventExecutor extends AbstractEventExecutor {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
   private static final int ST_NOT_STARTED = 1;
   private static final int ST_STARTED = 2;
   private static final int ST_SHUTTING_DOWN = 3;
   private static final int ST_SHUTDOWN = 4;
   private static final int ST_TERMINATED = 5;
   private static final Runnable WAKEUP_TASK = new Runnable() {
      public void run() {
      }
   };
   private final EventExecutorGroup parent;
   private final Queue taskQueue;
   final Queue delayedTaskQueue = new PriorityQueue();
   private final Thread thread;
   private final Object stateLock = new Object();
   private final Semaphore threadLock = new Semaphore(0);
   private final Set shutdownHooks = new LinkedHashSet();
   private final boolean addTaskWakesUp;
   private long lastExecutionTime;
   private volatile int state = 1;
   private volatile long gracefulShutdownQuietPeriod;
   private volatile long gracefulShutdownTimeout;
   private long gracefulShutdownStartTime;
   private final Promise terminationFuture;
   private static final long SCHEDULE_PURGE_INTERVAL;
   static final boolean $assertionsDisabled = !SingleThreadEventExecutor.class.desiredAssertionStatus();

   protected SingleThreadEventExecutor(EventExecutorGroup var1, ThreadFactory var2, boolean var3) {
      this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
      if (var2 == null) {
         throw new NullPointerException("threadFactory");
      } else {
         this.parent = var1;
         this.addTaskWakesUp = var3;
         this.thread = var2.newThread(new Runnable(this) {
            final SingleThreadEventExecutor this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               boolean var1 = false;
               this.this$0.updateLastExecutionTime();

               Object var2;
               try {
                  this.this$0.run();
                  var1 = true;
               } catch (Throwable var35) {
                  SingleThreadEventExecutor.access$000().warn("Unexpected exception from an event executor: ", var35);
                  if (SingleThreadEventExecutor.access$100(this.this$0) < 3) {
                     SingleThreadEventExecutor.access$102(this.this$0, 3);
                  }

                  if (var1 && SingleThreadEventExecutor.access$200(this.this$0) == 0L) {
                     SingleThreadEventExecutor.access$000().error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
                  }

                  while(!this.this$0.confirmShutdown()) {
                  }

                  this.this$0.cleanup();
                  synchronized(var2 = SingleThreadEventExecutor.access$300(this.this$0)){}
                  SingleThreadEventExecutor.access$102(this.this$0, 5);
                  SingleThreadEventExecutor.access$400(this.this$0).release();
                  if (!SingleThreadEventExecutor.access$500(this.this$0).isEmpty()) {
                     SingleThreadEventExecutor.access$000().warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.access$500(this.this$0).size() + ')');
                  }

                  SingleThreadEventExecutor.access$600(this.this$0).setSuccess((Object)null);
                  return;
               }

               if (SingleThreadEventExecutor.access$100(this.this$0) < 3) {
                  SingleThreadEventExecutor.access$102(this.this$0, 3);
               }

               if (var1 && SingleThreadEventExecutor.access$200(this.this$0) == 0L) {
                  SingleThreadEventExecutor.access$000().error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
               }

               while(!this.this$0.confirmShutdown()) {
               }

               this.this$0.cleanup();
               synchronized(var2 = SingleThreadEventExecutor.access$300(this.this$0)){}
               SingleThreadEventExecutor.access$102(this.this$0, 5);
               SingleThreadEventExecutor.access$400(this.this$0).release();
               if (!SingleThreadEventExecutor.access$500(this.this$0).isEmpty()) {
                  SingleThreadEventExecutor.access$000().warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.access$500(this.this$0).size() + ')');
               }

               SingleThreadEventExecutor.access$600(this.this$0).setSuccess((Object)null);
            }
         });
         this.taskQueue = this.newTaskQueue();
      }
   }

   protected Queue newTaskQueue() {
      return new LinkedBlockingQueue();
   }

   public EventExecutorGroup parent() {
      return this.parent;
   }

   protected void interruptThread() {
      this.thread.interrupt();
   }

   protected Runnable pollTask() {
      if (!$assertionsDisabled && !this.inEventLoop()) {
         throw new AssertionError();
      } else {
         Runnable var1;
         do {
            var1 = (Runnable)this.taskQueue.poll();
         } while(var1 == WAKEUP_TASK);

         return var1;
      }
   }

   protected Runnable takeTask() {
      if (!$assertionsDisabled && !this.inEventLoop()) {
         throw new AssertionError();
      } else if (!(this.taskQueue instanceof BlockingQueue)) {
         throw new UnsupportedOperationException();
      } else {
         BlockingQueue var1 = (BlockingQueue)this.taskQueue;

         Runnable var5;
         do {
            ScheduledFutureTask var2 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
            if (var2 == null) {
               Runnable var9 = null;

               try {
                  var9 = (Runnable)var1.take();
                  if (var9 == WAKEUP_TASK) {
                     var9 = null;
                  }
               } catch (InterruptedException var7) {
               }

               return var9;
            }

            long var3 = var2.delayNanos();
            var5 = null;
            if (var3 > 0L) {
               try {
                  var5 = (Runnable)var1.poll(var3, TimeUnit.NANOSECONDS);
               } catch (InterruptedException var8) {
                  return null;
               }
            }

            if (var5 == null) {
               this.fetchFromDelayedQueue();
               var5 = (Runnable)var1.poll();
            }
         } while(var5 == null);

         return var5;
      }
   }

   private void fetchFromDelayedQueue() {
      long var1 = 0L;

      while(true) {
         ScheduledFutureTask var3 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
         if (var3 == null) {
            break;
         }

         if (var1 == 0L) {
            var1 = ScheduledFutureTask.nanoTime();
         }

         if (var3.deadlineNanos() > var1) {
            break;
         }

         this.delayedTaskQueue.remove();
         this.taskQueue.add(var3);
      }

   }

   protected Runnable peekTask() {
      if (!$assertionsDisabled && !this.inEventLoop()) {
         throw new AssertionError();
      } else {
         return (Runnable)this.taskQueue.peek();
      }
   }

   protected boolean hasTasks() {
      if (!$assertionsDisabled && !this.inEventLoop()) {
         throw new AssertionError();
      } else {
         return !this.taskQueue.isEmpty();
      }
   }

   public final int pendingTasks() {
      return this.taskQueue.size();
   }

   protected void addTask(Runnable param1) {
      // $FF: Couldn't be decompiled
   }

   protected boolean runAllTasks(long var1) {
      this.fetchFromDelayedQueue();
      Runnable var3 = this.pollTask();
      if (var3 == null) {
         return false;
      } else {
         long var4 = ScheduledFutureTask.nanoTime() + var1;
         long var6 = 0L;

         long var8;
         while(true) {
            try {
               var3.run();
            } catch (Throwable var11) {
               logger.warn("A task raised an exception.", var11);
            }

            ++var6;
            if ((var6 & 63L) == 0L) {
               var8 = ScheduledFutureTask.nanoTime();
               if (var8 >= var4) {
                  break;
               }
            }

            var3 = this.pollTask();
            if (var3 == null) {
               var8 = ScheduledFutureTask.nanoTime();
               break;
            }
         }

         this.lastExecutionTime = var8;
         return true;
      }
   }

   protected long delayNanos(long var1) {
      ScheduledFutureTask var3 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
      return var3 == null ? SCHEDULE_PURGE_INTERVAL : var3.delayNanos(var1);
   }

   protected void updateLastExecutionTime() {
      this.lastExecutionTime = ScheduledFutureTask.nanoTime();
   }

   protected abstract void run();

   protected void cleanup() {
   }

   protected void wakeup(boolean var1) {
      if (!var1 || this.state == 3) {
         this.taskQueue.add(WAKEUP_TASK);
      }

   }

   public boolean inEventLoop(Thread var1) {
      return var1 == this.thread;
   }

   public void addShutdownHook(Runnable var1) {
      if (this.inEventLoop()) {
         this.shutdownHooks.add(var1);
      } else {
         this.execute(new Runnable(this, var1) {
            final Runnable val$task;
            final SingleThreadEventExecutor this$0;

            {
               this.this$0 = var1;
               this.val$task = var2;
            }

            public void run() {
               SingleThreadEventExecutor.access$700(this.this$0).add(this.val$task);
            }
         });
      }

   }

   public void removeShutdownHook(Runnable var1) {
      if (this.inEventLoop()) {
         this.shutdownHooks.remove(var1);
      } else {
         this.execute(new Runnable(this, var1) {
            final Runnable val$task;
            final SingleThreadEventExecutor this$0;

            {
               this.this$0 = var1;
               this.val$task = var2;
            }

            public void run() {
               SingleThreadEventExecutor.access$700(this.this$0).remove(this.val$task);
            }
         });
      }

   }

   public Future shutdownGracefully(long param1, long param3, TimeUnit param5) {
      // $FF: Couldn't be decompiled
   }

   public Future terminationFuture() {
      return this.terminationFuture;
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      // $FF: Couldn't be decompiled
   }

   public boolean isTerminated() {
      return this.state == 5;
   }

   protected boolean confirmShutdown() {
      // $FF: Couldn't be decompiled
   }

   private void cancelDelayedTasks() {
      if (!this.delayedTaskQueue.isEmpty()) {
         ScheduledFutureTask[] var1 = (ScheduledFutureTask[])this.delayedTaskQueue.toArray(new ScheduledFutureTask[this.delayedTaskQueue.size()]);
         ScheduledFutureTask[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ScheduledFutureTask var5 = var2[var4];
            var5.cancel(false);
         }

         this.delayedTaskQueue.clear();
      }
   }

   public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      if (var3 == null) {
         throw new NullPointerException("unit");
      } else if (this.inEventLoop()) {
         throw new IllegalStateException("cannot await termination of the current thread");
      } else {
         if (this.threadLock.tryAcquire(var1, var3)) {
            this.threadLock.release();
         }

         return this.isTerminated();
      }
   }

   public void execute(Runnable param1) {
      // $FF: Couldn't be decompiled
   }

   protected static void reject() {
      throw new RejectedExecutionException("event executor terminated");
   }

   public ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      if (var1 == null) {
         throw new NullPointerException("command");
      } else if (var4 == null) {
         throw new NullPointerException("unit");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", var2));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, var1, (Object)null, ScheduledFutureTask.deadlineNanos(var4.toNanos(var2))));
      }
   }

   public ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      if (var1 == null) {
         throw new NullPointerException("callable");
      } else if (var4 == null) {
         throw new NullPointerException("unit");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", var2));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, var1, ScheduledFutureTask.deadlineNanos(var4.toNanos(var2))));
      }
   }

   public ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      if (var1 == null) {
         throw new NullPointerException("command");
      } else if (var6 == null) {
         throw new NullPointerException("unit");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", var2));
      } else if (var4 <= 0L) {
         throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", var4));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(var1, (Object)null), ScheduledFutureTask.deadlineNanos(var6.toNanos(var2)), var6.toNanos(var4)));
      }
   }

   public ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      if (var1 == null) {
         throw new NullPointerException("command");
      } else if (var6 == null) {
         throw new NullPointerException("unit");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", var2));
      } else if (var4 <= 0L) {
         throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", var4));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(var1, (Object)null), ScheduledFutureTask.deadlineNanos(var6.toNanos(var2)), -var6.toNanos(var4)));
      }
   }

   private ScheduledFuture schedule(ScheduledFutureTask var1) {
      if (var1 == null) {
         throw new NullPointerException("task");
      } else {
         if (this.inEventLoop()) {
            this.delayedTaskQueue.add(var1);
         } else {
            this.execute(new Runnable(this, var1) {
               final ScheduledFutureTask val$task;
               final SingleThreadEventExecutor this$0;

               {
                  this.this$0 = var1;
                  this.val$task = var2;
               }

               public void run() {
                  this.this$0.delayedTaskQueue.add(this.val$task);
               }
            });
         }

         return var1;
      }
   }

   private void startThread() {
      Object var1;
      synchronized(var1 = this.stateLock){}
      if (this.state == 1) {
         this.state = 2;
         this.delayedTaskQueue.add(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(new SingleThreadEventExecutor.PurgeTask(this), (Object)null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL));
         this.thread.start();
      }

   }

   public java.util.concurrent.ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleWithFixedDelay(var1, var2, var4, var6);
   }

   public java.util.concurrent.ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleAtFixedRate(var1, var2, var4, var6);
   }

   public java.util.concurrent.ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   public java.util.concurrent.ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   static InternalLogger access$000() {
      return logger;
   }

   static int access$100(SingleThreadEventExecutor var0) {
      return var0.state;
   }

   static int access$102(SingleThreadEventExecutor var0, int var1) {
      return var0.state = var1;
   }

   static long access$200(SingleThreadEventExecutor var0) {
      return var0.gracefulShutdownStartTime;
   }

   static Object access$300(SingleThreadEventExecutor var0) {
      return var0.stateLock;
   }

   static Semaphore access$400(SingleThreadEventExecutor var0) {
      return var0.threadLock;
   }

   static Queue access$500(SingleThreadEventExecutor var0) {
      return var0.taskQueue;
   }

   static Promise access$600(SingleThreadEventExecutor var0) {
      return var0.terminationFuture;
   }

   static Set access$700(SingleThreadEventExecutor var0) {
      return var0.shutdownHooks;
   }

   static {
      SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
   }

   private final class PurgeTask implements Runnable {
      final SingleThreadEventExecutor this$0;

      private PurgeTask(SingleThreadEventExecutor var1) {
         this.this$0 = var1;
      }

      public void run() {
         Iterator var1 = this.this$0.delayedTaskQueue.iterator();

         while(var1.hasNext()) {
            ScheduledFutureTask var2 = (ScheduledFutureTask)var1.next();
            if (var2.isCancelled()) {
               var1.remove();
            }
         }

      }

      PurgeTask(SingleThreadEventExecutor var1, Object var2) {
         this(var1);
      }
   }
}
