package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoop;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class EpollSocketChannel extends AbstractEpollChannel implements SocketChannel {
  private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
  
  private final EpollSocketChannelConfig config;
  
  private ChannelPromise connectPromise;
  
  private ScheduledFuture<?> connectTimeoutFuture;
  
  private SocketAddress requestedRemoteAddress;
  
  private volatile InetSocketAddress local;
  
  private volatile InetSocketAddress remote;
  
  private volatile boolean inputShutdown;
  
  private volatile boolean outputShutdown;
  
  EpollSocketChannel(Channel parent, int fd) {
    super(parent, fd, 1, true);
    this.config = new EpollSocketChannelConfig(this);
    this.remote = Native.remoteAddress(fd);
    this.local = Native.localAddress(fd);
  }
  
  public EpollSocketChannel() {
    super(Native.socketStreamFd(), 1);
    this.config = new EpollSocketChannelConfig(this);
  }
  
  protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
    return new EpollSocketUnsafe();
  }
  
  protected SocketAddress localAddress0() {
    return this.local;
  }
  
  protected SocketAddress remoteAddress0() {
    return this.remote;
  }
  
  protected void doBind(SocketAddress local) throws Exception {
    InetSocketAddress localAddress = (InetSocketAddress)local;
    Native.bind(this.fd, localAddress.getAddress(), localAddress.getPort());
    this.local = Native.localAddress(this.fd);
  }
  
  private boolean writeBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
    // Byte code:
    //   0: aload_2
    //   1: invokevirtual readableBytes : ()I
    //   4: istore_3
    //   5: iload_3
    //   6: ifne -> 16
    //   9: aload_1
    //   10: invokevirtual remove : ()Z
    //   13: pop
    //   14: iconst_1
    //   15: ireturn
    //   16: iconst_0
    //   17: istore #4
    //   19: lconst_0
    //   20: lstore #5
    //   22: aload_2
    //   23: invokevirtual hasMemoryAddress : ()Z
    //   26: ifeq -> 118
    //   29: aload_2
    //   30: invokevirtual memoryAddress : ()J
    //   33: lstore #7
    //   35: aload_2
    //   36: invokevirtual readerIndex : ()I
    //   39: istore #9
    //   41: aload_2
    //   42: invokevirtual writerIndex : ()I
    //   45: istore #10
    //   47: aload_0
    //   48: getfield fd : I
    //   51: lload #7
    //   53: iload #9
    //   55: iload #10
    //   57: invokestatic writeAddress : (IJII)I
    //   60: istore #11
    //   62: iload #11
    //   64: ifle -> 99
    //   67: lload #5
    //   69: iload #11
    //   71: i2l
    //   72: ladd
    //   73: lstore #5
    //   75: lload #5
    //   77: iload_3
    //   78: i2l
    //   79: lcmp
    //   80: ifne -> 89
    //   83: iconst_1
    //   84: istore #4
    //   86: goto -> 109
    //   89: iload #9
    //   91: iload #11
    //   93: iadd
    //   94: istore #9
    //   96: goto -> 106
    //   99: aload_0
    //   100: invokevirtual setEpollOut : ()V
    //   103: goto -> 109
    //   106: goto -> 47
    //   109: aload_1
    //   110: lload #5
    //   112: invokevirtual removeBytes : (J)V
    //   115: iload #4
    //   117: ireturn
    //   118: aload_2
    //   119: invokevirtual nioBufferCount : ()I
    //   122: iconst_1
    //   123: if_icmpne -> 230
    //   126: aload_2
    //   127: invokevirtual readerIndex : ()I
    //   130: istore #7
    //   132: aload_2
    //   133: iload #7
    //   135: aload_2
    //   136: invokevirtual readableBytes : ()I
    //   139: invokevirtual internalNioBuffer : (II)Ljava/nio/ByteBuffer;
    //   142: astore #8
    //   144: aload #8
    //   146: invokevirtual position : ()I
    //   149: istore #9
    //   151: aload #8
    //   153: invokevirtual limit : ()I
    //   156: istore #10
    //   158: aload_0
    //   159: getfield fd : I
    //   162: aload #8
    //   164: iload #9
    //   166: iload #10
    //   168: invokestatic write : (ILjava/nio/ByteBuffer;II)I
    //   171: istore #11
    //   173: iload #11
    //   175: ifle -> 211
    //   178: aload #8
    //   180: iload #9
    //   182: iload #11
    //   184: iadd
    //   185: invokevirtual position : (I)Ljava/nio/Buffer;
    //   188: pop
    //   189: lload #5
    //   191: iload #11
    //   193: i2l
    //   194: ladd
    //   195: lstore #5
    //   197: lload #5
    //   199: iload_3
    //   200: i2l
    //   201: lcmp
    //   202: ifne -> 218
    //   205: iconst_1
    //   206: istore #4
    //   208: goto -> 221
    //   211: aload_0
    //   212: invokevirtual setEpollOut : ()V
    //   215: goto -> 221
    //   218: goto -> 144
    //   221: aload_1
    //   222: lload #5
    //   224: invokevirtual removeBytes : (J)V
    //   227: iload #4
    //   229: ireturn
    //   230: aload_2
    //   231: invokevirtual nioBuffers : ()[Ljava/nio/ByteBuffer;
    //   234: astore #7
    //   236: aload_0
    //   237: aload_1
    //   238: aload #7
    //   240: aload #7
    //   242: arraylength
    //   243: iload_3
    //   244: i2l
    //   245: invokespecial writeBytesMultiple : (Lio/netty/channel/ChannelOutboundBuffer;[Ljava/nio/ByteBuffer;IJ)Z
    //   248: ireturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #112	-> 0
    //   #113	-> 5
    //   #114	-> 9
    //   #115	-> 14
    //   #118	-> 16
    //   #119	-> 19
    //   #120	-> 22
    //   #121	-> 29
    //   #122	-> 35
    //   #123	-> 41
    //   #125	-> 47
    //   #126	-> 62
    //   #127	-> 67
    //   #128	-> 75
    //   #129	-> 83
    //   #130	-> 86
    //   #132	-> 89
    //   #135	-> 99
    //   #136	-> 103
    //   #138	-> 106
    //   #140	-> 109
    //   #141	-> 115
    //   #142	-> 118
    //   #143	-> 126
    //   #144	-> 132
    //   #146	-> 144
    //   #147	-> 151
    //   #148	-> 158
    //   #149	-> 173
    //   #150	-> 178
    //   #151	-> 189
    //   #152	-> 197
    //   #153	-> 205
    //   #154	-> 208
    //   #158	-> 211
    //   #159	-> 215
    //   #161	-> 218
    //   #163	-> 221
    //   #164	-> 227
    //   #166	-> 230
    //   #167	-> 236
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   62	44	11	localFlushedAmount	I
    //   35	83	7	memoryAddress	J
    //   41	77	9	readerIndex	I
    //   47	71	10	writerIndex	I
    //   151	67	9	pos	I
    //   158	60	10	limit	I
    //   173	45	11	localFlushedAmount	I
    //   132	98	7	readerIndex	I
    //   144	86	8	nioBuf	Ljava/nio/ByteBuffer;
    //   236	13	7	nioBuffers	[Ljava/nio/ByteBuffer;
    //   0	249	0	this	Lio/netty/channel/epoll/EpollSocketChannel;
    //   0	249	1	in	Lio/netty/channel/ChannelOutboundBuffer;
    //   0	249	2	buf	Lio/netty/buffer/ByteBuf;
    //   5	244	3	readableBytes	I
    //   19	230	4	done	Z
    //   22	227	5	writtenBytes	J
  }
  
  private boolean writeBytesMultiple(ChannelOutboundBuffer in, IovArray array) throws IOException {
    long expectedWrittenBytes = array.size();
    int cnt = array.count();
    assert expectedWrittenBytes != 0L;
    assert cnt != 0;
    boolean done = false;
    long writtenBytes = 0L;
    int offset = 0;
    int end = offset + cnt;
    label29: while (true) {
      long localWrittenBytes = Native.writevAddresses(this.fd, array.memoryAddress(offset), cnt);
      if (localWrittenBytes == 0L) {
        setEpollOut();
        break;
      } 
      expectedWrittenBytes -= localWrittenBytes;
      writtenBytes += localWrittenBytes;
      if (expectedWrittenBytes == 0L) {
        done = true;
        break;
      } 
      while (true) {
        long bytes = array.processWritten(offset, localWrittenBytes);
        if (bytes == -1L)
          continue label29; 
        offset++;
        cnt--;
        localWrittenBytes -= bytes;
        if (offset < end) {
          if (localWrittenBytes <= 0L)
            continue label29; 
          continue;
        } 
        continue label29;
      } 
    } 
    in.removeBytes(writtenBytes);
    return done;
  }
  
  private boolean writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes) throws IOException {
    assert expectedWrittenBytes != 0L;
    boolean done = false;
    long writtenBytes = 0L;
    int offset = 0;
    int end = offset + nioBufferCnt;
    label26: while (true) {
      long localWrittenBytes = Native.writev(this.fd, nioBuffers, offset, nioBufferCnt);
      if (localWrittenBytes == 0L) {
        setEpollOut();
        break;
      } 
      expectedWrittenBytes -= localWrittenBytes;
      writtenBytes += localWrittenBytes;
      if (expectedWrittenBytes == 0L) {
        done = true;
        break;
      } 
      while (true) {
        ByteBuffer buffer = nioBuffers[offset];
        int pos = buffer.position();
        int bytes = buffer.limit() - pos;
        if (bytes > localWrittenBytes) {
          buffer.position(pos + (int)localWrittenBytes);
          continue label26;
        } 
        offset++;
        nioBufferCnt--;
        localWrittenBytes -= bytes;
        if (offset < end) {
          if (localWrittenBytes <= 0L)
            continue label26; 
          continue;
        } 
        continue label26;
      } 
    } 
    in.removeBytes(writtenBytes);
    return done;
  }
  
  private boolean writeFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region) throws Exception {
    long regionCount = region.count();
    if (region.transfered() >= regionCount) {
      in.remove();
      return true;
    } 
    long baseOffset = region.position();
    boolean done = false;
    long flushedAmount = 0L;
    for (int i = config().getWriteSpinCount() - 1; i >= 0; i--) {
      long offset = region.transfered();
      long localFlushedAmount = Native.sendfile(this.fd, region, baseOffset, offset, regionCount - offset);
      if (localFlushedAmount == 0L) {
        setEpollOut();
        break;
      } 
      flushedAmount += localFlushedAmount;
      if (region.transfered() >= regionCount) {
        done = true;
        break;
      } 
    } 
    if (flushedAmount > 0L)
      in.progress(flushedAmount); 
    if (done)
      in.remove(); 
    return done;
  }
  
  protected void doWrite(ChannelOutboundBuffer in) throws Exception {
    int msgCount;
    do {
      msgCount = in.size();
      if (msgCount == 0) {
        clearEpollOut();
        break;
      } 
    } while ((msgCount > 1) ? ((in.current() instanceof ByteBuf) ? 
      !doWriteMultiple(in) : 
      
      !doWriteSingle(in)) : !doWriteSingle(in));
  }
  
  private boolean doWriteSingle(ChannelOutboundBuffer in) throws Exception {
    Object msg = in.current();
    if (msg instanceof ByteBuf) {
      ByteBuf buf = (ByteBuf)msg;
      if (!writeBytes(in, buf))
        return false; 
    } else if (msg instanceof DefaultFileRegion) {
      DefaultFileRegion region = (DefaultFileRegion)msg;
      if (!writeFileRegion(in, region))
        return false; 
    } else {
      throw new Error();
    } 
    return true;
  }
  
  private boolean doWriteMultiple(ChannelOutboundBuffer in) throws Exception {
    if (PlatformDependent.hasUnsafe()) {
      IovArray array = IovArray.get(in);
      int cnt = array.count();
      if (cnt >= 1) {
        if (!writeBytesMultiple(in, array))
          return false; 
      } else {
        in.removeBytes(0L);
      } 
    } else {
      ByteBuffer[] buffers = in.nioBuffers();
      int cnt = in.nioBufferCount();
      if (cnt >= 1) {
        if (!writeBytesMultiple(in, buffers, cnt, in.nioBufferSize()))
          return false; 
      } else {
        in.removeBytes(0L);
      } 
    } 
    return true;
  }
  
  protected Object filterOutboundMessage(Object msg) {
    if (msg instanceof ByteBuf) {
      ByteBuf buf = (ByteBuf)msg;
      if (!buf.hasMemoryAddress() && (PlatformDependent.hasUnsafe() || !buf.isDirect())) {
        buf = newDirectBuffer(buf);
        assert buf.hasMemoryAddress();
      } 
      return buf;
    } 
    if (msg instanceof DefaultFileRegion)
      return msg; 
    throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
  }
  
  public EpollSocketChannelConfig config() {
    return this.config;
  }
  
  public boolean isInputShutdown() {
    return this.inputShutdown;
  }
  
  public boolean isOutputShutdown() {
    return (this.outputShutdown || !isActive());
  }
  
  public ChannelFuture shutdownOutput() {
    return shutdownOutput(newPromise());
  }
  
  public ChannelFuture shutdownOutput(final ChannelPromise promise) {
    EventLoop loop = eventLoop();
    if (loop.inEventLoop()) {
      try {
        Native.shutdown(this.fd, false, true);
        this.outputShutdown = true;
        promise.setSuccess();
      } catch (Throwable t) {
        promise.setFailure(t);
      } 
    } else {
      loop.execute(new Runnable() {
            public void run() {
              EpollSocketChannel.this.shutdownOutput(promise);
            }
          });
    } 
    return (ChannelFuture)promise;
  }
  
  public ServerSocketChannel parent() {
    return (ServerSocketChannel)super.parent();
  }
  
  final class EpollSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
    private RecvByteBufAllocator.Handle allocHandle;
    
    private void closeOnRead(ChannelPipeline pipeline) {
      EpollSocketChannel.this.inputShutdown = true;
      if (EpollSocketChannel.this.isOpen())
        if (Boolean.TRUE.equals(EpollSocketChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
          clearEpollIn0();
          pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
        } else {
          close(voidPromise());
        }  
    }
    
    private boolean handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close) {
      if (byteBuf != null)
        if (byteBuf.isReadable()) {
          this.readPending = false;
          pipeline.fireChannelRead(byteBuf);
        } else {
          byteBuf.release();
        }  
      pipeline.fireChannelReadComplete();
      pipeline.fireExceptionCaught(cause);
      if (close || cause instanceof IOException) {
        closeOnRead(pipeline);
        return true;
      } 
      return false;
    }
    
    public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
      if (!promise.setUncancellable() || !ensureOpen(promise))
        return; 
      try {
        if (EpollSocketChannel.this.connectPromise != null)
          throw new IllegalStateException("connection attempt already made"); 
        boolean wasActive = EpollSocketChannel.this.isActive();
        if (doConnect((InetSocketAddress)remoteAddress, (InetSocketAddress)localAddress)) {
          fulfillConnectPromise(promise, wasActive);
        } else {
          EpollSocketChannel.this.connectPromise = promise;
          EpollSocketChannel.this.requestedRemoteAddress = remoteAddress;
          int connectTimeoutMillis = EpollSocketChannel.this.config().getConnectTimeoutMillis();
          if (connectTimeoutMillis > 0)
            EpollSocketChannel.this.connectTimeoutFuture = (ScheduledFuture<?>)EpollSocketChannel.this.eventLoop().schedule(new Runnable() {
                  public void run() {
                    ChannelPromise connectPromise = EpollSocketChannel.this.connectPromise;
                    ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
                    if (connectPromise != null && connectPromise.tryFailure((Throwable)cause))
                      EpollSocketChannel.EpollSocketUnsafe.this.close(EpollSocketChannel.EpollSocketUnsafe.this.voidPromise()); 
                  }
                },  connectTimeoutMillis, TimeUnit.MILLISECONDS); 
          promise.addListener((GenericFutureListener)new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                  if (future.isCancelled()) {
                    if (EpollSocketChannel.this.connectTimeoutFuture != null)
                      EpollSocketChannel.this.connectTimeoutFuture.cancel(false); 
                    EpollSocketChannel.this.connectPromise = null;
                    EpollSocketChannel.EpollSocketUnsafe.this.close(EpollSocketChannel.EpollSocketUnsafe.this.voidPromise());
                  } 
                }
              });
        } 
      } catch (Throwable t) {
        if (t instanceof ConnectException) {
          Throwable newT = new ConnectException(t.getMessage() + ": " + remoteAddress);
          newT.setStackTrace(t.getStackTrace());
          t = newT;
        } 
        closeIfClosed();
        promise.tryFailure(t);
      } 
    }
    
    private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
      if (promise == null)
        return; 
      EpollSocketChannel.this.active = true;
      boolean promiseSet = promise.trySuccess();
      if (!wasActive && EpollSocketChannel.this.isActive())
        EpollSocketChannel.this.pipeline().fireChannelActive(); 
      if (!promiseSet)
        close(voidPromise()); 
    }
    
    private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
      if (promise == null)
        return; 
      promise.tryFailure(cause);
      closeIfClosed();
    }
    
    private void finishConnect() {
      assert EpollSocketChannel.this.eventLoop().inEventLoop();
      boolean connectStillInProgress = false;
      try {
        boolean wasActive = EpollSocketChannel.this.isActive();
        if (!doFinishConnect()) {
          connectStillInProgress = true;
          return;
        } 
        fulfillConnectPromise(EpollSocketChannel.this.connectPromise, wasActive);
      } catch (Throwable t) {
        if (t instanceof ConnectException) {
          Throwable newT = new ConnectException(t.getMessage() + ": " + EpollSocketChannel.this.requestedRemoteAddress);
          newT.setStackTrace(t.getStackTrace());
          t = newT;
        } 
        fulfillConnectPromise(EpollSocketChannel.this.connectPromise, t);
      } finally {
        if (!connectStillInProgress) {
          if (EpollSocketChannel.this.connectTimeoutFuture != null)
            EpollSocketChannel.this.connectTimeoutFuture.cancel(false); 
          EpollSocketChannel.this.connectPromise = null;
        } 
      } 
    }
    
    void epollOutReady() {
      if (EpollSocketChannel.this.connectPromise != null) {
        finishConnect();
      } else {
        super.epollOutReady();
      } 
    }
    
    private boolean doConnect(InetSocketAddress remoteAddress, InetSocketAddress localAddress) throws Exception {
      if (localAddress != null) {
        AbstractEpollChannel.checkResolvable(localAddress);
        Native.bind(EpollSocketChannel.this.fd, localAddress.getAddress(), localAddress.getPort());
      } 
      boolean success = false;
      try {
        AbstractEpollChannel.checkResolvable(remoteAddress);
        boolean connected = Native.connect(EpollSocketChannel.this.fd, remoteAddress.getAddress(), remoteAddress.getPort());
        EpollSocketChannel.this.remote = remoteAddress;
        EpollSocketChannel.this.local = Native.localAddress(EpollSocketChannel.this.fd);
        if (!connected)
          EpollSocketChannel.this.setEpollOut(); 
        success = true;
        return connected;
      } finally {
        if (!success)
          EpollSocketChannel.this.doClose(); 
      } 
    }
    
    private boolean doFinishConnect() throws Exception {
      if (Native.finishConnect(EpollSocketChannel.this.fd)) {
        EpollSocketChannel.this.clearEpollOut();
        return true;
      } 
      EpollSocketChannel.this.setEpollOut();
      return false;
    }
    
    private int doReadBytes(ByteBuf byteBuf) throws Exception {
      int localReadAmount, writerIndex = byteBuf.writerIndex();
      if (byteBuf.hasMemoryAddress()) {
        localReadAmount = Native.readAddress(EpollSocketChannel.this.fd, byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
      } else {
        ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, byteBuf.writableBytes());
        localReadAmount = Native.read(EpollSocketChannel.this.fd, buf, buf.position(), buf.limit());
      } 
      if (localReadAmount > 0)
        byteBuf.writerIndex(writerIndex + localReadAmount); 
      return localReadAmount;
    }
    
    void epollRdHupReady() {
      if (EpollSocketChannel.this.isActive()) {
        epollInReady();
      } else {
        closeOnRead(EpollSocketChannel.this.pipeline());
      } 
    }
    
    void epollInReady() {
      EpollSocketChannelConfig epollSocketChannelConfig = EpollSocketChannel.this.config();
      ChannelPipeline pipeline = EpollSocketChannel.this.pipeline();
      ByteBufAllocator allocator = epollSocketChannelConfig.getAllocator();
      RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
      if (allocHandle == null)
        this.allocHandle = allocHandle = epollSocketChannelConfig.getRecvByteBufAllocator().newHandle(); 
      ByteBuf byteBuf = null;
      boolean close = false;
      try {
        int writable, localReadAmount, totalReadAmount = 0;
        do {
          byteBuf = allocHandle.allocate(allocator);
          writable = byteBuf.writableBytes();
          localReadAmount = doReadBytes(byteBuf);
          if (localReadAmount <= 0) {
            byteBuf.release();
            close = (localReadAmount < 0);
            break;
          } 
          this.readPending = false;
          pipeline.fireChannelRead(byteBuf);
          byteBuf = null;
          if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount) {
            allocHandle.record(totalReadAmount);
            totalReadAmount = localReadAmount;
          } else {
            totalReadAmount += localReadAmount;
          } 
        } while (localReadAmount >= writable);
        pipeline.fireChannelReadComplete();
        allocHandle.record(totalReadAmount);
        if (close) {
          closeOnRead(pipeline);
          close = false;
        } 
      } catch (Throwable t) {
        boolean closed = handleReadException(pipeline, byteBuf, t, close);
        if (!closed)
          EpollSocketChannel.this.eventLoop().execute(new Runnable() {
                public void run() {
                  EpollSocketChannel.EpollSocketUnsafe.this.epollInReady();
                }
              }); 
      } finally {
        if (!epollSocketChannelConfig.isAutoRead() && !this.readPending)
          clearEpollIn0(); 
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\epoll\EpollSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */