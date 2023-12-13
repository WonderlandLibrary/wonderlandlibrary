package io.netty.channel;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

public class DefaultChannelPromise extends DefaultPromise implements ChannelPromise, ChannelFlushPromiseNotifier.FlushCheckpoint {
   private final Channel channel;
   private long checkpoint;

   public DefaultChannelPromise(Channel var1) {
      this.channel = var1;
   }

   public DefaultChannelPromise(Channel var1, EventExecutor var2) {
      super(var2);
      this.channel = var1;
   }

   protected EventExecutor executor() {
      EventExecutor var1 = super.executor();
      return (EventExecutor)(var1 == null ? this.channel().eventLoop() : var1);
   }

   public Channel channel() {
      return this.channel;
   }

   public ChannelPromise setSuccess() {
      return this.setSuccess((Void)null);
   }

   public ChannelPromise setSuccess(Void var1) {
      super.setSuccess(var1);
      return this;
   }

   public boolean trySuccess() {
      return this.trySuccess((Object)null);
   }

   public ChannelPromise setFailure(Throwable var1) {
      super.setFailure(var1);
      return this;
   }

   public ChannelPromise addListener(GenericFutureListener var1) {
      super.addListener(var1);
      return this;
   }

   public ChannelPromise addListeners(GenericFutureListener... var1) {
      super.addListeners(var1);
      return this;
   }

   public ChannelPromise removeListener(GenericFutureListener var1) {
      super.removeListener(var1);
      return this;
   }

   public ChannelPromise removeListeners(GenericFutureListener... var1) {
      super.removeListeners(var1);
      return this;
   }

   public ChannelPromise sync() throws InterruptedException {
      super.sync();
      return this;
   }

   public ChannelPromise syncUninterruptibly() {
      super.syncUninterruptibly();
      return this;
   }

   public ChannelPromise await() throws InterruptedException {
      super.await();
      return this;
   }

   public ChannelPromise awaitUninterruptibly() {
      super.awaitUninterruptibly();
      return this;
   }

   public long flushCheckpoint() {
      return this.checkpoint;
   }

   public void flushCheckpoint(long var1) {
      this.checkpoint = var1;
   }

   public ChannelPromise promise() {
      return this;
   }

   protected void checkDeadLock() {
      if (this.channel().isRegistered()) {
         super.checkDeadLock();
      }

   }

   public Promise setFailure(Throwable var1) {
      return this.setFailure(var1);
   }

   public Promise setSuccess(Object var1) {
      return this.setSuccess((Void)var1);
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

   public ChannelFuture awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   public ChannelFuture await() throws InterruptedException {
      return this.await();
   }

   public ChannelFuture syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   public ChannelFuture sync() throws InterruptedException {
      return this.sync();
   }

   public ChannelFuture removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   public ChannelFuture removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   public ChannelFuture addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   public ChannelFuture addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }
}
