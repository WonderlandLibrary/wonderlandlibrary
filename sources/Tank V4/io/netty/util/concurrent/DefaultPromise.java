package io.netty.util.concurrent;

import io.netty.util.Signal;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

public class DefaultPromise extends AbstractFuture implements Promise {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
   private static final int MAX_LISTENER_STACK_DEPTH = 8;
   private static final ThreadLocal LISTENER_STACK_DEPTH = new ThreadLocal() {
      protected Integer initialValue() {
         return 0;
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final Signal SUCCESS = Signal.valueOf(DefaultPromise.class.getName() + ".SUCCESS");
   private static final Signal UNCANCELLABLE = Signal.valueOf(DefaultPromise.class.getName() + ".UNCANCELLABLE");
   private static final DefaultPromise.CauseHolder CANCELLATION_CAUSE_HOLDER = new DefaultPromise.CauseHolder(new CancellationException());
   private final EventExecutor executor;
   private volatile Object result;
   private Object listeners;
   private short waiters;

   public DefaultPromise(EventExecutor var1) {
      if (var1 == null) {
         throw new NullPointerException("executor");
      } else {
         this.executor = var1;
      }
   }

   protected DefaultPromise() {
      this.executor = null;
   }

   protected EventExecutor executor() {
      return this.executor;
   }

   public boolean isCancelled() {
      return isCancelled0(this.result);
   }

   private static boolean isCancelled0(Object var0) {
      return var0 instanceof DefaultPromise.CauseHolder && ((DefaultPromise.CauseHolder)var0).cause instanceof CancellationException;
   }

   public boolean isCancellable() {
      return this.result == null;
   }

   public boolean isDone() {
      return isDone0(this.result);
   }

   public boolean isSuccess() {
      Object var1 = this.result;
      if (var1 != null && var1 != UNCANCELLABLE) {
         return !(var1 instanceof DefaultPromise.CauseHolder);
      } else {
         return false;
      }
   }

   public Throwable cause() {
      Object var1 = this.result;
      return var1 instanceof DefaultPromise.CauseHolder ? ((DefaultPromise.CauseHolder)var1).cause : null;
   }

   public Promise addListener(GenericFutureListener var1) {
      if (var1 == null) {
         throw new NullPointerException("listener");
      } else if (this.isDone()) {
         notifyListener(this.executor(), this, var1);
         return this;
      } else {
         synchronized(this){}
         if (!this.isDone()) {
            if (this.listeners == null) {
               this.listeners = var1;
            } else if (this.listeners instanceof DefaultFutureListeners) {
               ((DefaultFutureListeners)this.listeners).add(var1);
            } else {
               GenericFutureListener var3 = (GenericFutureListener)this.listeners;
               this.listeners = new DefaultFutureListeners(var3, var1);
            }

            return this;
         } else {
            notifyListener(this.executor(), this, var1);
            return this;
         }
      }
   }

   public Promise addListeners(GenericFutureListener... var1) {
      if (var1 == null) {
         throw new NullPointerException("listeners");
      } else {
         GenericFutureListener[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            GenericFutureListener var5 = var2[var4];
            if (var5 == null) {
               break;
            }

            this.addListener(var5);
         }

         return this;
      }
   }

   public Promise removeListener(GenericFutureListener var1) {
      if (var1 == null) {
         throw new NullPointerException("listener");
      } else if (this.isDone()) {
         return this;
      } else {
         synchronized(this){}
         if (!this.isDone()) {
            if (this.listeners instanceof DefaultFutureListeners) {
               ((DefaultFutureListeners)this.listeners).remove(var1);
            } else if (this.listeners == var1) {
               this.listeners = null;
            }
         }

         return this;
      }
   }

   public Promise removeListeners(GenericFutureListener... var1) {
      if (var1 == null) {
         throw new NullPointerException("listeners");
      } else {
         GenericFutureListener[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            GenericFutureListener var5 = var2[var4];
            if (var5 == null) {
               break;
            }

            this.removeListener(var5);
         }

         return this;
      }
   }

   public Promise sync() throws InterruptedException {
      this.await();
      this.rethrowIfFailed();
      return this;
   }

   public Promise syncUninterruptibly() {
      this.awaitUninterruptibly();
      this.rethrowIfFailed();
      return this;
   }

   private void rethrowIfFailed() {
      Throwable var1 = this.cause();
      if (var1 != null) {
         PlatformDependent.throwException(var1);
      }
   }

   public Promise await() throws InterruptedException {
      if (this.isDone()) {
         return this;
      } else if (Thread.interrupted()) {
         throw new InterruptedException(this.toString());
      } else {
         synchronized(this){}

         while(!this.isDone()) {
            this.checkDeadLock();
            this.incWaiters();
            this.wait();
            this.decWaiters();
         }

         return this;
      }
   }

   public boolean await(long var1, TimeUnit var3) throws InterruptedException {
      return this.await0(var3.toNanos(var1), true);
   }

   public boolean await(long var1) throws InterruptedException {
      return this.await0(TimeUnit.MILLISECONDS.toNanos(var1), true);
   }

   public Promise awaitUninterruptibly() {
      if (this.isDone()) {
         return this;
      } else {
         boolean var1 = false;
         synchronized(this){}

         while(!this.isDone()) {
            this.checkDeadLock();
            this.incWaiters();

            try {
               this.wait();
            } catch (InterruptedException var6) {
               var1 = true;
               this.decWaiters();
               continue;
            }

            this.decWaiters();
         }

         if (var1) {
            Thread.currentThread().interrupt();
         }

         return this;
      }
   }

   public boolean awaitUninterruptibly(long var1, TimeUnit var3) {
      try {
         return this.await0(var3.toNanos(var1), false);
      } catch (InterruptedException var5) {
         throw new InternalError();
      }
   }

   public boolean awaitUninterruptibly(long var1) {
      try {
         return this.await0(TimeUnit.MILLISECONDS.toNanos(var1), false);
      } catch (InterruptedException var4) {
         throw new InternalError();
      }
   }

   private boolean await0(long var1, boolean var3) throws InterruptedException {
      if (this.isDone()) {
         return true;
      } else if (var1 <= 0L) {
         return this.isDone();
      } else if (var3 && Thread.interrupted()) {
         throw new InterruptedException(this.toString());
      } else {
         long var4 = System.nanoTime();
         long var6 = var1;
         boolean var8 = false;
         synchronized(this){}
         boolean var10;
         if (this.isDone()) {
            var10 = true;
            if (var8) {
               Thread.currentThread().interrupt();
            }

            return var10;
         } else if (var1 <= 0L) {
            var10 = this.isDone();
            if (var8) {
               Thread.currentThread().interrupt();
            }

            return var10;
         } else {
            this.checkDeadLock();
            this.incWaiters();

            do {
               try {
                  this.wait(var6 / 1000000L, (int)(var6 % 1000000L));
               } catch (InterruptedException var14) {
                  if (var3) {
                     throw var14;
                  }

                  var8 = true;
               }

               if (this.isDone()) {
                  var10 = true;
                  this.decWaiters();
                  if (var8) {
                     Thread.currentThread().interrupt();
                  }

                  return var10;
               }

               var6 = var1 - (System.nanoTime() - var4);
            } while(var6 > 0L);

            var10 = this.isDone();
            this.decWaiters();
            if (var8) {
               Thread.currentThread().interrupt();
            }

            return var10;
         }
      }
   }

   protected void checkDeadLock() {
      EventExecutor var1 = this.executor();
      if (var1 != null && var1.inEventLoop()) {
         throw new BlockingOperationException(this.toString());
      }
   }

   public Promise setSuccess(Object var1) {
      if (var1 != false) {
         this.notifyListeners();
         return this;
      } else {
         throw new IllegalStateException("complete already: " + this);
      }
   }

   public boolean trySuccess(Object var1) {
      if (var1 != false) {
         this.notifyListeners();
         return true;
      } else {
         return false;
      }
   }

   public Promise setFailure(Throwable var1) {
      if (var1 != false) {
         this.notifyListeners();
         return this;
      } else {
         throw new IllegalStateException("complete already: " + this, var1);
      }
   }

   public boolean tryFailure(Throwable var1) {
      if (var1 != false) {
         this.notifyListeners();
         return true;
      } else {
         return false;
      }
   }

   public boolean cancel(boolean var1) {
      Object var2 = this.result;
      if (var2 != null && var2 != UNCANCELLABLE) {
         synchronized(this){}
         var2 = this.result;
         if (var2 != null && var2 != UNCANCELLABLE) {
            this.result = CANCELLATION_CAUSE_HOLDER;
            if (this > 0) {
               this.notifyAll();
            }

            this.notifyListeners();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean setUncancellable() {
      Object var1 = this.result;
      if (var1 != null) {
         return false;
      } else {
         synchronized(this){}
         var1 = this.result;
         if (var1 != null) {
            return false;
         } else {
            this.result = UNCANCELLABLE;
            return true;
         }
      }
   }

   public Object getNow() {
      Object var1 = this.result;
      return !(var1 instanceof DefaultPromise.CauseHolder) && var1 != SUCCESS ? var1 : null;
   }

   private void incWaiters() {
      if (this.waiters == 32767) {
         throw new IllegalStateException("too many waiters: " + this);
      } else {
         ++this.waiters;
      }
   }

   private void decWaiters() {
      --this.waiters;
   }

   private void notifyListeners() {
      Object var1 = this.listeners;
      if (var1 != null) {
         this.listeners = null;
         EventExecutor var2 = this.executor();
         if (var2.inEventLoop()) {
            Integer var3 = (Integer)LISTENER_STACK_DEPTH.get();
            if (var3 < 8) {
               LISTENER_STACK_DEPTH.set(var3 + 1);
               if (var1 instanceof DefaultFutureListeners) {
                  notifyListeners0(this, (DefaultFutureListeners)var1);
               } else {
                  GenericFutureListener var4 = (GenericFutureListener)var1;
                  notifyListener0(this, var4);
               }

               LISTENER_STACK_DEPTH.set(var3);
               return;
            }
         }

         try {
            if (var1 instanceof DefaultFutureListeners) {
               DefaultFutureListeners var7 = (DefaultFutureListeners)var1;
               var2.execute(new Runnable(this, var7) {
                  final DefaultFutureListeners val$dfl;
                  final DefaultPromise this$0;

                  {
                     this.this$0 = var1;
                     this.val$dfl = var2;
                  }

                  public void run() {
                     DefaultPromise.access$100(this.this$0, this.val$dfl);
                  }
               });
            } else {
               GenericFutureListener var8 = (GenericFutureListener)var1;
               var2.execute(new Runnable(this, var8) {
                  final GenericFutureListener val$l;
                  final DefaultPromise this$0;

                  {
                     this.this$0 = var1;
                     this.val$l = var2;
                  }

                  public void run() {
                     DefaultPromise.notifyListener0(this.this$0, this.val$l);
                  }
               });
            }
         } catch (Throwable var6) {
            logger.error("Failed to notify listener(s). Event loop shut down?", var6);
         }

      }
   }

   private static void notifyListeners0(Future var0, DefaultFutureListeners var1) {
      GenericFutureListener[] var2 = var1.listeners();
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         notifyListener0(var0, var2[var4]);
      }

   }

   protected static void notifyListener(EventExecutor var0, Future var1, GenericFutureListener var2) {
      if (var0.inEventLoop()) {
         Integer var3 = (Integer)LISTENER_STACK_DEPTH.get();
         if (var3 < 8) {
            LISTENER_STACK_DEPTH.set(var3 + 1);
            notifyListener0(var1, var2);
            LISTENER_STACK_DEPTH.set(var3);
            return;
         }
      }

      try {
         var0.execute(new Runnable(var1, var2) {
            final Future val$future;
            final GenericFutureListener val$l;

            {
               this.val$future = var1;
               this.val$l = var2;
            }

            public void run() {
               DefaultPromise.notifyListener0(this.val$future, this.val$l);
            }
         });
      } catch (Throwable var5) {
         logger.error("Failed to notify a listener. Event loop shut down?", var5);
      }

   }

   static void notifyListener0(Future var0, GenericFutureListener var1) {
      try {
         var1.operationComplete(var0);
      } catch (Throwable var3) {
         if (logger.isWarnEnabled()) {
            logger.warn("An exception was thrown by " + var1.getClass().getName() + ".operationComplete()", var3);
         }
      }

   }

   private synchronized Object progressiveListeners() {
      Object var1 = this.listeners;
      if (var1 == null) {
         return null;
      } else if (var1 instanceof DefaultFutureListeners) {
         DefaultFutureListeners var2 = (DefaultFutureListeners)var1;
         int var3 = var2.progressiveSize();
         GenericFutureListener[] var4;
         int var6;
         switch(var3) {
         case 0:
            return null;
         case 1:
            var4 = var2.listeners();
            int var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               GenericFutureListener var7 = var4[var6];
               if (var7 instanceof GenericProgressiveFutureListener) {
                  return var7;
               }
            }

            return null;
         default:
            var4 = var2.listeners();
            GenericProgressiveFutureListener[] var9 = new GenericProgressiveFutureListener[var3];
            var6 = 0;

            for(int var10 = 0; var10 < var3; ++var6) {
               GenericFutureListener var8 = var4[var6];
               if (var8 instanceof GenericProgressiveFutureListener) {
                  var9[var10++] = (GenericProgressiveFutureListener)var8;
               }
            }

            return var9;
         }
      } else {
         return var1 instanceof GenericProgressiveFutureListener ? var1 : null;
      }
   }

   void notifyProgressiveListeners(long var1, long var3) {
      Object var5 = this.progressiveListeners();
      if (var5 != null) {
         ProgressiveFuture var6 = (ProgressiveFuture)this;
         EventExecutor var7 = this.executor();
         if (var7.inEventLoop()) {
            if (var5 instanceof GenericProgressiveFutureListener[]) {
               notifyProgressiveListeners0(var6, (GenericProgressiveFutureListener[])((GenericProgressiveFutureListener[])var5), var1, var3);
            } else {
               notifyProgressiveListener0(var6, (GenericProgressiveFutureListener)var5, var1, var3);
            }
         } else {
            try {
               if (var5 instanceof GenericProgressiveFutureListener[]) {
                  GenericProgressiveFutureListener[] var8 = (GenericProgressiveFutureListener[])((GenericProgressiveFutureListener[])var5);
                  var7.execute(new Runnable(this, var6, var8, var1, var3) {
                     final ProgressiveFuture val$self;
                     final GenericProgressiveFutureListener[] val$array;
                     final long val$progress;
                     final long val$total;
                     final DefaultPromise this$0;

                     {
                        this.this$0 = var1;
                        this.val$self = var2;
                        this.val$array = var3;
                        this.val$progress = var4;
                        this.val$total = var6;
                     }

                     public void run() {
                        DefaultPromise.access$200(this.val$self, this.val$array, this.val$progress, this.val$total);
                     }
                  });
               } else {
                  GenericProgressiveFutureListener var10 = (GenericProgressiveFutureListener)var5;
                  var7.execute(new Runnable(this, var6, var10, var1, var3) {
                     final ProgressiveFuture val$self;
                     final GenericProgressiveFutureListener val$l;
                     final long val$progress;
                     final long val$total;
                     final DefaultPromise this$0;

                     {
                        this.this$0 = var1;
                        this.val$self = var2;
                        this.val$l = var3;
                        this.val$progress = var4;
                        this.val$total = var6;
                     }

                     public void run() {
                        DefaultPromise.access$300(this.val$self, this.val$l, this.val$progress, this.val$total);
                     }
                  });
               }
            } catch (Throwable var9) {
               logger.error("Failed to notify listener(s). Event loop shut down?", var9);
            }
         }

      }
   }

   private static void notifyProgressiveListeners0(ProgressiveFuture var0, GenericProgressiveFutureListener[] var1, long var2, long var4) {
      GenericProgressiveFutureListener[] var6 = var1;
      int var7 = var1.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         GenericProgressiveFutureListener var9 = var6[var8];
         if (var9 == null) {
            break;
         }

         notifyProgressiveListener0(var0, var9, var2, var4);
      }

   }

   private static void notifyProgressiveListener0(ProgressiveFuture var0, GenericProgressiveFutureListener var1, long var2, long var4) {
      try {
         var1.operationProgressed(var0, var2, var4);
      } catch (Throwable var7) {
         if (logger.isWarnEnabled()) {
            logger.warn("An exception was thrown by " + var1.getClass().getName() + ".operationProgressed()", var7);
         }
      }

   }

   public String toString() {
      return this.toStringBuilder().toString();
   }

   protected StringBuilder toStringBuilder() {
      StringBuilder var1 = new StringBuilder(64);
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append('@');
      var1.append(Integer.toHexString(this.hashCode()));
      Object var2 = this.result;
      if (var2 == SUCCESS) {
         var1.append("(success)");
      } else if (var2 == UNCANCELLABLE) {
         var1.append("(uncancellable)");
      } else if (var2 instanceof DefaultPromise.CauseHolder) {
         var1.append("(failure(");
         var1.append(((DefaultPromise.CauseHolder)var2).cause);
         var1.append(')');
      } else {
         var1.append("(incomplete)");
      }

      return var1;
   }

   public Future awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   public Future await() throws InterruptedException {
      return this.await();
   }

   public Future syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   public Future sync() throws InterruptedException {
      return this.sync();
   }

   public Future removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   public Future removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   public Future addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   public Future addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   static void access$100(Future var0, DefaultFutureListeners var1) {
      notifyListeners0(var0, var1);
   }

   static void access$200(ProgressiveFuture var0, GenericProgressiveFutureListener[] var1, long var2, long var4) {
      notifyProgressiveListeners0(var0, var1, var2, var4);
   }

   static void access$300(ProgressiveFuture var0, GenericProgressiveFutureListener var1, long var2, long var4) {
      notifyProgressiveListener0(var0, var1, var2, var4);
   }

   static {
      CANCELLATION_CAUSE_HOLDER.cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
   }

   private static final class CauseHolder {
      final Throwable cause;

      private CauseHolder(Throwable var1) {
         this.cause = var1;
      }

      CauseHolder(Throwable var1, Object var2) {
         this(var1);
      }
   }
}
