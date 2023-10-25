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
import io.netty.channel.udt.UdtServerChannel;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;

public final class NioUdtProvider<T extends UdtChannel> implements ChannelFactory<T> {
  public static final ChannelFactory<UdtServerChannel> BYTE_ACCEPTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.ACCEPTOR);
  
  public static final ChannelFactory<UdtChannel> BYTE_CONNECTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.CONNECTOR);
  
  public static final SelectorProvider BYTE_PROVIDER = (SelectorProvider)SelectorProviderUDT.STREAM;
  
  public static final ChannelFactory<UdtChannel> BYTE_RENDEZVOUS = new NioUdtProvider(TypeUDT.STREAM, KindUDT.RENDEZVOUS);
  
  public static final ChannelFactory<UdtServerChannel> MESSAGE_ACCEPTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.ACCEPTOR);
  
  public static final ChannelFactory<UdtChannel> MESSAGE_CONNECTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.CONNECTOR);
  
  public static final SelectorProvider MESSAGE_PROVIDER = (SelectorProvider)SelectorProviderUDT.DATAGRAM;
  
  public static final ChannelFactory<UdtChannel> MESSAGE_RENDEZVOUS = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.RENDEZVOUS);
  
  private final KindUDT kind;
  
  private final TypeUDT type;
  
  public static ChannelUDT channelUDT(Channel channel) {
    if (channel instanceof NioUdtByteAcceptorChannel)
      return (ChannelUDT)((NioUdtByteAcceptorChannel)channel).javaChannel(); 
    if (channel instanceof NioUdtByteConnectorChannel)
      return (ChannelUDT)((NioUdtByteConnectorChannel)channel).javaChannel(); 
    if (channel instanceof NioUdtByteRendezvousChannel)
      return (ChannelUDT)((NioUdtByteRendezvousChannel)channel).javaChannel(); 
    if (channel instanceof NioUdtMessageAcceptorChannel)
      return (ChannelUDT)((NioUdtMessageAcceptorChannel)channel).javaChannel(); 
    if (channel instanceof NioUdtMessageConnectorChannel)
      return (ChannelUDT)((NioUdtMessageConnectorChannel)channel).javaChannel(); 
    if (channel instanceof NioUdtMessageRendezvousChannel)
      return (ChannelUDT)((NioUdtMessageRendezvousChannel)channel).javaChannel(); 
    return null;
  }
  
  static ServerSocketChannelUDT newAcceptorChannelUDT(TypeUDT type) {
    try {
      return SelectorProviderUDT.from(type).openServerSocketChannel();
    } catch (IOException e) {
      throw new ChannelException("failed to open a server socket channel", e);
    } 
  }
  
  static SocketChannelUDT newConnectorChannelUDT(TypeUDT type) {
    try {
      return SelectorProviderUDT.from(type).openSocketChannel();
    } catch (IOException e) {
      throw new ChannelException("failed to open a socket channel", e);
    } 
  }
  
  static RendezvousChannelUDT newRendezvousChannelUDT(TypeUDT type) {
    try {
      return SelectorProviderUDT.from(type).openRendezvousChannel();
    } catch (IOException e) {
      throw new ChannelException("failed to open a rendezvous channel", e);
    } 
  }
  
  public static SocketUDT socketUDT(Channel channel) {
    ChannelUDT channelUDT = channelUDT(channel);
    if (channelUDT == null)
      return null; 
    return channelUDT.socketUDT();
  }
  
  private NioUdtProvider(TypeUDT type, KindUDT kind) {
    this.type = type;
    this.kind = kind;
  }
  
  public KindUDT kind() {
    return this.kind;
  }
  
  public T newChannel() {
    switch (this.kind) {
      case ACCEPTOR:
        switch (this.type) {
          case ACCEPTOR:
            return (T)new NioUdtMessageAcceptorChannel();
          case CONNECTOR:
            return (T)new NioUdtByteAcceptorChannel();
        } 
        throw new IllegalStateException("wrong type=" + this.type);
      case CONNECTOR:
        switch (this.type) {
          case ACCEPTOR:
            return (T)new NioUdtMessageConnectorChannel();
          case CONNECTOR:
            return (T)new NioUdtByteConnectorChannel();
        } 
        throw new IllegalStateException("wrong type=" + this.type);
      case RENDEZVOUS:
        switch (this.type) {
          case ACCEPTOR:
            return (T)new NioUdtMessageRendezvousChannel();
          case CONNECTOR:
            return (T)new NioUdtByteRendezvousChannel();
        } 
        throw new IllegalStateException("wrong type=" + this.type);
    } 
    throw new IllegalStateException("wrong kind=" + this.kind);
  }
  
  public TypeUDT type() {
    return this.type;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channe\\udt\nio\NioUdtProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */