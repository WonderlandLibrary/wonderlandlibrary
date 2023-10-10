package io.netty.util.concurrent;

public class DefaultProgressivePromise extends DefaultPromise implements ProgressivePromise {
   public DefaultProgressivePromise(EventExecutor var1) {
      super(var1);
   }

   protected DefaultProgressivePromise() {
   }

   public ProgressivePromise setProgress(long var1, long var3) {
      if (var3 < 0L) {
         var3 = -1L;
         if (var1 < 0L) {
            throw new IllegalArgumentException("progress: " + var1 + " (expected: >= 0)");
         }
      } else if (var1 < 0L || var1 > var3) {
         throw new IllegalArgumentException("progress: " + var1 + " (expected: 0 <= progress <= total (" + var3 + "))");
      }

      if (this.isDone()) {
         throw new IllegalStateException("complete already");
      } else {
         this.notifyProgressiveListeners(var1, var3);
         return this;
      }
   }

   public boolean tryProgress(long var1, long var3) {
      if (var3 < 0L) {
         var3 = -1L;
         if (var1 < 0L || this.isDone()) {
            return false;
         }
      } else if (var1 < 0L || var1 > var3 || this.isDone()) {
         return false;
      }

      this.notifyProgressiveListeners(var1, var3);
      return true;
   }

   public ProgressivePromise addListener(GenericFutureListener var1) {
      super.addListener(var1);
      return this;
   }

   public ProgressivePromise addListeners(GenericFutureListener... var1) {
      super.addListeners(var1);
      return this;
   }

   public ProgressivePromise removeListener(GenericFutureListener var1) {
      super.removeListener(var1);
      return this;
   }

   public ProgressivePromise removeListeners(GenericFutureListener... var1) {
      super.removeListeners(var1);
      return this;
   }

   public ProgressivePromise sync() throws InterruptedException {
      super.sync();
      return this;
   }

   public ProgressivePromise syncUninterruptibly() {
      super.syncUninterruptibly();
      return this;
   }

   public ProgressivePromise await() throws InterruptedException {
      super.await();
      return this;
   }

   public ProgressivePromise awaitUninterruptibly() {
      super.awaitUninterruptibly();
      return this;
   }

   public ProgressivePromise setSuccess(Object var1) {
      super.setSuccess(var1);
      return this;
   }

   public ProgressivePromise setFailure(Throwable var1) {
      super.setFailure(var1);
      return this;
   }

   public Promise setFailure(Throwable var1) {
      return this.setFailure(var1);
   }

   public Promise setSuccess(Object var1) {
      return this.setSuccess(var1);
   }

   public Promise awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   public Promise await() throws InterruptedException {
      return this.await();
   }

   public Promise syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   public Promise sync() throws InterruptedException {
      return this.sync();
   }

   public Promise removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   public Promise removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   public Promise addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   public Promise addListener(GenericFutureListener var1) {
      return this.addListener(var1);
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

   public ProgressiveFuture awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   public ProgressiveFuture await() throws InterruptedException {
      return this.await();
   }

   public ProgressiveFuture syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   public ProgressiveFuture sync() throws InterruptedException {
      return this.sync();
   }

   public ProgressiveFuture removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   public ProgressiveFuture removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   public ProgressiveFuture addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   public ProgressiveFuture addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }
}
