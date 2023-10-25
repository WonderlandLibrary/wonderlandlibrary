package io.netty.channel.local;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.InternalThreadLocalMap;
import java.net.SocketAddress;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public class LocalChannel extends AbstractChannel {
  private static final ChannelMetadata METADATA = new ChannelMetadata(false);
  
  private static final int MAX_READER_STACK_DEPTH = 8;
  
  private final ChannelConfig config = (ChannelConfig)new DefaultChannelConfig((Channel)this);
  
  private final Queue<Object> inboundBuffer = new ArrayDeque();
  
  private final Runnable readTask = new Runnable() {
      public void run() {
        ChannelPipeline pipeline = LocalChannel.this.pipeline();
        while (true) {
          Object m = LocalChannel.this.inboundBuffer.poll();
          if (m == null)
            break; 
          pipeline.fireChannelRead(m);
        } 
        pipeline.fireChannelReadComplete();
      }
    };
  
  private final Runnable shutdownHook = new Runnable() {
      public void run() {
        LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
      }
    };
  
  private volatile int state;
  
  private volatile LocalChannel peer;
  
  private volatile LocalAddress localAddress;
  
  private volatile LocalAddress remoteAddress;
  
  private volatile ChannelPromise connectPromise;
  
  private volatile boolean readInProgress;
  
  private volatile boolean registerInProgress;
  
  public LocalChannel() {
    super(null);
  }
  
  LocalChannel(LocalServerChannel parent, LocalChannel peer) {
    super((Channel)parent);
    this.peer = peer;
    this.localAddress = parent.localAddress();
    this.remoteAddress = peer.localAddress();
  }
  
  public ChannelMetadata metadata() {
    return METADATA;
  }
  
  public ChannelConfig config() {
    return this.config;
  }
  
  public LocalServerChannel parent() {
    return (LocalServerChannel)super.parent();
  }
  
  public LocalAddress localAddress() {
    return (LocalAddress)super.localAddress();
  }
  
  public LocalAddress remoteAddress() {
    return (LocalAddress)super.remoteAddress();
  }
  
  public boolean isOpen() {
    return (this.state < 3);
  }
  
  public boolean isActive() {
    return (this.state == 2);
  }
  
  protected AbstractChannel.AbstractUnsafe newUnsafe() {
    return new LocalUnsafe();
  }
  
  protected boolean isCompatible(EventLoop loop) {
    return loop instanceof io.netty.channel.SingleThreadEventLoop;
  }
  
  protected SocketAddress localAddress0() {
    return this.localAddress;
  }
  
  protected SocketAddress remoteAddress0() {
    return this.remoteAddress;
  }
  
  protected void doRegister() throws Exception {
    if (this.peer != null && parent() != null) {
      final LocalChannel peer = this.peer;
      this.registerInProgress = true;
      this.state = 2;
      peer.remoteAddress = parent().localAddress();
      peer.state = 2;
      peer.eventLoop().execute(new Runnable() {
            public void run() {
              LocalChannel.this.registerInProgress = false;
              peer.pipeline().fireChannelActive();
              peer.connectPromise.setSuccess();
            }
          });
    } 
    ((SingleThreadEventExecutor)eventLoop()).addShutdownHook(this.shutdownHook);
  }
  
  protected void doBind(SocketAddress localAddress) throws Exception {
    this.localAddress = LocalChannelRegistry.register((Channel)this, this.localAddress, localAddress);
    this.state = 1;
  }
  
  protected void doDisconnect() throws Exception {
    doClose();
  }
  
  protected void doClose() throws Exception {
    if (this.state <= 2) {
      if (this.localAddress != null) {
        if (parent() == null)
          LocalChannelRegistry.unregister(this.localAddress); 
        this.localAddress = null;
      } 
      this.state = 3;
    } 
    final LocalChannel peer = this.peer;
    if (peer != null && peer.isActive()) {
      EventLoop eventLoop = peer.eventLoop();
      if (eventLoop.inEventLoop() && !this.registerInProgress) {
        peer.unsafe().close(unsafe().voidPromise());
      } else {
        peer.eventLoop().execute(new Runnable() {
              public void run() {
                peer.unsafe().close(LocalChannel.this.unsafe().voidPromise());
              }
            });
      } 
      this.peer = null;
    } 
  }
  
  protected void doDeregister() throws Exception {
    ((SingleThreadEventExecutor)eventLoop()).removeShutdownHook(this.shutdownHook);
  }
  
  protected void doBeginRead() throws Exception {
    if (this.readInProgress)
      return; 
    ChannelPipeline pipeline = pipeline();
    Queue<Object> inboundBuffer = this.inboundBuffer;
    if (inboundBuffer.isEmpty()) {
      this.readInProgress = true;
      return;
    } 
    InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
    Integer stackDepth = Integer.valueOf(threadLocals.localChannelReaderStackDepth());
    if (stackDepth.intValue() < 8) {
      threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue() + 1);
      try {
        while (true) {
          Object received = inboundBuffer.poll();
          if (received == null)
            break; 
          pipeline.fireChannelRead(received);
        } 
        pipeline.fireChannelReadComplete();
      } finally {
        threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue());
      } 
    } else {
      eventLoop().execute(this.readTask);
    } 
  }
  
  protected void doWrite(ChannelOutboundBuffer in) throws Exception {
    if (this.state < 2)
      throw new NotYetConnectedException(); 
    if (this.state > 2)
      throw new ClosedChannelException(); 
    final LocalChannel peer = this.peer;
    final ChannelPipeline peerPipeline = peer.pipeline();
    EventLoop peerLoop = peer.eventLoop();
    if (peerLoop == eventLoop()) {
      while (true) {
        Object msg = in.current();
        if (msg == null)
          break; 
        peer.inboundBuffer.add(msg);
        ReferenceCountUtil.retain(msg);
        in.remove();
      } 
      finishPeerRead(peer, peerPipeline);
    } else {
      final Object[] msgsCopy = new Object[in.size()];
      for (int i = 0; i < msgsCopy.length; i++) {
        msgsCopy[i] = ReferenceCountUtil.retain(in.current());
        in.remove();
      } 
      peerLoop.execute(new Runnable() {
            public void run() {
              Collections.addAll(peer.inboundBuffer, msgsCopy);
              LocalChannel.finishPeerRead(peer, peerPipeline);
            }
          });
    } 
  }
  
  private static void finishPeerRead(LocalChannel peer, ChannelPipeline peerPipeline) {
    if (peer.readInProgress) {
      peer.readInProgress = false;
      while (true) {
        Object received = peer.inboundBuffer.poll();
        if (received == null)
          break; 
        peerPipeline.fireChannelRead(received);
      } 
      peerPipeline.fireChannelReadComplete();
    } 
  }
  
  private class LocalUnsafe extends AbstractChannel.AbstractUnsafe {
    private LocalUnsafe() {
      super(LocalChannel.this);
    }
    
    public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
      if (!promise.setUncancellable() || !ensureOpen(promise))
        return; 
      if (LocalChannel.this.state == 2) {
        Exception cause = new AlreadyConnectedException();
        safeSetFailure(promise, cause);
        LocalChannel.this.pipeline().fireExceptionCaught(cause);
        return;
      } 
      if (LocalChannel.this.connectPromise != null)
        throw new ConnectionPendingException(); 
      LocalChannel.this.connectPromise = promise;
      if (LocalChannel.this.state != 1)
        if (localAddress == null)
          localAddress = new LocalAddress((Channel)LocalChannel.this);  
      if (localAddress != null)
        try {
          LocalChannel.this.doBind(localAddress);
        } catch (Throwable t) {
          safeSetFailure(promise, t);
          close(voidPromise());
          return;
        }  
      Channel boundChannel = LocalChannelRegistry.get(remoteAddress);
      if (!(boundChannel instanceof LocalServerChannel)) {
        ChannelException channelException = new ChannelException("connection refused");
        safeSetFailure(promise, (Throwable)channelException);
        close(voidPromise());
        return;
      } 
      LocalServerChannel serverChannel = (LocalServerChannel)boundChannel;
      LocalChannel.this.peer = serverChannel.serve(LocalChannel.this);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\local\LocalChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */