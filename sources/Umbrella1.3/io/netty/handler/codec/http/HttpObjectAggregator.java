/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCountUtil;
import java.util.List;

public class HttpObjectAggregator
extends MessageToMessageDecoder<HttpObject> {
    public static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
    private static final FullHttpResponse CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
    private final int maxContentLength;
    private FullHttpMessage currentMessage;
    private boolean tooLongFrameFound;
    private int maxCumulationBufferComponents = 1024;
    private ChannelHandlerContext ctx;

    public HttpObjectAggregator(int maxContentLength) {
        if (maxContentLength <= 0) {
            throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
        }
        this.maxContentLength = maxContentLength;
    }

    public final int getMaxCumulationBufferComponents() {
        return this.maxCumulationBufferComponents;
    }

    public final void setMaxCumulationBufferComponents(int maxCumulationBufferComponents) {
        if (maxCumulationBufferComponents < 2) {
            throw new IllegalArgumentException("maxCumulationBufferComponents: " + maxCumulationBufferComponents + " (expected: >= 2)");
        }
        if (this.ctx != null) {
            throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
        }
        this.maxCumulationBufferComponents = maxCumulationBufferComponents;
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
        FullHttpMessage currentMessage = this.currentMessage;
        if (msg instanceof HttpMessage) {
            this.tooLongFrameFound = false;
            assert (currentMessage == null);
            HttpMessage m = (HttpMessage)msg;
            if (HttpHeaders.is100ContinueExpected(m)) {
                ctx.writeAndFlush(CONTINUE).addListener(new ChannelFutureListener(){

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            ctx.fireExceptionCaught(future.cause());
                        }
                    }
                });
            }
            if (!m.getDecoderResult().isSuccess()) {
                HttpHeaders.removeTransferEncodingChunked(m);
                this.currentMessage = null;
                out.add(ReferenceCountUtil.retain(m));
                return;
            }
            if (msg instanceof HttpRequest) {
                HttpRequest header = (HttpRequest)msg;
                this.currentMessage = currentMessage = new DefaultFullHttpRequest(header.getProtocolVersion(), header.getMethod(), header.getUri(), Unpooled.compositeBuffer(this.maxCumulationBufferComponents));
            } else if (msg instanceof HttpResponse) {
                HttpResponse header = (HttpResponse)msg;
                this.currentMessage = currentMessage = new DefaultFullHttpResponse(header.getProtocolVersion(), header.getStatus(), Unpooled.compositeBuffer(this.maxCumulationBufferComponents));
            } else {
                throw new Error();
            }
            currentMessage.headers().set(m.headers());
            HttpHeaders.removeTransferEncodingChunked(currentMessage);
        } else if (msg instanceof HttpContent) {
            boolean last;
            if (this.tooLongFrameFound) {
                if (msg instanceof LastHttpContent) {
                    this.currentMessage = null;
                }
                return;
            }
            assert (currentMessage != null);
            HttpContent chunk = (HttpContent)msg;
            CompositeByteBuf content = (CompositeByteBuf)currentMessage.content();
            if (content.readableBytes() > this.maxContentLength - chunk.content().readableBytes()) {
                this.tooLongFrameFound = true;
                currentMessage.release();
                this.currentMessage = null;
                throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
            }
            if (chunk.content().isReadable()) {
                chunk.retain();
                content.addComponent(chunk.content());
                content.writerIndex(content.writerIndex() + chunk.content().readableBytes());
            }
            if (!chunk.getDecoderResult().isSuccess()) {
                currentMessage.setDecoderResult(DecoderResult.failure(chunk.getDecoderResult().cause()));
                last = true;
            } else {
                last = chunk instanceof LastHttpContent;
            }
            if (last) {
                this.currentMessage = null;
                if (chunk instanceof LastHttpContent) {
                    LastHttpContent trailer = (LastHttpContent)chunk;
                    currentMessage.headers().add(trailer.trailingHeaders());
                }
                currentMessage.headers().set("Content-Length", (Object)String.valueOf(content.readableBytes()));
                out.add(currentMessage);
            }
        } else {
            throw new Error();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
    }
}

