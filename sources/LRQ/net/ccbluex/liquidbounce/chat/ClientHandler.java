/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelPromise
 *  io.netty.channel.SimpleChannelInboundHandler
 *  io.netty.handler.codec.http.FullHttpResponse
 *  io.netty.handler.codec.http.websocketx.CloseWebSocketFrame
 *  io.netty.handler.codec.http.websocketx.TextWebSocketFrame
 *  io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker
 *  io.netty.handler.codec.http.websocketx.WebSocketHandshakeException
 */
package net.ccbluex.liquidbounce.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import net.ccbluex.liquidbounce.chat.Client;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public final class ClientHandler
extends SimpleChannelInboundHandler<Object> {
    public ChannelPromise handshakeFuture;
    private final Client client;
    private final WebSocketClientHandshaker handshaker;

    public final ChannelPromise getHandshakeFuture() {
        return this.handshakeFuture;
    }

    public final void setHandshakeFuture(ChannelPromise channelPromise) {
        this.handshakeFuture = channelPromise;
    }

    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    public void channelActive(ChannelHandlerContext ctx) {
        this.handshaker.handshake(ctx.channel());
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        this.client.onDisconnect();
        this.client.setChannel$LiquidSense(null);
        this.client.setUsername("");
        this.client.setJwt(false);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ClientUtils.getLogger().error("LiquidChat error", cause);
        this.client.onError(cause);
        if (!this.handshakeFuture.isDone()) {
            this.handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel channel = ctx.channel();
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                this.handshaker.finishHandshake(channel, (FullHttpResponse)msg);
                this.handshakeFuture.setSuccess();
            }
            catch (WebSocketHandshakeException exception) {
                this.handshakeFuture.setFailure((Throwable)exception);
            }
            this.client.onHandshake(this.handshakeFuture.isSuccess());
            return;
        }
        Object object = msg;
        if (object instanceof TextWebSocketFrame) {
            this.client.onMessage$LiquidSense(((TextWebSocketFrame)msg).text());
        } else if (object instanceof CloseWebSocketFrame) {
            channel.close();
        }
    }

    public final Client getClient() {
        return this.client;
    }

    public ClientHandler(Client client2, WebSocketClientHandshaker handshaker) {
        this.client = client2;
        this.handshaker = handshaker;
    }
}

