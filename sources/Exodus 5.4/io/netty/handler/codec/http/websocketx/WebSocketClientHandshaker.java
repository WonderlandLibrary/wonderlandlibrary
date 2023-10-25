/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import java.net.URI;

public abstract class WebSocketClientHandshaker {
    private final URI uri;
    private final int maxFramePayloadLength;
    protected final HttpHeaders customHeaders;
    private volatile boolean handshakeComplete;
    private final String expectedSubprotocol;
    private final WebSocketVersion version;
    private volatile String actualSubprotocol;

    protected abstract WebSocketFrameEncoder newWebSocketEncoder();

    private void setHandshakeComplete() {
        this.handshakeComplete = true;
    }

    public String expectedSubprotocol() {
        return this.expectedSubprotocol;
    }

    public ChannelFuture handshake(Channel channel) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return this.handshake(channel, channel.newPromise());
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame closeWebSocketFrame, ChannelPromise channelPromise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return channel.writeAndFlush(closeWebSocketFrame, channelPromise);
    }

    public int maxFramePayloadLength() {
        return this.maxFramePayloadLength;
    }

    protected abstract WebSocketFrameDecoder newWebsocketDecoder();

    protected abstract FullHttpRequest newHandshakeRequest();

    public URI uri() {
        return this.uri;
    }

    public boolean isHandshakeComplete() {
        return this.handshakeComplete;
    }

    protected WebSocketClientHandshaker(URI uRI, WebSocketVersion webSocketVersion, String string, HttpHeaders httpHeaders, int n) {
        this.uri = uRI;
        this.version = webSocketVersion;
        this.expectedSubprotocol = string;
        this.customHeaders = httpHeaders;
        this.maxFramePayloadLength = n;
    }

    public final ChannelFuture handshake(Channel channel, final ChannelPromise channelPromise) {
        HttpClientCodec httpClientCodec;
        FullHttpRequest fullHttpRequest = this.newHandshakeRequest();
        HttpResponseDecoder httpResponseDecoder = channel.pipeline().get(HttpResponseDecoder.class);
        if (httpResponseDecoder == null && (httpClientCodec = channel.pipeline().get(HttpClientCodec.class)) == null) {
            channelPromise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
            return channelPromise;
        }
        channel.writeAndFlush(fullHttpRequest).addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (channelFuture.isSuccess()) {
                    ChannelPipeline channelPipeline = channelFuture.channel().pipeline();
                    ChannelHandlerContext channelHandlerContext = channelPipeline.context(HttpRequestEncoder.class);
                    if (channelHandlerContext == null) {
                        channelHandlerContext = channelPipeline.context(HttpClientCodec.class);
                    }
                    if (channelHandlerContext == null) {
                        channelPromise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"));
                        return;
                    }
                    channelPipeline.addAfter(channelHandlerContext.name(), "ws-encoder", WebSocketClientHandshaker.this.newWebSocketEncoder());
                    channelPromise.setSuccess();
                } else {
                    channelPromise.setFailure(channelFuture.cause());
                }
            }
        });
        return channelPromise;
    }

    protected abstract void verify(FullHttpResponse var1);

    public final void finishHandshake(Channel channel, FullHttpResponse fullHttpResponse) {
        ChannelHandlerContext channelHandlerContext;
        this.verify(fullHttpResponse);
        this.setActualSubprotocol(fullHttpResponse.headers().get("Sec-WebSocket-Protocol"));
        this.setHandshakeComplete();
        ChannelPipeline channelPipeline = channel.pipeline();
        HttpContentDecompressor httpContentDecompressor = channelPipeline.get(HttpContentDecompressor.class);
        if (httpContentDecompressor != null) {
            channelPipeline.remove(httpContentDecompressor);
        }
        if ((channelHandlerContext = channelPipeline.context(HttpResponseDecoder.class)) == null) {
            channelHandlerContext = channelPipeline.context(HttpClientCodec.class);
            if (channelHandlerContext == null) {
                throw new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec");
            }
            channelPipeline.replace(channelHandlerContext.name(), "ws-decoder", (ChannelHandler)this.newWebsocketDecoder());
        } else {
            if (channelPipeline.get(HttpRequestEncoder.class) != null) {
                channelPipeline.remove(HttpRequestEncoder.class);
            }
            channelPipeline.replace(channelHandlerContext.name(), "ws-decoder", (ChannelHandler)this.newWebsocketDecoder());
        }
    }

    public WebSocketVersion version() {
        return this.version;
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame closeWebSocketFrame) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return this.close(channel, closeWebSocketFrame, channel.newPromise());
    }

    private void setActualSubprotocol(String string) {
        this.actualSubprotocol = string;
    }

    public String actualSubprotocol() {
        return this.actualSubprotocol;
    }
}

