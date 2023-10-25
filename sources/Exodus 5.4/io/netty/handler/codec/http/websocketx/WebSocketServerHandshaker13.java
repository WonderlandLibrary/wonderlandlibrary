/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocket13FrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocket13FrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketUtil;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;

public class WebSocketServerHandshaker13
extends WebSocketServerHandshaker {
    private final boolean allowExtensions;
    public static final String WEBSOCKET_13_ACCEPT_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    @Override
    protected WebSocketFrameEncoder newWebSocketEncoder() {
        return new WebSocket13FrameEncoder(false);
    }

    public WebSocketServerHandshaker13(String string, String string2, boolean bl, int n) {
        super(WebSocketVersion.V13, string, string2, n);
        this.allowExtensions = bl;
    }

    @Override
    protected WebSocketFrameDecoder newWebsocketDecoder() {
        return new WebSocket13FrameDecoder(true, this.allowExtensions, this.maxFramePayloadLength());
    }

    @Override
    protected FullHttpResponse newHandshakeResponse(FullHttpRequest fullHttpRequest, HttpHeaders httpHeaders) {
        String string;
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
        if (httpHeaders != null) {
            defaultFullHttpResponse.headers().add(httpHeaders);
        }
        if ((string = fullHttpRequest.headers().get("Sec-WebSocket-Key")) == null) {
            throw new WebSocketHandshakeException("not a WebSocket request: missing key");
        }
        String string2 = string + WEBSOCKET_13_ACCEPT_GUID;
        byte[] byArray = WebSocketUtil.sha1(string2.getBytes(CharsetUtil.US_ASCII));
        String string3 = WebSocketUtil.base64(byArray);
        if (logger.isDebugEnabled()) {
            logger.debug("WebSocket version 13 server handshake key: {}, response: {}", (Object)string, (Object)string3);
        }
        defaultFullHttpResponse.headers().add("Upgrade", (Object)"WebSocket".toLowerCase());
        defaultFullHttpResponse.headers().add("Connection", (Object)"Upgrade");
        defaultFullHttpResponse.headers().add("Sec-WebSocket-Accept", (Object)string3);
        String string4 = fullHttpRequest.headers().get("Sec-WebSocket-Protocol");
        if (string4 != null) {
            String string5 = this.selectSubprotocol(string4);
            if (string5 == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Requested subprotocol(s) not supported: {}", (Object)string4);
                }
            } else {
                defaultFullHttpResponse.headers().add("Sec-WebSocket-Protocol", (Object)string5);
            }
        }
        return defaultFullHttpResponse;
    }
}

