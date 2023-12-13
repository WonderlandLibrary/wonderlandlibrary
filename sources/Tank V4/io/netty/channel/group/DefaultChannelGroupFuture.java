package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.Promise;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

final class DefaultChannelGroupFuture extends DefaultPromise implements ChannelGroupFuture {
   private final ChannelGroup group;
   private final Map futures;
   private int successCount;
   private int failureCount;
   private final ChannelFutureListener childListener = new ChannelFutureListener(this) {
      static final boolean $assertionsDisabled = !DefaultChannelGroupFuture.class.desiredAssertionStatus();
      final DefaultChannelGroupFuture this$0;

      {
         this.this$0 = var1;
      }

      public void operationComplete(ChannelFuture var1) throws Exception {
         boolean var2 = var1.isSuccess();
         DefaultChannelGroupFuture var4;
         synchronized(var4 = this.this$0){}
         if (var2) {
            DefaultChannelGroupFuture.access$008(this.this$0);
         } else {
            DefaultChannelGroupFuture.access$108(this.this$0);
         }

         boolean var3 = DefaultChannelGroupFuture.access$000(this.this$0) + DefaultChannelGroupFuture.access$100(this.this$0) == DefaultChannelGroupFuture.access$200(this.this$0).size();
         if (!$assertionsDisabled && DefaultChannelGroupFuture.access$000(this.this$0) + DefaultChannelGroupFuture.access$100(this.this$0) > DefaultChannelGroupFuture.access$200(this.this$0).size()) {
            throw new AssertionError();
         } else {
            if (var3) {
               if (DefaultChannelGroupFuture.access$100(this.this$0) > 0) {
                  ArrayList var7 = new ArrayList(DefaultChannelGroupFuture.access$100(this.this$0));
                  Iterator var5 = DefaultChannelGroupFuture.access$200(this.this$0).values().iterator();

                  while(var5.hasNext()) {
                     ChannelFuture var6 = (ChannelFuture)var5.next();
                     if (!var6.isSuccess()) {
                        var7.add(new DefaultChannelGroupFuture.DefaultEntry(var6.channel(), var6.cause()));
                     }
                  }

                  DefaultChannelGroupFuture.access$300(this.this$0, new ChannelGroupException(var7));
               } else {
                  DefaultChannelGroupFuture.access$400(this.this$0);
               }
            }

         }
      }

      public void operationComplete(Future var1) throws Exception {
         this.operationComplete((ChannelFuture)var1);
      }
   };

   public DefaultChannelGroupFuture(ChannelGroup var1, Collection var2, EventExecutor var3) {
      super(var3);
      if (var1 == null) {
         throw new NullPointerException("group");
      } else if (var2 == null) {
         throw new NullPointerException("futures");
      } else {
         this.group = var1;
         LinkedHashMap var4 = new LinkedHashMap();
         Iterator var5 = var2.iterator();

         ChannelFuture var6;
         while(var5.hasNext()) {
            var6 = (ChannelFuture)var5.next();
            var4.put(var6.channel(), var6);
         }

         this.futures = Collections.unmodifiableMap(var4);
         var5 = this.futures.values().iterator();

         while(var5.hasNext()) {
            var6 = (ChannelFuture)var5.next();
            var6.addListener(this.childListener);
         }

         if (this.futures.isEmpty()) {
            this.setSuccess0();
         }

      }
   }

   DefaultChannelGroupFuture(ChannelGroup var1, Map var2, EventExecutor var3) {
      super(var3);
      this.group = var1;
      this.futures = Collections.unmodifiableMap(var2);
      Iterator var4 = this.futures.values().iterator();

      while(var4.hasNext()) {
         ChannelFuture var5 = (ChannelFuture)var4.next();
         var5.addListener(this.childListener);
      }

      if (this.futures.isEmpty()) {
         this.setSuccess0();
      }

   }

   public ChannelGroup group() {
      return this.group;
   }

   public ChannelFuture find(Channel var1) {
      return (ChannelFuture)this.futures.get(var1);
   }

   public Iterator iterator() {
      return this.futures.values().iterator();
   }

   public synchronized boolean isPartialSuccess() {
      return this.successCount != 0 && this.successCount != this.futures.size();
   }

   public synchronized boolean isPartialFailure() {
      return this.failureCount != 0 && this.failureCount != this.futures.size();
   }

   public DefaultChannelGroupFuture addListener(GenericFutureListener var1) {
      super.addListener(var1);
      return this;
   }

   public DefaultChannelGroupFuture addListeners(GenericFutureListener... var1) {
      super.addListeners(var1);
      return this;
   }

   public DefaultChannelGroupFuture removeListener(GenericFutureListener var1) {
      super.removeListener(var1);
      return this;
   }

   public DefaultChannelGroupFuture removeListeners(GenericFutureListener... var1) {
      super.removeListeners(var1);
      return this;
   }

   public DefaultChannelGroupFuture await() throws InterruptedException {
      super.await();
      return this;
   }

   public DefaultChannelGroupFuture awaitUninterruptibly() {
      super.awaitUninterruptibly();
      return this;
   }

   public DefaultChannelGroupFuture syncUninterruptibly() {
      super.syncUninterruptibly();
      return this;
   }

   public DefaultChannelGroupFuture sync() throws InterruptedException {
      super.sync();
      return this;
   }

   public ChannelGroupException cause() {
      return (ChannelGroupException)super.cause();
   }

   private void setSuccess0() {
      super.setSuccess((Object)null);
   }

   private void setFailure0(ChannelGroupException var1) {
      super.setFailure(var1);
   }

   public DefaultChannelGroupFuture setSuccess(Void var1) {
      throw new IllegalStateException();
   }

   public boolean trySuccess(Void var1) {
      throw new IllegalStateException();
   }

   public DefaultChannelGroupFuture setFailure(Throwable var1) {
      throw new IllegalStateException();
   }

   public boolean tryFailure(Throwable var1) {
      throw new IllegalStateException();
   }

   protected void checkDeadLock() {
      EventExecutor var1 = this.executor();
      if (var1 != null && var1 != ImmediateEventExecutor.INSTANCE && var1.inEventLoop()) {
         throw new BlockingOperationException();
      }
   }

   public Promise setFailure(Throwable var1) {
      return this.setFailure(var1);
   }

   public boolean trySuccess(Object var1) {
      return this.trySuccess((Void)var1);
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

   public Throwable cause() {
      return this.cause();
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

   public ChannelGroupFuture sync() throws InterruptedException {
      return this.sync();
   }

   public ChannelGroupFuture syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   public ChannelGroupFuture awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   public ChannelGroupFuture await() throws InterruptedException {
      return this.await();
   }

   public ChannelGroupFuture removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   public ChannelGroupFuture removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   public ChannelGroupFuture addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   public ChannelGroupFuture addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   static int access$008(DefaultChannelGroupFuture var0) {
      return var0.successCount++;
   }

   static int access$108(DefaultChannelGroupFuture var0) {
      return var0.failureCount++;
   }

   static int access$000(DefaultChannelGroupFuture var0) {
      return var0.successCount;
   }

   static int access$100(DefaultChannelGroupFuture var0) {
      return var0.failureCount;
   }

   static Map access$200(DefaultChannelGroupFuture var0) {
      return var0.futures;
   }

   static void access$300(DefaultChannelGroupFuture var0, ChannelGroupException var1) {
      var0.setFailure0(var1);
   }

   static void access$400(DefaultChannelGroupFuture var0) {
      var0.setSuccess0();
   }

   private static final class DefaultEntry implements Entry {
      private final Object key;
      private final Object value;

      public DefaultEntry(Object var1, Object var2) {
         this.key = var1;
         this.value = var2;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException("read-only");
      }
   }
}
