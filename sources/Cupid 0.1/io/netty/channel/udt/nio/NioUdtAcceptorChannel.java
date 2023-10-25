package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.ChannelUDT;
import com.barchart.udt.nio.ServerSocketChannelUDT;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.udt.DefaultUdtServerChannelConfig;
import io.netty.channel.udt.UdtChannel;
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
  
  protected NioUdtAcceptorChannel(ServerSocketChannelUDT channelUDT) {
    super(null, (SelectableChannel)channelUDT, 16);
    try {
      channelUDT.configureBlocking(false);
      this.config = (UdtServerChannelConfig)new DefaultUdtServerChannelConfig((UdtChannel)this, (ChannelUDT)channelUDT, true);
    } catch (Exception e) {
      try {
        channelUDT.close();
      } catch (Exception e2) {
        if (logger.isWarnEnabled())
          logger.warn("Failed to close channel.", e2); 
      } 
      throw new ChannelException("Failed to configure channel.", e);
    } 
  }
  
  protected NioUdtAcceptorChannel(TypeUDT type) {
    this(NioUdtProvider.newAcceptorChannelUDT(type));
  }
  
  public UdtServerChannelConfig config() {
    return this.config;
  }
  
  protected void doBind(SocketAddress localAddress) throws Exception {
    javaChannel().socket().bind(localAddress, this.config.getBacklog());
  }
  
  protected void doClose() throws Exception {
    javaChannel().close();
  }
  
  protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  protected void doDisconnect() throws Exception {
    throw new UnsupportedOperationException();
  }
  
  protected void doFinishConnect() throws Exception {
    throw new UnsupportedOperationException();
  }
  
  protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  protected final Object filterOutboundMessage(Object msg) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  public boolean isActive() {
    return javaChannel().socket().isBound();
  }
  
  protected ServerSocketChannelUDT javaChannel() {
    return (ServerSocketChannelUDT)super.javaChannel();
  }
  
  protected SocketAddress localAddress0() {
    return javaChannel().socket().getLocalSocketAddress();
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
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channe\\udt\nio\NioUdtAcceptorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */