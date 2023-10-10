package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AttributeMap;
import java.net.SocketAddress;

public interface Channel extends AttributeMap, Comparable {
   EventLoop eventLoop();

   Channel parent();

   ChannelConfig config();

   boolean isOpen();

   boolean isRegistered();

   boolean isActive();

   ChannelMetadata metadata();

   SocketAddress localAddress();

   SocketAddress remoteAddress();

   ChannelFuture closeFuture();

   boolean isWritable();

   Channel.Unsafe unsafe();

   ChannelPipeline pipeline();

   ByteBufAllocator alloc();

   ChannelPromise newPromise();

   ChannelProgressivePromise newProgressivePromise();

   ChannelFuture newSucceededFuture();

   ChannelFuture newFailedFuture(Throwable var1);

   ChannelPromise voidPromise();

   ChannelFuture bind(SocketAddress var1);

   ChannelFuture connect(SocketAddress var1);

   ChannelFuture connect(SocketAddress var1, SocketAddress var2);

   ChannelFuture disconnect();

   ChannelFuture close();

   /** @deprecated */
   @Deprecated
   ChannelFuture deregister();

   ChannelFuture bind(SocketAddress var1, ChannelPromise var2);

   ChannelFuture connect(SocketAddress var1, ChannelPromise var2);

   ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3);

   ChannelFuture disconnect(ChannelPromise var1);

   ChannelFuture close(ChannelPromise var1);

   /** @deprecated */
   @Deprecated
   ChannelFuture deregister(ChannelPromise var1);

   Channel read();

   ChannelFuture write(Object var1);

   ChannelFuture write(Object var1, ChannelPromise var2);

   Channel flush();

   ChannelFuture writeAndFlush(Object var1, ChannelPromise var2);

   ChannelFuture writeAndFlush(Object var1);

   public interface Unsafe {
      SocketAddress localAddress();

      SocketAddress remoteAddress();

      void register(EventLoop var1, ChannelPromise var2);

      void bind(SocketAddress var1, ChannelPromise var2);

      void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3);

      void disconnect(ChannelPromise var1);

      void close(ChannelPromise var1);

      void closeForcibly();

      /** @deprecated */
      @Deprecated
      void deregister(ChannelPromise var1);

      void beginRead();

      void write(Object var1, ChannelPromise var2);

      void flush();

      ChannelPromise voidPromise();

      ChannelOutboundBuffer outboundBuffer();
   }
}
