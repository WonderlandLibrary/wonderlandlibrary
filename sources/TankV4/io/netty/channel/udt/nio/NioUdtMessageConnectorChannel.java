package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.udt.UdtMessage;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectableChannel;
import java.util.List;

public class NioUdtMessageConnectorChannel extends AbstractNioMessageChannel implements UdtChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtMessageConnectorChannel.class);
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private final UdtChannelConfig config;

   public NioUdtMessageConnectorChannel() {
      this(TypeUDT.DATAGRAM);
   }

   public NioUdtMessageConnectorChannel(Channel var1, SocketChannelUDT var2) {
      super(var1, var2, 1);

      try {
         var2.configureBlocking(false);
         switch(var2.socketUDT().status()) {
         case INIT:
         case OPENED:
            this.config = new DefaultUdtChannelConfig(this, var2, true);
            break;
         default:
            this.config = new DefaultUdtChannelConfig(this, var2, false);
         }

      } catch (Exception var6) {
         try {
            var2.close();
         } catch (Exception var5) {
            if (logger.isWarnEnabled()) {
               logger.warn("Failed to close channel.", (Throwable)var5);
            }
         }

         throw new ChannelException("Failed to configure channel.", var6);
      }
   }

   public NioUdtMessageConnectorChannel(SocketChannelUDT var1) {
      this((Channel)null, var1);
   }

   public NioUdtMessageConnectorChannel(TypeUDT var1) {
      this(NioUdtProvider.newConnectorChannelUDT(var1));
   }

   public UdtChannelConfig config() {
      return this.config;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.javaChannel().bind(var1);
   }

   protected void doClose() throws Exception {
      this.javaChannel().close();
   }

   protected boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      this.doBind((SocketAddress)(var2 != null ? var2 : new InetSocketAddress(0)));
      boolean var3 = false;
      boolean var4 = this.javaChannel().connect(var1);
      if (!var4) {
         this.selectionKey().interestOps(this.selectionKey().interestOps() | 8);
      }

      var3 = true;
      if (!var3) {
         this.doClose();
      }

      return var4;
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doFinishConnect() throws Exception {
      if (this.javaChannel().finishConnect()) {
         this.selectionKey().interestOps(this.selectionKey().interestOps() & -9);
      } else {
         throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
      }
   }

   protected int doReadMessages(List var1) throws Exception {
      int var2 = this.config.getReceiveBufferSize();
      ByteBuf var3 = this.config.getAllocator().directBuffer(var2);
      int var4 = var3.writeBytes((ScatteringByteChannel)this.javaChannel(), var2);
      if (var4 <= 0) {
         var3.release();
         return 0;
      } else if (var4 >= var2) {
         this.javaChannel().close();
         throw new ChannelException("Invalid config : increase receive buffer size to avoid message truncation");
      } else {
         var1.add(new UdtMessage(var3));
         return 1;
      }
   }

   protected boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception {
      UdtMessage var3 = (UdtMessage)var1;
      ByteBuf var4 = var3.content();
      int var5 = var4.readableBytes();
      long var6;
      if (var4.nioBufferCount() == 1) {
         var6 = (long)this.javaChannel().write(var4.nioBuffer());
      } else {
         var6 = this.javaChannel().write(var4.nioBuffers());
      }

      if (var6 <= 0L && var5 > 0) {
         return false;
      } else if (var6 != (long)var5) {
         throw new Error("Provider error: failed to write message. Provider library should be upgraded.");
      } else {
         return true;
      }
   }

   public boolean isActive() {
      SocketChannelUDT var1 = this.javaChannel();
      return var1.isOpen() && var1.isConnectFinished();
   }

   protected SocketChannelUDT javaChannel() {
      return (SocketChannelUDT)super.javaChannel();
   }

   protected SocketAddress localAddress0() {
      return this.javaChannel().socket().getLocalSocketAddress();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   protected SocketAddress remoteAddress0() {
      return this.javaChannel().socket().getRemoteSocketAddress();
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
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
