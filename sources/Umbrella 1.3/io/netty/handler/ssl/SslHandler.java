/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.ssl.NotSslRecordException;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PendingWrite;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
    private final boolean startTls;
    private boolean sentFirstMessage;
    private final LazyChannelPromise handshakePromise = new LazyChannelPromise();
    private final LazyChannelPromise sslCloseFuture = new LazyChannelPromise();
    private final Deque<PendingWrite> pendingUnencryptedWrites = new ArrayDeque<PendingWrite>();
    private boolean needsFlush;
    private int packetLength;
    private ByteBuf decodeOut;
    private volatile long handshakeTimeoutMillis = 10000L;
    private volatile long closeNotifyTimeoutMillis = 3000L;

    public SslHandler(SSLEngine engine) {
        this(engine, false);
    }

    public SslHandler(SSLEngine engine, boolean startTls) {
        this(engine, startTls, ImmediateExecutor.INSTANCE);
    }

    @Deprecated
    public SslHandler(SSLEngine engine, Executor delegatedTaskExecutor) {
        this(engine, false, delegatedTaskExecutor);
    }

    @Deprecated
    public SslHandler(SSLEngine engine, boolean startTls, Executor delegatedTaskExecutor) {
        if (engine == null) {
            throw new NullPointerException("engine");
        }
        if (delegatedTaskExecutor == null) {
            throw new NullPointerException("delegatedTaskExecutor");
        }
        this.engine = engine;
        this.delegatedTaskExecutor = delegatedTaskExecutor;
        this.startTls = startTls;
        this.maxPacketBufferSize = engine.getSession().getPacketBufferSize();
    }

    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }

    public void setHandshakeTimeout(long handshakeTimeout, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
    }

    public void setHandshakeTimeoutMillis(long handshakeTimeoutMillis) {
        if (handshakeTimeoutMillis < 0L) {
            throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)");
        }
        this.handshakeTimeoutMillis = handshakeTimeoutMillis;
    }

    public long getCloseNotifyTimeoutMillis() {
        return this.closeNotifyTimeoutMillis;
    }

    public void setCloseNotifyTimeout(long closeNotifyTimeout, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.setCloseNotifyTimeoutMillis(unit.toMillis(closeNotifyTimeout));
    }

    public void setCloseNotifyTimeoutMillis(long closeNotifyTimeoutMillis) {
        if (closeNotifyTimeoutMillis < 0L) {
            throw new IllegalArgumentException("closeNotifyTimeoutMillis: " + closeNotifyTimeoutMillis + " (expected: >= 0)");
        }
        this.closeNotifyTimeoutMillis = closeNotifyTimeoutMillis;
    }

    public SSLEngine engine() {
        return this.engine;
    }

    public Future<Channel> handshakeFuture() {
        return this.handshakePromise;
    }

    public ChannelFuture close() {
        return this.close(this.ctx.newPromise());
    }

    public ChannelFuture close(final ChannelPromise future) {
        final ChannelHandlerContext ctx = this.ctx;
        ctx.executor().execute(new Runnable(){

            @Override
            public void run() {
                block2: {
                    SslHandler.this.engine.closeOutbound();
                    try {
                        SslHandler.this.write(ctx, Unpooled.EMPTY_BUFFER, future);
                        SslHandler.this.flush(ctx);
                    }
                    catch (Exception e) {
                        if (future.tryFailure(e)) break block2;
                        logger.warn("flush() raised a masked exception.", e);
                    }
                }
            }
        });
        return future;
    }

    public Future<Channel> sslCloseFuture() {
        return this.sslCloseFuture;
    }

    @Override
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        PendingWrite write;
        if (this.decodeOut != null) {
            this.decodeOut.release();
            this.decodeOut = null;
        }
        while ((write = this.pendingUnencryptedWrites.poll()) != null) {
            write.failAndRecycle(new ChannelException("Pending write on removal of SslHandler"));
        }
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    @Deprecated
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, true);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, false);
    }

    @Override
    public void read(ChannelHandlerContext ctx) {
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.pendingUnencryptedWrites.add(PendingWrite.newInstance(msg, promise));
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        if (this.startTls && !this.sentFirstMessage) {
            PendingWrite pendingWrite;
            this.sentFirstMessage = true;
            while ((pendingWrite = this.pendingUnencryptedWrites.poll()) != null) {
                ctx.write(pendingWrite.msg(), (ChannelPromise)pendingWrite.recycleAndGet());
            }
            ctx.flush();
            return;
        }
        if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(PendingWrite.newInstance(Unpooled.EMPTY_BUFFER, null));
        }
        this.wrap(ctx, false);
        ctx.flush();
    }

    /*
     * Exception decompiling
     */
    private void wrap(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid source, tried to remove [0] lbl56 : GotoStatement: goto lbl4;\u000a\u000afrom [] lbl3 : TryStatement: try { 0[TRYBLOCK]\u000a\u000abut was not a source.
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.removeSource(Op03SimpleStatement.java:351)
         * org.benf.cfr.reader.bytecode.analysis.parse.utils.finalhelp.FinalAnalyzer$2.call(FinalAnalyzer.java:259)
         * org.benf.cfr.reader.bytecode.analysis.parse.utils.finalhelp.FinalAnalyzer$2.call(FinalAnalyzer.java:247)
         * org.benf.cfr.reader.util.graph.GraphVisitorDFS.process(GraphVisitorDFS.java:68)
         * org.benf.cfr.reader.bytecode.analysis.parse.utils.finalhelp.FinalAnalyzer.identifyFinally(FinalAnalyzer.java:267)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.FinallyRewriter.identifyFinally(FinallyRewriter.java:40)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:513)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
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
        if (inUnwrap) {
            this.needsFlush = true;
        }
    }

    private void wrapNonAppData(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
        ReferenceCounted out = null;
        try {
            SSLEngineResult result;
            block12: do {
                if (out == null) {
                    out = ctx.alloc().buffer(this.maxPacketBufferSize);
                }
                if ((result = this.wrap(this.engine, Unpooled.EMPTY_BUFFER, (ByteBuf)out)).bytesProduced() > 0) {
                    ctx.write(out);
                    if (inUnwrap) {
                        this.needsFlush = true;
                    }
                    out = null;
                }
                switch (result.getHandshakeStatus()) {
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        break;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case NEED_UNWRAP: {
                        if (inUnwrap) continue block12;
                        this.unwrap(ctx);
                        break;
                    }
                    case NEED_WRAP: {
                        break;
                    }
                    case NOT_HANDSHAKING: {
                        if (inUnwrap) continue block12;
                        this.unwrap(ctx);
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown handshake status: " + (Object)((Object)result.getHandshakeStatus()));
                    }
                }
            } while (result.bytesProduced() != 0);
        }
        catch (SSLException e) {
            this.setHandshakeFailure(e);
            throw e;
        }
        finally {
            if (out != null) {
                out.release();
            }
        }
    }

    private SSLEngineResult wrap(SSLEngine engine, ByteBuf in, ByteBuf out) throws SSLException {
        SSLEngineResult result;
        ByteBuffer in0 = in.nioBuffer();
        block3: while (true) {
            ByteBuffer out0 = out.nioBuffer(out.writerIndex(), out.writableBytes());
            result = engine.wrap(in0, out0);
            in.skipBytes(result.bytesConsumed());
            out.writerIndex(out.writerIndex() + result.bytesProduced());
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW: {
                    out.ensureWritable(this.maxPacketBufferSize);
                    continue block3;
                }
            }
            break;
        }
        return result;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.setHandshakeFailure(CHANNEL_CLOSED);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (this.ignoreException(cause)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", cause);
            }
            if (ctx.channel().isActive()) {
                ctx.close();
            }
        } else {
            ctx.fireExceptionCaught(cause);
        }
    }

    private boolean ignoreException(Throwable t) {
        if (!(t instanceof SSLException) && t instanceof IOException && this.sslCloseFuture.isDone()) {
            StackTraceElement[] elements;
            String message = String.valueOf(t.getMessage()).toLowerCase();
            if (IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
                return true;
            }
            for (StackTraceElement element : elements = t.getStackTrace()) {
                String classname = element.getClassName();
                String methodname = element.getMethodName();
                if (classname.startsWith("io.netty.") || !"read".equals(methodname)) continue;
                if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
                    return true;
                }
                try {
                    Class<?> clazz = this.getClass().getClassLoader().loadClass(classname);
                    if (SocketChannel.class.isAssignableFrom(clazz) || DatagramChannel.class.isAssignableFrom(clazz)) {
                        return true;
                    }
                    if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                        return true;
                    }
                }
                catch (ClassNotFoundException e) {
                    // empty catch block
                }
            }
        }
        return false;
    }

    public static boolean isEncrypted(ByteBuf buffer) {
        if (buffer.readableBytes() < 5) {
            throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
        }
        return SslHandler.getEncryptedPacketLength(buffer, buffer.readerIndex()) != -1;
    }

    private static int getEncryptedPacketLength(ByteBuf buffer, int offset) {
        boolean tls;
        int packetLength = 0;
        switch (buffer.getUnsignedByte(offset)) {
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                tls = true;
                break;
            }
            default: {
                tls = false;
            }
        }
        if (tls) {
            short majorVersion = buffer.getUnsignedByte(offset + 1);
            if (majorVersion == 3) {
                packetLength = buffer.getUnsignedShort(offset + 3) + 5;
                if (packetLength <= 5) {
                    tls = false;
                }
            } else {
                tls = false;
            }
        }
        if (!tls) {
            boolean sslv2 = true;
            int headerLength = (buffer.getUnsignedByte(offset) & 0x80) != 0 ? 2 : 3;
            short majorVersion = buffer.getUnsignedByte(offset + headerLength + 1);
            if (majorVersion == 2 || majorVersion == 3) {
                packetLength = headerLength == 2 ? (buffer.getShort(offset) & 0x7FFF) + 2 : (buffer.getShort(offset) & 0x3FFF) + 3;
                if (packetLength <= headerLength) {
                    sslv2 = false;
                }
            } else {
                sslv2 = false;
            }
            if (!sslv2) {
                return -1;
            }
        }
        return packetLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException {
        int length;
        int readableBytes;
        int startOffset = in.readerIndex();
        int endOffset = in.writerIndex();
        int offset = startOffset;
        if (this.packetLength > 0) {
            if (endOffset - startOffset < this.packetLength) {
                return;
            }
            offset += this.packetLength;
            this.packetLength = 0;
        }
        boolean nonSslRecord = false;
        while ((readableBytes = endOffset - offset) >= 5) {
            int packetLength = SslHandler.getEncryptedPacketLength(in, offset);
            if (packetLength == -1) {
                nonSslRecord = true;
                break;
            }
            assert (packetLength > 0);
            if (packetLength > readableBytes) {
                this.packetLength = packetLength;
                break;
            }
            offset += packetLength;
        }
        if ((length = offset - startOffset) > 0) {
            in.skipBytes(length);
            ByteBuffer buffer = in.nioBuffer(startOffset, length);
            this.unwrap(ctx, buffer, out);
        }
        if (nonSslRecord) {
            NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
            in.skipBytes(in.readableBytes());
            ctx.fireExceptionCaught(e);
            this.setHandshakeFailure(e);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (this.needsFlush) {
            this.needsFlush = false;
            ctx.flush();
        }
        super.channelReadComplete(ctx);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void unwrap(ChannelHandlerContext ctx) throws SSLException {
        RecyclableArrayList out = RecyclableArrayList.newInstance();
        try {
            this.unwrap(ctx, Unpooled.EMPTY_BUFFER.nioBuffer(), out);
            int size = out.size();
            for (int i = 0; i < size; ++i) {
                ctx.fireChannelRead(out.get(i));
            }
        }
        finally {
            out.recycle();
        }
    }

    private void unwrap(ChannelHandlerContext ctx, ByteBuffer packet, List<Object> out) throws SSLException {
        boolean wrapLater = false;
        int totalProduced = 0;
        try {
            block12: while (true) {
                if (this.decodeOut == null) {
                    this.decodeOut = ctx.alloc().buffer(packet.remaining());
                }
                SSLEngineResult result = SslHandler.unwrap(this.engine, packet, this.decodeOut);
                SSLEngineResult.Status status = result.getStatus();
                SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
                int produced = result.bytesProduced();
                int consumed = result.bytesConsumed();
                totalProduced += produced;
                if (status == SSLEngineResult.Status.CLOSED) {
                    this.sslCloseFuture.trySuccess(ctx.channel());
                    break;
                }
                switch (handshakeStatus) {
                    case NEED_UNWRAP: {
                        break;
                    }
                    case NEED_WRAP: {
                        this.wrapNonAppData(ctx, true);
                        break;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        wrapLater = true;
                        continue block12;
                    }
                    case NOT_HANDSHAKING: {
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown handshake status: " + (Object)((Object)handshakeStatus));
                    }
                }
                if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW || consumed == 0 && produced == 0) break;
            }
            if (wrapLater) {
                this.wrap(ctx, true);
            }
        }
        catch (SSLException e) {
            this.setHandshakeFailure(e);
            throw e;
        }
        finally {
            if (totalProduced > 0) {
                ByteBuf decodeOut = this.decodeOut;
                this.decodeOut = null;
                out.add(decodeOut);
            }
        }
    }

    private static SSLEngineResult unwrap(SSLEngine engine, ByteBuffer in, ByteBuf out) throws SSLException {
        SSLEngineResult result;
        int overflows = 0;
        block6: while (true) {
            ByteBuffer out0 = out.nioBuffer(out.writerIndex(), out.writableBytes());
            result = engine.unwrap(in, out0);
            out.writerIndex(out.writerIndex() + result.bytesProduced());
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW: {
                    int max = engine.getSession().getApplicationBufferSize();
                    switch (overflows++) {
                        case 0: {
                            out.ensureWritable(Math.min(max, in.remaining()));
                            continue block6;
                        }
                    }
                    out.ensureWritable(max);
                    continue block6;
                }
            }
            break;
        }
        return result;
    }

    private void runDelegatedTasks() {
        if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
            Runnable task;
            while ((task = this.engine.getDelegatedTask()) != null) {
                task.run();
            }
        } else {
            Runnable task;
            final ArrayList<Runnable> tasks = new ArrayList<Runnable>(2);
            while ((task = this.engine.getDelegatedTask()) != null) {
                tasks.add(task);
            }
            if (tasks.isEmpty()) {
                return;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            this.delegatedTaskExecutor.execute(new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void run() {
                    try {
                        for (Runnable task : tasks) {
                            task.run();
                        }
                    }
                    catch (Exception e) {
                        SslHandler.this.ctx.fireExceptionCaught(e);
                    }
                    finally {
                        latch.countDown();
                    }
                }
            });
            boolean interrupted = false;
            while (latch.getCount() != 0L) {
                try {
                    latch.await();
                }
                catch (InterruptedException e) {
                    interrupted = true;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void setHandshakeSuccess() {
        if (this.handshakePromise.trySuccess(this.ctx.channel())) {
            this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
        }
    }

    private void setHandshakeFailure(Throwable cause) {
        PendingWrite write;
        block3: {
            this.engine.closeOutbound();
            try {
                this.engine.closeInbound();
            }
            catch (SSLException e) {
                String msg = e.getMessage();
                if (msg != null && msg.contains("possible truncation attack")) break block3;
                logger.debug("SSLEngine.closeInbound() raised an exception.", e);
            }
        }
        this.notifyHandshakeFailure(cause);
        while ((write = this.pendingUnencryptedWrites.poll()) != null) {
            write.failAndRecycle(cause);
        }
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
        this.write(ctx, Unpooled.EMPTY_BUFFER, closeNotifyFuture);
        this.flush(ctx);
        this.safeClose(ctx, closeNotifyFuture, promise);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        if (ctx.channel().isActive()) {
            this.handshake();
        }
    }

    private Future<Channel> handshake() {
        final ScheduledFuture<?> timeoutFuture = this.handshakeTimeoutMillis > 0L ? this.ctx.executor().schedule(new Runnable(){

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
            public void operationComplete(Future<Channel> f) throws Exception {
                if (timeoutFuture != null) {
                    timeoutFuture.cancel(false);
                }
            }
        });
        try {
            this.engine.beginHandshake();
            this.wrapNonAppData(this.ctx, false);
            this.ctx.flush();
        }
        catch (Exception e) {
            this.notifyHandshakeFailure(e);
        }
        return this.handshakePromise;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        if (!this.startTls && this.engine.getUseClientMode()) {
            this.handshake().addListener(new GenericFutureListener<Future<Channel>>(){

                @Override
                public void operationComplete(Future<Channel> future) throws Exception {
                    if (!future.isSuccess()) {
                        logger.debug("Failed to complete handshake", future.cause());
                        ctx.close();
                    }
                }
            });
        }
        ctx.fireChannelActive();
    }

    private void safeClose(final ChannelHandlerContext ctx, ChannelFuture flushFuture, final ChannelPromise promise) {
        if (!ctx.channel().isActive()) {
            ctx.close(promise);
            return;
        }
        final ScheduledFuture<?> timeoutFuture = this.closeNotifyTimeoutMillis > 0L ? ctx.executor().schedule(new Runnable(){

            @Override
            public void run() {
                logger.warn(ctx.channel() + " last write attempt timed out." + " Force-closing the connection.");
                ctx.close(promise);
            }
        }, this.closeNotifyTimeoutMillis, TimeUnit.MILLISECONDS) : null;
        flushFuture.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (timeoutFuture != null) {
                    timeoutFuture.cancel(false);
                }
                if (ctx.channel().isActive()) {
                    ctx.close(promise);
                }
            }
        });
    }

    static {
        SSLENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        HANDSHAKE_TIMED_OUT.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        CHANNEL_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
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

