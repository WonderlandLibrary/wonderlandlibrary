package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.ServerSocketChannelUDT;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.udt.DefaultUdtServerChannelConfig;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.udt.UdtServerChannel;
import io.netty.channel.udt.UdtServerChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;

public abstract class NioUdtAcceptorChannel extends AbstractNioMessageChannel implements UdtServerChannel {
   protected static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtAcceptorChannel.class);
   private final UdtServerChannelConfig config;

   protected NioUdtAcceptorChannel(ServerSocketChannelUDT var1) {
      super((Channel)null, var1, 16);

      try {
         var1.configureBlocking(false);
         this.config = new DefaultUdtServerChannelConfig(this, var1, true);
      } catch (Exception var5) {
         try {
            var1.close();
         } catch (Exception var4) {
            if (logger.isWarnEnabled()) {
               logger.warn("Failed to close channel.", (Throwable)var4);
            }
         }

         throw new ChannelException("Failed to configure channel.", var5);
      }
   }

   protected NioUdtAcceptorChannel(TypeUDT var1) {
      this(NioUdtProvider.newAcceptorChannelUDT(var1));
   }

   public UdtServerChannelConfig config() {
      return this.config;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.javaChannel().socket().bind(var1, this.config.getBacklog());
   }

   protected void doClose() throws Exception {
      this.javaChannel().close();
   }

   protected boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected void doDisconnect() throws Exception {
      throw new UnsupportedOperationException();
   }

   protected void doFinishConnect() throws Exception {
      throw new UnsupportedOperationException();
   }

   protected boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception {
      throw new UnsupportedOperationException();
   }

   public boolean isActive() {
      return this.javaChannel().socket().isBound();
   }

   protected ServerSocketChannelUDT javaChannel() {
      return (ServerSocketChannelUDT)super.javaChannel();
   }

   protected SocketAddress localAddress0() {
      return this.javaChannel().socket().getLocalSocketAddress();
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return null;
   }

   protected SocketAddress remoteAddress0() {
      return null;
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

   public UdtChannelConfig config() {
      return this.config();
   }
}
