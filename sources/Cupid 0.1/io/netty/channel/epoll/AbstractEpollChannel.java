package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.EventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.OneTimeTask;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.UnresolvedAddressException;

abstract class AbstractEpollChannel extends AbstractChannel {
  private static final ChannelMetadata DATA = new ChannelMetadata(false);
  
  private final int readFlag;
  
  protected int flags;
  
  protected volatile boolean active;
  
  volatile int fd;
  
  int id;
  
  AbstractEpollChannel(int fd, int flag) {
    this((Channel)null, fd, flag, false);
  }
  
  AbstractEpollChannel(Channel parent, int fd, int flag, boolean active) {
    super(parent);
    this.fd = fd;
    this.readFlag = flag;
    this.flags |= flag;
    this.active = active;
  }
  
  public boolean isActive() {
    return this.active;
  }
  
  public ChannelMetadata metadata() {
    return DATA;
  }
  
  protected void doClose() throws Exception {
    this.active = false;
    doDeregister();
    int fd = this.fd;
    this.fd = -1;
    Native.close(fd);
  }
  
  public InetSocketAddress remoteAddress() {
    return (InetSocketAddress)super.remoteAddress();
  }
  
  public InetSocketAddress localAddress() {
    return (InetSocketAddress)super.localAddress();
  }
  
  protected void doDisconnect() throws Exception {
    doClose();
  }
  
  protected boolean isCompatible(EventLoop loop) {
    return loop instanceof EpollEventLoop;
  }
  
  public boolean isOpen() {
    return (this.fd != -1);
  }
  
  protected void doDeregister() throws Exception {
    ((EpollEventLoop)eventLoop()).remove(this);
  }
  
  protected void doBeginRead() throws Exception {
    ((AbstractEpollUnsafe)unsafe()).readPending = true;
    if ((this.flags & this.readFlag) == 0) {
      this.flags |= this.readFlag;
      modifyEvents();
    } 
  }
  
  final void clearEpollIn() {
    if (isRegistered()) {
      EventLoop loop = eventLoop();
      final AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe)unsafe();
      if (loop.inEventLoop()) {
        unsafe.clearEpollIn0();
      } else {
        loop.execute((Runnable)new OneTimeTask() {
              public void run() {
                if (!AbstractEpollChannel.this.config().isAutoRead() && !unsafe.readPending)
                  unsafe.clearEpollIn0(); 
              }
            });
      } 
    } else {
      this.flags &= this.readFlag ^ 0xFFFFFFFF;
    } 
  }
  
  protected final void setEpollOut() {
    if ((this.flags & 0x2) == 0) {
      this.flags |= 0x2;
      modifyEvents();
    } 
  }
  
  protected final void clearEpollOut() {
    if ((this.flags & 0x2) != 0) {
      this.flags &= 0xFFFFFFFD;
      modifyEvents();
    } 
  }
  
  private void modifyEvents() {
    if (isOpen())
      ((EpollEventLoop)eventLoop()).modify(this); 
  }
  
  protected void doRegister() throws Exception {
    EpollEventLoop loop = (EpollEventLoop)eventLoop();
    loop.add(this);
  }
  
  protected final ByteBuf newDirectBuffer(ByteBuf buf) {
    return newDirectBuffer(buf, buf);
  }
  
  protected final ByteBuf newDirectBuffer(Object holder, ByteBuf buf) {
    int readableBytes = buf.readableBytes();
    if (readableBytes == 0) {
      ReferenceCountUtil.safeRelease(holder);
      return Unpooled.EMPTY_BUFFER;
    } 
    ByteBufAllocator alloc = alloc();
    if (alloc.isDirectBufferPooled())
      return newDirectBuffer0(holder, buf, alloc, readableBytes); 
    ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
    if (directBuf == null)
      return newDirectBuffer0(holder, buf, alloc, readableBytes); 
    directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
    ReferenceCountUtil.safeRelease(holder);
    return directBuf;
  }
  
  private static ByteBuf newDirectBuffer0(Object holder, ByteBuf buf, ByteBufAllocator alloc, int capacity) {
    ByteBuf directBuf = alloc.directBuffer(capacity);
    directBuf.writeBytes(buf, buf.readerIndex(), capacity);
    ReferenceCountUtil.safeRelease(holder);
    return directBuf;
  }
  
  protected static void checkResolvable(InetSocketAddress addr) {
    if (addr.isUnresolved())
      throw new UnresolvedAddressException(); 
  }
  
  protected abstract AbstractEpollUnsafe newUnsafe();
  
  protected abstract class AbstractEpollUnsafe extends AbstractChannel.AbstractUnsafe {
    protected boolean readPending;
    
    protected AbstractEpollUnsafe() {
      super(AbstractEpollChannel.this);
    }
    
    abstract void epollInReady();
    
    void epollRdHupReady() {}
    
    protected void flush0() {
      if (isFlushPending())
        return; 
      super.flush0();
    }
    
    void epollOutReady() {
      super.flush0();
    }
    
    private boolean isFlushPending() {
      return ((AbstractEpollChannel.this.flags & 0x2) != 0);
    }
    
    protected final void clearEpollIn0() {
      if ((AbstractEpollChannel.this.flags & AbstractEpollChannel.this.readFlag) != 0) {
        AbstractEpollChannel.this.flags &= AbstractEpollChannel.this.readFlag ^ 0xFFFFFFFF;
        AbstractEpollChannel.this.modifyEvents();
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\epoll\AbstractEpollChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */