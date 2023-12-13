package io.netty.channel.epoll;

import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class EpollServerSocketChannel extends AbstractEpollChannel implements ServerSocketChannel {
   private final EpollServerSocketChannelConfig config = new EpollServerSocketChannelConfig(this);
   private volatile InetSocketAddress local;

   public EpollServerSocketChannel() {
      super(Native.socketStreamFd(), 4);
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof EpollEventLoop;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      InetSocketAddress var2 = (InetSocketAddress)var1;
      checkResolvable(var2);
      Native.bind(this.fd, var2.getAddress(), var2.getPort());
      this.local = Native.localAddress(this.fd);
      Native.listen(this.fd, this.config.getBacklog());
      this.active = true;
   }

   public EpollServerSocketChannelConfig config() {
      return this.config;
   }

   protected InetSocketAddress localAddress0() {
      return this.local;
   }

   protected InetSocketAddress remoteAddress0() {
      return null;
   }

   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
      return new EpollServerSocketChannel.EpollServerSocketUnsafe(this);
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected Object filterOutboundMessage(Object var1) throws Exception {
      throw new UnsupportedOperationException();
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

   protected SocketAddress remoteAddress0() {
      return this.remoteAddress0();
   }

   protected SocketAddress localAddress0() {
      return this.localAddress0();
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
   }

   public ChannelConfig config() {
      return this.config();
   }

   public ServerSocketChannelConfig config() {
      return this.config();
   }

   static EpollServerSocketChannelConfig access$000(EpollServerSocketChannel var0) {
      return var0.config;
   }

   final class EpollServerSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
      static final boolean $assertionsDisabled = !EpollServerSocketChannel.class.desiredAssertionStatus();
      final EpollServerSocketChannel this$0;

      EpollServerSocketUnsafe(EpollServerSocketChannel var1) {
         super();
         this.this$0 = var1;
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         var3.setFailure(new UnsupportedOperationException());
      }

      void epollInReady() {
         if (!$assertionsDisabled && !this.this$0.eventLoop().inEventLoop()) {
            throw new AssertionError();
         } else {
            ChannelPipeline var1 = this.this$0.pipeline();
            Throwable var2 = null;

            try {
               while(true) {
                  int var3 = Native.accept(this.this$0.fd);
                  if (var3 == -1) {
                     break;
                  }

                  try {
                     this.readPending = false;
                     var1.fireChannelRead(new EpollSocketChannel(this.this$0, var3));
                  } catch (Throwable var6) {
                     var1.fireChannelReadComplete();
                     var1.fireExceptionCaught(var6);
                  }
               }
            } catch (Throwable var7) {
               var2 = var7;
            }

            var1.fireChannelReadComplete();
            if (var2 != null) {
               var1.fireExceptionCaught(var2);
            }

            if (!EpollServerSocketChannel.access$000(this.this$0).isAutoRead() && !this.readPending) {
               this.clearEpollIn0();
            }

         }
      }
   }
}
