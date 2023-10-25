/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandshakeHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import java.net.URI;
import java.util.List;

public class WebSocketClientProtocolHandler
extends WebSocketProtocolHandler {
    private final boolean handleCloseFrames;
    private final WebSocketClientHandshaker handshaker;

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker webSocketClientHandshaker) {
        this(webSocketClientHandshaker, true);
    }

    public WebSocketClientProtocolHandler(URI uRI, WebSocketVersion webSocketVersion, String string, boolean bl, HttpHeaders httpHeaders, int n) {
        this(uRI, webSocketVersion, string, bl, httpHeaders, n, true);
    }

    public WebSocketClientProtocolHandler(URI uRI, WebSocketVersion webSocketVersion, String string, boolean bl, HttpHeaders httpHeaders, int n, boolean bl2) {
        this(WebSocketClientHandshakerFactory.newHandshaker(uRI, webSocketVersion, string, bl, httpHeaders, n), bl2);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
        if (this.handleCloseFrames && webSocketFrame instanceof CloseWebSocketFrame) {
            channelHandlerContext.close();
            return;
        }
        super.decode(channelHandlerContext, webSocketFrame, list);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) {
        ChannelPipeline channelPipeline = channelHandlerContext.pipeline();
        if (channelPipeline.get(WebSocketClientProtocolHandshakeHandler.class) == null) {
            channelHandlerContext.pipeline().addBefore(channelHandlerContext.name(), WebSocketClientProtocolHandshakeHandler.class.getName(), new WebSocketClientProtocolHandshakeHandler(this.handshaker));
        }
    }

    public WebSocketClientProtocolHandler(WebSocketClientHandshaker webSocketClientHandshaker, boolean bl) {
        this.handshaker = webSocketClientHandshaker;
        this.handleCloseFrames = bl;
    }

    public static enum ClientHandshakeStateEvent {
        HANDSHAKE_ISSUED,
        HANDSHAKE_COMPLETE;

    }
}

