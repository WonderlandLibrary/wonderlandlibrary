package io.netty.channel.udt.nio;

import com.barchart.udt.SocketUDT;
import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.ChannelUDT;
import com.barchart.udt.nio.KindUDT;
import com.barchart.udt.nio.RendezvousChannelUDT;
import com.barchart.udt.nio.SelectorProviderUDT;
import com.barchart.udt.nio.ServerSocketChannelUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.udt.UdtChannel;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;

public final class NioUdtProvider implements ChannelFactory {
   public static final ChannelFactory BYTE_ACCEPTOR;
   public static final ChannelFactory BYTE_CONNECTOR;
   public static final SelectorProvider BYTE_PROVIDER;
   public static final ChannelFactory BYTE_RENDEZVOUS;
   public static final ChannelFactory MESSAGE_ACCEPTOR;
   public static final ChannelFactory MESSAGE_CONNECTOR;
   public static final SelectorProvider MESSAGE_PROVIDER;
   public static final ChannelFactory MESSAGE_RENDEZVOUS;
   private final KindUDT kind;
   private final TypeUDT type;

   public static ChannelUDT channelUDT(Channel var0) {
      if (var0 instanceof NioUdtByteAcceptorChannel) {
         return ((NioUdtByteAcceptorChannel)var0).javaChannel();
      } else if (var0 instanceof NioUdtByteConnectorChannel) {
         return ((NioUdtByteConnectorChannel)var0).javaChannel();
      } else if (var0 instanceof NioUdtByteRendezvousChannel) {
         return ((NioUdtByteRendezvousChannel)var0).javaChannel();
      } else if (var0 instanceof NioUdtMessageAcceptorChannel) {
         return ((NioUdtMessageAcceptorChannel)var0).javaChannel();
      } else if (var0 instanceof NioUdtMessageConnectorChannel) {
         return ((NioUdtMessageConnectorChannel)var0).javaChannel();
      } else {
         return var0 instanceof NioUdtMessageRendezvousChannel ? ((NioUdtMessageRendezvousChannel)var0).javaChannel() : null;
      }
   }

   protected static ServerSocketChannelUDT newAcceptorChannelUDT(TypeUDT var0) {
      try {
         return SelectorProviderUDT.from(var0).openServerSocketChannel();
      } catch (IOException var2) {
         throw new ChannelException("Failed to open channel");
      }
   }

   protected static SocketChannelUDT newConnectorChannelUDT(TypeUDT var0) {
      try {
         return SelectorProviderUDT.from(var0).openSocketChannel();
      } catch (IOException var2) {
         throw new ChannelException("Failed to open channel");
      }
   }

   protected static RendezvousChannelUDT newRendezvousChannelUDT(TypeUDT var0) {
      try {
         return SelectorProviderUDT.from(var0).openRendezvousChannel();
      } catch (IOException var2) {
         throw new ChannelException("Failed to open channel");
      }
   }

   public static SocketUDT socketUDT(Channel var0) {
      ChannelUDT var1 = channelUDT(var0);
      return var1 == null ? null : var1.socketUDT();
   }

   private NioUdtProvider(TypeUDT var1, KindUDT var2) {
      this.type = var1;
      this.kind = var2;
   }

   public KindUDT kind() {
      return this.kind;
   }

   public UdtChannel newChannel() {
      switch(this.kind) {
      case ACCEPTOR:
         switch(this.type) {
         case DATAGRAM:
            return new NioUdtMessageAcceptorChannel();
         case STREAM:
            return new NioUdtByteAcceptorChannel();
         default:
            throw new IllegalStateException("wrong type=" + this.type);
         }
      case CONNECTOR:
         switch(this.type) {
         case DATAGRAM:
            return new NioUdtMessageConnectorChannel();
         case STREAM:
            return new NioUdtByteConnectorChannel();
         default:
            throw new IllegalStateException("wrong type=" + this.type);
         }
      case RENDEZVOUS:
         switch(this.type) {
         case DATAGRAM:
            return new NioUdtMessageRendezvousChannel();
         case STREAM:
            return new NioUdtByteRendezvousChannel();
         default:
            throw new IllegalStateException("wrong type=" + this.type);
         }
      default:
         throw new IllegalStateException("wrong kind=" + this.kind);
      }
   }

   public TypeUDT type() {
      return this.type;
   }

   public Channel newChannel() {
      return this.newChannel();
   }

   static {
      BYTE_ACCEPTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.ACCEPTOR);
      BYTE_CONNECTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.CONNECTOR);
      BYTE_PROVIDER = SelectorProviderUDT.STREAM;
      BYTE_RENDEZVOUS = new NioUdtProvider(TypeUDT.STREAM, KindUDT.RENDEZVOUS);
      MESSAGE_ACCEPTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.ACCEPTOR);
      MESSAGE_CONNECTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.CONNECTOR);
      MESSAGE_PROVIDER = SelectorProviderUDT.DATAGRAM;
      MESSAGE_RENDEZVOUS = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.RENDEZVOUS);
   }
}
