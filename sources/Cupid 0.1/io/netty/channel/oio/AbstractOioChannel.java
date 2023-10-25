package io.netty.channel.oio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import java.net.ConnectException;
import java.net.SocketAddress;

public abstract class AbstractOioChannel extends AbstractChannel {
  protected static final int SO_TIMEOUT = 1000;
  
  private volatile boolean readPending;
  
  private final Runnable readTask = new Runnable() {
      public void run() {
        if (!AbstractOioChannel.this.isReadPending() && !AbstractOioChannel.this.config().isAutoRead())
          return; 
        AbstractOioChannel.this.setReadPending(false);
        AbstractOioChannel.this.doRead();
      }
    };
  
  protected AbstractOioChannel(Channel parent) {
    super(parent);
  }
  
  protected AbstractChannel.AbstractUnsafe newUnsafe() {
    return new DefaultOioUnsafe();
  }
  
  private final class DefaultOioUnsafe extends AbstractChannel.AbstractUnsafe {
    private DefaultOioUnsafe() {
      super(AbstractOioChannel.this);
    }
    
    public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
      if (!promise.setUncancellable() || !ensureOpen(promise))
        return; 
      try {
        boolean wasActive = AbstractOioChannel.this.isActive();
        AbstractOioChannel.this.doConnect(remoteAddress, localAddress);
        safeSetSuccess(promise);
        if (!wasActive && AbstractOioChannel.this.isActive())
          AbstractOioChannel.this.pipeline().fireChannelActive(); 
      } catch (Throwable t) {
        if (t instanceof ConnectException) {
          Throwable newT = new ConnectException(t.getMessage() + ": " + remoteAddress);
          newT.setStackTrace(t.getStackTrace());
          t = newT;
        } 
        safeSetFailure(promise, t);
        closeIfClosed();
      } 
    }
  }
  
  protected boolean isCompatible(EventLoop loop) {
    return loop instanceof io.netty.channel.ThreadPerChannelEventLoop;
  }
  
  protected abstract void doConnect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2) throws Exception;
  
  protected void doBeginRead() throws Exception {
    if (isReadPending())
      return; 
    setReadPending(true);
    eventLoop().execute(this.readTask);
  }
  
  protected abstract void doRead();
  
  protected boolean isReadPending() {
    return this.readPending;
  }
  
  protected void setReadPending(boolean readPending) {
    this.readPending = readPending;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\oio\AbstractOioChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */