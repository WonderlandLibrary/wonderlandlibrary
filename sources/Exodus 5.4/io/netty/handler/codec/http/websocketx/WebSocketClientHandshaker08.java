/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocket08FrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocket08FrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketUtil;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.URI;

public class WebSocketClientHandshaker08
extends WebSocketClientHandshaker {
    private String expectedChallengeResponseString;
    private final boolean allowExtensions;
    public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker08.class);

    public WebSocketClientHandshaker08(URI uRI, WebSocketVersion webSocketVersion, String string, boolean bl, HttpHeaders httpHeaders, int n) {
        super(uRI, webSocketVersion, string, httpHeaders, n);
        this.allowExtensions = bl;
    }

    @Override
    protected void verify(FullHttpResponse fullHttpResponse) {
        HttpResponseStatus httpResponseStatus = HttpResponseStatus.SWITCHING_PROTOCOLS;
        HttpHeaders httpHeaders = fullHttpResponse.headers();
        if (!fullHttpResponse.getStatus().equals(httpResponseStatus)) {
            throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + fullHttpResponse.getStatus());
        }
        String string = httpHeaders.get("Upgrade");
        if (!"WebSocket".equalsIgnoreCase(string)) {
            throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + string);
        }
        String string2 = httpHeaders.get("Connection");
        if (!"Upgrade".equalsIgnoreCase(string2)) {
            throw new WebSocketHandshakeException("Invalid handshake response connection: " + string2);
        }
        String string3 = httpHeaders.get("Sec-WebSocket-Accept");
        if (string3 == null || !string3.equals(this.expectedChallengeResponseString)) {
            throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", string3, this.expectedChallengeResponseString));
        }
    }

    @Override
    protected FullHttpRequest newHandshakeRequest() {
        URI uRI = this.uri();
        String string = uRI.getPath();
        if (uRI.getQuery() != null && !uRI.getQuery().isEmpty()) {
            string = uRI.getPath() + '?' + uRI.getQuery();
        }
        if (string == null || string.isEmpty()) {
            string = "/";
        }
        byte[] byArray = WebSocketUtil.randomBytes(16);
        String string2 = WebSocketUtil.base64(byArray);
        String string3 = string2 + MAGIC_GUID;
        byte[] byArray2 = WebSocketUtil.sha1(string3.getBytes(CharsetUtil.US_ASCII));
        this.expectedChallengeResponseString = WebSocketUtil.base64(byArray2);
        if (logger.isDebugEnabled()) {
            logger.debug("WebSocket version 08 client handshake key: {}, expected response: {}", (Object)string2, (Object)this.expectedChallengeResponseString);
        }
        DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, string);
        HttpHeaders httpHeaders = defaultFullHttpRequest.headers();
        httpHeaders.add("Upgrade", (Object)"WebSocket".toLowerCase()).add("Connection", (Object)"Upgrade").add("Sec-WebSocket-Key", (Object)string2).add("Host", (Object)uRI.getHost());
        int n = uRI.getPort();
        String string4 = "http://" + uRI.getHost();
        if (n != 80 && n != 443) {
            string4 = string4 + ':' + n;
        }
        httpHeaders.add("Sec-WebSocket-Origin", (Object)string4);
        String string5 = this.expectedSubprotocol();
        if (string5 != null && !string5.isEmpty()) {
            httpHeaders.add("Sec-WebSocket-Protocol", (Object)string5);
        }
        httpHeaders.add("Sec-WebSocket-Version", (Object)"8");
        if (this.customHeaders != null) {
            httpHeaders.add(this.customHeaders);
        }
        return defaultFullHttpRequest;
    }

    @Override
    protected WebSocketFrameDecoder newWebsocketDecoder() {
        return new WebSocket08FrameDecoder(false, this.allowExtensions, this.maxFramePayloadLength());
    }

    @Override
    protected WebSocketFrameEncoder newWebSocketEncoder() {
        return new WebSocket08FrameEncoder(true);
    }
}

