package io.netty.channel.socket.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.FileRegion;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.OneTimeTask;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

public class NioSocketChannel extends AbstractNioByteChannel implements SocketChannel {
  private static final ChannelMetadata METADATA = new ChannelMetadata(false);
  
  private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
  
  private final SocketChannelConfig config;
  
  private static SocketChannel newSocket(SelectorProvider provider) {
    try {
      return provider.openSocketChannel();
    } catch (IOException e) {
      throw new ChannelException("Failed to open a socket.", e);
    } 
  }
  
  public NioSocketChannel() {
    this(newSocket(DEFAULT_SELECTOR_PROVIDER));
  }
  
  public NioSocketChannel(SelectorProvider provider) {
    this(newSocket(provider));
  }
  
  public NioSocketChannel(SocketChannel socket) {
    this((Channel)null, socket);
  }
  
  public NioSocketChannel(Channel parent, SocketChannel socket) {
    super(parent, socket);
    this.config = (SocketChannelConfig)new NioSocketChannelConfig(this, socket.socket());
  }
  
  public ServerSocketChannel parent() {
    return (ServerSocketChannel)super.parent();
  }
  
  public ChannelMetadata metadata() {
    return METADATA;
  }
  
  public SocketChannelConfig config() {
    return this.config;
  }
  
  protected SocketChannel javaChannel() {
    return (SocketChannel)super.javaChannel();
  }
  
  public boolean isActive() {
    SocketChannel ch = javaChannel();
    return (ch.isOpen() && ch.isConnected());
  }
  
  public boolean isInputShutdown() {
    return super.isInputShutdown();
  }
  
  public InetSocketAddress localAddress() {
    return (InetSocketAddress)super.localAddress();
  }
  
  public InetSocketAddress remoteAddress() {
    return (InetSocketAddress)super.remoteAddress();
  }
  
  public boolean isOutputShutdown() {
    return (javaChannel().socket().isOutputShutdown() || !isActive());
  }
  
  public ChannelFuture shutdownOutput() {
    return shutdownOutput(newPromise());
  }
  
  public ChannelFuture shutdownOutput(final ChannelPromise promise) {
    NioEventLoop nioEventLoop = eventLoop();
    if (nioEventLoop.inEventLoop()) {
      try {
        javaChannel().socket().shutdownOutput();
        promise.setSuccess();
      } catch (Throwable t) {
        promise.setFailure(t);
      } 
    } else {
      nioEventLoop.execute((Runnable)new OneTimeTask() {
            public void run() {
              NioSocketChannel.this.shutdownOutput(promise);
            }
          });
    } 
    return (ChannelFuture)promise;
  }
  
  protected SocketAddress localAddress0() {
    return javaChannel().socket().getLocalSocketAddress();
  }
  
  protected SocketAddress remoteAddress0() {
    return javaChannel().socket().getRemoteSocketAddress();
  }
  
  protected void doBind(SocketAddress localAddress) throws Exception {
    javaChannel().socket().bind(localAddress);
  }
  
  protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
    if (localAddress != null)
      javaChannel().socket().bind(localAddress); 
    boolean success = false;
    try {
      boolean connected = javaChannel().connect(remoteAddress);
      if (!connected)
        selectionKey().interestOps(8); 
      success = true;
      return connected;
    } finally {
      if (!success)
        doClose(); 
    } 
  }
  
  protected void doFinishConnect() throws Exception {
    if (!javaChannel().finishConnect())
      throw new Error(); 
  }
  
  protected void doDisconnect() throws Exception {
    doClose();
  }
  
  protected void doClose() throws Exception {
    javaChannel().close();
  }
  
  protected int doReadBytes(ByteBuf byteBuf) throws Exception {
    return byteBuf.writeBytes(javaChannel(), byteBuf.writableBytes());
  }
  
  protected int doWriteBytes(ByteBuf buf) throws Exception {
    int expectedWrittenBytes = buf.readableBytes();
    return buf.readBytes(javaChannel(), expectedWrittenBytes);
  }
  
  protected long doWriteFileRegion(FileRegion region) throws Exception {
    long position = region.transfered();
    return region.transferTo(javaChannel(), position);
  }
  
  protected void doWrite(ChannelOutboundBuffer in) throws Exception {
    while (true) {
      ByteBuffer nioBuffer;
      int i, size = in.size();
      if (size == 0) {
        clearOpWrite();
        break;
      } 
      long writtenBytes = 0L;
      boolean done = false;
      boolean setOpWrite = false;
      ByteBuffer[] nioBuffers = in.nioBuffers();
      int nioBufferCnt = in.nioBufferCount();
      long expectedWrittenBytes = in.nioBufferSize();
      SocketChannel ch = javaChannel();
      switch (nioBufferCnt) {
        case 0:
          super.doWrite(in);
          return;
        case 1:
          nioBuffer = nioBuffers[0];
          for (i = config().getWriteSpinCount() - 1; i >= 0; i--) {
            int localWrittenBytes = ch.write(nioBuffer);
            if (localWrittenBytes == 0) {
              setOpWrite = true;
              break;
            } 
            expectedWrittenBytes -= localWrittenBytes;
            writtenBytes += localWrittenBytes;
            if (expectedWrittenBytes == 0L) {
              done = true;
              break;
            } 
          } 
          break;
        default:
          for (i = config().getWriteSpinCount() - 1; i >= 0; i--) {
            long localWrittenBytes = ch.write(nioBuffers, 0, nioBufferCnt);
            if (localWrittenBytes == 0L) {
              setOpWrite = true;
              break;
            } 
            expectedWrittenBytes -= localWrittenBytes;
            writtenBytes += localWrittenBytes;
            if (expectedWrittenBytes == 0L) {
              done = true;
              break;
            } 
          } 
          break;
      } 
      in.removeBytes(writtenBytes);
      if (!done) {
        incompleteWrite(setOpWrite);
        break;
      } 
    } 
  }
  
  private final class NioSocketChannelConfig extends DefaultSocketChannelConfig {
    private NioSocketChannelConfig(NioSocketChannel channel, Socket javaSocket) {
      super(channel, javaSocket);
    }
    
    protected void autoReadCleared() {
      NioSocketChannel.this.setReadPending(false);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\nio\NioSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */