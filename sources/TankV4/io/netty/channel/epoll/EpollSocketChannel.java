package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoop;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class EpollSocketChannel extends AbstractEpollChannel implements SocketChannel {
   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
   private final EpollSocketChannelConfig config = new EpollSocketChannelConfig(this);
   private ChannelPromise connectPromise;
   private ScheduledFuture connectTimeoutFuture;
   private SocketAddress requestedRemoteAddress;
   private volatile InetSocketAddress local;
   private volatile InetSocketAddress remote;
   private volatile boolean inputShutdown;
   private volatile boolean outputShutdown;
   static final boolean $assertionsDisabled = !EpollSocketChannel.class.desiredAssertionStatus();

   EpollSocketChannel(Channel var1, int var2) {
      super(var1, var2, 1, true);
      this.remote = Native.remoteAddress(var2);
      this.local = Native.localAddress(var2);
   }

   public EpollSocketChannel() {
      super(Native.socketStreamFd(), 1);
   }

   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
      return new EpollSocketChannel.EpollSocketUnsafe(this);
   }

   protected SocketAddress localAddress0() {
      return this.local;
   }

   protected SocketAddress remoteAddress0() {
      return this.remote;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      InetSocketAddress var2 = (InetSocketAddress)var1;
      Native.bind(this.fd, var2.getAddress(), var2.getPort());
      this.local = Native.localAddress(this.fd);
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         int var2 = var1.size();
         if (var2 == 0) {
            this.clearEpollOut();
         } else if (var2 > 1 && var1.current() instanceof ByteBuf) {
            if (var1 == false) {
               continue;
            }
         } else if (var1 == false) {
            continue;
         }

         return;
      }
   }

   protected Object filterOutboundMessage(Object var1) {
      if (!(var1 instanceof ByteBuf)) {
         if (var1 instanceof DefaultFileRegion) {
            return var1;
         } else {
            throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var1) + EXPECTED_TYPES);
         }
      } else {
         ByteBuf var2 = (ByteBuf)var1;
         if (!var2.hasMemoryAddress() && (PlatformDependent.hasUnsafe() || !var2.isDirect())) {
            var2 = this.newDirectBuffer(var2);
            if (!$assertionsDisabled && !var2.hasMemoryAddress()) {
               throw new AssertionError();
            }
         }

         return var2;
      }
   }

   public EpollSocketChannelConfig config() {
      return this.config;
   }

   public boolean isInputShutdown() {
      return this.inputShutdown;
   }

   public boolean isOutputShutdown() {
      return this.outputShutdown || !this.isActive();
   }

   public ChannelFuture shutdownOutput() {
      return this.shutdownOutput(this.newPromise());
   }

   public ChannelFuture shutdownOutput(ChannelPromise var1) {
      EventLoop var2 = this.eventLoop();
      if (var2.inEventLoop()) {
         try {
            Native.shutdown(this.fd, false, true);
            this.outputShutdown = true;
            var1.setSuccess();
         } catch (Throwable var4) {
            var1.setFailure(var4);
         }
      } else {
         var2.execute(new Runnable(this, var1) {
            final ChannelPromise val$promise;
            final EpollSocketChannel this$0;

            {
               this.this$0 = var1;
               this.val$promise = var2;
            }

            public void run() {
               this.this$0.shutdownOutput(this.val$promise);
            }
         });
      }

      return var1;
   }

   public ServerSocketChannel parent() {
      return (ServerSocketChannel)super.parent();
   }

   public boolean isOpen() {
      return super.isOpen();
   }

   public InetSocketAddress localAddress() {
      return super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return super.remoteAddress();
   }

   public ChannelMetadata metadata() {
      return super.metadata();
   }

   public boolean isActive() {
      return super.isActive();
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
   }

   public Channel parent() {
      return this.parent();
   }

   public ChannelConfig config() {
      return this.config();
   }

   public SocketChannelConfig config() {
      return this.config();
   }

   static boolean access$002(EpollSocketChannel var0, boolean var1) {
      return var0.inputShutdown = var1;
   }

   static ChannelPromise access$100(EpollSocketChannel var0) {
      return var0.connectPromise;
   }

   static ChannelPromise access$102(EpollSocketChannel var0, ChannelPromise var1) {
      return var0.connectPromise = var1;
   }

   static SocketAddress access$202(EpollSocketChannel var0, SocketAddress var1) {
      return var0.requestedRemoteAddress = var1;
   }

   static ScheduledFuture access$302(EpollSocketChannel var0, ScheduledFuture var1) {
      return var0.connectTimeoutFuture = var1;
   }

   static ScheduledFuture access$300(EpollSocketChannel var0) {
      return var0.connectTimeoutFuture;
   }

   static SocketAddress access$200(EpollSocketChannel var0) {
      return var0.requestedRemoteAddress;
   }

   static InetSocketAddress access$402(EpollSocketChannel var0, InetSocketAddress var1) {
      return var0.remote = var1;
   }

   static InetSocketAddress access$502(EpollSocketChannel var0, InetSocketAddress var1) {
      return var0.local = var1;
   }

   final class EpollSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
      private RecvByteBufAllocator.Handle allocHandle;
      static final boolean $assertionsDisabled = !EpollSocketChannel.class.desiredAssertionStatus();
      final EpollSocketChannel this$0;

      EpollSocketUnsafe(EpollSocketChannel var1) {
         super();
         this.this$0 = var1;
      }

      private void closeOnRead(ChannelPipeline var1) {
         EpollSocketChannel.access$002(this.this$0, true);
         if (this.this$0.isOpen()) {
            if (Boolean.TRUE.equals(this.this$0.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
               this.clearEpollIn0();
               var1.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
            } else {
               this.close(this.voidPromise());
            }
         }

      }

      private boolean handleReadException(ChannelPipeline var1, ByteBuf var2, Throwable var3, boolean var4) {
         if (var2 != null) {
            if (var2.isReadable()) {
               this.readPending = false;
               var1.fireChannelRead(var2);
            } else {
               var2.release();
            }
         }

         var1.fireChannelReadComplete();
         var1.fireExceptionCaught(var3);
         if (!var4 && !(var3 instanceof IOException)) {
            return false;
         } else {
            this.closeOnRead(var1);
            return true;
         }
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if (var3.setUncancellable() && this.ensureOpen(var3)) {
            try {
               if (EpollSocketChannel.access$100(this.this$0) != null) {
                  throw new IllegalStateException("connection attempt already made");
               }

               boolean var7 = this.this$0.isActive();
               InetSocketAddress var10001 = (InetSocketAddress)var1;
               if ((InetSocketAddress)var2 != null) {
                  this.fulfillConnectPromise(var3, var7);
               } else {
                  EpollSocketChannel.access$102(this.this$0, var3);
                  EpollSocketChannel.access$202(this.this$0, var1);
                  int var8 = this.this$0.config().getConnectTimeoutMillis();
                  if (var8 > 0) {
                     EpollSocketChannel.access$302(this.this$0, this.this$0.eventLoop().schedule(new Runnable(this, var1) {
                        final SocketAddress val$remoteAddress;
                        final EpollSocketChannel.EpollSocketUnsafe this$1;

                        {
                           this.this$1 = var1;
                           this.val$remoteAddress = var2;
                        }

                        public void run() {
                           ChannelPromise var1 = EpollSocketChannel.access$100(this.this$1.this$0);
                           ConnectTimeoutException var2 = new ConnectTimeoutException("connection timed out: " + this.val$remoteAddress);
                           if (var1 != null && var1.tryFailure(var2)) {
                              this.this$1.close(this.this$1.voidPromise());
                           }

                        }
                     }, (long)var8, TimeUnit.MILLISECONDS));
                  }

                  var3.addListener(new ChannelFutureListener(this) {
                     final EpollSocketChannel.EpollSocketUnsafe this$1;

                     {
                        this.this$1 = var1;
                     }

                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if (var1.isCancelled()) {
                           if (EpollSocketChannel.access$300(this.this$1.this$0) != null) {
                              EpollSocketChannel.access$300(this.this$1.this$0).cancel(false);
                           }

                           EpollSocketChannel.access$102(this.this$1.this$0, (ChannelPromise)null);
                           this.this$1.close(this.this$1.voidPromise());
                        }

                     }

                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               }
            } catch (Throwable var6) {
               Object var4 = var6;
               if (var6 instanceof ConnectException) {
                  ConnectException var5 = new ConnectException(var6.getMessage() + ": " + var1);
                  var5.setStackTrace(var6.getStackTrace());
                  var4 = var5;
               }

               this.closeIfClosed();
               var3.tryFailure((Throwable)var4);
            }

         }
      }

      private void fulfillConnectPromise(ChannelPromise var1, boolean var2) {
         if (var1 != null) {
            this.this$0.active = true;
            boolean var3 = var1.trySuccess();
            if (!var2 && this.this$0.isActive()) {
               this.this$0.pipeline().fireChannelActive();
            }

            if (!var3) {
               this.close(this.voidPromise());
            }

         }
      }

      private void fulfillConnectPromise(ChannelPromise var1, Throwable var2) {
         if (var1 != null) {
            var1.tryFailure(var2);
            this.closeIfClosed();
         }
      }

      private void finishConnect() {
         if (!$assertionsDisabled && !this.this$0.eventLoop().inEventLoop()) {
            throw new AssertionError();
         } else {
            boolean var1 = false;

            label71: {
               try {
                  boolean var6 = this.this$0.isActive();
                  if (this != false) {
                     var1 = true;
                     break label71;
                  }

                  this.fulfillConnectPromise(EpollSocketChannel.access$100(this.this$0), var6);
               } catch (Throwable var5) {
                  Object var2 = var5;
                  if (var5 instanceof ConnectException) {
                     ConnectException var3 = new ConnectException(var5.getMessage() + ": " + EpollSocketChannel.access$200(this.this$0));
                     var3.setStackTrace(var5.getStackTrace());
                     var2 = var3;
                  }

                  this.fulfillConnectPromise(EpollSocketChannel.access$100(this.this$0), (Throwable)var2);
                  if (!var1) {
                     if (EpollSocketChannel.access$300(this.this$0) != null) {
                        EpollSocketChannel.access$300(this.this$0).cancel(false);
                     }

                     EpollSocketChannel.access$102(this.this$0, (ChannelPromise)null);
                  }

                  return;
               }

               if (!var1) {
                  if (EpollSocketChannel.access$300(this.this$0) != null) {
                     EpollSocketChannel.access$300(this.this$0).cancel(false);
                  }

                  EpollSocketChannel.access$102(this.this$0, (ChannelPromise)null);
               }

               return;
            }

            if (!var1) {
               if (EpollSocketChannel.access$300(this.this$0) != null) {
                  EpollSocketChannel.access$300(this.this$0).cancel(false);
               }

               EpollSocketChannel.access$102(this.this$0, (ChannelPromise)null);
            }

         }
      }

      void epollOutReady() {
         if (EpollSocketChannel.access$100(this.this$0) != null) {
            this.finishConnect();
         } else {
            super.epollOutReady();
         }

      }

      private int doReadBytes(ByteBuf var1) throws Exception {
         int var2 = var1.writerIndex();
         int var3;
         if (var1.hasMemoryAddress()) {
            var3 = Native.readAddress(this.this$0.fd, var1.memoryAddress(), var2, var1.capacity());
         } else {
            ByteBuffer var4 = var1.internalNioBuffer(var2, var1.writableBytes());
            var3 = Native.read(this.this$0.fd, var4, var4.position(), var4.limit());
         }

         if (var3 > 0) {
            var1.writerIndex(var2 + var3);
         }

         return var3;
      }

      void epollRdHupReady() {
         if (this.this$0.isActive()) {
            this.epollInReady();
         } else {
            this.closeOnRead(this.this$0.pipeline());
         }

      }

      void epollInReady() {
         EpollSocketChannelConfig var1 = this.this$0.config();
         ChannelPipeline var2 = this.this$0.pipeline();
         ByteBufAllocator var3 = var1.getAllocator();
         RecvByteBufAllocator.Handle var4 = this.allocHandle;
         if (var4 == null) {
            this.allocHandle = var4 = var1.getRecvByteBufAllocator().newHandle();
         }

         ByteBuf var5 = null;
         boolean var6 = false;

         try {
            int var7 = 0;

            int var9;
            int var12;
            do {
               var5 = var4.allocate(var3);
               var12 = var5.writableBytes();
               var9 = this.doReadBytes(var5);
               if (var9 <= 0) {
                  var5.release();
                  var6 = var9 < 0;
                  break;
               }

               this.readPending = false;
               var2.fireChannelRead(var5);
               var5 = null;
               if (var7 >= Integer.MAX_VALUE - var9) {
                  var4.record(var7);
                  var7 = var9;
               } else {
                  var7 += var9;
               }
            } while(var9 >= var12);

            var2.fireChannelReadComplete();
            var4.record(var7);
            if (var6) {
               this.closeOnRead(var2);
               var6 = false;
            }
         } catch (Throwable var11) {
            boolean var8 = this.handleReadException(var2, var5, var11, var6);
            if (!var8) {
               this.this$0.eventLoop().execute(new Runnable(this) {
                  final EpollSocketChannel.EpollSocketUnsafe this$1;

                  {
                     this.this$1 = var1;
                  }

                  public void run() {
                     this.this$1.epollInReady();
                  }
               });
            }

            if (!var1.isAutoRead() && !this.readPending) {
               this.clearEpollIn0();
            }

            return;
         }

         if (!var1.isAutoRead() && !this.readPending) {
            this.clearEpollIn0();
         }

      }
   }
}
