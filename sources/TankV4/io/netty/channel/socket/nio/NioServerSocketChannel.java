package io.netty.channel.socket.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.socket.DefaultServerSocketChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

public class NioServerSocketChannel extends AbstractNioMessageChannel implements ServerSocketChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioServerSocketChannel.class);
   private final ServerSocketChannelConfig config = new DefaultServerSocketChannelConfig(this, this.javaChannel().socket());

   private static java.nio.channels.ServerSocketChannel newSocket() {
      try {
         return java.nio.channels.ServerSocketChannel.open();
      } catch (IOException var1) {
         throw new ChannelException("Failed to open a server socket.", var1);
      }
   }

   public NioServerSocketChannel() {
      super((Channel)null, newSocket(), 16);
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public ServerSocketChannelConfig config() {
      return this.config;
   }

   public boolean isActive() {
      return this.javaChannel().socket().isBound();
   }

   public InetSocketAddress remoteAddress() {
      return null;
   }

   protected java.nio.channels.ServerSocketChannel javaChannel() {
      return (java.nio.channels.ServerSocketChannel)super.javaChannel();
   }

   protected SocketAddress localAddress0() {
      return this.javaChannel().socket().getLocalSocketAddress();
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.javaChannel().socket().bind(var1, this.config.getBacklog());
   }

   protected void doClose() throws Exception {
      this.javaChannel().close();
   }

   protected int doReadMessages(List var1) throws Exception {
      SocketChannel var2 = this.javaChannel().accept();

      try {
         if (var2 != null) {
            var1.add(new NioSocketChannel(this, var2));
            return 1;
         }
      } catch (Throwable var6) {
         logger.warn("Failed to create a new channel from an accepted socket.", var6);

         try {
            var2.close();
         } catch (Throwable var5) {
            logger.warn("Failed to close a socket.", var5);
         }
      }

      return 0;
   }

   protected boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected void doFinishConnect() throws Exception {
      throw new UnsupportedOperationException();
   }

   protected SocketAddress remoteAddress0() {
      return null;
   }

   protected void doDisconnect() throws Exception {
      throw new UnsupportedOperationException();
   }

   protected boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected SelectableChannel javaChannel() {
      return this.javaChannel();
   }

   public SocketAddress remoteAddress() {
      return this.remoteAddress();
   }

   public SocketAddress localAddress() {
      return this.localAddress();
   }

   public ChannelConfig config() {
      return this.config();
   }
}
