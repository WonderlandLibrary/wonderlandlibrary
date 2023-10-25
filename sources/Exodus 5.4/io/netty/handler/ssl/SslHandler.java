/*
 * Decompiled with CFR 0.152.
 */
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
import io.netty.handler.ssl.NotSslRecordException;
import io.netty.handler.ssl.OpenSslEngine;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
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
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;

public class SslHandler
extends ByteToMessageDecoder
implements ChannelOutboundHandler {
    private boolean wantsInboundHeapBuffer;
    private static final Pattern IGNORABLE_CLASS_IN_STACK;
    private static final SSLException HANDSHAKE_TIMED_OUT;
    private static final Pattern IGNORABLE_ERROR_MESSAGE;
    private volatile long closeNotifyTimeoutMillis = 3000L;
    private final boolean wantsLargeOutboundNetworkBuffer;
    private volatile ChannelHandlerContext ctx;
    private boolean flushedBeforeHandshakeDone;
    private final Executor delegatedTaskExecutor;
    private PendingWriteQueue pendingUnencryptedWrites;
    private final LazyChannelPromise handshakePromise = new LazyChannelPromise();
    private volatile long handshakeTimeoutMillis = 10000L;
    private static final InternalLogger logger;
    private final int maxPacketBufferSize;
    private boolean needsFlush;
    private final SSLEngine engine;
    private final LazyChannelPromise sslCloseFuture = new LazyChannelPromise();
    private static final SSLException SSLENGINE_CLOSED;
    private final boolean startTls;
    private final boolean wantsDirectBuffer;
    private static final ClosedChannelException CHANNEL_CLOSED;
    private boolean sentFirstMessage;
    private int packetLength;

    public void setHandshakeTimeoutMillis(long l) {
        if (l < 0L) {
            throw new IllegalArgumentException("handshakeTimeoutMillis: " + l + " (expected: >= 0)");
        }
        this.handshakeTimeoutMillis = l;
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.setHandshakeFailure(CHANNEL_CLOSED);
        super.channelInactive(channelHandlerContext);
    }

    public ChannelFuture close(final ChannelPromise channelPromise) {
        final ChannelHandlerContext channelHandlerContext = this.ctx;
        channelHandlerContext.executor().execute(new Runnable(){

            @Override
            public void run() {
                block2: {
                    SslHandler.this.engine.closeOutbound();
                    try {
                        SslHandler.this.write(channelHandlerContext, Unpooled.EMPTY_BUFFER, channelPromise);
                        SslHandler.this.flush(channelHandlerContext);
                    }
                    catch (Exception exception) {
                        if (channelPromise.tryFailure(exception)) break block2;
                        logger.warn("flush() raised a masked exception.", exception);
                    }
                }
            }
        });
        return channelPromise;
    }

    public SslHandler(SSLEngine sSLEngine, boolean bl) {
        this(sSLEngine, bl, ImmediateExecutor.INSTANCE);
    }

    public Future<Channel> handshakeFuture() {
        return this.handshakePromise;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws SSLException {
        int n;
        int n2 = byteBuf.readerIndex();
        int n3 = byteBuf.writerIndex();
        int n4 = n2;
        int n5 = 0;
        if (this.packetLength > 0) {
            if (n3 - n2 < this.packetLength) {
                return;
            }
            n4 += this.packetLength;
            n5 = this.packetLength;
            this.packetLength = 0;
        }
        boolean bl = false;
        while (n5 < 18713 && (n = n3 - n4) >= 5) {
            int n6 = SslHandler.getEncryptedPacketLength(byteBuf, n4);
            if (n6 == -1) {
                bl = true;
                break;
            }
            assert (n6 > 0);
            if (n6 > n) {
                this.packetLength = n6;
                break;
            }
            int n7 = n5 + n6;
            if (n7 > 18713) break;
            n4 += n6;
            n5 = n7;
        }
        if (n5 > 0) {
            byteBuf.skipBytes(n5);
            ByteBuffer byteBuffer = byteBuf.nioBuffer(n2, n5);
            this.unwrap(channelHandlerContext, byteBuffer, n5);
            assert (!byteBuffer.hasRemaining() || this.engine.isInboundDone());
        }
        if (bl) {
            NotSslRecordException notSslRecordException = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(byteBuf));
            byteBuf.skipBytes(byteBuf.readableBytes());
            channelHandlerContext.fireExceptionCaught(notSslRecordException);
            this.setHandshakeFailure(notSslRecordException);
        }
    }

    private ByteBuf allocate(ChannelHandlerContext channelHandlerContext, int n) {
        ByteBufAllocator byteBufAllocator = channelHandlerContext.alloc();
        if (this.wantsDirectBuffer) {
            return byteBufAllocator.directBuffer(n);
        }
        return byteBufAllocator.buffer(n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void wrapNonAppData(ChannelHandlerContext channelHandlerContext, boolean bl) throws SSLException {
        ByteBuf byteBuf = null;
        try {
            SSLEngineResult sSLEngineResult;
            block9: do {
                if (byteBuf == null) {
                    byteBuf = this.allocateOutNetBuf(channelHandlerContext, 0);
                }
                if ((sSLEngineResult = this.wrap(this.engine, Unpooled.EMPTY_BUFFER, byteBuf)).bytesProduced() > 0) {
                    channelHandlerContext.write(byteBuf);
                    if (bl) {
                        this.needsFlush = true;
                    }
                    byteBuf = null;
                }
                switch (sSLEngineResult.getHandshakeStatus()) {
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        break;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case NEED_UNWRAP: {
                        if (bl) continue block9;
                        this.unwrapNonAppData(channelHandlerContext);
                        break;
                    }
                    case NEED_WRAP: {
                        break;
                    }
                    case NOT_HANDSHAKING: {
                        this.setHandshakeSuccessIfStillHandshaking();
                        if (bl) continue block9;
                        this.unwrapNonAppData(channelHandlerContext);
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown handshake status: " + (Object)((Object)sSLEngineResult.getHandshakeStatus()));
                    }
                }
            } while (sSLEngineResult.bytesProduced() != 0);
            if (byteBuf == null) return;
        }
        catch (SSLException sSLException) {
            this.setHandshakeFailure(sSLException);
            throw sSLException;
        }
        byteBuf.release();
    }

    private void unwrap(ChannelHandlerContext channelHandlerContext, ByteBuffer byteBuffer, int n) throws SSLException {
        ByteBuf byteBuf;
        block17: {
            ByteBuffer byteBuffer2;
            ByteBuf byteBuf2;
            int n2 = byteBuffer.position();
            if (this.wantsInboundHeapBuffer && byteBuffer.isDirect()) {
                byteBuf2 = channelHandlerContext.alloc().heapBuffer(byteBuffer.limit() - n2);
                byteBuf2.writeBytes(byteBuffer);
                byteBuffer2 = byteBuffer;
                byteBuffer = byteBuf2.nioBuffer();
            } else {
                byteBuffer2 = null;
                byteBuf2 = null;
            }
            boolean bl = false;
            byteBuf = this.allocate(channelHandlerContext, n);
            try {
                block9: while (true) {
                    SSLEngineResult sSLEngineResult = SslHandler.unwrap(this.engine, byteBuffer, byteBuf);
                    SSLEngineResult.Status status = sSLEngineResult.getStatus();
                    SSLEngineResult.HandshakeStatus handshakeStatus = sSLEngineResult.getHandshakeStatus();
                    int n3 = sSLEngineResult.bytesProduced();
                    int n4 = sSLEngineResult.bytesConsumed();
                    if (status == SSLEngineResult.Status.CLOSED) {
                        this.sslCloseFuture.trySuccess(channelHandlerContext.channel());
                        break;
                    }
                    switch (handshakeStatus) {
                        case NEED_UNWRAP: {
                            break;
                        }
                        case NEED_WRAP: {
                            this.wrapNonAppData(channelHandlerContext, true);
                            break;
                        }
                        case NEED_TASK: {
                            this.runDelegatedTasks();
                            break;
                        }
                        case FINISHED: {
                            this.setHandshakeSuccess();
                            bl = true;
                            continue block9;
                        }
                        case NOT_HANDSHAKING: {
                            if (this.setHandshakeSuccessIfStillHandshaking()) {
                                bl = true;
                                continue block9;
                            }
                            if (!this.flushedBeforeHandshakeDone) break;
                            this.flushedBeforeHandshakeDone = false;
                            bl = true;
                            break;
                        }
                        default: {
                            throw new IllegalStateException("Unknown handshake status: " + (Object)((Object)handshakeStatus));
                        }
                    }
                    if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW || n4 == 0 && n3 == 0) break;
                }
                if (bl) {
                    this.wrap(channelHandlerContext, true);
                }
                if (byteBuf2 == null) break block17;
            }
            catch (SSLException sSLException) {
                this.setHandshakeFailure(sSLException);
                throw sSLException;
            }
            byteBuffer2.position(n2 + byteBuffer.position());
            byteBuf2.release();
        }
        if (byteBuf.isReadable()) {
            channelHandlerContext.fireChannelRead(byteBuf);
        } else {
            byteBuf.release();
        }
    }

    public static boolean isEncrypted(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() < 5) {
            throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
        }
        return SslHandler.getEncryptedPacketLength(byteBuf, byteBuf.readerIndex()) != -1;
    }

    @Override
    public void handlerRemoved0(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (!this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.removeAndFailAll(new ChannelException("Pending write on removal of SslHandler"));
        }
    }

    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }

    private static int getEncryptedPacketLength(ByteBuf byteBuf, int n) {
        short s;
        boolean bl;
        int n2 = 0;
        switch (byteBuf.getUnsignedByte(n)) {
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                bl = true;
                break;
            }
            default: {
                bl = false;
            }
        }
        if (bl) {
            s = byteBuf.getUnsignedByte(n + 1);
            if (s == 3) {
                n2 = byteBuf.getUnsignedShort(n + 3) + 5;
                if (n2 <= 5) {
                    bl = false;
                }
            } else {
                bl = false;
            }
        }
        if (!bl) {
            s = 1;
            int n3 = (byteBuf.getUnsignedByte(n) & 0x80) != 0 ? 2 : 3;
            short s2 = byteBuf.getUnsignedByte(n + n3 + 1);
            if (s2 == 2 || s2 == 3) {
                n2 = n3 == 2 ? (byteBuf.getShort(n) & Short.MAX_VALUE) + 2 : (byteBuf.getShort(n) & 0x3FFF) + 3;
                if (n2 <= n3) {
                    s = 0;
                }
            } else {
                s = 0;
            }
            if (s == 0) {
                return -1;
            }
        }
        return n2;
    }

    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.bind(socketAddress, channelPromise);
    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.connect(socketAddress, socketAddress2, channelPromise);
    }

    private void setHandshakeFailure(Throwable throwable) {
        block2: {
            this.engine.closeOutbound();
            try {
                this.engine.closeInbound();
            }
            catch (SSLException sSLException) {
                String string = sSLException.getMessage();
                if (string != null && string.contains("possible truncation attack")) break block2;
                logger.debug("SSLEngine.closeInbound() raised an exception.", sSLException);
            }
        }
        this.notifyHandshakeFailure(throwable);
        this.pendingUnencryptedWrites.removeAndFailAll(throwable);
    }

    public void setHandshakeTimeout(long l, TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        this.setHandshakeTimeoutMillis(timeUnit.toMillis(l));
    }

    @Override
    public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.deregister(channelPromise);
    }

    private void setHandshakeSuccess() {
        String string = String.valueOf(this.engine.getSession().getCipherSuite());
        if (!this.wantsDirectBuffer && (string.contains("_GCM_") || string.contains("-GCM-"))) {
            this.wantsInboundHeapBuffer = true;
        }
        if (this.handshakePromise.trySuccess(this.ctx.channel())) {
            if (logger.isDebugEnabled()) {
                logger.debug(this.ctx.channel() + " HANDSHAKEN: " + this.engine.getSession().getCipherSuite());
            }
            this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
        }
    }

    private void unwrapNonAppData(ChannelHandlerContext channelHandlerContext) throws SSLException {
        this.unwrap(channelHandlerContext, Unpooled.EMPTY_BUFFER.nioBuffer(), 0);
    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.read();
    }

    public SSLEngine engine() {
        return this.engine;
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        this.pendingUnencryptedWrites.add(object, channelPromise);
    }

    private void finishWrap(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ChannelPromise channelPromise, boolean bl) {
        if (byteBuf == null) {
            byteBuf = Unpooled.EMPTY_BUFFER;
        } else if (!byteBuf.isReadable()) {
            byteBuf.release();
            byteBuf = Unpooled.EMPTY_BUFFER;
        }
        if (channelPromise != null) {
            channelHandlerContext.write(byteBuf, channelPromise);
        } else {
            channelHandlerContext.write(byteBuf);
        }
        if (bl) {
            this.needsFlush = true;
        }
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        this.closeOutboundAndChannel(channelHandlerContext, channelPromise, false);
    }

    private boolean ignoreException(Throwable throwable) {
        if (!(throwable instanceof SSLException) && throwable instanceof IOException && this.sslCloseFuture.isDone()) {
            StackTraceElement[] stackTraceElementArray;
            String string = String.valueOf(throwable.getMessage()).toLowerCase();
            if (IGNORABLE_ERROR_MESSAGE.matcher(string).matches()) {
                return true;
            }
            for (StackTraceElement stackTraceElement : stackTraceElementArray = throwable.getStackTrace()) {
                String string2 = stackTraceElement.getClassName();
                String string3 = stackTraceElement.getMethodName();
                if (string2.startsWith("io.netty.") || !"read".equals(string3)) continue;
                if (IGNORABLE_CLASS_IN_STACK.matcher(string2).matches()) {
                    return true;
                }
                try {
                    Class<?> clazz = PlatformDependent.getClassLoader(this.getClass()).loadClass(string2);
                    if (SocketChannel.class.isAssignableFrom(clazz) || DatagramChannel.class.isAssignableFrom(clazz)) {
                        return true;
                    }
                    if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                        return true;
                    }
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
        }
        return false;
    }

    @Override
    public void channelActive(final ChannelHandlerContext channelHandlerContext) throws Exception {
        if (!this.startTls && this.engine.getUseClientMode()) {
            this.handshake().addListener(new GenericFutureListener<Future<Channel>>(){

                @Override
                public void operationComplete(Future<Channel> future) throws Exception {
                    if (!future.isSuccess()) {
                        logger.debug("Failed to complete handshake", future.cause());
                        channelHandlerContext.close();
                    }
                }
            });
        }
        channelHandlerContext.fireChannelActive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        if (this.ignoreException(throwable)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", throwable);
            }
            if (channelHandlerContext.channel().isActive()) {
                channelHandlerContext.close();
            }
        } else {
            channelHandlerContext.fireExceptionCaught(throwable);
        }
    }

    public SslHandler(SSLEngine sSLEngine) {
        this(sSLEngine, false);
    }

    private Future<Channel> handshake() {
        final ScheduledFuture<?> scheduledFuture = this.handshakeTimeoutMillis > 0L ? this.ctx.executor().schedule(new Runnable(){

            @Override
            public void run() {
                if (SslHandler.this.handshakePromise.isDone()) {
                    return;
                }
                SslHandler.this.notifyHandshakeFailure(HANDSHAKE_TIMED_OUT);
            }
        }, this.handshakeTimeoutMillis, TimeUnit.MILLISECONDS) : null;
        this.handshakePromise.addListener(new GenericFutureListener<Future<Channel>>(){

            @Override
            public void operationComplete(Future<Channel> future) throws Exception {
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
            }
        });
        try {
            this.engine.beginHandshake();
            this.wrapNonAppData(this.ctx, false);
            this.ctx.flush();
        }
        catch (Exception exception) {
            this.notifyHandshakeFailure(exception);
        }
        return this.handshakePromise;
    }

    public ChannelFuture close() {
        return this.close(this.ctx.newPromise());
    }

    @Deprecated
    public SslHandler(SSLEngine sSLEngine, Executor executor) {
        this(sSLEngine, false, executor);
    }

    private void closeOutboundAndChannel(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise, boolean bl) throws Exception {
        if (!channelHandlerContext.channel().isActive()) {
            if (bl) {
                channelHandlerContext.disconnect(channelPromise);
            } else {
                channelHandlerContext.close(channelPromise);
            }
            return;
        }
        this.engine.closeOutbound();
        ChannelPromise channelPromise2 = channelHandlerContext.newPromise();
        this.write(channelHandlerContext, Unpooled.EMPTY_BUFFER, channelPromise2);
        this.flush(channelHandlerContext);
        this.safeClose(channelHandlerContext, channelPromise2, channelPromise);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void wrap(ChannelHandlerContext channelHandlerContext, boolean bl) throws SSLException {
        ByteBuf byteBuf = null;
        ChannelPromise channelPromise = null;
        try {
            Object object;
            block11: while ((object = this.pendingUnencryptedWrites.current()) != null) {
                if (!(object instanceof ByteBuf)) {
                    this.pendingUnencryptedWrites.removeAndWrite();
                    continue;
                }
                ByteBuf byteBuf2 = (ByteBuf)object;
                if (byteBuf == null) {
                    byteBuf = this.allocateOutNetBuf(channelHandlerContext, byteBuf2.readableBytes());
                }
                SSLEngineResult sSLEngineResult = this.wrap(this.engine, byteBuf2, byteBuf);
                channelPromise = !byteBuf2.isReadable() ? this.pendingUnencryptedWrites.remove() : null;
                if (sSLEngineResult.getStatus() == SSLEngineResult.Status.CLOSED) {
                    this.pendingUnencryptedWrites.removeAndFailAll(SSLENGINE_CLOSED);
                    this.finishWrap(channelHandlerContext, byteBuf, channelPromise, bl);
                    return;
                }
                switch (sSLEngineResult.getHandshakeStatus()) {
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        continue block11;
                    }
                    case FINISHED: {
                        this.setHandshakeSuccess();
                    }
                    case NOT_HANDSHAKING: {
                        this.setHandshakeSuccessIfStillHandshaking();
                    }
                    case NEED_WRAP: {
                        this.finishWrap(channelHandlerContext, byteBuf, channelPromise, bl);
                        channelPromise = null;
                        byteBuf = null;
                        continue block11;
                    }
                    case NEED_UNWRAP: {
                        this.finishWrap(channelHandlerContext, byteBuf, channelPromise, bl);
                        return;
                    }
                }
                throw new IllegalStateException("Unknown handshake status: " + (Object)((Object)sSLEngineResult.getHandshakeStatus()));
            }
        }
        catch (SSLException sSLException) {
            this.setHandshakeFailure(sSLException);
            throw sSLException;
        }
        this.finishWrap(channelHandlerContext, byteBuf, channelPromise, bl);
    }

    static {
        logger = InternalLoggerFactory.getInstance(SslHandler.class);
        IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
        IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
        SSLENGINE_CLOSED = new SSLException("SSLEngine closed already");
        HANDSHAKE_TIMED_OUT = new SSLException("handshake timed out");
        CHANNEL_CLOSED = new ClosedChannelException();
        SSLENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        HANDSHAKE_TIMED_OUT.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        CHANNEL_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }

    private void safeClose(final ChannelHandlerContext channelHandlerContext, ChannelFuture channelFuture, final ChannelPromise channelPromise) {
        if (!channelHandlerContext.channel().isActive()) {
            channelHandlerContext.close(channelPromise);
            return;
        }
        final ScheduledFuture<?> scheduledFuture = this.closeNotifyTimeoutMillis > 0L ? channelHandlerContext.executor().schedule(new Runnable(){

            @Override
            public void run() {
                logger.warn(channelHandlerContext.channel() + " last write attempt timed out." + " Force-closing the connection.");
                channelHandlerContext.close(channelPromise);
            }
        }, this.closeNotifyTimeoutMillis, TimeUnit.MILLISECONDS) : null;
        channelFuture.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
                channelHandlerContext.close(channelPromise);
            }
        });
    }

    @Override
    public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        this.closeOutboundAndChannel(channelHandlerContext, channelPromise, true);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.ctx = channelHandlerContext;
        this.pendingUnencryptedWrites = new PendingWriteQueue(channelHandlerContext);
        if (channelHandlerContext.channel().isActive() && this.engine.getUseClientMode()) {
            this.handshake();
        }
    }

    public long getCloseNotifyTimeoutMillis() {
        return this.closeNotifyTimeoutMillis;
    }

    private boolean setHandshakeSuccessIfStillHandshaking() {
        if (!this.handshakePromise.isDone()) {
            this.setHandshakeSuccess();
            return true;
        }
        return false;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.needsFlush) {
            this.needsFlush = false;
            channelHandlerContext.flush();
        }
        super.channelReadComplete(channelHandlerContext);
    }

    private void notifyHandshakeFailure(Throwable throwable) {
        if (this.handshakePromise.tryFailure(throwable)) {
            this.ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(throwable));
            this.ctx.close();
        }
    }

    private void runDelegatedTasks() {
        if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
            Runnable runnable;
            while ((runnable = this.engine.getDelegatedTask()) != null) {
                runnable.run();
            }
        } else {
            Object object;
            final ArrayList<Runnable> arrayList = new ArrayList<Runnable>(2);
            while ((object = this.engine.getDelegatedTask()) != null) {
                arrayList.add((Runnable)object);
            }
            if (arrayList.isEmpty()) {
                return;
            }
            object = new CountDownLatch(1);
            this.delegatedTaskExecutor.execute(new Runnable((CountDownLatch)object){
                final /* synthetic */ CountDownLatch val$latch;
                {
                    this.val$latch = countDownLatch;
                }

                @Override
                public void run() {
                    try {
                        for (Runnable runnable : arrayList) {
                            runnable.run();
                        }
                        this.val$latch.countDown();
                    }
                    catch (Exception exception) {
                        SslHandler.this.ctx.fireExceptionCaught(exception);
                        this.val$latch.countDown();
                    }
                }
            });
            boolean bl = false;
            while (((CountDownLatch)object).getCount() != 0L) {
                try {
                    ((CountDownLatch)object).await();
                }
                catch (InterruptedException interruptedException) {
                    bl = true;
                }
            }
            if (bl) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Future<Channel> sslCloseFuture() {
        return this.sslCloseFuture;
    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.startTls && !this.sentFirstMessage) {
            this.sentFirstMessage = true;
            this.pendingUnencryptedWrites.removeAndWriteAll();
            channelHandlerContext.flush();
            return;
        }
        if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, channelHandlerContext.voidPromise());
        }
        if (!this.handshakePromise.isDone()) {
            this.flushedBeforeHandshakeDone = true;
        }
        this.wrap(channelHandlerContext, false);
        channelHandlerContext.flush();
    }

    public void setCloseNotifyTimeout(long l, TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        this.setCloseNotifyTimeoutMillis(timeUnit.toMillis(l));
    }

    private static SSLEngineResult unwrap(SSLEngine sSLEngine, ByteBuffer byteBuffer, ByteBuf byteBuf) throws SSLException {
        SSLEngineResult sSLEngineResult;
        int n = 0;
        block6: while (true) {
            ByteBuffer byteBuffer2 = byteBuf.nioBuffer(byteBuf.writerIndex(), byteBuf.writableBytes());
            sSLEngineResult = sSLEngine.unwrap(byteBuffer, byteBuffer2);
            byteBuf.writerIndex(byteBuf.writerIndex() + sSLEngineResult.bytesProduced());
            switch (sSLEngineResult.getStatus()) {
                case BUFFER_OVERFLOW: {
                    int n2 = sSLEngine.getSession().getApplicationBufferSize();
                    switch (n++) {
                        case 0: {
                            byteBuf.ensureWritable(Math.min(n2, byteBuffer.remaining()));
                            continue block6;
                        }
                    }
                    byteBuf.ensureWritable(n2);
                    continue block6;
                }
            }
            break;
        }
        return sSLEngineResult;
    }

    @Deprecated
    public SslHandler(SSLEngine sSLEngine, boolean bl, Executor executor) {
        if (sSLEngine == null) {
            throw new NullPointerException("engine");
        }
        if (executor == null) {
            throw new NullPointerException("delegatedTaskExecutor");
        }
        this.engine = sSLEngine;
        this.delegatedTaskExecutor = executor;
        this.startTls = bl;
        this.maxPacketBufferSize = sSLEngine.getSession().getPacketBufferSize();
        this.wantsDirectBuffer = sSLEngine instanceof OpenSslEngine;
        this.wantsLargeOutboundNetworkBuffer = !(sSLEngine instanceof OpenSslEngine);
    }

    private SSLEngineResult wrap(SSLEngine sSLEngine, ByteBuf byteBuf, ByteBuf byteBuf2) throws SSLException {
        SSLEngineResult sSLEngineResult;
        ByteBuffer byteBuffer;
        ByteBuffer byteBuffer2 = byteBuf.nioBuffer();
        if (!byteBuffer2.isDirect()) {
            byteBuffer = ByteBuffer.allocateDirect(byteBuffer2.remaining());
            byteBuffer.put(byteBuffer2).flip();
            byteBuffer2 = byteBuffer;
        }
        block3: while (true) {
            byteBuffer = byteBuf2.nioBuffer(byteBuf2.writerIndex(), byteBuf2.writableBytes());
            sSLEngineResult = sSLEngine.wrap(byteBuffer2, byteBuffer);
            byteBuf.skipBytes(sSLEngineResult.bytesConsumed());
            byteBuf2.writerIndex(byteBuf2.writerIndex() + sSLEngineResult.bytesProduced());
            switch (sSLEngineResult.getStatus()) {
                case BUFFER_OVERFLOW: {
                    byteBuf2.ensureWritable(this.maxPacketBufferSize);
                    continue block3;
                }
            }
            break;
        }
        return sSLEngineResult;
    }

    public void setCloseNotifyTimeoutMillis(long l) {
        if (l < 0L) {
            throw new IllegalArgumentException("closeNotifyTimeoutMillis: " + l + " (expected: >= 0)");
        }
        this.closeNotifyTimeoutMillis = l;
    }

    private ByteBuf allocateOutNetBuf(ChannelHandlerContext channelHandlerContext, int n) {
        if (this.wantsLargeOutboundNetworkBuffer) {
            return this.allocate(channelHandlerContext, this.maxPacketBufferSize);
        }
        return this.allocate(channelHandlerContext, Math.min(n + 2329, this.maxPacketBufferSize));
    }

    private final class LazyChannelPromise
    extends DefaultPromise<Channel> {
        private LazyChannelPromise() {
        }

        @Override
        protected EventExecutor executor() {
            if (SslHandler.this.ctx == null) {
                throw new IllegalStateException();
            }
            return SslHandler.this.ctx.executor();
        }
    }
}

