package io.netty.util.internal.chmv8;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import sun.misc.Unsafe;

public abstract class ForkJoinTask implements Future, Serializable {
   volatile int status;
   static final int DONE_MASK = -268435456;
   static final int NORMAL = -268435456;
   static final int CANCELLED = -1073741824;
   static final int EXCEPTIONAL = Integer.MIN_VALUE;
   static final int SIGNAL = 65536;
   static final int SMASK = 65535;
   private static final ForkJoinTask.ExceptionNode[] exceptionTable = new ForkJoinTask.ExceptionNode[32];
   private static final ReentrantLock exceptionTableLock = new ReentrantLock();
   private static final ReferenceQueue exceptionTableRefQueue = new ReferenceQueue();
   private static final int EXCEPTION_MAP_CAPACITY = 32;
   private static final long serialVersionUID = -7721805057305804111L;
   private static final Unsafe U;
   private static final long STATUS;

   private int setCompletion(int var1) {
      int var2;
      do {
         if ((var2 = this.status) < 0) {
            return var2;
         }
      } while(!U.compareAndSwapInt(this, STATUS, var2, var2 | var1));

      if (var2 >>> 16 != 0) {
         synchronized(this){}
         this.notifyAll();
      }

      return var1;
   }

   final int doExec() {
      int var1;
      if ((var1 = this.status) >= 0) {
         boolean var2;
         try {
            var2 = this.exec();
         } catch (Throwable var4) {
            return this.setExceptionalCompletion(var4);
         }

         if (var2) {
            var1 = this.setCompletion(-268435456);
         }
      }

      return var1;
   }

   final boolean trySetSignal() {
      int var1 = this.status;
      return var1 >= 0 && U.compareAndSwapInt(this, STATUS, var1, var1 | 65536);
   }

   private int externalAwaitDone() {
      ForkJoinPool var2 = ForkJoinPool.common;
      int var1;
      if ((var1 = this.status) >= 0) {
         if (var2 != null) {
            if (this instanceof CountedCompleter) {
               var1 = var2.externalHelpComplete((CountedCompleter)this);
            } else if (var2.tryExternalUnpush(this)) {
               var1 = this.doExec();
            }
         }

         if (var1 >= 0 && (var1 = this.status) >= 0) {
            boolean var3 = false;

            do {
               if (U.compareAndSwapInt(this, STATUS, var1, var1 | 65536)) {
                  synchronized(this){}
                  if (this.status >= 0) {
                     try {
                        this.wait();
                     } catch (InterruptedException var7) {
                        var3 = true;
                     }
                  } else {
                     this.notifyAll();
                  }
               }
            } while((var1 = this.status) >= 0);

            if (var3) {
               Thread.currentThread().interrupt();
            }
         }
      }

      return var1;
   }

   private int externalInterruptibleAwaitDone() throws InterruptedException {
      ForkJoinPool var2 = ForkJoinPool.common;
      if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         if (this.status >= 0 && var2 != null) {
            if (this instanceof CountedCompleter) {
               var2.externalHelpComplete((CountedCompleter)this);
            } else if (var2.tryExternalUnpush(this)) {
               this.doExec();
            }
         }

         int var1;
         while((var1 = this.status) >= 0) {
            if (U.compareAndSwapInt(this, STATUS, var1, var1 | 65536)) {
               synchronized(this){}
               if (this.status >= 0) {
                  this.wait();
               } else {
                  this.notifyAll();
               }
            }
         }

         return var1;
      }
   }

   private int doJoin() {
      int var1;
      Thread var2;
      ForkJoinWorkerThread var3;
      ForkJoinPool.WorkQueue var4;
      return (var1 = this.status) < 0 ? var1 : ((var2 = Thread.currentThread()) instanceof ForkJoinWorkerThread ? ((var4 = (var3 = (ForkJoinWorkerThread)var2).workQueue).tryUnpush(this) && (var1 = this.doExec()) < 0 ? var1 : var3.pool.awaitJoin(var4, this)) : this.externalAwaitDone());
   }

   private int doInvoke() {
      int var1;
      Thread var2;
      ForkJoinWorkerThread var3;
      return (var1 = this.doExec()) < 0 ? var1 : ((var2 = Thread.currentThread()) instanceof ForkJoinWorkerThread ? (var3 = (ForkJoinWorkerThread)var2).pool.awaitJoin(var3.workQueue, this) : this.externalAwaitDone());
   }

   final int recordExceptionalCompletion(Throwable var1) {
      int var2;
      if ((var2 = this.status) >= 0) {
         int var3 = System.identityHashCode(this);
         ReentrantLock var4 = exceptionTableLock;
         var4.lock();
         expungeStaleExceptions();
         ForkJoinTask.ExceptionNode[] var5 = exceptionTable;
         int var6 = var3 & var5.length - 1;
         ForkJoinTask.ExceptionNode var7 = var5[var6];

         while(true) {
            if (var7 == null) {
               var5[var6] = new ForkJoinTask.ExceptionNode(this, var1, var5[var6]);
               break;
            }

            if (var7.get() == this) {
               break;
            }

            var7 = var7.next;
         }

         var4.unlock();
         var2 = this.setCompletion(Integer.MIN_VALUE);
      }

      return var2;
   }

   private int setExceptionalCompletion(Throwable var1) {
      int var2 = this.recordExceptionalCompletion(var1);
      if ((var2 & -268435456) == Integer.MIN_VALUE) {
         this.internalPropagateException(var1);
      }

      return var2;
   }

   void internalPropagateException(Throwable var1) {
   }

   static final void cancelIgnoringExceptions(ForkJoinTask var0) {
      if (var0 != null && var0.status >= 0) {
         try {
            var0.cancel(false);
         } catch (Throwable var2) {
         }
      }

   }

   private void clearExceptionalCompletion() {
      int var1 = System.identityHashCode(this);
      ReentrantLock var2 = exceptionTableLock;
      var2.lock();
      ForkJoinTask.ExceptionNode[] var3 = exceptionTable;
      int var4 = var1 & var3.length - 1;
      ForkJoinTask.ExceptionNode var5 = var3[var4];

      ForkJoinTask.ExceptionNode var7;
      for(ForkJoinTask.ExceptionNode var6 = null; var5 != null; var5 = var7) {
         var7 = var5.next;
         if (var5.get() == this) {
            if (var6 == null) {
               var3[var4] = var7;
            } else {
               var6.next = var7;
            }
            break;
         }

         var6 = var5;
      }

      expungeStaleExceptions();
      this.status = 0;
      var2.unlock();
   }

   private Throwable getThrowableException() {
      if ((this.status & -268435456) != Integer.MIN_VALUE) {
         return null;
      } else {
         int var1 = System.identityHashCode(this);
         ReentrantLock var3 = exceptionTableLock;
         var3.lock();
         expungeStaleExceptions();
         ForkJoinTask.ExceptionNode[] var4 = exceptionTable;

         ForkJoinTask.ExceptionNode var2;
         for(var2 = var4[var1 & var4.length - 1]; var2 != null && var2.get() != this; var2 = var2.next) {
         }

         var3.unlock();
         Throwable var6;
         return var2 != null && (var6 = var2.ex) != null ? var6 : null;
      }
   }

   private static void expungeStaleExceptions() {
      label27:
      while(true) {
         Reference var0;
         if ((var0 = exceptionTableRefQueue.poll()) != null) {
            if (!(var0 instanceof ForkJoinTask.ExceptionNode)) {
               continue;
            }

            ForkJoinTask var1 = (ForkJoinTask)((ForkJoinTask.ExceptionNode)var0).get();
            ForkJoinTask.ExceptionNode[] var2 = exceptionTable;
            int var3 = System.identityHashCode(var1) & var2.length - 1;
            ForkJoinTask.ExceptionNode var4 = var2[var3];
            ForkJoinTask.ExceptionNode var5 = null;

            while(true) {
               if (var4 == null) {
                  continue label27;
               }

               ForkJoinTask.ExceptionNode var6 = var4.next;
               if (var4 == var0) {
                  if (var5 == null) {
                     var2[var3] = var6;
                     continue label27;
                  }

                  var5.next = var6;
                  continue label27;
               }

               var5 = var4;
               var4 = var6;
            }
         }

         return;
      }
   }

   static final void helpExpungeStaleExceptions() {
      ReentrantLock var0 = exceptionTableLock;
      if (var0.tryLock()) {
         expungeStaleExceptions();
         var0.unlock();
      }

   }

   static void rethrow(Throwable var0) {
      if (var0 != null) {
         uncheckedThrow(var0);
      }

   }

   static void uncheckedThrow(Throwable var0) throws Throwable {
      throw var0;
   }

   private void reportException(int var1) {
      if (var1 == -1073741824) {
         throw new CancellationException();
      } else {
         if (var1 == Integer.MIN_VALUE) {
            rethrow(this.getThrowableException());
         }

      }
   }

   public final ForkJoinTask fork() {
      Thread var1;
      if ((var1 = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
         ((ForkJoinWorkerThread)var1).workQueue.push(this);
      } else {
         ForkJoinPool.common.externalPush(this);
      }

      return this;
   }

   public final Object join() {
      int var1;
      if ((var1 = this.doJoin() & -268435456) != -268435456) {
         this.reportException(var1);
      }

      return this.getRawResult();
   }

   public final Object invoke() {
      int var1;
      if ((var1 = this.doInvoke() & -268435456) != -268435456) {
         this.reportException(var1);
      }

      return this.getRawResult();
   }

   public static void invokeAll(ForkJoinTask var0, ForkJoinTask var1) {
      var1.fork();
      int var2;
      if ((var2 = var0.doInvoke() & -268435456) != -268435456) {
         var0.reportException(var2);
      }

      int var3;
      if ((var3 = var1.doJoin() & -268435456) != -268435456) {
         var1.reportException(var3);
      }

   }

   public static void invokeAll(ForkJoinTask... var0) {
      Object var1 = null;
      int var2 = var0.length - 1;

      int var3;
      ForkJoinTask var4;
      for(var3 = var2; var3 >= 0; --var3) {
         var4 = var0[var3];
         if (var4 == null) {
            if (var1 == null) {
               var1 = new NullPointerException();
            }
         } else if (var3 != 0) {
            var4.fork();
         } else if (var4.doInvoke() < -268435456 && var1 == null) {
            var1 = var4.getException();
         }
      }

      for(var3 = 1; var3 <= var2; ++var3) {
         var4 = var0[var3];
         if (var4 != null) {
            if (var1 != null) {
               var4.cancel(false);
            } else if (var4.doJoin() < -268435456) {
               var1 = var4.getException();
            }
         }
      }

      if (var1 != null) {
         rethrow((Throwable)var1);
      }

   }

   public static Collection invokeAll(Collection var0) {
      if (var0 instanceof RandomAccess && var0 instanceof List) {
         List var1 = (List)var0;
         Object var2 = null;
         int var3 = var1.size() - 1;

         int var4;
         ForkJoinTask var5;
         for(var4 = var3; var4 >= 0; --var4) {
            var5 = (ForkJoinTask)var1.get(var4);
            if (var5 == null) {
               if (var2 == null) {
                  var2 = new NullPointerException();
               }
            } else if (var4 != 0) {
               var5.fork();
            } else if (var5.doInvoke() < -268435456 && var2 == null) {
               var2 = var5.getException();
            }
         }

         for(var4 = 1; var4 <= var3; ++var4) {
            var5 = (ForkJoinTask)var1.get(var4);
            if (var5 != null) {
               if (var2 != null) {
                  var5.cancel(false);
               } else if (var5.doJoin() < -268435456) {
                  var2 = var5.getException();
               }
            }
         }

         if (var2 != null) {
            rethrow((Throwable)var2);
         }

         return var0;
      } else {
         invokeAll((ForkJoinTask[])var0.toArray(new ForkJoinTask[var0.size()]));
         return var0;
      }
   }

   public boolean cancel(boolean var1) {
      return (this.setCompletion(-1073741824) & -268435456) == -1073741824;
   }

   public final boolean isDone() {
      return this.status < 0;
   }

   public final boolean isCancelled() {
      return (this.status & -268435456) == -1073741824;
   }

   public final boolean isCompletedAbnormally() {
      return this.status < -268435456;
   }

   public final boolean isCompletedNormally() {
      return (this.status & -268435456) == -268435456;
   }

   public final Throwable getException() {
      int var1 = this.status & -268435456;
      return (Throwable)(var1 >= -268435456 ? null : (var1 == -1073741824 ? new CancellationException() : this.getThrowableException()));
   }

   public void completeExceptionally(Throwable var1) {
      this.setExceptionalCompletion((Throwable)(!(var1 instanceof RuntimeException) && !(var1 instanceof Error) ? new RuntimeException(var1) : var1));
   }

   public void complete(Object var1) {
      try {
         this.setRawResult(var1);
      } catch (Throwable var3) {
         this.setExceptionalCompletion(var3);
         return;
      }

      this.setCompletion(-268435456);
   }

   public final void quietlyComplete() {
      this.setCompletion(-268435456);
   }

   public final Object get() throws InterruptedException, ExecutionException {
      int var1 = Thread.currentThread() instanceof ForkJoinWorkerThread ? this.doJoin() : this.externalInterruptibleAwaitDone();
      if ((var1 &= -268435456) == -1073741824) {
         throw new CancellationException();
      } else {
         Throwable var2;
         if (var1 == Integer.MIN_VALUE && (var2 = this.getThrowableException()) != null) {
            throw new ExecutionException(var2);
         } else {
            return this.getRawResult();
         }
      }
   }

   public final Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         long var7 = var3.toNanos(var1);
         int var4;
         if ((var4 = this.status) >= 0 && var7 > 0L) {
            long var10 = System.nanoTime() + var7;
            ForkJoinPool var12 = null;
            ForkJoinPool.WorkQueue var13 = null;
            Thread var14 = Thread.currentThread();
            if (var14 instanceof ForkJoinWorkerThread) {
               ForkJoinWorkerThread var15 = (ForkJoinWorkerThread)var14;
               var12 = var15.pool;
               var13 = var15.workQueue;
               var12.helpJoinOnce(var13, this);
            } else {
               ForkJoinPool var9;
               if ((var9 = ForkJoinPool.common) != null) {
                  if (this instanceof CountedCompleter) {
                     var9.externalHelpComplete((CountedCompleter)this);
                  } else if (var9.tryExternalUnpush(this)) {
                     this.doExec();
                  }
               }
            }

            boolean var23 = false;
            boolean var16 = false;

            label104:
            while(true) {
               while(true) {
                  if ((var4 = this.status) < 0) {
                     break label104;
                  }

                  if (var13 != null && var13.qlock < 0) {
                     cancelIgnoringExceptions(this);
                  } else if (!var23) {
                     if (var12 == null || var12.tryCompensate(var12.ctl)) {
                        var23 = true;
                     }
                  } else {
                     long var5;
                     if ((var5 = TimeUnit.NANOSECONDS.toMillis(var7)) > 0L && U.compareAndSwapInt(this, STATUS, var4, var4 | 65536)) {
                        synchronized(this){}
                        if (this.status >= 0) {
                           try {
                              this.wait(var5);
                           } catch (InterruptedException var21) {
                              if (var12 == null) {
                                 var16 = true;
                              }
                           }
                        } else {
                           this.notifyAll();
                        }
                     }

                     if ((var4 = this.status) < 0 || var16 || (var7 = var10 - System.nanoTime()) <= 0L) {
                        break label104;
                     }
                  }
               }
            }

            if (var12 != null && var23) {
               var12.incrementActiveCount();
            }

            if (var16) {
               throw new InterruptedException();
            }
         }

         if ((var4 &= -268435456) != -268435456) {
            if (var4 == -1073741824) {
               throw new CancellationException();
            }

            if (var4 != Integer.MIN_VALUE) {
               throw new TimeoutException();
            }

            Throwable var22;
            if ((var22 = this.getThrowableException()) != null) {
               throw new ExecutionException(var22);
            }
         }

         return this.getRawResult();
      }
   }

   public final void quietlyJoin() {
      this.doJoin();
   }

   public final void quietlyInvoke() {
      this.doInvoke();
   }

   public static void helpQuiesce() {
      Thread var0;
      if ((var0 = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
         ForkJoinWorkerThread var1 = (ForkJoinWorkerThread)var0;
         var1.pool.helpQuiescePool(var1.workQueue);
      } else {
         ForkJoinPool.quiesceCommonPool();
      }

   }

   public void reinitialize() {
      if ((this.status & -268435456) == Integer.MIN_VALUE) {
         this.clearExceptionalCompletion();
      } else {
         this.status = 0;
      }

   }

   public static ForkJoinPool getPool() {
      Thread var0 = Thread.currentThread();
      return var0 instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)var0).pool : null;
   }

   public static boolean inForkJoinPool() {
      return Thread.currentThread() instanceof ForkJoinWorkerThread;
   }

   public boolean tryUnfork() {
      Thread var1;
      return (var1 = Thread.currentThread()) instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)var1).workQueue.tryUnpush(this) : ForkJoinPool.common.tryExternalUnpush(this);
   }

   public static int getQueuedTaskCount() {
      Thread var0;
      ForkJoinPool.WorkQueue var1;
      if ((var0 = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
         var1 = ((ForkJoinWorkerThread)var0).workQueue;
      } else {
         var1 = ForkJoinPool.commonSubmitterQueue();
      }

      return var1 == null ? 0 : var1.queueSize();
   }

   public static int getSurplusQueuedTaskCount() {
      return ForkJoinPool.getSurplusQueuedTaskCount();
   }

   public abstract Object getRawResult();

   protected abstract void setRawResult(Object var1);

   protected abstract boolean exec();

   protected static ForkJoinTask peekNextLocalTask() {
      Thread var0;
      ForkJoinPool.WorkQueue var1;
      if ((var0 = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
         var1 = ((ForkJoinWorkerThread)var0).workQueue;
      } else {
         var1 = ForkJoinPool.commonSubmitterQueue();
      }

      return var1 == null ? null : var1.peek();
   }

   protected static ForkJoinTask pollNextLocalTask() {
      Thread var0;
      return (var0 = Thread.currentThread()) instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)var0).workQueue.nextLocalTask() : null;
   }

   protected static ForkJoinTask pollTask() {
      Thread var0;
      ForkJoinWorkerThread var1;
      return (var0 = Thread.currentThread()) instanceof ForkJoinWorkerThread ? (var1 = (ForkJoinWorkerThread)var0).pool.nextTaskFor(var1.workQueue) : null;
   }

   public final short getForkJoinTaskTag() {
      return (short)this.status;
   }

   public final short setForkJoinTaskTag(short var1) {
      int var2;
      while(!U.compareAndSwapInt(this, STATUS, var2 = this.status, var2 & -65536 | var1 & '\uffff')) {
      }

      return (short)var2;
   }

   public final boolean compareAndSetForkJoinTaskTag(short var1, short var2) {
      int var3;
      do {
         if ((short)(var3 = this.status) != var1) {
            return false;
         }
      } while(!U.compareAndSwapInt(this, STATUS, var3, var3 & -65536 | var2 & '\uffff'));

      return true;
   }

   public static ForkJoinTask adapt(Runnable var0) {
      return new ForkJoinTask.AdaptedRunnableAction(var0);
   }

   public static ForkJoinTask adapt(Runnable var0, Object var1) {
      return new ForkJoinTask.AdaptedRunnable(var0, var1);
   }

   public static ForkJoinTask adapt(Callable var0) {
      return new ForkJoinTask.AdaptedCallable(var0);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.getException());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      Object var2 = var1.readObject();
      if (var2 != null) {
         this.setExceptionalCompletion((Throwable)var2);
      }

   }

   private static Unsafe getUnsafe() {
      try {
         return Unsafe.getUnsafe();
      } catch (SecurityException var2) {
         try {
            return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Unsafe run() throws Exception {
                  Class var1 = Unsafe.class;
                  Field[] var2 = var1.getDeclaredFields();
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Field var5 = var2[var4];
                     var5.setAccessible(true);
                     Object var6 = var5.get((Object)null);
                     if (var1.isInstance(var6)) {
                        return (Unsafe)var1.cast(var6);
                     }
                  }

                  throw new NoSuchFieldError("the Unsafe");
               }

               public Object run() throws Exception {
                  return this.run();
               }
            });
         } catch (PrivilegedActionException var1) {
            throw new RuntimeException("Could not initialize intrinsics", var1.getCause());
         }
      }
   }

   static ReferenceQueue access$000() {
      return exceptionTableRefQueue;
   }

   static {
      try {
         U = getUnsafe();
         Class var0 = ForkJoinTask.class;
         STATUS = U.objectFieldOffset(var0.getDeclaredField("status"));
      } catch (Exception var1) {
         throw new Error(var1);
      }
   }

   static final class AdaptedCallable extends ForkJoinTask implements RunnableFuture {
      final Callable callable;
      Object result;
      private static final long serialVersionUID = 2838392045355241008L;

      AdaptedCallable(Callable var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this.callable = var1;
         }
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void setRawResult(Object var1) {
         this.result = var1;
      }

      public final boolean exec() {
         try {
            this.result = this.callable.call();
            return true;
         } catch (Error var2) {
            throw var2;
         } catch (RuntimeException var3) {
            throw var3;
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      }

      public final void run() {
         this.invoke();
      }
   }

   static final class RunnableExecuteAction extends ForkJoinTask {
      final Runnable runnable;
      private static final long serialVersionUID = 5232453952276885070L;

      RunnableExecuteAction(Runnable var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this.runnable = var1;
         }
      }

      public final Void getRawResult() {
         return null;
      }

      public final void setRawResult(Void var1) {
      }

      public final boolean exec() {
         this.runnable.run();
         return true;
      }

      void internalPropagateException(Throwable var1) {
         rethrow(var1);
      }

      public void setRawResult(Object var1) {
         this.setRawResult((Void)var1);
      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class AdaptedRunnableAction extends ForkJoinTask implements RunnableFuture {
      final Runnable runnable;
      private static final long serialVersionUID = 5232453952276885070L;

      AdaptedRunnableAction(Runnable var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this.runnable = var1;
         }
      }

      public final Void getRawResult() {
         return null;
      }

      public final void setRawResult(Void var1) {
      }

      public final boolean exec() {
         this.runnable.run();
         return true;
      }

      public final void run() {
         this.invoke();
      }

      public void setRawResult(Object var1) {
         this.setRawResult((Void)var1);
      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class AdaptedRunnable extends ForkJoinTask implements RunnableFuture {
      final Runnable runnable;
      Object result;
      private static final long serialVersionUID = 5232453952276885070L;

      AdaptedRunnable(Runnable var1, Object var2) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this.runnable = var1;
            this.result = var2;
         }
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void setRawResult(Object var1) {
         this.result = var1;
      }

      public final boolean exec() {
         this.runnable.run();
         return true;
      }

      public final void run() {
         this.invoke();
      }
   }

   static final class ExceptionNode extends WeakReference {
      final Throwable ex;
      ForkJoinTask.ExceptionNode next;
      final long thrower;

      ExceptionNode(ForkJoinTask var1, Throwable var2, ForkJoinTask.ExceptionNode var3) {
         super(var1, ForkJoinTask.access$000());
         this.ex = var2;
         this.next = var3;
         this.thrower = Thread.currentThread().getId();
      }
   }
}
