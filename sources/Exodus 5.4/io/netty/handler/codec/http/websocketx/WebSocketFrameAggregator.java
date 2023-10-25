/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;

public class WebSocketFrameAggregator
extends MessageToMessageDecoder<WebSocketFrame> {
    private boolean tooLongFrameFound;
    private WebSocketFrame currentFrame;
    private final int maxFrameSize;

    public WebSocketFrameAggregator(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("maxFrameSize must be > 0");
        }
        this.maxFrameSize = n;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.handlerRemoved(channelHandlerContext);
        if (this.currentFrame != null) {
            this.currentFrame.release();
            this.currentFrame = null;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelInactive(channelHandlerContext);
        if (this.currentFrame != null) {
            this.currentFrame.release();
            this.currentFrame = null;
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
        if (this.currentFrame == null) {
            this.tooLongFrameFound = false;
            if (webSocketFrame.isFinalFragment()) {
                list.add(webSocketFrame.retain());
                return;
            }
            CompositeByteBuf compositeByteBuf = channelHandlerContext.alloc().compositeBuffer().addComponent((ByteBuf)webSocketFrame.content().retain());
            ((ByteBuf)compositeByteBuf).writerIndex(((ByteBuf)compositeByteBuf).writerIndex() + webSocketFrame.content().readableBytes());
            if (webSocketFrame instanceof TextWebSocketFrame) {
                this.currentFrame = new TextWebSocketFrame(true, webSocketFrame.rsv(), compositeByteBuf);
            } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
                this.currentFrame = new BinaryWebSocketFrame(true, webSocketFrame.rsv(), compositeByteBuf);
            } else {
                compositeByteBuf.release();
                throw new IllegalStateException("WebSocket frame was not of type TextWebSocketFrame or BinaryWebSocketFrame");
            }
            return;
        }
        if (webSocketFrame instanceof ContinuationWebSocketFrame) {
            if (this.tooLongFrameFound) {
                if (webSocketFrame.isFinalFragment()) {
                    this.currentFrame = null;
                }
                return;
            }
            CompositeByteBuf compositeByteBuf = (CompositeByteBuf)this.currentFrame.content();
            if (compositeByteBuf.readableBytes() > this.maxFrameSize - webSocketFrame.content().readableBytes()) {
                this.currentFrame.release();
                this.tooLongFrameFound = true;
                throw new TooLongFrameException("WebSocketFrame length exceeded " + compositeByteBuf + " bytes.");
            }
            compositeByteBuf.addComponent((ByteBuf)webSocketFrame.content().retain());
            compositeByteBuf.writerIndex(compositeByteBuf.writerIndex() + webSocketFrame.content().readableBytes());
            if (webSocketFrame.isFinalFragment()) {
                WebSocketFrame webSocketFrame2 = this.currentFrame;
                this.currentFrame = null;
                list.add(webSocketFrame2);
                return;
            }
            return;
        }
        list.add(webSocketFrame.retain());
    }
}

