package io.netty.util.concurrent;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class GlobalEventExecutor extends AbstractEventExecutor {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
   private static final int ST_NOT_STARTED = 1;
   private static final int ST_STARTED = 2;
   private static final long SCHEDULE_PURGE_INTERVAL;
   public static final GlobalEventExecutor INSTANCE;
   final Queue taskQueue = new LinkedBlockingQueue();
   final Queue delayedTaskQueue = new PriorityQueue();
   final ScheduledFutureTask purgeTask;
   private final ThreadFactory threadFactory;
   private final GlobalEventExecutor.TaskRunner taskRunner;
   private final Object stateLock;
   volatile Thread thread;
   private volatile int state;
   private final Future terminationFuture;

   private GlobalEventExecutor() {
      this.purgeTask = new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(new GlobalEventExecutor.PurgeTask(this), (Object)null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL);
      this.threadFactory = new DefaultThreadFactory(this.getClass());
      this.taskRunner = new GlobalEventExecutor.TaskRunner(this);
      this.stateLock = new Object();
      this.state = 1;
      this.terminationFuture = new FailedFuture(this, new UnsupportedOperationException());
      this.delayedTaskQueue.add(this.purgeTask);
   }

   public EventExecutorGroup parent() {
      return null;
   }

   Runnable takeTask() {
      BlockingQueue var1 = (BlockingQueue)this.taskQueue;

      Runnable var5;
      do {
         ScheduledFutureTask var2 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
         if (var2 == null) {
            Runnable var9 = null;

            try {
               var9 = (Runnable)var1.take();
            } catch (InterruptedException var7) {
            }

            return var9;
         }

         long var3 = var2.delayNanos();
         if (var3 > 0L) {
            try {
               var5 = (Runnable)var1.poll(var3, TimeUnit.NANOSECONDS);
            } catch (InterruptedException var8) {
               return null;
            }
         } else {
            var5 = (Runnable)var1.poll();
         }

         if (var5 == null) {
            this.fetchFromDelayedQueue();
            var5 = (Runnable)var1.poll();
         }
      } while(var5 == null);

      return var5;
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

   public int pendingTasks() {
      return this.taskQueue.size();
   }

   private void addTask(Runnable var1) {
      if (var1 == null) {
         throw new NullPointerException("task");
      } else {
         this.taskQueue.add(var1);
      }
   }

   public boolean inEventLoop(Thread var1) {
      return var1 == this.thread;
   }

   public Future shutdownGracefully(long var1, long var3, TimeUnit var5) {
      return this.terminationFuture();
   }

   public Future terminationFuture() {
      return this.terminationFuture;
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      throw new UnsupportedOperationException();
   }

   public boolean isShuttingDown() {
      return false;
   }

   public boolean isShutdown() {
      return false;
   }

   public boolean isTerminated() {
      return false;
   }

   public boolean awaitTermination(long var1, TimeUnit var3) {
      return false;
   }

   public void execute(Runnable var1) {
      if (var1 == null) {
         throw new NullPointerException("task");
      } else {
         this.addTask(var1);
         if (!this.inEventLoop()) {
            this.startThread();
         }

      }
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
               final GlobalEventExecutor this$0;

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
         this.thread = this.threadFactory.newThread(this.taskRunner);
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

   static InternalLogger access$100() {
      return logger;
   }

   static Object access$200(GlobalEventExecutor var0) {
      return var0.stateLock;
   }

   static int access$302(GlobalEventExecutor var0, int var1) {
      return var0.state = var1;
   }

   static {
      SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
      INSTANCE = new GlobalEventExecutor();
   }

   private final class PurgeTask implements Runnable {
      final GlobalEventExecutor this$0;

      private PurgeTask(GlobalEventExecutor var1) {
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

      PurgeTask(GlobalEventExecutor var1, Object var2) {
         this(var1);
      }
   }

   final class TaskRunner implements Runnable {
      final GlobalEventExecutor this$0;

      TaskRunner(GlobalEventExecutor var1) {
         this.this$0 = var1;
      }

      public void run() {
         while(true) {
            Runnable var1 = this.this$0.takeTask();
            if (var1 != null) {
               try {
                  var1.run();
               } catch (Throwable var4) {
                  GlobalEventExecutor.access$100().warn("Unexpected exception from the global event executor: ", var4);
               }

               if (var1 != this.this$0.purgeTask) {
                  continue;
               }
            }

            if (this.this$0.taskQueue.isEmpty() && this.this$0.delayedTaskQueue.size() == 1) {
               Object var2;
               synchronized(var2 = GlobalEventExecutor.access$200(this.this$0)){}
               if (this.this$0.taskQueue.isEmpty() && this.this$0.delayedTaskQueue.size() == 1) {
                  GlobalEventExecutor.access$302(this.this$0, 1);
                  return;
               }
            }
         }
      }
   }
}
