/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocket00FrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocket00FrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketUtil;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketClientHandshaker00
extends WebSocketClientHandshaker {
    private ByteBuf expectedChallengeResponseBytes;

    public WebSocketClientHandshaker00(URI uRI, WebSocketVersion webSocketVersion, String string, HttpHeaders httpHeaders, int n) {
        super(uRI, webSocketVersion, string, httpHeaders, n);
    }

    @Override
    protected void verify(FullHttpResponse fullHttpResponse) {
        HttpResponseStatus httpResponseStatus = new HttpResponseStatus(101, "WebSocket Protocol Handshake");
        if (!fullHttpResponse.getStatus().equals(httpResponseStatus)) {
            throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + fullHttpResponse.getStatus());
        }
        HttpHeaders httpHeaders = fullHttpResponse.headers();
        String string = httpHeaders.get("Upgrade");
        if (!"WebSocket".equalsIgnoreCase(string)) {
            throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + string);
        }
        String string2 = httpHeaders.get("Connection");
        if (!"Upgrade".equalsIgnoreCase(string2)) {
            throw new WebSocketHandshakeException("Invalid handshake response connection: " + string2);
        }
        ByteBuf byteBuf = fullHttpResponse.content();
        if (!byteBuf.equals(this.expectedChallengeResponseBytes)) {
            throw new WebSocketHandshakeException("Invalid challenge");
        }
    }

    private static String insertSpaces(String string, int n) {
        for (int i = 0; i < n; ++i) {
            int n2 = WebSocketUtil.randomNumber(1, string.length() - 1);
            String string2 = string.substring(0, n2);
            String string3 = string.substring(n2);
            string = string2 + ' ' + string3;
        }
        return string;
    }

    @Override
    protected FullHttpRequest newHandshakeRequest() {
        int n = WebSocketUtil.randomNumber(1, 12);
        int n2 = WebSocketUtil.randomNumber(1, 12);
        int n3 = Integer.MAX_VALUE / n;
        int n4 = Integer.MAX_VALUE / n2;
        int n5 = WebSocketUtil.randomNumber(0, n3);
        int n6 = WebSocketUtil.randomNumber(0, n4);
        int n7 = n5 * n;
        int n8 = n6 * n2;
        String string = Integer.toString(n7);
        String string2 = Integer.toString(n8);
        string = WebSocketClientHandshaker00.insertRandomCharacters(string);
        string2 = WebSocketClientHandshaker00.insertRandomCharacters(string2);
        string = WebSocketClientHandshaker00.insertSpaces(string, n);
        string2 = WebSocketClientHandshaker00.insertSpaces(string2, n2);
        byte[] byArray = WebSocketUtil.randomBytes(8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(n5);
        byte[] byArray2 = byteBuffer.array();
        byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(n6);
        byte[] byArray3 = byteBuffer.array();
        byte[] byArray4 = new byte[16];
        System.arraycopy(byArray2, 0, byArray4, 0, 4);
        System.arraycopy(byArray3, 0, byArray4, 4, 4);
        System.arraycopy(byArray, 0, byArray4, 8, 8);
        this.expectedChallengeResponseBytes = Unpooled.wrappedBuffer(WebSocketUtil.md5(byArray4));
        URI uRI = this.uri();
        String string3 = uRI.getPath();
        if (uRI.getQuery() != null && !uRI.getQuery().isEmpty()) {
            string3 = uRI.getPath() + '?' + uRI.getQuery();
        }
        if (string3 == null || string3.isEmpty()) {
            string3 = "/";
        }
        DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, string3);
        HttpHeaders httpHeaders = defaultFullHttpRequest.headers();
        httpHeaders.add("Upgrade", (Object)"WebSocket").add("Connection", (Object)"Upgrade").add("Host", (Object)uRI.getHost());
        int n9 = uRI.getPort();
        String string4 = "http://" + uRI.getHost();
        if (n9 != 80 && n9 != 443) {
            string4 = string4 + ':' + n9;
        }
        httpHeaders.add("Origin", (Object)string4).add("Sec-WebSocket-Key1", (Object)string).add("Sec-WebSocket-Key2", (Object)string2);
        String string5 = this.expectedSubprotocol();
        if (string5 != null && !string5.isEmpty()) {
            httpHeaders.add("Sec-WebSocket-Protocol", (Object)string5);
        }
        if (this.customHeaders != null) {
            httpHeaders.add(this.customHeaders);
        }
        httpHeaders.set("Content-Length", (Object)byArray.length);
        defaultFullHttpRequest.content().writeBytes(byArray);
        return defaultFullHttpRequest;
    }

    @Override
    protected WebSocketFrameEncoder newWebSocketEncoder() {
        return new WebSocket00FrameEncoder();
    }

    @Override
    protected WebSocketFrameDecoder newWebsocketDecoder() {
        return new WebSocket00FrameDecoder(this.maxFramePayloadLength());
    }

    private static String insertRandomCharacters(String string) {
        int n;
        int n2 = WebSocketUtil.randomNumber(1, 12);
        char[] cArray = new char[n2];
        int n3 = 0;
        while (n3 < n2) {
            n = (int)(Math.random() * 126.0 + 33.0);
            if ((33 >= n || n >= 47) && (58 >= n || n >= 126)) continue;
            cArray[n3] = (char)n;
            ++n3;
        }
        for (n = 0; n < n2; ++n) {
            int n4 = WebSocketUtil.randomNumber(0, string.length());
            String string2 = string.substring(0, n4);
            String string3 = string.substring(n4);
            string = string2 + cArray[n] + string3;
        }
        return string;
    }
}

