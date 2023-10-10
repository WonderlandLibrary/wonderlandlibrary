package io.netty.channel;

import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

abstract class CompleteChannelFuture extends CompleteFuture implements ChannelFuture {
   private final Channel channel;

   protected CompleteChannelFuture(Channel var1, EventExecutor var2) {
      super(var2);
      if (var1 == null) {
         throw new NullPointerException("channel");
      } else {
         this.channel = var1;
      }
   }

   protected EventExecutor executor() {
      EventExecutor var1 = super.executor();
      return (EventExecutor)(var1 == null ? this.channel().eventLoop() : var1);
   }

   public ChannelFuture addListener(GenericFutureListener var1) {
      super.addListener(var1);
      return this;
   }

   public ChannelFuture addListeners(GenericFutureListener... var1) {
      super.addListeners(var1);
      return this;
   }

   public ChannelFuture removeListener(GenericFutureListener var1) {
      super.removeListener(var1);
      return this;
   }

   public ChannelFuture removeListeners(GenericFutureListener... var1) {
      super.removeListeners(var1);
      return this;
   }

   public ChannelFuture syncUninterruptibly() {
      return this;
   }

   public ChannelFuture sync() throws InterruptedException {
      return this;
   }

   public ChannelFuture await() throws InterruptedException {
      return this;
   }

   public ChannelFuture awaitUninterruptibly() {
      return this;
   }

   public Channel channel() {
      return this.channel;
   }

   public Void getNow() {
      return null;
   }

   public Future awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   public Future syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   public Future sync() throws InterruptedException {
      return this.sync();
   }

   public Future await() throws InterruptedException {
      return this.await();
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

   public Object getNow() {
      return this.getNow();
   }
}
