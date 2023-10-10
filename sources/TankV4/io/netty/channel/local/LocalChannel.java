package io.netty.channel.local;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.net.SocketAddress;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public class LocalChannel extends AbstractChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private static final int MAX_READER_STACK_DEPTH = 8;
   private static final ThreadLocal READER_STACK_DEPTH = new ThreadLocal() {
      protected Integer initialValue() {
         return 0;
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private final ChannelConfig config = new DefaultChannelConfig(this);
   private final Queue inboundBuffer = new ArrayDeque();
   private final Runnable readTask = new Runnable(this) {
      final LocalChannel this$0;

      {
         this.this$0 = var1;
      }

      public void run() {
         ChannelPipeline var1 = this.this$0.pipeline();

         while(true) {
            Object var2 = LocalChannel.access$000(this.this$0).poll();
            if (var2 == null) {
               var1.fireChannelReadComplete();
               return;
            }

            var1.fireChannelRead(var2);
         }
      }
   };
   private final Runnable shutdownHook = new Runnable(this) {
      final LocalChannel this$0;

      {
         this.this$0 = var1;
      }

      public void run() {
         this.this$0.unsafe().close(this.this$0.unsafe().voidPromise());
      }
   };
   private volatile int state;
   private volatile LocalChannel peer;
   private volatile LocalAddress localAddress;
   private volatile LocalAddress remoteAddress;
   private volatile ChannelPromise connectPromise;
   private volatile boolean readInProgress;

   public LocalChannel() {
      super((Channel)null);
   }

   LocalChannel(LocalServerChannel var1, LocalChannel var2) {
      super(var1);
      this.peer = var2;
      this.localAddress = var1.localAddress();
      this.remoteAddress = var2.localAddress();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public ChannelConfig config() {
      return this.config;
   }

   public LocalServerChannel parent() {
      return (LocalServerChannel)super.parent();
   }

   public LocalAddress localAddress() {
      return (LocalAddress)super.localAddress();
   }

   public LocalAddress remoteAddress() {
      return (LocalAddress)super.remoteAddress();
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new LocalChannel.LocalUnsafe(this);
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof SingleThreadEventLoop;
   }

   protected SocketAddress localAddress0() {
      return this.localAddress;
   }

   protected SocketAddress remoteAddress0() {
      return this.remoteAddress;
   }

   protected void doRegister() throws Exception {
      if (this.peer != null) {
         this.state = 2;
         this.peer.remoteAddress = this.parent().localAddress();
         this.peer.state = 2;
         this.peer.eventLoop().execute(new Runnable(this) {
            final LocalChannel this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               LocalChannel.access$200(this.this$0).pipeline().fireChannelActive();
               LocalChannel.access$300(LocalChannel.access$200(this.this$0)).setSuccess();
            }
         });
      }

      ((SingleThreadEventExecutor)this.eventLoop()).addShutdownHook(this.shutdownHook);
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.localAddress = LocalChannelRegistry.register(this, this.localAddress, var1);
      this.state = 1;
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      // $FF: Couldn't be decompiled
   }

   protected void doDeregister() throws Exception {
      // $FF: Couldn't be decompiled
   }

   protected void doBeginRead() throws Exception {
      if (!this.readInProgress) {
         ChannelPipeline var1 = this.pipeline();
         Queue var2 = this.inboundBuffer;
         if (var2.isEmpty()) {
            this.readInProgress = true;
         } else {
            Integer var3 = (Integer)READER_STACK_DEPTH.get();
            if (var3 < 8) {
               READER_STACK_DEPTH.set(var3 + 1);

               while(true) {
                  Object var4 = var2.poll();
                  if (var4 == null) {
                     var1.fireChannelReadComplete();
                     READER_STACK_DEPTH.set(var3);
                     break;
                  }

                  var1.fireChannelRead(var4);
               }
            } else {
               this.eventLoop().execute(this.readTask);
            }

         }
      }
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      if (this.state < 2) {
         throw new NotYetConnectedException();
      } else if (this.state > 2) {
         throw new ClosedChannelException();
      } else {
         LocalChannel var2 = this.peer;
         ChannelPipeline var3 = var2.pipeline();
         EventLoop var4 = var2.eventLoop();
         if (var4 != this.eventLoop()) {
            Object[] var7 = new Object[var1.size()];

            for(int var6 = 0; var6 < var7.length; ++var6) {
               var7[var6] = ReferenceCountUtil.retain(var1.current());
               var1.remove();
            }

            var4.execute(new Runnable(this, var2, var7, var3) {
               final LocalChannel val$peer;
               final Object[] val$msgsCopy;
               final ChannelPipeline val$peerPipeline;
               final LocalChannel this$0;

               {
                  this.this$0 = var1;
                  this.val$peer = var2;
                  this.val$msgsCopy = var3;
                  this.val$peerPipeline = var4;
               }

               public void run() {
                  Collections.addAll(LocalChannel.access$000(this.val$peer), this.val$msgsCopy);
                  LocalChannel.access$400(this.val$peer, this.val$peerPipeline);
               }
            });
         } else {
            while(true) {
               Object var5 = var1.current();
               if (var5 == null) {
                  finishPeerRead(var2, var3);
                  break;
               }

               var2.inboundBuffer.add(var5);
               ReferenceCountUtil.retain(var5);
               var1.remove();
            }
         }

      }
   }

   private static void finishPeerRead(LocalChannel var0, ChannelPipeline var1) {
      if (var0.readInProgress) {
         var0.readInProgress = false;

         while(true) {
            Object var2 = var0.inboundBuffer.poll();
            if (var2 == null) {
               var1.fireChannelReadComplete();
               break;
            }

            var1.fireChannelRead(var2);
         }
      }

   }

   public SocketAddress remoteAddress() {
      return this.remoteAddress();
   }

   public SocketAddress localAddress() {
      return this.localAddress();
   }

   public Channel parent() {
      return this.parent();
   }

   static Queue access$000(LocalChannel var0) {
      return var0.inboundBuffer;
   }

   static LocalChannel access$200(LocalChannel var0) {
      return var0.peer;
   }

   static ChannelPromise access$300(LocalChannel var0) {
      return var0.connectPromise;
   }

   static void access$400(LocalChannel var0, ChannelPipeline var1) {
      finishPeerRead(var0, var1);
   }

   static int access$500(LocalChannel var0) {
      return var0.state;
   }

   static ChannelPromise access$302(LocalChannel var0, ChannelPromise var1) {
      return var0.connectPromise = var1;
   }

   static LocalChannel access$202(LocalChannel var0, LocalChannel var1) {
      return var0.peer = var1;
   }

   private class LocalUnsafe extends AbstractChannel.AbstractUnsafe {
      final LocalChannel this$0;

      private LocalUnsafe(LocalChannel var1) {
         super();
         this.this$0 = var1;
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if (this.ensureOpen(var3)) {
            if (LocalChannel.access$500(this.this$0) == 2) {
               AlreadyConnectedException var7 = new AlreadyConnectedException();
               var3.setFailure(var7);
               this.this$0.pipeline().fireExceptionCaught(var7);
            } else if (LocalChannel.access$300(this.this$0) != null) {
               throw new ConnectionPendingException();
            } else {
               LocalChannel.access$302(this.this$0, var3);
               if (LocalChannel.access$500(this.this$0) != 1 && var2 == null) {
                  var2 = new LocalAddress(this.this$0);
               }

               if (var2 != null) {
                  try {
                     this.this$0.doBind((SocketAddress)var2);
                  } catch (Throwable var6) {
                     var3.setFailure(var6);
                     this.close(this.voidPromise());
                     return;
                  }
               }

               Channel var4 = LocalChannelRegistry.get(var1);
               if (!(var4 instanceof LocalServerChannel)) {
                  ChannelException var8 = new ChannelException("connection refused");
                  var3.setFailure(var8);
                  this.close(this.voidPromise());
               } else {
                  LocalServerChannel var5 = (LocalServerChannel)var4;
                  LocalChannel.access$202(this.this$0, var5.serve(this.this$0));
               }
            }
         }
      }

      LocalUnsafe(LocalChannel var1, Object var2) {
         this(var1);
      }
   }
}
