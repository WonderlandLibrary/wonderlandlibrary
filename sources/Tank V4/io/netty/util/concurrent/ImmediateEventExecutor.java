package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public final class ImmediateEventExecutor extends AbstractEventExecutor {
   public static final ImmediateEventExecutor INSTANCE = new ImmediateEventExecutor();
   private final Future terminationFuture;

   private ImmediateEventExecutor() {
      this.terminationFuture = new FailedFuture(GlobalEventExecutor.INSTANCE, new UnsupportedOperationException());
   }

   public EventExecutorGroup parent() {
      return null;
   }

   public boolean inEventLoop() {
      return true;
   }

   public boolean inEventLoop(Thread var1) {
      return true;
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
         throw new NullPointerException("command");
      } else {
         var1.run();
      }
   }

   public Promise newPromise() {
      return new ImmediateEventExecutor.ImmediatePromise(this);
   }

   public ProgressivePromise newProgressivePromise() {
      return new ImmediateEventExecutor.ImmediateProgressivePromise(this);
   }

   static class ImmediateProgressivePromise extends DefaultProgressivePromise {
      ImmediateProgressivePromise(EventExecutor var1) {
         super(var1);
      }

      protected void checkDeadLock() {
      }
   }

   static class ImmediatePromise extends DefaultPromise {
      ImmediatePromise(EventExecutor var1) {
         super(var1);
      }

      protected void checkDeadLock() {
      }
   }
}
