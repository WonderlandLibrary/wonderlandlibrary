package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@Beta
public abstract class AbstractScheduledService implements Service {
   private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
   private final AbstractService delegate = new AbstractService(this) {
      private volatile Future runningTask;
      private volatile ScheduledExecutorService executorService;
      private final ReentrantLock lock;
      private final Runnable task;
      final AbstractScheduledService this$0;

      {
         this.this$0 = var1;
         this.lock = new ReentrantLock();
         this.task = new Runnable(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public void run() {
               null.access$100(this.this$1).lock();

               try {
                  this.this$1.this$0.runOneIteration();
               } catch (Throwable var5) {
                  try {
                     this.this$1.this$0.shutDown();
                  } catch (Exception var4) {
                     AbstractScheduledService.access$200().log(Level.WARNING, "Error while attempting to shut down the service after failure.", var4);
                  }

                  this.this$1.notifyFailed(var5);
                  throw Throwables.propagate(var5);
               }

               null.access$100(this.this$1).unlock();
            }
         };
      }

      protected final void doStart() {
         this.executorService = MoreExecutors.renamingDecorator(this.this$0.executor(), new Supplier(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public String get() {
               return this.this$1.this$0.serviceName() + " " + this.this$1.state();
            }

            public Object get() {
               return this.get();
            }
         });
         this.executorService.execute(new Runnable(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public void run() {
               null.access$100(this.this$1).lock();

               try {
                  this.this$1.this$0.startUp();
                  null.access$302(this.this$1, this.this$1.this$0.scheduler().schedule(AbstractScheduledService.access$400(this.this$1.this$0), null.access$500(this.this$1), null.access$600(this.this$1)));
                  this.this$1.notifyStarted();
               } catch (Throwable var3) {
                  this.this$1.notifyFailed(var3);
                  throw Throwables.propagate(var3);
               }

               null.access$100(this.this$1).unlock();
            }
         });
      }

      protected final void doStop() {
         this.runningTask.cancel(false);
         this.executorService.execute(new Runnable(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public void run() {
               try {
                  null.access$100(this.this$1).lock();
                  if (this.this$1.state() != Service.State.STOPPING) {
                     null.access$100(this.this$1).unlock();
                  } else {
                     this.this$1.this$0.shutDown();
                     null.access$100(this.this$1).unlock();
                     this.this$1.notifyStopped();
                  }
               } catch (Throwable var2) {
                  this.this$1.notifyFailed(var2);
                  throw Throwables.propagate(var2);
               }
            }
         });
      }

      static ReentrantLock access$100(Object var0) {
         return var0.lock;
      }

      static Future access$302(Object var0, Future var1) {
         return var0.runningTask = var1;
      }

      static ScheduledExecutorService access$500(Object var0) {
         return var0.executorService;
      }

      static Runnable access$600(Object var0) {
         return var0.task;
      }
   };

   protected AbstractScheduledService() {
   }

   protected abstract void runOneIteration() throws Exception;

   protected void startUp() throws Exception {
   }

   protected void shutDown() throws Exception {
   }

   protected abstract AbstractScheduledService.Scheduler scheduler();

   protected ScheduledExecutorService executor() {
      ScheduledExecutorService var1 = Executors.newSingleThreadScheduledExecutor(new ThreadFactory(this) {
         final AbstractScheduledService this$0;

         {
            this.this$0 = var1;
         }

         public Thread newThread(Runnable var1) {
            return MoreExecutors.newThread(this.this$0.serviceName(), var1);
         }
      });
      this.addListener(new Service.Listener(this, var1) {
         final ScheduledExecutorService val$executor;
         final AbstractScheduledService this$0;

         {
            this.this$0 = var1;
            this.val$executor = var2;
         }

         public void terminated(Service.State var1) {
            this.val$executor.shutdown();
         }

         public void failed(Service.State var1, Throwable var2) {
            this.val$executor.shutdown();
         }
      }, MoreExecutors.sameThreadExecutor());
      return var1;
   }

   protected String serviceName() {
      return this.getClass().getSimpleName();
   }

   public String toString() {
      return this.serviceName() + " [" + this.state() + "]";
   }

   public final boolean isRunning() {
      return this.delegate.isRunning();
   }

   public final Service.State state() {
      return this.delegate.state();
   }

   public final void addListener(Service.Listener var1, Executor var2) {
      this.delegate.addListener(var1, var2);
   }

   public final Throwable failureCause() {
      return this.delegate.failureCause();
   }

   public final Service startAsync() {
      this.delegate.startAsync();
      return this;
   }

   public final Service stopAsync() {
      this.delegate.stopAsync();
      return this;
   }

   public final void awaitRunning() {
      this.delegate.awaitRunning();
   }

   public final void awaitRunning(long var1, TimeUnit var3) throws TimeoutException {
      this.delegate.awaitRunning(var1, var3);
   }

   public final void awaitTerminated() {
      this.delegate.awaitTerminated();
   }

   public final void awaitTerminated(long var1, TimeUnit var3) throws TimeoutException {
      this.delegate.awaitTerminated(var1, var3);
   }

   static Logger access$200() {
      return logger;
   }

   static AbstractService access$400(AbstractScheduledService var0) {
      return var0.delegate;
   }

   @Beta
   public abstract static class CustomScheduler extends AbstractScheduledService.Scheduler {
      public CustomScheduler() {
         super(null);
      }

      final Future schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3) {
         AbstractScheduledService.CustomScheduler.ReschedulableCallable var4 = new AbstractScheduledService.CustomScheduler.ReschedulableCallable(this, var1, var2, var3);
         var4.reschedule();
         return var4;
      }

      protected abstract AbstractScheduledService.CustomScheduler.Schedule getNextSchedule() throws Exception;

      @Beta
      protected static final class Schedule {
         private final long delay;
         private final TimeUnit unit;

         public Schedule(long var1, TimeUnit var3) {
            this.delay = var1;
            this.unit = (TimeUnit)Preconditions.checkNotNull(var3);
         }

         static long access$700(AbstractScheduledService.CustomScheduler.Schedule var0) {
            return var0.delay;
         }

         static TimeUnit access$800(AbstractScheduledService.CustomScheduler.Schedule var0) {
            return var0.unit;
         }
      }

      private class ReschedulableCallable extends ForwardingFuture implements Callable {
         private final Runnable wrappedRunnable;
         private final ScheduledExecutorService executor;
         private final AbstractService service;
         private final ReentrantLock lock;
         @GuardedBy("lock")
         private Future currentFuture;
         final AbstractScheduledService.CustomScheduler this$0;

         ReschedulableCallable(AbstractScheduledService.CustomScheduler var1, AbstractService var2, ScheduledExecutorService var3, Runnable var4) {
            this.this$0 = var1;
            this.lock = new ReentrantLock();
            this.wrappedRunnable = var4;
            this.executor = var3;
            this.service = var2;
         }

         public Void call() throws Exception {
            this.wrappedRunnable.run();
            this.reschedule();
            return null;
         }

         public void reschedule() {
            this.lock.lock();

            try {
               if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
                  AbstractScheduledService.CustomScheduler.Schedule var1 = this.this$0.getNextSchedule();
                  this.currentFuture = this.executor.schedule(this, AbstractScheduledService.CustomScheduler.Schedule.access$700(var1), AbstractScheduledService.CustomScheduler.Schedule.access$800(var1));
               }
            } catch (Throwable var3) {
               this.service.notifyFailed(var3);
               this.lock.unlock();
               return;
            }

            this.lock.unlock();
         }

         public boolean cancel(boolean var1) {
            this.lock.lock();
            boolean var2 = this.currentFuture.cancel(var1);
            this.lock.unlock();
            return var2;
         }

         protected Future delegate() {
            throw new UnsupportedOperationException("Only cancel is supported by this future");
         }

         protected Object delegate() {
            return this.delegate();
         }

         public Object call() throws Exception {
            return this.call();
         }
      }
   }

   public abstract static class Scheduler {
      public static AbstractScheduledService.Scheduler newFixedDelaySchedule(long var0, long var2, TimeUnit var4) {
         return new AbstractScheduledService.Scheduler(var0, var2, var4) {
            final long val$initialDelay;
            final long val$delay;
            final TimeUnit val$unit;

            {
               this.val$initialDelay = var1;
               this.val$delay = var3;
               this.val$unit = var5;
            }

            public Future schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3) {
               return var2.scheduleWithFixedDelay(var3, this.val$initialDelay, this.val$delay, this.val$unit);
            }
         };
      }

      public static AbstractScheduledService.Scheduler newFixedRateSchedule(long var0, long var2, TimeUnit var4) {
         return new AbstractScheduledService.Scheduler(var0, var2, var4) {
            final long val$initialDelay;
            final long val$period;
            final TimeUnit val$unit;

            {
               this.val$initialDelay = var1;
               this.val$period = var3;
               this.val$unit = var5;
            }

            public Future schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3) {
               return var2.scheduleAtFixedRate(var3, this.val$initialDelay, this.val$period, this.val$unit);
            }
         };
      }

      abstract Future schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3);

      private Scheduler() {
      }

      Scheduler(Object var1) {
         this();
      }
   }
}
