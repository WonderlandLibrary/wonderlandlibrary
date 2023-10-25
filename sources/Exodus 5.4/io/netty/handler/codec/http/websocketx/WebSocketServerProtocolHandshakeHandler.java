/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;

class WebSocketServerProtocolHandshakeHandler
extends ChannelInboundHandlerAdapter {
    private final String subprotocols;
    private final boolean allowExtensions;
    private final int maxFramePayloadSize;
    private final String websocketPath;

    @Override
    public void channelRead(final ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        FullHttpRequest fullHttpRequest = (FullHttpRequest)object;
        if (fullHttpRequest.getMethod() != HttpMethod.GET) {
            WebSocketServerProtocolHandshakeHandler.sendHttpResponse(channelHandlerContext, fullHttpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            fullHttpRequest.release();
            return;
        }
        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory(WebSocketServerProtocolHandshakeHandler.getWebSocketLocation(channelHandlerContext.pipeline(), fullHttpRequest, this.websocketPath), this.subprotocols, this.allowExtensions, this.maxFramePayloadSize);
        WebSocketServerHandshaker webSocketServerHandshaker = webSocketServerHandshakerFactory.newHandshaker(fullHttpRequest);
        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channelHandlerContext.channel());
        } else {
            ChannelFuture channelFuture = webSocketServerHandshaker.handshake(channelHandlerContext.channel(), fullHttpRequest);
            channelFuture.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        channelHandlerContext.fireExceptionCaught(channelFuture.cause());
                    } else {
                        channelHandlerContext.fireUserEventTriggered((Object)WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                    }
                }
            });
            WebSocketServerProtocolHandler.setHandshaker(channelHandlerContext, webSocketServerHandshaker);
            channelHandlerContext.pipeline().replace(this, "WS403Responder", WebSocketServerProtocolHandler.forbiddenHttpRequestResponder());
        }
        fullHttpRequest.release();
    }

    private static String getWebSocketLocation(ChannelPipeline channelPipeline, HttpRequest httpRequest, String string) {
        String string2 = "ws";
        if (channelPipeline.get(SslHandler.class) != null) {
            string2 = "wss";
        }
        return string2 + "://" + httpRequest.headers().get("Host") + string;
    }

    private static void sendHttpResponse(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest, HttpResponse httpResponse) {
        ChannelFuture channelFuture = channelHandlerContext.channel().writeAndFlush(httpResponse);
        if (!HttpHeaders.isKeepAlive(httpRequest) || httpResponse.getStatus().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    WebSocketServerProtocolHandshakeHandler(String string, String string2, boolean bl, int n) {
        this.websocketPath = string;
        this.subprotocols = string2;
        this.allowExtensions = bl;
        this.maxFramePayloadSize = n;
    }
}

