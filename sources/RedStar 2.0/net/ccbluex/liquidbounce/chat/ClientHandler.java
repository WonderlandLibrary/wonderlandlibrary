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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\n\n\u0000\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\b\u00002\b00B00¢J020HJ020HJ02020HJ02020HJ020HR0¢\b\n\u0000\b\b\tR\n0X.¢\n\u0000\b\f\r\"\bR0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/ClientHandler;", "Lio/netty/channel/SimpleChannelInboundHandler;", "", "client", "Lnet/ccbluex/liquidbounce/chat/Client;", "handshaker", "Lio/netty/handler/codec/http/websocketx/WebSocketClientHandshaker;", "(Lnet/ccbluex/liquidbounce/chat/Client;Lio/netty/handler/codec/http/websocketx/WebSocketClientHandshaker;)V", "getClient", "()Lnet/ccbluex/liquidbounce/chat/Client;", "handshakeFuture", "Lio/netty/channel/ChannelPromise;", "getHandshakeFuture", "()Lio/netty/channel/ChannelPromise;", "setHandshakeFuture", "(Lio/netty/channel/ChannelPromise;)V", "channelActive", "", "ctx", "Lio/netty/channel/ChannelHandlerContext;", "channelInactive", "channelRead0", "msg", "exceptionCaught", "cause", "", "handlerAdded", "Pride"})
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
        this.client.setChannel$Pride(null);
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
        block7: {
            Object object;
            Channel channel;
            block6: {
                Intrinsics.checkParameterIsNotNull(ctx, "ctx");
                Intrinsics.checkParameterIsNotNull(msg, "msg");
                channel = ctx.channel();
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
                object = msg;
                if (!(object instanceof TextWebSocketFrame)) break block6;
                String string = ((TextWebSocketFrame)msg).text();
                Intrinsics.checkExpressionValueIsNotNull(string, "msg.text()");
                this.client.onMessage$Pride(string);
                break block7;
            }
            if (!(object instanceof CloseWebSocketFrame)) break block7;
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
