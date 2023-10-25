/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocket00FrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocket00FrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketUtil;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import java.util.regex.Pattern;

public class WebSocketServerHandshaker00
extends WebSocketServerHandshaker {
    private static final Pattern BEGINNING_DIGIT = Pattern.compile("[^0-9]");
    private static final Pattern BEGINNING_SPACE = Pattern.compile("[^ ]");

    public WebSocketServerHandshaker00(String string, String string2, int n) {
        super(WebSocketVersion.V00, string, string2, n);
    }

    @Override
    protected FullHttpResponse newHandshakeResponse(FullHttpRequest fullHttpRequest, HttpHeaders httpHeaders) {
        if (!"Upgrade".equalsIgnoreCase(fullHttpRequest.headers().get("Connection")) || !"WebSocket".equalsIgnoreCase(fullHttpRequest.headers().get("Upgrade"))) {
            throw new WebSocketHandshakeException("not a WebSocket handshake request: missing upgrade");
        }
        boolean bl = fullHttpRequest.headers().contains("Sec-WebSocket-Key1") && fullHttpRequest.headers().contains("Sec-WebSocket-Key2");
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(101, bl ? "WebSocket Protocol Handshake" : "Web Socket Protocol Handshake"));
        if (httpHeaders != null) {
            defaultFullHttpResponse.headers().add(httpHeaders);
        }
        defaultFullHttpResponse.headers().add("Upgrade", (Object)"WebSocket");
        defaultFullHttpResponse.headers().add("Connection", (Object)"Upgrade");
        if (bl) {
            String string;
            defaultFullHttpResponse.headers().add("Sec-WebSocket-Origin", (Object)fullHttpRequest.headers().get("Origin"));
            defaultFullHttpResponse.headers().add("Sec-WebSocket-Location", (Object)this.uri());
            String string2 = fullHttpRequest.headers().get("Sec-WebSocket-Protocol");
            if (string2 != null) {
                string = this.selectSubprotocol(string2);
                if (string == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Requested subprotocol(s) not supported: {}", (Object)string2);
                    }
                } else {
                    defaultFullHttpResponse.headers().add("Sec-WebSocket-Protocol", (Object)string);
                }
            }
            string = fullHttpRequest.headers().get("Sec-WebSocket-Key1");
            String string3 = fullHttpRequest.headers().get("Sec-WebSocket-Key2");
            int n = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(string).replaceAll("")) / (long)BEGINNING_SPACE.matcher(string).replaceAll("").length());
            int n2 = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(string3).replaceAll("")) / (long)BEGINNING_SPACE.matcher(string3).replaceAll("").length());
            long l = fullHttpRequest.content().readLong();
            ByteBuf byteBuf = Unpooled.buffer(16);
            byteBuf.writeInt(n);
            byteBuf.writeInt(n2);
            byteBuf.writeLong(l);
            defaultFullHttpResponse.content().writeBytes(WebSocketUtil.md5(byteBuf.array()));
        } else {
            defaultFullHttpResponse.headers().add("WebSocket-Origin", (Object)fullHttpRequest.headers().get("Origin"));
            defaultFullHttpResponse.headers().add("WebSocket-Location", (Object)this.uri());
            String string = fullHttpRequest.headers().get("WebSocket-Protocol");
            if (string != null) {
                defaultFullHttpResponse.headers().add("WebSocket-Protocol", (Object)this.selectSubprotocol(string));
            }
        }
        return defaultFullHttpResponse;
    }

    @Override
    public ChannelFuture close(Channel channel, CloseWebSocketFrame closeWebSocketFrame, ChannelPromise channelPromise) {
        return channel.writeAndFlush(closeWebSocketFrame, channelPromise);
    }

    @Override
    protected WebSocketFrameEncoder newWebSocketEncoder() {
        return new WebSocket00FrameEncoder();
    }

    @Override
    protected WebSocketFrameDecoder newWebsocketDecoder() {
        return new WebSocket00FrameDecoder(this.maxFramePayloadLength());
    }
}

