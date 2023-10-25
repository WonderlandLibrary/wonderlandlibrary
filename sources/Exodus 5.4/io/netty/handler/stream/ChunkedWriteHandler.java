/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ChunkedWriteHandler
extends ChannelDuplexHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChunkedWriteHandler.class);
    private PendingWrite currentWrite;
    private final Queue<PendingWrite> queue = new ArrayDeque<PendingWrite>();
    private volatile ChannelHandlerContext ctx;

    private static int amount(Object object) {
        if (object instanceof ByteBuf) {
            return ((ByteBuf)object).readableBytes();
        }
        if (object instanceof ByteBufHolder) {
            return ((ByteBufHolder)object).content().readableBytes();
        }
        return 1;
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        this.queue.add(new PendingWrite(object, channelPromise));
    }

    public ChunkedWriteHandler() {
    }

    private void discard(Throwable throwable) {
        while (true) {
            PendingWrite pendingWrite = this.currentWrite;
            if (this.currentWrite == null) {
                pendingWrite = this.queue.poll();
            } else {
                this.currentWrite = null;
            }
            if (pendingWrite == null) break;
            Object object = pendingWrite.msg;
            if (object instanceof ChunkedInput) {
                ChunkedInput chunkedInput = (ChunkedInput)object;
                try {
                    if (!chunkedInput.isEndOfInput()) {
                        if (throwable == null) {
                            throwable = new ClosedChannelException();
                        }
                        pendingWrite.fail(throwable);
                    } else {
                        pendingWrite.success();
                    }
                    ChunkedWriteHandler.closeInput(chunkedInput);
                }
                catch (Exception exception) {
                    pendingWrite.fail(exception);
                    logger.warn(ChunkedInput.class.getSimpleName() + ".isEndOfInput() failed", exception);
                    ChunkedWriteHandler.closeInput(chunkedInput);
                }
                continue;
            }
            if (throwable == null) {
                throwable = new ClosedChannelException();
            }
            pendingWrite.fail(throwable);
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (channelHandlerContext.channel().isWritable()) {
            this.doFlush(channelHandlerContext);
        }
        channelHandlerContext.fireChannelWritabilityChanged();
    }

    public void resumeTransfer() {
        final ChannelHandlerContext channelHandlerContext = this.ctx;
        if (channelHandlerContext == null) {
            return;
        }
        if (channelHandlerContext.executor().inEventLoop()) {
            try {
                this.doFlush(channelHandlerContext);
            }
            catch (Exception exception) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Unexpected exception while sending chunks.", exception);
                }
            }
        } else {
            channelHandlerContext.executor().execute(new Runnable(){

                @Override
                public void run() {
                    block2: {
                        try {
                            ChunkedWriteHandler.this.doFlush(channelHandlerContext);
                        }
                        catch (Exception exception) {
                            if (!logger.isWarnEnabled()) break block2;
                            logger.warn("Unexpected exception while sending chunks.", exception);
                        }
                    }
                }
            });
        }
    }

    static void closeInput(ChunkedInput<?> chunkedInput) {
        block2: {
            try {
                chunkedInput.close();
            }
            catch (Throwable throwable) {
                if (!logger.isWarnEnabled()) break block2;
                logger.warn("Failed to close a chunked input.", throwable);
            }
        }
    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        Channel channel = channelHandlerContext.channel();
        if (channel.isWritable() || !channel.isActive()) {
            this.doFlush(channelHandlerContext);
        }
    }

    @Deprecated
    public ChunkedWriteHandler(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("maxPendingWrites: " + n + " (expected: > 0)");
        }
    }

    private void doFlush(ChannelHandlerContext channelHandlerContext) throws Exception {
        final Channel channel = channelHandlerContext.channel();
        if (!channel.isActive()) {
            this.discard(null);
            return;
        }
        while (channel.isWritable()) {
            if (this.currentWrite == null) {
                this.currentWrite = this.queue.poll();
            }
            if (this.currentWrite == null) break;
            final PendingWrite pendingWrite = this.currentWrite;
            final Object object = pendingWrite.msg;
            if (object instanceof ChunkedInput) {
                boolean bl;
                boolean bl2;
                final ChunkedInput chunkedInput = (ChunkedInput)object;
                ByteBuf byteBuf = null;
                try {
                    byteBuf = (ByteBuf)chunkedInput.readChunk(channelHandlerContext);
                    bl2 = chunkedInput.isEndOfInput();
                    bl = byteBuf == null ? !bl2 : false;
                }
                catch (Throwable throwable) {
                    this.currentWrite = null;
                    if (byteBuf != null) {
                        ReferenceCountUtil.release(byteBuf);
                    }
                    pendingWrite.fail(throwable);
                    ChunkedWriteHandler.closeInput(chunkedInput);
                    break;
                }
                if (bl) break;
                if (byteBuf == null) {
                    byteBuf = Unpooled.EMPTY_BUFFER;
                }
                final int n = ChunkedWriteHandler.amount(byteBuf);
                ChannelFuture channelFuture = channelHandlerContext.write(byteBuf);
                if (bl2) {
                    this.currentWrite = null;
                    channelFuture.addListener(new ChannelFutureListener(){

                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            pendingWrite.progress(n);
                            pendingWrite.success();
                            ChunkedWriteHandler.closeInput(chunkedInput);
                        }
                    });
                } else if (channel.isWritable()) {
                    channelFuture.addListener(new ChannelFutureListener(){

                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (!channelFuture.isSuccess()) {
                                ChunkedWriteHandler.closeInput((ChunkedInput)object);
                                pendingWrite.fail(channelFuture.cause());
                            } else {
                                pendingWrite.progress(n);
                            }
                        }
                    });
                } else {
                    channelFuture.addListener(new ChannelFutureListener(){

                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (!channelFuture.isSuccess()) {
                                ChunkedWriteHandler.closeInput((ChunkedInput)object);
                                pendingWrite.fail(channelFuture.cause());
                            } else {
                                pendingWrite.progress(n);
                                if (channel.isWritable()) {
                                    ChunkedWriteHandler.this.resumeTransfer();
                                }
                            }
                        }
                    });
                }
            } else {
                channelHandlerContext.write(object, pendingWrite.promise);
                this.currentWrite = null;
            }
            channelHandlerContext.flush();
            if (channel.isActive()) continue;
            this.discard(new ClosedChannelException());
            return;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.doFlush(channelHandlerContext);
        super.channelInactive(channelHandlerContext);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.ctx = channelHandlerContext;
    }

    private static final class PendingWrite {
        final Object msg;
        private long progress;
        final ChannelPromise promise;

        void fail(Throwable throwable) {
            ReferenceCountUtil.release(this.msg);
            this.promise.tryFailure(throwable);
        }

        void success() {
            if (this.promise.isDone()) {
                return;
            }
            if (this.promise instanceof ChannelProgressivePromise) {
                ((ChannelProgressivePromise)this.promise).tryProgress(this.progress, this.progress);
            }
            this.promise.trySuccess();
        }

        void progress(int n) {
            this.progress += (long)n;
            if (this.promise instanceof ChannelProgressivePromise) {
                ((ChannelProgressivePromise)this.promise).tryProgress(this.progress, -1L);
            }
        }

        PendingWrite(Object object, ChannelPromise channelPromise) {
            this.msg = object;
            this.promise = channelPromise;
        }
    }
}

