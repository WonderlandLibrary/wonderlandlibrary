package io.netty.channel.embedded;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;

public class EmbeddedChannel extends AbstractChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private final EmbeddedEventLoop loop = new EmbeddedEventLoop();
   private final ChannelConfig config = new DefaultChannelConfig(this);
   private final SocketAddress localAddress = new EmbeddedSocketAddress();
   private final SocketAddress remoteAddress = new EmbeddedSocketAddress();
   private final Queue inboundMessages = new ArrayDeque();
   private final Queue outboundMessages = new ArrayDeque();
   private Throwable lastException;
   private int state;
   static final boolean $assertionsDisabled = !EmbeddedChannel.class.desiredAssertionStatus();

   public EmbeddedChannel(ChannelHandler... var1) {
      super((Channel)null);
      if (var1 == null) {
         throw new NullPointerException("handlers");
      } else {
         int var2 = 0;
         ChannelPipeline var3 = this.pipeline();
         ChannelHandler[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ChannelHandler var7 = var4[var6];
            if (var7 == null) {
               break;
            }

            ++var2;
            var3.addLast(var7);
         }

         if (var2 == 0) {
            throw new IllegalArgumentException("handlers is empty.");
         } else {
            var3.addLast(new EmbeddedChannel.LastInboundHandler(this));
            this.loop.register(this);
         }
      }
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public ChannelConfig config() {
      return this.config;
   }

   public Queue inboundMessages() {
      return this.inboundMessages;
   }

   /** @deprecated */
   @Deprecated
   public Queue lastInboundBuffer() {
      return this.inboundMessages();
   }

   public Queue outboundMessages() {
      return this.outboundMessages;
   }

   /** @deprecated */
   @Deprecated
   public Queue lastOutboundBuffer() {
      return this.outboundMessages();
   }

   public Object readInbound() {
      return this.inboundMessages.poll();
   }

   public Object readOutbound() {
      return this.outboundMessages.poll();
   }

   public boolean writeInbound(Object... var1) {
      this.ensureOpen();
      if (var1.length == 0) {
         return !this.inboundMessages.isEmpty();
      } else {
         ChannelPipeline var2 = this.pipeline();
         Object[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            var2.fireChannelRead(var6);
         }

         var2.fireChannelReadComplete();
         this.runPendingTasks();
         this.checkException();
         return !this.inboundMessages.isEmpty();
      }
   }

   public boolean writeOutbound(Object... var1) {
      this.ensureOpen();
      if (var1.length == 0) {
         return !this.outboundMessages.isEmpty();
      } else {
         RecyclableArrayList var2 = RecyclableArrayList.newInstance(var1.length);
         Object[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            if (var6 == null) {
               break;
            }

            var2.add(this.write(var6));
         }

         this.flush();
         int var8 = var2.size();

         for(var4 = 0; var4 < var8; ++var4) {
            ChannelFuture var10 = (ChannelFuture)var2.get(var4);
            if (!$assertionsDisabled && !var10.isDone()) {
               throw new AssertionError();
            }

            if (var10.cause() != null) {
               this.recordException(var10.cause());
            }
         }

         this.runPendingTasks();
         this.checkException();
         boolean var9 = !this.outboundMessages.isEmpty();
         var2.recycle();
         return var9;
      }
   }

   public boolean finish() {
      this.close();
      this.runPendingTasks();
      this.checkException();
      return !this.inboundMessages.isEmpty() || !this.outboundMessages.isEmpty();
   }

   public void runPendingTasks() {
      try {
         this.loop.runTasks();
      } catch (Exception var2) {
         this.recordException(var2);
      }

   }

   private void recordException(Throwable var1) {
      if (this.lastException == null) {
         this.lastException = var1;
      } else {
         logger.warn("More than one exception was raised. Will report only the first one and log others.", var1);
      }

   }

   public void checkException() {
      Throwable var1 = this.lastException;
      if (var1 != null) {
         this.lastException = null;
         PlatformDependent.throwException(var1);
      }
   }

   protected final void ensureOpen() {
      // $FF: Couldn't be decompiled
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof EmbeddedEventLoop;
   }

   protected SocketAddress localAddress0() {
      // $FF: Couldn't be decompiled
   }

   protected SocketAddress remoteAddress0() {
      // $FF: Couldn't be decompiled
   }

   protected void doRegister() throws Exception {
      this.state = 1;
   }

   protected void doBind(SocketAddress var1) throws Exception {
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      this.state = 2;
   }

   protected void doBeginRead() throws Exception {
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new EmbeddedChannel.DefaultUnsafe(this);
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         Object var2 = var1.current();
         if (var2 == null) {
            return;
         }

         ReferenceCountUtil.retain(var2);
         this.outboundMessages.add(var2);
         var1.remove();
      }
   }

   static Queue access$200(EmbeddedChannel var0) {
      return var0.inboundMessages;
   }

   static void access$300(EmbeddedChannel var0, Throwable var1) {
      var0.recordException(var1);
   }

   private final class LastInboundHandler extends ChannelInboundHandlerAdapter {
      final EmbeddedChannel this$0;

      private LastInboundHandler(EmbeddedChannel var1) {
         this.this$0 = var1;
      }

      public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
         EmbeddedChannel.access$200(this.this$0).add(var2);
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
         EmbeddedChannel.access$300(this.this$0, var2);
      }

      LastInboundHandler(EmbeddedChannel var1, Object var2) {
         this(var1);
      }
   }

   private class DefaultUnsafe extends AbstractChannel.AbstractUnsafe {
      final EmbeddedChannel this$0;

      private DefaultUnsafe(EmbeddedChannel var1) {
         super();
         this.this$0 = var1;
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         var3.setSuccess();
      }

      DefaultUnsafe(EmbeddedChannel var1, Object var2) {
         this(var1);
      }
   }
}
