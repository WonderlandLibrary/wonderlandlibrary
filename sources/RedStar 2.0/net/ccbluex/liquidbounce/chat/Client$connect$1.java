package net.ccbluex.liquidbounce.chat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.ClientHandler;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\n\u0000\n\n\b*\u0000\b\n\u00002\b00J020H¨"}, d2={"net/ccbluex/liquidbounce/chat/Client$connect$1", "Lio/netty/channel/ChannelInitializer;", "Lio/netty/channel/socket/SocketChannel;", "initChannel", "", "ch", "Pride"})
public static final class Client$connect$1
extends ChannelInitializer<SocketChannel> {
    final SslContext $sslContext;
    final ClientHandler $handler;

    protected void initChannel(@NotNull SocketChannel ch) {
        Intrinsics.checkParameterIsNotNull(ch, "ch");
        ChannelPipeline pipeline = ch.pipeline();
        if (this.$sslContext != null) {
            pipeline.addLast(new ChannelHandler[]{(ChannelHandler)this.$sslContext.newHandler(ch.alloc())});
        }
        pipeline.addLast(new ChannelHandler[]{(ChannelHandler)new HttpClientCodec(), (ChannelHandler)new HttpObjectAggregator(8192), (ChannelHandler)this.$handler});
    }

    Client$connect$1(SslContext $captured_local_variable$0, ClientHandler $captured_local_variable$1) {
        this.$sslContext = $captured_local_variable$0;
        this.$handler = $captured_local_variable$1;
    }
}
