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
 *  org.jetbrains.annotations.NotNull
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
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.Client;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0002H\u0014J\u0018\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/chat/ClientHandler;", "Lio/netty/channel/SimpleChannelInboundHandler;", "", "client", "Lnet/ccbluex/liquidbounce/chat/Client;", "handshaker", "Lio/netty/handler/codec/http/websocketx/WebSocketClientHandshaker;", "(Lnet/ccbluex/liquidbounce/chat/Client;Lio/netty/handler/codec/http/websocketx/WebSocketClientHandshaker;)V", "getClient", "()Lnet/ccbluex/liquidbounce/chat/Client;", "handshakeFuture", "Lio/netty/channel/ChannelPromise;", "getHandshakeFuture", "()Lio/netty/channel/ChannelPromise;", "setHandshakeFuture", "(Lio/netty/channel/ChannelPromise;)V", "channelActive", "", "ctx", "Lio/netty/channel/ChannelHandlerContext;", "channelInactive", "channelRead0", "msg", "exceptionCaught", "cause", "", "handlerAdded", "KyinoClient"})
public final class ClientHandler
extends SimpleChannelInboundHandler<Object> {
    @NotNull
    public ChannelPromise handshakeFuture;
    @NotNull
    private final Client client;
    private final WebSocketClientHandshaker handshaker;

    @NotNull
    public final ChannelPromise getHandshakeFuture() {
        ChannelPromise channelPromise = this.handshakeFuture;
        if (channelPromise == null) {
            Intrinsics.throwUninitializedPropertyAccessException("handshakeFuture");
        }
        return channelPromise;
    }

    public final void setHandshakeFuture(@NotNull ChannelPromise channelPromise) {
        Intrinsics.checkParameterIsNotNull(channelPromise, "<set-?>");
        this.handshakeFuture = channelPromise;
    }

    public void handlerAdded(@NotNull ChannelHandlerContext ctx) {
        Intrinsics.checkParameterIsNotNull(ctx, "ctx");
        ChannelPromise channelPromise = ctx.newPromise();
        Intrinsics.checkExpressionValueIsNotNull(channelPromise, "ctx.newPromise()");
        this.handshakeFuture = channelPromise;
    }

    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        Intrinsics.checkParameterIsNotNull(ctx, "ctx");
        this.handshaker.handshake(ctx.channel());
    }

    public void channelInactive(@NotNull ChannelHandlerContext ctx) {
        Intrinsics.checkParameterIsNotNull(ctx, "ctx");
        this.client.onDisconnect();
        this.client.setChannel$KyinoClient(null);
        this.client.setUsername("");
        this.client.setJwt(false);
    }

    public void exceptionCaught(@NotNull ChannelHandlerContext ctx, @NotNull Throwable cause) {
        Intrinsics.checkParameterIsNotNull(ctx, "ctx");
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        ClientUtils.getLogger().error("LiquidChat error", cause);
        this.client.onError(cause);
        ChannelPromise channelPromise = this.handshakeFuture;
        if (channelPromise == null) {
            Intrinsics.throwUninitializedPropertyAccessException("handshakeFuture");
        }
        if (!channelPromise.isDone()) {
            ChannelPromise channelPromise2 = this.handshakeFuture;
            if (channelPromise2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("handshakeFuture");
            }
            channelPromise2.setFailure(cause);
        }
        ctx.close();
    }

    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
        Intrinsics.checkParameterIsNotNull(ctx, "ctx");
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        Channel channel = ctx.channel();
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                this.handshaker.finishHandshake(channel, (FullHttpResponse)msg);
                ChannelPromise channelPromise = this.handshakeFuture;
                if (channelPromise == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("handshakeFuture");
                }
                channelPromise.setSuccess();
            }
            catch (WebSocketHandshakeException exception) {
                ChannelPromise channelPromise = this.handshakeFuture;
                if (channelPromise == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("handshakeFuture");
                }
                channelPromise.setFailure((Throwable)exception);
            }
            ChannelPromise channelPromise = this.handshakeFuture;
            if (channelPromise == null) {
                Intrinsics.throwUninitializedPropertyAccessException("handshakeFuture");
            }
            this.client.onHandshake(channelPromise.isSuccess());
            return;
        }
        Object object = msg;
        if (object instanceof TextWebSocketFrame) {
            String string = ((TextWebSocketFrame)msg).text();
            Intrinsics.checkExpressionValueIsNotNull(string, "msg.text()");
            this.client.onMessage$KyinoClient(string);
        } else if (object instanceof CloseWebSocketFrame) {
            channel.close();
        }
    }

    @NotNull
    public final Client getClient() {
        return this.client;
    }

    public ClientHandler(@NotNull Client client, @NotNull WebSocketClientHandshaker handshaker) {
        Intrinsics.checkParameterIsNotNull(client, "client");
        Intrinsics.checkParameterIsNotNull(handshaker, "handshaker");
        this.client = client;
        this.handshaker = handshaker;
    }
}

