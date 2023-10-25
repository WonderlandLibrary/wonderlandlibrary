package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.channel.PendingWriteQueue;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;

public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(SslHandler.class);
  
  private static final Pattern IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
  
  private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
  
  private static final SSLException SSLENGINE_CLOSED = new SSLException("SSLEngine closed already");
  
  private static final SSLException HANDSHAKE_TIMED_OUT = new SSLException("handshake timed out");
  
  private static final ClosedChannelException CHANNEL_CLOSED = new ClosedChannelException();
  
  private volatile ChannelHandlerContext ctx;
  
  private final SSLEngine engine;
  
  private final int maxPacketBufferSize;
  
  private final Executor delegatedTaskExecutor;
  
  private final boolean wantsDirectBuffer;
  
  private final boolean wantsLargeOutboundNetworkBuffer;
  
  private boolean wantsInboundHeapBuffer;
  
  private final boolean startTls;
  
  private boolean sentFirstMessage;
  
  private boolean flushedBeforeHandshakeDone;
  
  private PendingWriteQueue pendingUnencryptedWrites;
  
  static {
    SSLENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    HANDSHAKE_TIMED_OUT.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    CHANNEL_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
  }
  
  private final LazyChannelPromise handshakePromise = new LazyChannelPromise();
  
  private final LazyChannelPromise sslCloseFuture = new LazyChannelPromise();
  
  private boolean needsFlush;
  
  private int packetLength;
  
  private volatile long handshakeTimeoutMillis = 10000L;
  
  private volatile long closeNotifyTimeoutMillis = 3000L;
  
  public SslHandler(SSLEngine engine) {
    this(engine, false);
  }
  
  public SslHandler(SSLEngine engine, boolean startTls) {
    this(engine, startTls, (Executor)ImmediateExecutor.INSTANCE);
  }
  
  @Deprecated
  public SslHandler(SSLEngine engine, Executor delegatedTaskExecutor) {
    this(engine, false, delegatedTaskExecutor);
  }
  
  @Deprecated
  public SslHandler(SSLEngine engine, boolean startTls, Executor delegatedTaskExecutor) {
    if (engine == null)
      throw new NullPointerException("engine"); 
    if (delegatedTaskExecutor == null)
      throw new NullPointerException("delegatedTaskExecutor"); 
    this.engine = engine;
    this.delegatedTaskExecutor = delegatedTaskExecutor;
    this.startTls = startTls;
    this.maxPacketBufferSize = engine.getSession().getPacketBufferSize();
    this.wantsDirectBuffer = engine instanceof OpenSslEngine;
    this.wantsLargeOutboundNetworkBuffer = !(engine instanceof OpenSslEngine);
  }
  
  public long getHandshakeTimeoutMillis() {
    return this.handshakeTimeoutMillis;
  }
  
  public void setHandshakeTimeout(long handshakeTimeout, TimeUnit unit) {
    if (unit == null)
      throw new NullPointerException("unit"); 
    setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
  }
  
  public void setHandshakeTimeoutMillis(long handshakeTimeoutMillis) {
    if (handshakeTimeoutMillis < 0L)
      throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)"); 
    this.handshakeTimeoutMillis = handshakeTimeoutMillis;
  }
  
  public long getCloseNotifyTimeoutMillis() {
    return this.closeNotifyTimeoutMillis;
  }
  
  public void setCloseNotifyTimeout(long closeNotifyTimeout, TimeUnit unit) {
    if (unit == null)
      throw new NullPointerException("unit"); 
    setCloseNotifyTimeoutMillis(unit.toMillis(closeNotifyTimeout));
  }
  
  public void setCloseNotifyTimeoutMillis(long closeNotifyTimeoutMillis) {
    if (closeNotifyTimeoutMillis < 0L)
      throw new IllegalArgumentException("closeNotifyTimeoutMillis: " + closeNotifyTimeoutMillis + " (expected: >= 0)"); 
    this.closeNotifyTimeoutMillis = closeNotifyTimeoutMillis;
  }
  
  public SSLEngine engine() {
    return this.engine;
  }
  
  public Future<Channel> handshakeFuture() {
    return (Future<Channel>)this.handshakePromise;
  }
  
  public ChannelFuture close() {
    return close(this.ctx.newPromise());
  }
  
  public ChannelFuture close(final ChannelPromise future) {
    final ChannelHandlerContext ctx = this.ctx;
    ctx.executor().execute(new Runnable() {
          public void run() {
            SslHandler.this.engine.closeOutbound();
            try {
              SslHandler.this.write(ctx, Unpooled.EMPTY_BUFFER, future);
              SslHandler.this.flush(ctx);
            } catch (Exception e) {
              if (!future.tryFailure(e))
                SslHandler.logger.warn("flush() raised a masked exception.", e); 
            } 
          }
        });
    return (ChannelFuture)future;
  }
  
  public Future<Channel> sslCloseFuture() {
    return (Future<Channel>)this.sslCloseFuture;
  }
  
  public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
    if (!this.pendingUnencryptedWrites.isEmpty())
      this.pendingUnencryptedWrites.removeAndFailAll((Throwable)new ChannelException("Pending write on removal of SslHandler")); 
  }
  
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
    ctx.bind(localAddress, promise);
  }
  
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
    ctx.connect(remoteAddress, localAddress, promise);
  }
  
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    ctx.deregister(promise);
  }
  
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    closeOutboundAndChannel(ctx, promise, true);
  }
  
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    closeOutboundAndChannel(ctx, promise, false);
  }
  
  public void read(ChannelHandlerContext ctx) {
    ctx.read();
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    this.pendingUnencryptedWrites.add(msg, promise);
  }
  
  public void flush(ChannelHandlerContext ctx) throws Exception {
    if (this.startTls && !this.sentFirstMessage) {
      this.sentFirstMessage = true;
      this.pendingUnencryptedWrites.removeAndWriteAll();
      ctx.flush();
      return;
    } 
    if (this.pendingUnencryptedWrites.isEmpty())
      this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.voidPromise()); 
    if (!this.handshakePromise.isDone())
      this.flushedBeforeHandshakeDone = true; 
    wrap(ctx, false);
    ctx.flush();
  }
  
  private void wrap(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
    ByteBuf out = null;
    ChannelPromise promise = null;
    try {
      while (true) {
        Object msg = this.pendingUnencryptedWrites.current();
        if (msg == null)
          break; 
        if (!(msg instanceof ByteBuf)) {
          this.pendingUnencryptedWrites.removeAndWrite();
          continue;
        } 
        ByteBuf buf = (ByteBuf)msg;
        if (out == null)
          out = allocateOutNetBuf(ctx, buf.readableBytes()); 
        SSLEngineResult result = wrap(this.engine, buf, out);
        if (!buf.isReadable()) {
          promise = this.pendingUnencryptedWrites.remove();
        } else {
          promise = null;
        } 
        if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
          this.pendingUnencryptedWrites.removeAndFailAll(SSLENGINE_CLOSED);
          return;
        } 
        switch (result.getHandshakeStatus()) {
          case BUFFER_OVERFLOW:
            runDelegatedTasks();
            continue;
          case null:
            setHandshakeSuccess();
          case null:
            setHandshakeSuccessIfStillHandshaking();
          case null:
            finishWrap(ctx, out, promise, inUnwrap);
            promise = null;
            out = null;
            continue;
          case null:
            return;
        } 
        throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
      } 
    } catch (SSLException e) {
      setHandshakeFailure(e);
      throw e;
    } finally {
      finishWrap(ctx, out, promise, inUnwrap);
    } 
  }
  
  private void finishWrap(ChannelHandlerContext ctx, ByteBuf out, ChannelPromise promise, boolean inUnwrap) {
    if (out == null) {
      out = Unpooled.EMPTY_BUFFER;
    } else if (!out.isReadable()) {
      out.release();
      out = Unpooled.EMPTY_BUFFER;
    } 
    if (promise != null) {
      ctx.write(out, promise);
    } else {
      ctx.write(out);
    } 
    if (inUnwrap)
      this.needsFlush = true; 
  }
  
  private void wrapNonAppData(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
    ByteBuf out = null;
    try {
      SSLEngineResult result;
      do {
        if (out == null)
          out = allocateOutNetBuf(ctx, 0); 
        result = wrap(this.engine, Unpooled.EMPTY_BUFFER, out);
        if (result.bytesProduced() > 0) {
          ctx.write(out);
          if (inUnwrap)
            this.needsFlush = true; 
          out = null;
        } 
        switch (result.getHandshakeStatus()) {
          case null:
            setHandshakeSuccess();
            break;
          case BUFFER_OVERFLOW:
            runDelegatedTasks();
            break;
          case null:
            if (!inUnwrap)
              unwrapNonAppData(ctx); 
            break;
          case null:
            break;
          case null:
            setHandshakeSuccessIfStillHandshaking();
            if (!inUnwrap)
              unwrapNonAppData(ctx); 
            break;
          default:
            throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
        } 
      } while (result.bytesProduced() != 0);
    } catch (SSLException e) {
      setHandshakeFailure(e);
      throw e;
    } finally {
      if (out != null)
        out.release(); 
    } 
  }
  
  private SSLEngineResult wrap(SSLEngine engine, ByteBuf in, ByteBuf out) throws SSLException {
    SSLEngineResult result;
    ByteBuffer in0 = in.nioBuffer();
    if (!in0.isDirect()) {
      ByteBuffer newIn0 = ByteBuffer.allocateDirect(in0.remaining());
      newIn0.put(in0).flip();
      in0 = newIn0;
    } 
    while (true) {
      ByteBuffer out0 = out.nioBuffer(out.writerIndex(), out.writableBytes());
      result = engine.wrap(in0, out0);
      in.skipBytes(result.bytesConsumed());
      out.writerIndex(out.writerIndex() + result.bytesProduced());
      switch (result.getStatus()) {
        case BUFFER_OVERFLOW:
          out.ensureWritable(this.maxPacketBufferSize);
          continue;
      } 
      break;
    } 
    return result;
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    setHandshakeFailure(CHANNEL_CLOSED);
    super.channelInactive(ctx);
  }
  
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (ignoreException(cause)) {
      if (logger.isDebugEnabled())
        logger.debug("Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", cause); 
      if (ctx.channel().isActive())
        ctx.close(); 
    } else {
      ctx.fireExceptionCaught(cause);
    } 
  }
  
  private boolean ignoreException(Throwable t) {
    if (!(t instanceof SSLException) && t instanceof java.io.IOException && this.sslCloseFuture.isDone()) {
      String message = String.valueOf(t.getMessage()).toLowerCase();
      if (IGNORABLE_ERROR_MESSAGE.matcher(message).matches())
        return true; 
      StackTraceElement[] elements = t.getStackTrace();
      for (StackTraceElement element : elements) {
        String classname = element.getClassName();
        String methodname = element.getMethodName();
        if (!classname.startsWith("io.netty."))
          if ("read".equals(methodname)) {
            if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches())
              return true; 
            try {
              Class<?> clazz = PlatformDependent.getClassLoader(getClass()).loadClass(classname);
              if (SocketChannel.class.isAssignableFrom(clazz) || DatagramChannel.class.isAssignableFrom(clazz))
                return true; 
              if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName()))
                return true; 
            } catch (ClassNotFoundException e) {}
          }  
      } 
    } 
    return false;
  }
  
  public static boolean isEncrypted(ByteBuf buffer) {
    if (buffer.readableBytes() < 5)
      throw new IllegalArgumentException("buffer must have at least 5 readable bytes"); 
    return (getEncryptedPacketLength(buffer, buffer.readerIndex()) != -1);
  }
  
  private static int getEncryptedPacketLength(ByteBuf buffer, int offset) {
    boolean tls;
    int packetLength = 0;
    switch (buffer.getUnsignedByte(offset)) {
      case 20:
      case 21:
      case 22:
      case 23:
        tls = true;
        break;
      default:
        tls = false;
        break;
    } 
    if (tls) {
      int majorVersion = buffer.getUnsignedByte(offset + 1);
      if (majorVersion == 3) {
        packetLength = buffer.getUnsignedShort(offset + 3) + 5;
        if (packetLength <= 5)
          tls = false; 
      } else {
        tls = false;
      } 
    } 
    if (!tls) {
      boolean sslv2 = true;
      int headerLength = ((buffer.getUnsignedByte(offset) & 0x80) != 0) ? 2 : 3;
      int majorVersion = buffer.getUnsignedByte(offset + headerLength + 1);
      if (majorVersion == 2 || majorVersion == 3) {
        if (headerLength == 2) {
          packetLength = (buffer.getShort(offset) & Short.MAX_VALUE) + 2;
        } else {
          packetLength = (buffer.getShort(offset) & 0x3FFF) + 3;
        } 
        if (packetLength <= headerLength)
          sslv2 = false; 
      } else {
        sslv2 = false;
      } 
      if (!sslv2)
        return -1; 
    } 
    return packetLength;
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException {
    int startOffset = in.readerIndex();
    int endOffset = in.writerIndex();
    int offset = startOffset;
    int totalLength = 0;
    if (this.packetLength > 0) {
      if (endOffset - startOffset < this.packetLength)
        return; 
      offset += this.packetLength;
      totalLength = this.packetLength;
      this.packetLength = 0;
    } 
    boolean nonSslRecord = false;
    while (totalLength < 18713) {
      int readableBytes = endOffset - offset;
      if (readableBytes < 5)
        break; 
      int packetLength = getEncryptedPacketLength(in, offset);
      if (packetLength == -1) {
        nonSslRecord = true;
        break;
      } 
      assert packetLength > 0;
      if (packetLength > readableBytes) {
        this.packetLength = packetLength;
        break;
      } 
      int newTotalLength = totalLength + packetLength;
      if (newTotalLength > 18713)
        break; 
      offset += packetLength;
      totalLength = newTotalLength;
    } 
    if (totalLength > 0) {
      in.skipBytes(totalLength);
      ByteBuffer inNetBuf = in.nioBuffer(startOffset, totalLength);
      unwrap(ctx, inNetBuf, totalLength);
      assert !inNetBuf.hasRemaining() || this.engine.isInboundDone();
    } 
    if (nonSslRecord) {
      NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
      in.skipBytes(in.readableBytes());
      ctx.fireExceptionCaught(e);
      setHandshakeFailure(e);
    } 
  }
  
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    if (this.needsFlush) {
      this.needsFlush = false;
      ctx.flush();
    } 
    super.channelReadComplete(ctx);
  }
  
  private void unwrapNonAppData(ChannelHandlerContext ctx) throws SSLException {
    unwrap(ctx, Unpooled.EMPTY_BUFFER.nioBuffer(), 0);
  }
  
  private void unwrap(ChannelHandlerContext ctx, ByteBuffer packet, int initialOutAppBufCapacity) throws SSLException {
    ByteBuffer oldPacket;
    ByteBuf newPacket;
    int oldPos = packet.position();
    if (this.wantsInboundHeapBuffer && packet.isDirect()) {
      newPacket = ctx.alloc().heapBuffer(packet.limit() - oldPos);
      newPacket.writeBytes(packet);
      oldPacket = packet;
      packet = newPacket.nioBuffer();
    } else {
      oldPacket = null;
      newPacket = null;
    } 
    boolean wrapLater = false;
    ByteBuf decodeOut = allocate(ctx, initialOutAppBufCapacity);
    try {
      while (true) {
        SSLEngineResult result = unwrap(this.engine, packet, decodeOut);
        SSLEngineResult.Status status = result.getStatus();
        SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
        int produced = result.bytesProduced();
        int consumed = result.bytesConsumed();
        if (status == SSLEngineResult.Status.CLOSED) {
          this.sslCloseFuture.trySuccess(ctx.channel());
          break;
        } 
        switch (handshakeStatus) {
          case null:
            break;
          case null:
            wrapNonAppData(ctx, true);
            break;
          case BUFFER_OVERFLOW:
            runDelegatedTasks();
            break;
          case null:
            setHandshakeSuccess();
            wrapLater = true;
            continue;
          case null:
            if (setHandshakeSuccessIfStillHandshaking()) {
              wrapLater = true;
              continue;
            } 
            if (this.flushedBeforeHandshakeDone) {
              this.flushedBeforeHandshakeDone = false;
              wrapLater = true;
            } 
            break;
          default:
            throw new IllegalStateException("Unknown handshake status: " + handshakeStatus);
        } 
        if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW || (consumed == 0 && produced == 0))
          break; 
      } 
      if (wrapLater)
        wrap(ctx, true); 
    } catch (SSLException e) {
      setHandshakeFailure(e);
      throw e;
    } finally {
      if (newPacket != null) {
        oldPacket.position(oldPos + packet.position());
        newPacket.release();
      } 
      if (decodeOut.isReadable()) {
        ctx.fireChannelRead(decodeOut);
      } else {
        decodeOut.release();
      } 
    } 
  }
  
  private static SSLEngineResult unwrap(SSLEngine engine, ByteBuffer in, ByteBuf out) throws SSLException {
    SSLEngineResult result;
    int overflows = 0;
    while (true) {
      int max;
      ByteBuffer out0 = out.nioBuffer(out.writerIndex(), out.writableBytes());
      result = engine.unwrap(in, out0);
      out.writerIndex(out.writerIndex() + result.bytesProduced());
      switch (result.getStatus()) {
        case BUFFER_OVERFLOW:
          max = engine.getSession().getApplicationBufferSize();
          switch (overflows++) {
            case 0:
              out.ensureWritable(Math.min(max, in.remaining()));
              continue;
          } 
          out.ensureWritable(max);
          continue;
      } 
      break;
    } 
    return result;
  }
  
  private void runDelegatedTasks() {
    if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
      while (true) {
        Runnable task = this.engine.getDelegatedTask();
        if (task == null)
          break; 
        task.run();
      } 
    } else {
      final List<Runnable> tasks = new ArrayList<Runnable>(2);
      while (true) {
        Runnable task = this.engine.getDelegatedTask();
        if (task == null)
          break; 
        tasks.add(task);
      } 
      if (tasks.isEmpty())
        return; 
      final CountDownLatch latch = new CountDownLatch(1);
      this.delegatedTaskExecutor.execute(new Runnable() {
            public void run() {
              try {
                for (Runnable task : tasks)
                  task.run(); 
              } catch (Exception e) {
                SslHandler.this.ctx.fireExceptionCaught(e);
              } finally {
                latch.countDown();
              } 
            }
          });
      boolean interrupted = false;
      while (latch.getCount() != 0L) {
        try {
          latch.await();
        } catch (InterruptedException e) {
          interrupted = true;
        } 
      } 
      if (interrupted)
        Thread.currentThread().interrupt(); 
    } 
  }
  
  private boolean setHandshakeSuccessIfStillHandshaking() {
    if (!this.handshakePromise.isDone()) {
      setHandshakeSuccess();
      return true;
    } 
    return false;
  }
  
  private void setHandshakeSuccess() {
    String cipherSuite = String.valueOf(this.engine.getSession().getCipherSuite());
    if (!this.wantsDirectBuffer && (cipherSuite.contains("_GCM_") || cipherSuite.contains("-GCM-")))
      this.wantsInboundHeapBuffer = true; 
    if (this.handshakePromise.trySuccess(this.ctx.channel())) {
      if (logger.isDebugEnabled())
        logger.debug(this.ctx.channel() + " HANDSHAKEN: " + this.engine.getSession().getCipherSuite()); 
      this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
    } 
  }
  
  private void setHandshakeFailure(Throwable cause) {
    this.engine.closeOutbound();
    try {
      this.engine.closeInbound();
    } catch (SSLException e) {
      String msg = e.getMessage();
      if (msg == null || !msg.contains("possible truncation attack"))
        logger.debug("SSLEngine.closeInbound() raised an exception.", e); 
    } 
    notifyHandshakeFailure(cause);
    this.pendingUnencryptedWrites.removeAndFailAll(cause);
  }
  
  private void notifyHandshakeFailure(Throwable cause) {
    if (this.handshakePromise.tryFailure(cause)) {
      this.ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
      this.ctx.close();
    } 
  }
  
  private void closeOutboundAndChannel(ChannelHandlerContext ctx, ChannelPromise promise, boolean disconnect) throws Exception {
    if (!ctx.channel().isActive()) {
      if (disconnect) {
        ctx.disconnect(promise);
      } else {
        ctx.close(promise);
      } 
      return;
    } 
    this.engine.closeOutbound();
    ChannelPromise closeNotifyFuture = ctx.newPromise();
    write(ctx, Unpooled.EMPTY_BUFFER, closeNotifyFuture);
    flush(ctx);
    safeClose(ctx, (ChannelFuture)closeNotifyFuture, promise);
  }
  
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    this.ctx = ctx;
    this.pendingUnencryptedWrites = new PendingWriteQueue(ctx);
    if (ctx.channel().isActive() && this.engine.getUseClientMode())
      handshake(); 
  }
  
  private Future<Channel> handshake() {
    final ScheduledFuture<?> timeoutFuture;
    if (this.handshakeTimeoutMillis > 0L) {
      ScheduledFuture scheduledFuture = this.ctx.executor().schedule(new Runnable() {
            public void run() {
              if (SslHandler.this.handshakePromise.isDone())
                return; 
              SslHandler.this.notifyHandshakeFailure(SslHandler.HANDSHAKE_TIMED_OUT);
            }
          },  this.handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
    } else {
      timeoutFuture = null;
    } 
    this.handshakePromise.addListener(new GenericFutureListener<Future<Channel>>() {
          public void operationComplete(Future<Channel> f) throws Exception {
            if (timeoutFuture != null)
              timeoutFuture.cancel(false); 
          }
        });
    try {
      this.engine.beginHandshake();
      wrapNonAppData(this.ctx, false);
      this.ctx.flush();
    } catch (Exception e) {
      notifyHandshakeFailure(e);
    } 
    return (Future<Channel>)this.handshakePromise;
  }
  
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    if (!this.startTls && this.engine.getUseClientMode())
      handshake().addListener(new GenericFutureListener<Future<Channel>>() {
            public void operationComplete(Future<Channel> future) throws Exception {
              if (!future.isSuccess()) {
                SslHandler.logger.debug("Failed to complete handshake", future.cause());
                ctx.close();
              } 
            }
          }); 
    ctx.fireChannelActive();
  }
  
  private void safeClose(final ChannelHandlerContext ctx, ChannelFuture flushFuture, final ChannelPromise promise) {
    final ScheduledFuture<?> timeoutFuture;
    if (!ctx.channel().isActive()) {
      ctx.close(promise);
      return;
    } 
    if (this.closeNotifyTimeoutMillis > 0L) {
      ScheduledFuture scheduledFuture = ctx.executor().schedule(new Runnable() {
            public void run() {
              SslHandler.logger.warn(ctx.channel() + " last write attempt timed out." + " Force-closing the connection.");
              ctx.close(promise);
            }
          },  this.closeNotifyTimeoutMillis, TimeUnit.MILLISECONDS);
    } else {
      timeoutFuture = null;
    } 
    flushFuture.addListener((GenericFutureListener)new ChannelFutureListener() {
          public void operationComplete(ChannelFuture f) throws Exception {
            if (timeoutFuture != null)
              timeoutFuture.cancel(false); 
            ctx.close(promise);
          }
        });
  }
  
  private ByteBuf allocate(ChannelHandlerContext ctx, int capacity) {
    ByteBufAllocator alloc = ctx.alloc();
    if (this.wantsDirectBuffer)
      return alloc.directBuffer(capacity); 
    return alloc.buffer(capacity);
  }
  
  private ByteBuf allocateOutNetBuf(ChannelHandlerContext ctx, int pendingBytes) {
    if (this.wantsLargeOutboundNetworkBuffer)
      return allocate(ctx, this.maxPacketBufferSize); 
    return allocate(ctx, Math.min(pendingBytes + 2329, this.maxPacketBufferSize));
  }
  
  private final class LazyChannelPromise extends DefaultPromise<Channel> {
    private LazyChannelPromise() {}
    
    protected EventExecutor executor() {
      if (SslHandler.this.ctx == null)
        throw new IllegalStateException(); 
      return SslHandler.this.ctx.executor();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\SslHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */