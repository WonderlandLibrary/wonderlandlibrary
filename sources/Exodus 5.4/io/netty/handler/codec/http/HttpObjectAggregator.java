/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import java.util.List;

public class HttpObjectAggregator
extends MessageToMessageDecoder<HttpObject> {
    private boolean tooLongFrameFound;
    private static final FullHttpResponse CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
    private AggregatedFullHttpMessage currentMessage;
    private final int maxContentLength;
    public static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
    private ChannelHandlerContext ctx;
    private int maxCumulationBufferComponents = 1024;

    private static FullHttpMessage toFullMessage(HttpMessage httpMessage) {
        AggregatedFullHttpMessage aggregatedFullHttpMessage;
        if (httpMessage instanceof FullHttpMessage) {
            return ((FullHttpMessage)httpMessage).retain();
        }
        if (httpMessage instanceof HttpRequest) {
            aggregatedFullHttpMessage = new AggregatedFullHttpRequest((HttpRequest)httpMessage, Unpooled.EMPTY_BUFFER, (HttpHeaders)new DefaultHttpHeaders());
        } else if (httpMessage instanceof HttpResponse) {
            aggregatedFullHttpMessage = new AggregatedFullHttpResponse((HttpResponse)httpMessage, Unpooled.EMPTY_BUFFER, (HttpHeaders)new DefaultHttpHeaders());
        } else {
            throw new IllegalStateException();
        }
        return aggregatedFullHttpMessage;
    }

    @Override
    protected void decode(final ChannelHandlerContext channelHandlerContext, HttpObject httpObject, List<Object> list) throws Exception {
        AggregatedFullHttpMessage aggregatedFullHttpMessage = this.currentMessage;
        if (httpObject instanceof HttpMessage) {
            this.tooLongFrameFound = false;
            assert (aggregatedFullHttpMessage == null);
            HttpMessage httpMessage = (HttpMessage)httpObject;
            if (HttpHeaders.is100ContinueExpected(httpMessage)) {
                channelHandlerContext.writeAndFlush(CONTINUE).addListener(new ChannelFutureListener(){

                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (!channelFuture.isSuccess()) {
                            channelHandlerContext.fireExceptionCaught(channelFuture.cause());
                        }
                    }
                });
            }
            if (!httpMessage.getDecoderResult().isSuccess()) {
                HttpHeaders.removeTransferEncodingChunked(httpMessage);
                list.add(HttpObjectAggregator.toFullMessage(httpMessage));
                this.currentMessage = null;
                return;
            }
            if (httpObject instanceof HttpRequest) {
                HttpRequest httpRequest = (HttpRequest)httpObject;
                this.currentMessage = aggregatedFullHttpMessage = new AggregatedFullHttpRequest(httpRequest, (ByteBuf)channelHandlerContext.alloc().compositeBuffer(this.maxCumulationBufferComponents), null);
            } else if (httpObject instanceof HttpResponse) {
                HttpResponse httpResponse = (HttpResponse)httpObject;
                this.currentMessage = aggregatedFullHttpMessage = new AggregatedFullHttpResponse(httpResponse, (ByteBuf)Unpooled.compositeBuffer(this.maxCumulationBufferComponents), null);
            } else {
                throw new Error();
            }
            HttpHeaders.removeTransferEncodingChunked(aggregatedFullHttpMessage);
        } else if (httpObject instanceof HttpContent) {
            boolean bl;
            if (this.tooLongFrameFound) {
                if (httpObject instanceof LastHttpContent) {
                    this.currentMessage = null;
                }
                return;
            }
            assert (aggregatedFullHttpMessage != null);
            HttpContent httpContent = (HttpContent)httpObject;
            CompositeByteBuf compositeByteBuf = (CompositeByteBuf)aggregatedFullHttpMessage.content();
            if (compositeByteBuf.readableBytes() > this.maxContentLength - httpContent.content().readableBytes()) {
                this.tooLongFrameFound = true;
                aggregatedFullHttpMessage.release();
                this.currentMessage = null;
                throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
            }
            if (httpContent.content().isReadable()) {
                httpContent.retain();
                compositeByteBuf.addComponent(httpContent.content());
                compositeByteBuf.writerIndex(compositeByteBuf.writerIndex() + httpContent.content().readableBytes());
            }
            if (!httpContent.getDecoderResult().isSuccess()) {
                aggregatedFullHttpMessage.setDecoderResult(DecoderResult.failure(httpContent.getDecoderResult().cause()));
                bl = true;
            } else {
                bl = httpContent instanceof LastHttpContent;
            }
            if (bl) {
                this.currentMessage = null;
                if (httpContent instanceof LastHttpContent) {
                    LastHttpContent lastHttpContent = (LastHttpContent)httpContent;
                    aggregatedFullHttpMessage.setTrailingHeaders(lastHttpContent.trailingHeaders());
                } else {
                    aggregatedFullHttpMessage.setTrailingHeaders(new DefaultHttpHeaders());
                }
                aggregatedFullHttpMessage.headers().set("Content-Length", (Object)String.valueOf(compositeByteBuf.readableBytes()));
                list.add(aggregatedFullHttpMessage);
            }
        } else {
            throw new Error();
        }
    }

    public final void setMaxCumulationBufferComponents(int n) {
        if (n < 2) {
            throw new IllegalArgumentException("maxCumulationBufferComponents: " + n + " (expected: >= 2)");
        }
        if (this.ctx != null) {
            throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
        }
        this.maxCumulationBufferComponents = n;
    }

    public final int getMaxCumulationBufferComponents() {
        return this.maxCumulationBufferComponents;
    }

    public HttpObjectAggregator(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("maxContentLength must be a positive integer: " + n);
        }
        this.maxContentLength = n;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.ctx = channelHandlerContext;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.handlerRemoved(channelHandlerContext);
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelInactive(channelHandlerContext);
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
    }

    private static final class AggregatedFullHttpResponse
    extends AggregatedFullHttpMessage
    implements FullHttpResponse {
        private AggregatedFullHttpResponse(HttpResponse httpResponse, ByteBuf byteBuf, HttpHeaders httpHeaders) {
            super(httpResponse, byteBuf, httpHeaders);
        }

        @Override
        public HttpResponseStatus getStatus() {
            return ((HttpResponse)this.message).getStatus();
        }

        @Override
        public FullHttpResponse setStatus(HttpResponseStatus httpResponseStatus) {
            ((HttpResponse)this.message).setStatus(httpResponseStatus);
            return this;
        }

        @Override
        public FullHttpResponse duplicate() {
            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().duplicate());
            defaultFullHttpResponse.headers().set(this.headers());
            defaultFullHttpResponse.trailingHeaders().set(this.trailingHeaders());
            return defaultFullHttpResponse;
        }

        @Override
        public FullHttpResponse setProtocolVersion(HttpVersion httpVersion) {
            super.setProtocolVersion(httpVersion);
            return this;
        }

        @Override
        public FullHttpResponse retain(int n) {
            super.retain(n);
            return this;
        }

        @Override
        public FullHttpResponse copy() {
            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().copy());
            defaultFullHttpResponse.headers().set(this.headers());
            defaultFullHttpResponse.trailingHeaders().set(this.trailingHeaders());
            return defaultFullHttpResponse;
        }

        @Override
        public FullHttpResponse retain() {
            super.retain();
            return this;
        }
    }

    private static final class AggregatedFullHttpRequest
    extends AggregatedFullHttpMessage
    implements FullHttpRequest {
        @Override
        public FullHttpRequest setProtocolVersion(HttpVersion httpVersion) {
            super.setProtocolVersion(httpVersion);
            return this;
        }

        @Override
        public FullHttpRequest retain() {
            super.retain();
            return this;
        }

        @Override
        public FullHttpRequest setUri(String string) {
            ((HttpRequest)this.message).setUri(string);
            return this;
        }

        private AggregatedFullHttpRequest(HttpRequest httpRequest, ByteBuf byteBuf, HttpHeaders httpHeaders) {
            super(httpRequest, byteBuf, httpHeaders);
        }

        @Override
        public String getUri() {
            return ((HttpRequest)this.message).getUri();
        }

        @Override
        public FullHttpRequest duplicate() {
            DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate());
            defaultFullHttpRequest.headers().set(this.headers());
            defaultFullHttpRequest.trailingHeaders().set(this.trailingHeaders());
            return defaultFullHttpRequest;
        }

        @Override
        public FullHttpRequest copy() {
            DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy());
            defaultFullHttpRequest.headers().set(this.headers());
            defaultFullHttpRequest.trailingHeaders().set(this.trailingHeaders());
            return defaultFullHttpRequest;
        }

        @Override
        public FullHttpRequest retain(int n) {
            super.retain(n);
            return this;
        }

        @Override
        public HttpMethod getMethod() {
            return ((HttpRequest)this.message).getMethod();
        }

        @Override
        public FullHttpRequest setMethod(HttpMethod httpMethod) {
            ((HttpRequest)this.message).setMethod(httpMethod);
            return this;
        }
    }

    private static abstract class AggregatedFullHttpMessage
    extends DefaultByteBufHolder
    implements FullHttpMessage {
        protected final HttpMessage message;
        private HttpHeaders trailingHeaders;

        @Override
        public void setDecoderResult(DecoderResult decoderResult) {
            this.message.setDecoderResult(decoderResult);
        }

        @Override
        public abstract FullHttpMessage copy();

        @Override
        public FullHttpMessage setProtocolVersion(HttpVersion httpVersion) {
            this.message.setProtocolVersion(httpVersion);
            return this;
        }

        private AggregatedFullHttpMessage(HttpMessage httpMessage, ByteBuf byteBuf, HttpHeaders httpHeaders) {
            super(byteBuf);
            this.message = httpMessage;
            this.trailingHeaders = httpHeaders;
        }

        @Override
        public FullHttpMessage retain(int n) {
            super.retain(n);
            return this;
        }

        public void setTrailingHeaders(HttpHeaders httpHeaders) {
            this.trailingHeaders = httpHeaders;
        }

        @Override
        public FullHttpMessage retain() {
            super.retain();
            return this;
        }

        @Override
        public HttpHeaders headers() {
            return this.message.headers();
        }

        @Override
        public abstract FullHttpMessage duplicate();

        @Override
        public HttpVersion getProtocolVersion() {
            return this.message.getProtocolVersion();
        }

        @Override
        public DecoderResult getDecoderResult() {
            return this.message.getDecoderResult();
        }

        @Override
        public HttpHeaders trailingHeaders() {
            return this.trailingHeaders;
        }
    }
}

