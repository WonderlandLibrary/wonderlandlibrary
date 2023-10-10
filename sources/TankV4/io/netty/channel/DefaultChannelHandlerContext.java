package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

final class DefaultChannelHandlerContext extends DefaultAttributeMap implements ChannelHandlerContext {
   volatile DefaultChannelHandlerContext next;
   volatile DefaultChannelHandlerContext prev;
   private final boolean inbound;
   private final boolean outbound;
   private final AbstractChannel channel;
   private final DefaultChannelPipeline pipeline;
   private final String name;
   private final ChannelHandler handler;
   private boolean removed;
   final EventExecutor executor;
   private ChannelFuture succeededFuture;
   private Runnable invokeChannelReadCompleteTask;
   private Runnable invokeReadTask;
   private Runnable invokeFlushTask;
   private Runnable invokeChannelWritableStateChangedTask;

   DefaultChannelHandlerContext(DefaultChannelPipeline var1, EventExecutorGroup var2, String var3, ChannelHandler var4) {
      if (var3 == null) {
         throw new NullPointerException("name");
      } else if (var4 == null) {
         throw new NullPointerException("handler");
      } else {
         this.channel = var1.channel;
         this.pipeline = var1;
         this.name = var3;
         this.handler = var4;
         if (var2 != null) {
            EventExecutor var5 = (EventExecutor)var1.childExecutors.get(var2);
            if (var5 == null) {
               var5 = var2.next();
               var1.childExecutors.put(var2, var5);
            }

            this.executor = var5;
         } else {
            this.executor = null;
         }

         this.inbound = var4 instanceof ChannelInboundHandler;
         this.outbound = var4 instanceof ChannelOutboundHandler;
      }
   }

   void teardown() {
      EventExecutor var1 = this.executor();
      if (var1.inEventLoop()) {
         this.teardown0();
      } else {
         var1.execute(new Runnable(this) {
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               DefaultChannelHandlerContext.access$000(this.this$0);
            }
         });
      }

   }

   private void teardown0() {
      DefaultChannelHandlerContext var1 = this.prev;
      if (var1 != null) {
         DefaultChannelPipeline var2;
         synchronized(var2 = this.pipeline){}
         this.pipeline.remove0(this);
         var1.teardown();
      }

   }

   public Channel channel() {
      return this.channel;
   }

   public ChannelPipeline pipeline() {
      return this.pipeline;
   }

   public ByteBufAllocator alloc() {
      return this.channel().config().getAllocator();
   }

   public EventExecutor executor() {
      return (EventExecutor)(this.executor == null ? this.channel().eventLoop() : this.executor);
   }

   public ChannelHandler handler() {
      return this.handler;
   }

   public String name() {
      return this.name;
   }

   public ChannelHandlerContext fireChannelRegistered() {
      DefaultChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeChannelRegistered();
      } else {
         var2.execute(new Runnable(this, var1) {
            final DefaultChannelHandlerContext val$next;
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
               this.val$next = var2;
            }

            public void run() {
               DefaultChannelHandlerContext.access$100(this.val$next);
            }
         });
      }

      return this;
   }

   private void invokeChannelRegistered() {
      try {
         ((ChannelInboundHandler)this.handler).channelRegistered(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelUnregistered() {
      DefaultChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeChannelUnregistered();
      } else {
         var2.execute(new Runnable(this, var1) {
            final DefaultChannelHandlerContext val$next;
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
               this.val$next = var2;
            }

            public void run() {
               DefaultChannelHandlerContext.access$200(this.val$next);
            }
         });
      }

      return this;
   }

   private void invokeChannelUnregistered() {
      try {
         ((ChannelInboundHandler)this.handler).channelUnregistered(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelActive() {
      DefaultChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeChannelActive();
      } else {
         var2.execute(new Runnable(this, var1) {
            final DefaultChannelHandlerContext val$next;
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
               this.val$next = var2;
            }

            public void run() {
               DefaultChannelHandlerContext.access$300(this.val$next);
            }
         });
      }

      return this;
   }

   private void invokeChannelActive() {
      try {
         ((ChannelInboundHandler)this.handler).channelActive(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelInactive() {
      DefaultChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeChannelInactive();
      } else {
         var2.execute(new Runnable(this, var1) {
            final DefaultChannelHandlerContext val$next;
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
               this.val$next = var2;
            }

            public void run() {
               DefaultChannelHandlerContext.access$400(this.val$next);
            }
         });
      }

      return this;
   }

   private void invokeChannelInactive() {
      try {
         ((ChannelInboundHandler)this.handler).channelInactive(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireExceptionCaught(Throwable var1) {
      if (var1 == null) {
         throw new NullPointerException("cause");
      } else {
         DefaultChannelHandlerContext var2 = this.next;
         EventExecutor var3 = var2.executor();
         if (var3.inEventLoop()) {
            var2.invokeExceptionCaught(var1);
         } else {
            try {
               var3.execute(new Runnable(this, var2, var1) {
                  final DefaultChannelHandlerContext val$next;
                  final Throwable val$cause;
                  final DefaultChannelHandlerContext this$0;

                  {
                     this.this$0 = var1;
                     this.val$next = var2;
                     this.val$cause = var3;
                  }

                  public void run() {
                     DefaultChannelHandlerContext.access$500(this.val$next, this.val$cause);
                  }
               });
            } catch (Throwable var5) {
               if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                  DefaultChannelPipeline.logger.warn("Failed to submit an exceptionCaught() event.", var5);
                  DefaultChannelPipeline.logger.warn("The exceptionCaught() event that was failed to submit was:", var1);
               }
            }
         }

         return this;
      }
   }

   private void invokeExceptionCaught(Throwable var1) {
      try {
         this.handler.exceptionCaught(this, var1);
      } catch (Throwable var3) {
         if (DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler's exceptionCaught() method while handling the following exception:", var1);
         }
      }

   }

   public ChannelHandlerContext fireUserEventTriggered(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("event");
      } else {
         DefaultChannelHandlerContext var2 = this.findContextInbound();
         EventExecutor var3 = var2.executor();
         if (var3.inEventLoop()) {
            var2.invokeUserEventTriggered(var1);
         } else {
            var3.execute(new Runnable(this, var2, var1) {
               final DefaultChannelHandlerContext val$next;
               final Object val$event;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
                  this.val$event = var3;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$600(this.val$next, this.val$event);
               }
            });
         }

         return this;
      }
   }

   private void invokeUserEventTriggered(Object var1) {
      try {
         ((ChannelInboundHandler)this.handler).userEventTriggered(this, var1);
      } catch (Throwable var3) {
         this.notifyHandlerException(var3);
      }

   }

   public ChannelHandlerContext fireChannelRead(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("msg");
      } else {
         DefaultChannelHandlerContext var2 = this.findContextInbound();
         EventExecutor var3 = var2.executor();
         if (var3.inEventLoop()) {
            var2.invokeChannelRead(var1);
         } else {
            var3.execute(new Runnable(this, var2, var1) {
               final DefaultChannelHandlerContext val$next;
               final Object val$msg;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
                  this.val$msg = var3;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$700(this.val$next, this.val$msg);
               }
            });
         }

         return this;
      }
   }

   private void invokeChannelRead(Object var1) {
      try {
         ((ChannelInboundHandler)this.handler).channelRead(this, var1);
      } catch (Throwable var3) {
         this.notifyHandlerException(var3);
      }

   }

   public ChannelHandlerContext fireChannelReadComplete() {
      DefaultChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeChannelReadComplete();
      } else {
         Runnable var3 = var1.invokeChannelReadCompleteTask;
         if (var3 == null) {
            var1.invokeChannelReadCompleteTask = var3 = new Runnable(this, var1) {
               final DefaultChannelHandlerContext val$next;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$800(this.val$next);
               }
            };
         }

         var2.execute(var3);
      }

      return this;
   }

   private void invokeChannelReadComplete() {
      try {
         ((ChannelInboundHandler)this.handler).channelReadComplete(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelWritabilityChanged() {
      DefaultChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeChannelWritabilityChanged();
      } else {
         Runnable var3 = var1.invokeChannelWritableStateChangedTask;
         if (var3 == null) {
            var1.invokeChannelWritableStateChangedTask = var3 = new Runnable(this, var1) {
               final DefaultChannelHandlerContext val$next;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$900(this.val$next);
               }
            };
         }

         var2.execute(var3);
      }

      return this;
   }

   private void invokeChannelWritabilityChanged() {
      try {
         ((ChannelInboundHandler)this.handler).channelWritabilityChanged(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelFuture bind(SocketAddress var1) {
      return this.bind(var1, this.newPromise());
   }

   public ChannelFuture connect(SocketAddress var1) {
      return this.connect(var1, this.newPromise());
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2) {
      return this.connect(var1, var2, this.newPromise());
   }

   public ChannelFuture disconnect() {
      return this.disconnect(this.newPromise());
   }

   public ChannelFuture close() {
      return this.close(this.newPromise());
   }

   public ChannelFuture deregister() {
      return this.deregister(this.newPromise());
   }

   public ChannelFuture bind(SocketAddress var1, ChannelPromise var2) {
      if (var1 == null) {
         throw new NullPointerException("localAddress");
      } else {
         this.validatePromise(var2, false);
         DefaultChannelHandlerContext var3 = this.findContextOutbound();
         EventExecutor var4 = var3.executor();
         if (var4.inEventLoop()) {
            var3.invokeBind(var1, var2);
         } else {
            safeExecute(var4, new Runnable(this, var3, var1, var2) {
               final DefaultChannelHandlerContext val$next;
               final SocketAddress val$localAddress;
               final ChannelPromise val$promise;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
                  this.val$localAddress = var3;
                  this.val$promise = var4;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$1000(this.val$next, this.val$localAddress, this.val$promise);
               }
            }, var2, (Object)null);
         }

         return var2;
      }
   }

   private void invokeBind(SocketAddress var1, ChannelPromise var2) {
      try {
         ((ChannelOutboundHandler)this.handler).bind(this, var1, var2);
      } catch (Throwable var4) {
         notifyOutboundHandlerException(var4, var2);
      }

   }

   public ChannelFuture connect(SocketAddress var1, ChannelPromise var2) {
      return this.connect(var1, (SocketAddress)null, var2);
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
      if (var1 == null) {
         throw new NullPointerException("remoteAddress");
      } else {
         this.validatePromise(var3, false);
         DefaultChannelHandlerContext var4 = this.findContextOutbound();
         EventExecutor var5 = var4.executor();
         if (var5.inEventLoop()) {
            var4.invokeConnect(var1, var2, var3);
         } else {
            safeExecute(var5, new Runnable(this, var4, var1, var2, var3) {
               final DefaultChannelHandlerContext val$next;
               final SocketAddress val$remoteAddress;
               final SocketAddress val$localAddress;
               final ChannelPromise val$promise;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
                  this.val$remoteAddress = var3;
                  this.val$localAddress = var4;
                  this.val$promise = var5;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$1100(this.val$next, this.val$remoteAddress, this.val$localAddress, this.val$promise);
               }
            }, var3, (Object)null);
         }

         return var3;
      }
   }

   private void invokeConnect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
      try {
         ((ChannelOutboundHandler)this.handler).connect(this, var1, var2, var3);
      } catch (Throwable var5) {
         notifyOutboundHandlerException(var5, var3);
      }

   }

   public ChannelFuture disconnect(ChannelPromise var1) {
      this.validatePromise(var1, false);
      DefaultChannelHandlerContext var2 = this.findContextOutbound();
      EventExecutor var3 = var2.executor();
      if (var3.inEventLoop()) {
         if (!this.channel().metadata().hasDisconnect()) {
            var2.invokeClose(var1);
         } else {
            var2.invokeDisconnect(var1);
         }
      } else {
         safeExecute(var3, new Runnable(this, var2, var1) {
            final DefaultChannelHandlerContext val$next;
            final ChannelPromise val$promise;
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
               this.val$next = var2;
               this.val$promise = var3;
            }

            public void run() {
               if (!this.this$0.channel().metadata().hasDisconnect()) {
                  DefaultChannelHandlerContext.access$1200(this.val$next, this.val$promise);
               } else {
                  DefaultChannelHandlerContext.access$1300(this.val$next, this.val$promise);
               }

            }
         }, var1, (Object)null);
      }

      return var1;
   }

   private void invokeDisconnect(ChannelPromise var1) {
      try {
         ((ChannelOutboundHandler)this.handler).disconnect(this, var1);
      } catch (Throwable var3) {
         notifyOutboundHandlerException(var3, var1);
      }

   }

   public ChannelFuture close(ChannelPromise var1) {
      this.validatePromise(var1, false);
      DefaultChannelHandlerContext var2 = this.findContextOutbound();
      EventExecutor var3 = var2.executor();
      if (var3.inEventLoop()) {
         var2.invokeClose(var1);
      } else {
         safeExecute(var3, new Runnable(this, var2, var1) {
            final DefaultChannelHandlerContext val$next;
            final ChannelPromise val$promise;
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
               this.val$next = var2;
               this.val$promise = var3;
            }

            public void run() {
               DefaultChannelHandlerContext.access$1200(this.val$next, this.val$promise);
            }
         }, var1, (Object)null);
      }

      return var1;
   }

   private void invokeClose(ChannelPromise var1) {
      try {
         ((ChannelOutboundHandler)this.handler).close(this, var1);
      } catch (Throwable var3) {
         notifyOutboundHandlerException(var3, var1);
      }

   }

   public ChannelFuture deregister(ChannelPromise var1) {
      this.validatePromise(var1, false);
      DefaultChannelHandlerContext var2 = this.findContextOutbound();
      EventExecutor var3 = var2.executor();
      if (var3.inEventLoop()) {
         var2.invokeDeregister(var1);
      } else {
         safeExecute(var3, new Runnable(this, var2, var1) {
            final DefaultChannelHandlerContext val$next;
            final ChannelPromise val$promise;
            final DefaultChannelHandlerContext this$0;

            {
               this.this$0 = var1;
               this.val$next = var2;
               this.val$promise = var3;
            }

            public void run() {
               DefaultChannelHandlerContext.access$1400(this.val$next, this.val$promise);
            }
         }, var1, (Object)null);
      }

      return var1;
   }

   private void invokeDeregister(ChannelPromise var1) {
      try {
         ((ChannelOutboundHandler)this.handler).deregister(this, var1);
      } catch (Throwable var3) {
         notifyOutboundHandlerException(var3, var1);
      }

   }

   public ChannelHandlerContext read() {
      DefaultChannelHandlerContext var1 = this.findContextOutbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeRead();
      } else {
         Runnable var3 = var1.invokeReadTask;
         if (var3 == null) {
            var1.invokeReadTask = var3 = new Runnable(this, var1) {
               final DefaultChannelHandlerContext val$next;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$1500(this.val$next);
               }
            };
         }

         var2.execute(var3);
      }

      return this;
   }

   private void invokeRead() {
      try {
         ((ChannelOutboundHandler)this.handler).read(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelFuture write(Object var1) {
      return this.write(var1, this.newPromise());
   }

   public ChannelFuture write(Object var1, ChannelPromise var2) {
      if (var1 == null) {
         throw new NullPointerException("msg");
      } else {
         this.validatePromise(var2, true);
         this.write(var1, false, var2);
         return var2;
      }
   }

   private void invokeWrite(Object var1, ChannelPromise var2) {
      try {
         ((ChannelOutboundHandler)this.handler).write(this, var1, var2);
      } catch (Throwable var4) {
         notifyOutboundHandlerException(var4, var2);
      }

   }

   public ChannelHandlerContext flush() {
      DefaultChannelHandlerContext var1 = this.findContextOutbound();
      EventExecutor var2 = var1.executor();
      if (var2.inEventLoop()) {
         var1.invokeFlush();
      } else {
         Runnable var3 = var1.invokeFlushTask;
         if (var3 == null) {
            var1.invokeFlushTask = var3 = new Runnable(this, var1) {
               final DefaultChannelHandlerContext val$next;
               final DefaultChannelHandlerContext this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
               }

               public void run() {
                  DefaultChannelHandlerContext.access$1600(this.val$next);
               }
            };
         }

         safeExecute(var2, var3, this.channel.voidPromise(), (Object)null);
      }

      return this;
   }

   private void invokeFlush() {
      try {
         ((ChannelOutboundHandler)this.handler).flush(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2) {
      if (var1 == null) {
         throw new NullPointerException("msg");
      } else {
         this.validatePromise(var2, true);
         this.write(var1, true, var2);
         return var2;
      }
   }

   private void write(Object var1, boolean var2, ChannelPromise var3) {
      DefaultChannelHandlerContext var4 = this.findContextOutbound();
      EventExecutor var5 = var4.executor();
      if (var5.inEventLoop()) {
         var4.invokeWrite(var1, var3);
         if (var2) {
            var4.invokeFlush();
         }
      } else {
         int var6 = this.channel.estimatorHandle().size(var1);
         if (var6 > 0) {
            ChannelOutboundBuffer var7 = this.channel.unsafe().outboundBuffer();
            if (var7 != null) {
               var7.incrementPendingOutboundBytes(var6);
            }
         }

         safeExecute(var5, DefaultChannelHandlerContext.WriteTask.access$1700(var4, var1, var6, var2, var3), var3, var1);
      }

   }

   public ChannelFuture writeAndFlush(Object var1) {
      return this.writeAndFlush(var1, this.newPromise());
   }

   private static void notifyOutboundHandlerException(Throwable var0, ChannelPromise var1) {
      if (!(var1 instanceof VoidChannelPromise)) {
         if (!var1.tryFailure(var0) && DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("Failed to fail the promise because it's done already: {}", var1, var0);
         }

      }
   }

   private void notifyHandlerException(Throwable var1) {
      if (var1 != null) {
         if (DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", var1);
         }

      } else {
         this.invokeExceptionCaught(var1);
      }
   }

   public ChannelPromise newPromise() {
      return new DefaultChannelPromise(this.channel(), this.executor());
   }

   public ChannelProgressivePromise newProgressivePromise() {
      return new DefaultChannelProgressivePromise(this.channel(), this.executor());
   }

   public ChannelFuture newSucceededFuture() {
      Object var1 = this.succeededFuture;
      if (var1 == null) {
         this.succeededFuture = (ChannelFuture)(var1 = new SucceededChannelFuture(this.channel(), this.executor()));
      }

      return (ChannelFuture)var1;
   }

   public ChannelFuture newFailedFuture(Throwable var1) {
      return new FailedChannelFuture(this.channel(), this.executor(), var1);
   }

   private void validatePromise(ChannelPromise var1, boolean var2) {
      if (var1 == null) {
         throw new NullPointerException("promise");
      } else if (var1.isDone()) {
         throw new IllegalArgumentException("promise already done: " + var1);
      } else if (var1.channel() != this.channel()) {
         throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", var1.channel(), this.channel()));
      } else if (var1.getClass() != DefaultChannelPromise.class) {
         if (!var2 && var1 instanceof VoidChannelPromise) {
            throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
         } else if (var1 instanceof AbstractChannel.CloseFuture) {
            throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
         }
      }
   }

   private DefaultChannelHandlerContext findContextInbound() {
      DefaultChannelHandlerContext var1 = this;

      do {
         var1 = var1.next;
      } while(!var1.inbound);

      return var1;
   }

   private DefaultChannelHandlerContext findContextOutbound() {
      DefaultChannelHandlerContext var1 = this;

      do {
         var1 = var1.prev;
      } while(!var1.outbound);

      return var1;
   }

   public ChannelPromise voidPromise() {
      return this.channel.voidPromise();
   }

   void setRemoved() {
      this.removed = true;
   }

   public boolean isRemoved() {
      return this.removed;
   }

   private static void safeExecute(EventExecutor var0, Runnable var1, ChannelPromise var2, Object var3) {
      try {
         var0.execute(var1);
      } catch (Throwable var6) {
         var2.setFailure(var6);
         if (var3 != null) {
            ReferenceCountUtil.release(var3);
         }
      }

   }

   static void access$000(DefaultChannelHandlerContext var0) {
      var0.teardown0();
   }

   static void access$100(DefaultChannelHandlerContext var0) {
      var0.invokeChannelRegistered();
   }

   static void access$200(DefaultChannelHandlerContext var0) {
      var0.invokeChannelUnregistered();
   }

   static void access$300(DefaultChannelHandlerContext var0) {
      var0.invokeChannelActive();
   }

   static void access$400(DefaultChannelHandlerContext var0) {
      var0.invokeChannelInactive();
   }

   static void access$500(DefaultChannelHandlerContext var0, Throwable var1) {
      var0.invokeExceptionCaught(var1);
   }

   static void access$600(DefaultChannelHandlerContext var0, Object var1) {
      var0.invokeUserEventTriggered(var1);
   }

   static void access$700(DefaultChannelHandlerContext var0, Object var1) {
      var0.invokeChannelRead(var1);
   }

   static void access$800(DefaultChannelHandlerContext var0) {
      var0.invokeChannelReadComplete();
   }

   static void access$900(DefaultChannelHandlerContext var0) {
      var0.invokeChannelWritabilityChanged();
   }

   static void access$1000(DefaultChannelHandlerContext var0, SocketAddress var1, ChannelPromise var2) {
      var0.invokeBind(var1, var2);
   }

   static void access$1100(DefaultChannelHandlerContext var0, SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
      var0.invokeConnect(var1, var2, var3);
   }

   static void access$1200(DefaultChannelHandlerContext var0, ChannelPromise var1) {
      var0.invokeClose(var1);
   }

   static void access$1300(DefaultChannelHandlerContext var0, ChannelPromise var1) {
      var0.invokeDisconnect(var1);
   }

   static void access$1400(DefaultChannelHandlerContext var0, ChannelPromise var1) {
      var0.invokeDeregister(var1);
   }

   static void access$1500(DefaultChannelHandlerContext var0) {
      var0.invokeRead();
   }

   static void access$1600(DefaultChannelHandlerContext var0) {
      var0.invokeFlush();
   }

   static AbstractChannel access$1900(DefaultChannelHandlerContext var0) {
      return var0.channel;
   }

   static void access$2000(DefaultChannelHandlerContext var0, Object var1, ChannelPromise var2) {
      var0.invokeWrite(var1, var2);
   }

   static final class WriteTask implements Runnable {
      private DefaultChannelHandlerContext ctx;
      private Object msg;
      private ChannelPromise promise;
      private int size;
      private boolean flush;
      private static final Recycler RECYCLER = new Recycler() {
         protected DefaultChannelHandlerContext.WriteTask newObject(Recycler.Handle var1) {
            return new DefaultChannelHandlerContext.WriteTask(var1);
         }

         protected Object newObject(Recycler.Handle var1) {
            return this.newObject(var1);
         }
      };
      private final Recycler.Handle handle;

      private static DefaultChannelHandlerContext.WriteTask newInstance(DefaultChannelHandlerContext var0, Object var1, int var2, boolean var3, ChannelPromise var4) {
         DefaultChannelHandlerContext.WriteTask var5 = (DefaultChannelHandlerContext.WriteTask)RECYCLER.get();
         var5.ctx = var0;
         var5.msg = var1;
         var5.promise = var4;
         var5.size = var2;
         var5.flush = var3;
         return var5;
      }

      private WriteTask(Recycler.Handle var1) {
         this.handle = var1;
      }

      public void run() {
         if (this.size > 0) {
            ChannelOutboundBuffer var1 = DefaultChannelHandlerContext.access$1900(this.ctx).unsafe().outboundBuffer();
            if (var1 != null) {
               var1.decrementPendingOutboundBytes(this.size);
            }
         }

         DefaultChannelHandlerContext.access$2000(this.ctx, this.msg, this.promise);
         if (this.flush) {
            DefaultChannelHandlerContext.access$1600(this.ctx);
         }

         this.ctx = null;
         this.msg = null;
         this.promise = null;
         RECYCLER.recycle(this, this.handle);
      }

      static DefaultChannelHandlerContext.WriteTask access$1700(DefaultChannelHandlerContext var0, Object var1, int var2, boolean var3, ChannelPromise var4) {
         return newInstance(var0, var1, var2, var3, var4);
      }

      WriteTask(Recycler.Handle var1, Object var2) {
         this(var1);
      }
   }
}
