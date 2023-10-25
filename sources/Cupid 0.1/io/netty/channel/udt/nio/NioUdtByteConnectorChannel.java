package io.netty.channel.udt.nio;

import com.barchart.udt.StatusUDT;
import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.ChannelUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.FileRegion;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectableChannel;

public class NioUdtByteConnectorChannel extends AbstractNioByteChannel implements UdtChannel {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtByteConnectorChannel.class);
  
  private static final ChannelMetadata METADATA = new ChannelMetadata(false);
  
  private final UdtChannelConfig config;
  
  public NioUdtByteConnectorChannel() {
    this(TypeUDT.STREAM);
  }
  
  public NioUdtByteConnectorChannel(Channel parent, SocketChannelUDT channelUDT) {
    super(parent, (SelectableChannel)channelUDT);
    try {
      channelUDT.configureBlocking(false);
      switch (channelUDT.socketUDT().status()) {
        case INIT:
        case OPENED:
          this.config = (UdtChannelConfig)new DefaultUdtChannelConfig(this, (ChannelUDT)channelUDT, true);
          return;
      } 
      this.config = (UdtChannelConfig)new DefaultUdtChannelConfig(this, (ChannelUDT)channelUDT, false);
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
  
  public NioUdtByteConnectorChannel(SocketChannelUDT channelUDT) {
    this(null, channelUDT);
  }
  
  public NioUdtByteConnectorChannel(TypeUDT type) {
    this(NioUdtProvider.newConnectorChannelUDT(type));
  }
  
  public UdtChannelConfig config() {
    return this.config;
  }
  
  protected void doBind(SocketAddress localAddress) throws Exception {
    javaChannel().bind(localAddress);
  }
  
  protected void doClose() throws Exception {
    javaChannel().close();
  }
  
  protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
    doBind((localAddress != null) ? localAddress : new InetSocketAddress(0));
    boolean success = false;
    try {
      boolean connected = javaChannel().connect(remoteAddress);
      if (!connected)
        selectionKey().interestOps(selectionKey().interestOps() | 0x8); 
      success = true;
      return connected;
    } finally {
      if (!success)
        doClose(); 
    } 
  }
  
  protected void doDisconnect() throws Exception {
    doClose();
  }
  
  protected void doFinishConnect() throws Exception {
    if (javaChannel().finishConnect()) {
      selectionKey().interestOps(selectionKey().interestOps() & 0xFFFFFFF7);
    } else {
      throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
    } 
  }
  
  protected int doReadBytes(ByteBuf byteBuf) throws Exception {
    return byteBuf.writeBytes((ScatteringByteChannel)javaChannel(), byteBuf.writableBytes());
  }
  
  protected int doWriteBytes(ByteBuf byteBuf) throws Exception {
    int expectedWrittenBytes = byteBuf.readableBytes();
    return byteBuf.readBytes((GatheringByteChannel)javaChannel(), expectedWrittenBytes);
  }
  
  protected long doWriteFileRegion(FileRegion region) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  public boolean isActive() {
    SocketChannelUDT channelUDT = javaChannel();
    return (channelUDT.isOpen() && channelUDT.isConnectFinished());
  }
  
  protected SocketChannelUDT javaChannel() {
    return (SocketChannelUDT)super.javaChannel();
  }
  
  protected SocketAddress localAddress0() {
    return javaChannel().socket().getLocalSocketAddress();
  }
  
  public ChannelMetadata metadata() {
    return METADATA;
  }
  
  protected SocketAddress remoteAddress0() {
    return javaChannel().socket().getRemoteSocketAddress();
  }
  
  public InetSocketAddress localAddress() {
    return (InetSocketAddress)super.localAddress();
  }
  
  public InetSocketAddress remoteAddress() {
    return (InetSocketAddress)super.remoteAddress();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channe\\udt\nio\NioUdtByteConnectorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */