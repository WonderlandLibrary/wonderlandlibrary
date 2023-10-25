/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.spdy.DefaultSpdyDataFrame;
import io.netty.handler.codec.spdy.DefaultSpdyGoAwayFrame;
import io.netty.handler.codec.spdy.DefaultSpdyRstStreamFrame;
import io.netty.handler.codec.spdy.DefaultSpdyWindowUpdateFrame;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyDataFrame;
import io.netty.handler.codec.spdy.SpdyFrame;
import io.netty.handler.codec.spdy.SpdyGoAwayFrame;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyPingFrame;
import io.netty.handler.codec.spdy.SpdyProtocolException;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.handler.codec.spdy.SpdySession;
import io.netty.handler.codec.spdy.SpdySessionStatus;
import io.netty.handler.codec.spdy.SpdySettingsFrame;
import io.netty.handler.codec.spdy.SpdyStreamStatus;
import io.netty.handler.codec.spdy.SpdySynReplyFrame;
import io.netty.handler.codec.spdy.SpdySynStreamFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.codec.spdy.SpdyWindowUpdateFrame;
import io.netty.util.internal.EmptyArrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SpdySessionHandler
extends ChannelDuplexHandler {
    private static final int DEFAULT_MAX_CONCURRENT_STREAMS = Integer.MAX_VALUE;
    private int localConcurrentStreams;
    private final AtomicInteger pings;
    private static final int DEFAULT_WINDOW_SIZE = 65536;
    private int initialSendWindowSize = 65536;
    private int initialReceiveWindowSize = 65536;
    private final SpdySession spdySession = new SpdySession(this.initialSendWindowSize, this.initialReceiveWindowSize);
    private volatile int initialSessionReceiveWindowSize = 65536;
    private final int minorVersion;
    private int lastGoodStreamId;
    private static final SpdyProtocolException PROTOCOL_EXCEPTION = new SpdyProtocolException();
    private boolean sentGoAwayFrame;
    private int remoteConcurrentStreams = Integer.MAX_VALUE;
    private static final SpdyProtocolException STREAM_CLOSED = new SpdyProtocolException("Stream closed");
    private final boolean server;
    private boolean receivedGoAwayFrame;
    private ChannelFutureListener closeSessionFutureListener;

    private boolean isRemoteInitiatedId(int n) {
        boolean bl = SpdyCodecUtil.isServerId(n);
        return this.server && !bl || !this.server && bl;
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        if (object instanceof SpdyDataFrame || object instanceof SpdySynStreamFrame || object instanceof SpdySynReplyFrame || object instanceof SpdyRstStreamFrame || object instanceof SpdySettingsFrame || object instanceof SpdyPingFrame || object instanceof SpdyGoAwayFrame || object instanceof SpdyHeadersFrame || object instanceof SpdyWindowUpdateFrame) {
            this.handleOutboundMessage(channelHandlerContext, object, channelPromise);
        } else {
            channelHandlerContext.write(object, channelPromise);
        }
    }

    private synchronized boolean acceptStream(int n, byte by, boolean bl, boolean bl2) {
        int n2;
        if (this.receivedGoAwayFrame || this.sentGoAwayFrame) {
            return false;
        }
        boolean bl3 = this.isRemoteInitiatedId(n);
        int n3 = n2 = bl3 ? this.localConcurrentStreams : this.remoteConcurrentStreams;
        if (this.spdySession.numActiveStreams(bl3) >= n2) {
            return false;
        }
        this.spdySession.acceptStream(n, by, bl, bl2, this.initialSendWindowSize, this.initialReceiveWindowSize, bl3);
        if (bl3) {
            this.lastGoodStreamId = n;
        }
        return true;
    }

    private void issueStreamError(ChannelHandlerContext channelHandlerContext, int n, SpdyStreamStatus spdyStreamStatus) {
        boolean bl = !this.spdySession.isRemoteSideClosed(n);
        ChannelPromise channelPromise = channelHandlerContext.newPromise();
        this.removeStream(n, channelPromise);
        DefaultSpdyRstStreamFrame defaultSpdyRstStreamFrame = new DefaultSpdyRstStreamFrame(n, spdyStreamStatus);
        channelHandlerContext.writeAndFlush(defaultSpdyRstStreamFrame, channelPromise);
        if (bl) {
            channelHandlerContext.fireChannelRead(defaultSpdyRstStreamFrame);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        if (object instanceof SpdyDataFrame) {
            SpdyFrame spdyFrame;
            int n;
            SpdyDataFrame spdyDataFrame = (SpdyDataFrame)object;
            int n2 = spdyDataFrame.streamId();
            int n3 = -1 * spdyDataFrame.content().readableBytes();
            int n4 = this.spdySession.updateReceiveWindowSize(0, n3);
            if (n4 < 0) {
                this.issueSessionError(channelHandlerContext, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            if (n4 <= this.initialSessionReceiveWindowSize / 2) {
                n = this.initialSessionReceiveWindowSize - n4;
                this.spdySession.updateReceiveWindowSize(0, n);
                spdyFrame = new DefaultSpdyWindowUpdateFrame(0, n);
                channelHandlerContext.writeAndFlush(spdyFrame);
            }
            if (!this.spdySession.isActiveStream(n2)) {
                spdyDataFrame.release();
                if (n2 <= this.lastGoodStreamId) {
                    this.issueStreamError(channelHandlerContext, n2, SpdyStreamStatus.PROTOCOL_ERROR);
                } else if (!this.sentGoAwayFrame) {
                    this.issueStreamError(channelHandlerContext, n2, SpdyStreamStatus.INVALID_STREAM);
                }
                return;
            }
            if (this.spdySession.isRemoteSideClosed(n2)) {
                spdyDataFrame.release();
                this.issueStreamError(channelHandlerContext, n2, SpdyStreamStatus.STREAM_ALREADY_CLOSED);
                return;
            }
            if (!this.isRemoteInitiatedId(n2) && !this.spdySession.hasReceivedReply(n2)) {
                spdyDataFrame.release();
                this.issueStreamError(channelHandlerContext, n2, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            n = this.spdySession.updateReceiveWindowSize(n2, n3);
            if (n < this.spdySession.getReceiveWindowSizeLowerBound(n2)) {
                spdyDataFrame.release();
                this.issueStreamError(channelHandlerContext, n2, SpdyStreamStatus.FLOW_CONTROL_ERROR);
                return;
            }
            if (n < 0) {
                while (spdyDataFrame.content().readableBytes() > this.initialReceiveWindowSize) {
                    spdyFrame = new DefaultSpdyDataFrame(n2, (ByteBuf)spdyDataFrame.content().readSlice(this.initialReceiveWindowSize).retain());
                    channelHandlerContext.writeAndFlush(spdyFrame);
                }
            }
            if (n <= this.initialReceiveWindowSize / 2 && !spdyDataFrame.isLast()) {
                int n5 = this.initialReceiveWindowSize - n;
                this.spdySession.updateReceiveWindowSize(n2, n5);
                DefaultSpdyWindowUpdateFrame defaultSpdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(n2, n5);
                channelHandlerContext.writeAndFlush(defaultSpdyWindowUpdateFrame);
            }
            if (spdyDataFrame.isLast()) {
                this.halfCloseStream(n2, true, channelHandlerContext.newSucceededFuture());
            }
        } else if (object instanceof SpdySynStreamFrame) {
            boolean bl;
            boolean bl2;
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)object;
            int n = spdySynStreamFrame.streamId();
            if (spdySynStreamFrame.isInvalid() || !this.isRemoteInitiatedId(n) || this.spdySession.isActiveStream(n)) {
                this.issueStreamError(channelHandlerContext, n, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            if (n <= this.lastGoodStreamId) {
                this.issueSessionError(channelHandlerContext, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            byte by = spdySynStreamFrame.priority();
            if (!this.acceptStream(n, by, bl2 = spdySynStreamFrame.isLast(), bl = spdySynStreamFrame.isUnidirectional())) {
                this.issueStreamError(channelHandlerContext, n, SpdyStreamStatus.REFUSED_STREAM);
                return;
            }
        } else if (object instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)object;
            int n = spdySynReplyFrame.streamId();
            if (spdySynReplyFrame.isInvalid() || this.isRemoteInitiatedId(n) || this.spdySession.isRemoteSideClosed(n)) {
                this.issueStreamError(channelHandlerContext, n, SpdyStreamStatus.INVALID_STREAM);
                return;
            }
            if (this.spdySession.hasReceivedReply(n)) {
                this.issueStreamError(channelHandlerContext, n, SpdyStreamStatus.STREAM_IN_USE);
                return;
            }
            this.spdySession.receivedReply(n);
            if (spdySynReplyFrame.isLast()) {
                this.halfCloseStream(n, true, channelHandlerContext.newSucceededFuture());
            }
        } else if (object instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)object;
            this.removeStream(spdyRstStreamFrame.streamId(), channelHandlerContext.newSucceededFuture());
        } else if (object instanceof SpdySettingsFrame) {
            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)object;
            int n = spdySettingsFrame.getValue(0);
            if (n >= 0 && n != this.minorVersion) {
                this.issueSessionError(channelHandlerContext, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            int n6 = spdySettingsFrame.getValue(4);
            if (n6 >= 0) {
                this.remoteConcurrentStreams = n6;
            }
            if (spdySettingsFrame.isPersisted(7)) {
                spdySettingsFrame.removeValue(7);
            }
            spdySettingsFrame.setPersistValue(7, false);
            int n7 = spdySettingsFrame.getValue(7);
            if (n7 >= 0) {
                this.updateInitialSendWindowSize(n7);
            }
        } else if (object instanceof SpdyPingFrame) {
            SpdyPingFrame spdyPingFrame = (SpdyPingFrame)object;
            if (this.isRemoteInitiatedId(spdyPingFrame.id())) {
                channelHandlerContext.writeAndFlush(spdyPingFrame);
                return;
            }
            if (this.pings.get() == 0) {
                return;
            }
            this.pings.getAndDecrement();
        } else if (object instanceof SpdyGoAwayFrame) {
            this.receivedGoAwayFrame = true;
        } else if (object instanceof SpdyHeadersFrame) {
            SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)object;
            int n = spdyHeadersFrame.streamId();
            if (spdyHeadersFrame.isInvalid()) {
                this.issueStreamError(channelHandlerContext, n, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            if (this.spdySession.isRemoteSideClosed(n)) {
                this.issueStreamError(channelHandlerContext, n, SpdyStreamStatus.INVALID_STREAM);
                return;
            }
            if (spdyHeadersFrame.isLast()) {
                this.halfCloseStream(n, true, channelHandlerContext.newSucceededFuture());
            }
        } else if (object instanceof SpdyWindowUpdateFrame) {
            SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame)object;
            int n = spdyWindowUpdateFrame.streamId();
            int n8 = spdyWindowUpdateFrame.deltaWindowSize();
            if (n != 0 && this.spdySession.isLocalSideClosed(n)) {
                return;
            }
            if (this.spdySession.getSendWindowSize(n) > Integer.MAX_VALUE - n8) {
                if (n == 0) {
                    this.issueSessionError(channelHandlerContext, SpdySessionStatus.PROTOCOL_ERROR);
                } else {
                    this.issueStreamError(channelHandlerContext, n, SpdyStreamStatus.FLOW_CONTROL_ERROR);
                }
                return;
            }
            this.updateSendWindowSize(channelHandlerContext, n, n8);
        }
        channelHandlerContext.fireChannelRead(object);
    }

    static {
        PROTOCOL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        STREAM_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }

    public SpdySessionHandler(SpdyVersion spdyVersion, boolean bl) {
        this.localConcurrentStreams = Integer.MAX_VALUE;
        this.pings = new AtomicInteger();
        if (spdyVersion == null) {
            throw new NullPointerException("version");
        }
        this.server = bl;
        this.minorVersion = spdyVersion.getMinorVersion();
    }

    private void sendGoAwayFrame(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) {
        if (!channelHandlerContext.channel().isActive()) {
            channelHandlerContext.close(channelPromise);
            return;
        }
        ChannelFuture channelFuture = this.sendGoAwayFrame(channelHandlerContext, SpdySessionStatus.OK);
        if (this.spdySession.noActiveStreams()) {
            channelFuture.addListener(new ClosingChannelFutureListener(channelHandlerContext, channelPromise));
        } else {
            this.closeSessionFutureListener = new ClosingChannelFutureListener(channelHandlerContext, channelPromise);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        for (Integer n : this.spdySession.activeStreams().keySet()) {
            this.removeStream(n, channelHandlerContext.newSucceededFuture());
        }
        channelHandlerContext.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        if (throwable instanceof SpdyProtocolException) {
            this.issueSessionError(channelHandlerContext, SpdySessionStatus.PROTOCOL_ERROR);
        }
        channelHandlerContext.fireExceptionCaught(throwable);
    }

    private void issueSessionError(ChannelHandlerContext channelHandlerContext, SpdySessionStatus spdySessionStatus) {
        this.sendGoAwayFrame(channelHandlerContext, spdySessionStatus).addListener(new ClosingChannelFutureListener(channelHandlerContext, channelHandlerContext.newPromise()));
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        this.sendGoAwayFrame(channelHandlerContext, channelPromise);
    }

    private synchronized void updateInitialReceiveWindowSize(int n) {
        int n2 = n - this.initialReceiveWindowSize;
        this.initialReceiveWindowSize = n;
        this.spdySession.updateAllReceiveWindowSizes(n2);
    }

    private void updateSendWindowSize(final ChannelHandlerContext channelHandlerContext, int n, int n2) {
        this.spdySession.updateSendWindowSize(n, n2);
        SpdySession.PendingWrite pendingWrite;
        while ((pendingWrite = this.spdySession.getPendingWrite(n)) != null) {
            SpdyDataFrame spdyDataFrame = pendingWrite.spdyDataFrame;
            int n3 = spdyDataFrame.content().readableBytes();
            int n4 = spdyDataFrame.streamId();
            int n5 = this.spdySession.getSendWindowSize(n4);
            int n6 = this.spdySession.getSendWindowSize(0);
            if ((n5 = Math.min(n5, n6)) <= 0) {
                return;
            }
            if (n5 < n3) {
                this.spdySession.updateSendWindowSize(n4, -1 * n5);
                this.spdySession.updateSendWindowSize(0, -1 * n5);
                DefaultSpdyDataFrame defaultSpdyDataFrame = new DefaultSpdyDataFrame(n4, (ByteBuf)spdyDataFrame.content().readSlice(n5).retain());
                channelHandlerContext.writeAndFlush(defaultSpdyDataFrame).addListener(new ChannelFutureListener(){

                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (!channelFuture.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(channelHandlerContext, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
                continue;
            }
            this.spdySession.removePendingWrite(n4);
            this.spdySession.updateSendWindowSize(n4, -1 * n3);
            this.spdySession.updateSendWindowSize(0, -1 * n3);
            if (spdyDataFrame.isLast()) {
                this.halfCloseStream(n4, false, pendingWrite.promise);
            }
            channelHandlerContext.writeAndFlush(spdyDataFrame, pendingWrite.promise).addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        SpdySessionHandler.this.issueSessionError(channelHandlerContext, SpdySessionStatus.INTERNAL_ERROR);
                    }
                }
            });
        }
        return;
    }

    private void handleOutboundMessage(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        if (object instanceof SpdyDataFrame) {
            SpdyDataFrame spdyDataFrame = (SpdyDataFrame)object;
            int n = spdyDataFrame.streamId();
            if (this.spdySession.isLocalSideClosed(n)) {
                spdyDataFrame.release();
                channelPromise.setFailure(PROTOCOL_EXCEPTION);
                return;
            }
            int n2 = spdyDataFrame.content().readableBytes();
            int n3 = this.spdySession.getSendWindowSize(n);
            int n4 = this.spdySession.getSendWindowSize(0);
            if ((n3 = Math.min(n3, n4)) <= 0) {
                this.spdySession.putPendingWrite(n, new SpdySession.PendingWrite(spdyDataFrame, channelPromise));
                return;
            }
            if (n3 < n2) {
                this.spdySession.updateSendWindowSize(n, -1 * n3);
                this.spdySession.updateSendWindowSize(0, -1 * n3);
                DefaultSpdyDataFrame defaultSpdyDataFrame = new DefaultSpdyDataFrame(n, (ByteBuf)spdyDataFrame.content().readSlice(n3).retain());
                this.spdySession.putPendingWrite(n, new SpdySession.PendingWrite(spdyDataFrame, channelPromise));
                final ChannelHandlerContext channelHandlerContext2 = channelHandlerContext;
                channelHandlerContext.write(defaultSpdyDataFrame).addListener(new ChannelFutureListener(){

                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (!channelFuture.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(channelHandlerContext2, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
                return;
            }
            this.spdySession.updateSendWindowSize(n, -1 * n2);
            this.spdySession.updateSendWindowSize(0, -1 * n2);
            final ChannelHandlerContext channelHandlerContext3 = channelHandlerContext;
            channelPromise.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        SpdySessionHandler.this.issueSessionError(channelHandlerContext3, SpdySessionStatus.INTERNAL_ERROR);
                    }
                }
            });
            if (spdyDataFrame.isLast()) {
                this.halfCloseStream(n, false, channelPromise);
            }
        } else if (object instanceof SpdySynStreamFrame) {
            boolean bl;
            boolean bl2;
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)object;
            int n = spdySynStreamFrame.streamId();
            if (this.isRemoteInitiatedId(n)) {
                channelPromise.setFailure(PROTOCOL_EXCEPTION);
                return;
            }
            byte by = spdySynStreamFrame.priority();
            if (!this.acceptStream(n, by, bl2 = spdySynStreamFrame.isUnidirectional(), bl = spdySynStreamFrame.isLast())) {
                channelPromise.setFailure(PROTOCOL_EXCEPTION);
                return;
            }
        } else if (object instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)object;
            int n = spdySynReplyFrame.streamId();
            if (!this.isRemoteInitiatedId(n) || this.spdySession.isLocalSideClosed(n)) {
                channelPromise.setFailure(PROTOCOL_EXCEPTION);
                return;
            }
            if (spdySynReplyFrame.isLast()) {
                this.halfCloseStream(n, false, channelPromise);
            }
        } else if (object instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)object;
            this.removeStream(spdyRstStreamFrame.streamId(), channelPromise);
        } else if (object instanceof SpdySettingsFrame) {
            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)object;
            int n = spdySettingsFrame.getValue(0);
            if (n >= 0 && n != this.minorVersion) {
                channelPromise.setFailure(PROTOCOL_EXCEPTION);
                return;
            }
            int n5 = spdySettingsFrame.getValue(4);
            if (n5 >= 0) {
                this.localConcurrentStreams = n5;
            }
            if (spdySettingsFrame.isPersisted(7)) {
                spdySettingsFrame.removeValue(7);
            }
            spdySettingsFrame.setPersistValue(7, false);
            int n6 = spdySettingsFrame.getValue(7);
            if (n6 >= 0) {
                this.updateInitialReceiveWindowSize(n6);
            }
        } else if (object instanceof SpdyPingFrame) {
            SpdyPingFrame spdyPingFrame = (SpdyPingFrame)object;
            if (this.isRemoteInitiatedId(spdyPingFrame.id())) {
                channelHandlerContext.fireExceptionCaught(new IllegalArgumentException("invalid PING ID: " + spdyPingFrame.id()));
                return;
            }
            this.pings.getAndIncrement();
        } else {
            if (object instanceof SpdyGoAwayFrame) {
                channelPromise.setFailure(PROTOCOL_EXCEPTION);
                return;
            }
            if (object instanceof SpdyHeadersFrame) {
                SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)object;
                int n = spdyHeadersFrame.streamId();
                if (this.spdySession.isLocalSideClosed(n)) {
                    channelPromise.setFailure(PROTOCOL_EXCEPTION);
                    return;
                }
                if (spdyHeadersFrame.isLast()) {
                    this.halfCloseStream(n, false, channelPromise);
                }
            } else if (object instanceof SpdyWindowUpdateFrame) {
                channelPromise.setFailure(PROTOCOL_EXCEPTION);
                return;
            }
        }
        channelHandlerContext.write(object, channelPromise);
    }

    private void removeStream(int n, ChannelFuture channelFuture) {
        this.spdySession.removeStream(n, STREAM_CLOSED, this.isRemoteInitiatedId(n));
        if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
            channelFuture.addListener(this.closeSessionFutureListener);
        }
    }

    public void setSessionReceiveWindowSize(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("sessionReceiveWindowSize");
        }
        this.initialSessionReceiveWindowSize = n;
    }

    private synchronized void updateInitialSendWindowSize(int n) {
        int n2 = n - this.initialSendWindowSize;
        this.initialSendWindowSize = n;
        this.spdySession.updateAllSendWindowSizes(n2);
    }

    private void halfCloseStream(int n, boolean bl, ChannelFuture channelFuture) {
        if (bl) {
            this.spdySession.closeRemoteSide(n, this.isRemoteInitiatedId(n));
        } else {
            this.spdySession.closeLocalSide(n, this.isRemoteInitiatedId(n));
        }
        if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
            channelFuture.addListener(this.closeSessionFutureListener);
        }
    }

    private synchronized ChannelFuture sendGoAwayFrame(ChannelHandlerContext channelHandlerContext, SpdySessionStatus spdySessionStatus) {
        if (!this.sentGoAwayFrame) {
            this.sentGoAwayFrame = true;
            DefaultSpdyGoAwayFrame defaultSpdyGoAwayFrame = new DefaultSpdyGoAwayFrame(this.lastGoodStreamId, spdySessionStatus);
            return channelHandlerContext.writeAndFlush(defaultSpdyGoAwayFrame);
        }
        return channelHandlerContext.newSucceededFuture();
    }

    private static final class ClosingChannelFutureListener
    implements ChannelFutureListener {
        private final ChannelHandlerContext ctx;
        private final ChannelPromise promise;

        ClosingChannelFutureListener(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) {
            this.ctx = channelHandlerContext;
            this.promise = channelPromise;
        }

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            this.ctx.close(this.promise);
        }
    }
}

