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
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.udt.UdtMessage;
import io.netty.util.internal.StringUtil;
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
  
  private static final String EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName(UdtMessage.class) + ')';
  
  private final UdtChannelConfig config;
  
  public NioUdtMessageConnectorChannel() {
    this(TypeUDT.DATAGRAM);
  }
  
  public NioUdtMessageConnectorChannel(Channel parent, SocketChannelUDT channelUDT) {
    super(parent, (SelectableChannel)channelUDT, 1);
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
  
  public NioUdtMessageConnectorChannel(SocketChannelUDT channelUDT) {
    this(null, channelUDT);
  }
  
  public NioUdtMessageConnectorChannel(TypeUDT type) {
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
  
  protected int doReadMessages(List<Object> buf) throws Exception {
    int maximumMessageSize = this.config.getReceiveBufferSize();
    ByteBuf byteBuf = this.config.getAllocator().directBuffer(maximumMessageSize);
    int receivedMessageSize = byteBuf.writeBytes((ScatteringByteChannel)javaChannel(), maximumMessageSize);
    if (receivedMessageSize <= 0) {
      byteBuf.release();
      return 0;
    } 
    if (receivedMessageSize >= maximumMessageSize) {
      javaChannel().close();
      throw new ChannelException("Invalid config : increase receive buffer size to avoid message truncation");
    } 
    buf.add(new UdtMessage(byteBuf));
    return 1;
  }
  
  protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
    long writtenBytes;
    UdtMessage message = (UdtMessage)msg;
    ByteBuf byteBuf = message.content();
    int messageSize = byteBuf.readableBytes();
    if (byteBuf.nioBufferCount() == 1) {
      writtenBytes = javaChannel().write(byteBuf.nioBuffer());
    } else {
      writtenBytes = javaChannel().write(byteBuf.nioBuffers());
    } 
    if (writtenBytes <= 0L && messageSize > 0)
      return false; 
    if (writtenBytes != messageSize)
      throw new Error("Provider error: failed to write message. Provider library should be upgraded."); 
    return true;
  }
  
  protected final Object filterOutboundMessage(Object msg) throws Exception {
    if (msg instanceof UdtMessage)
      return msg; 
    throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPE);
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channe\\udt\nio\NioUdtMessageConnectorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */