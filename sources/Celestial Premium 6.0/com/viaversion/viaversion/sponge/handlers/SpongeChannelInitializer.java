/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.socket.SocketChannel
 *  io.netty.handler.codec.ByteToMessageDecoder
 *  io.netty.handler.codec.MessageToByteEncoder
 */
package com.viaversion.viaversion.sponge.handlers;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.sponge.handlers.SpongeDecodeHandler;
import com.viaversion.viaversion.sponge.handlers.SpongeEncodeHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.reflect.Method;

public class SpongeChannelInitializer
extends ChannelInitializer<Channel> {
    private final ChannelInitializer<Channel> original;
    private Method method;

    public SpongeChannelInitializer(ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
        try {
            this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
            this.method.setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    protected void initChannel(Channel channel) throws Exception {
        if (Via.getAPI().getServerVersion().isKnown() && channel instanceof SocketChannel) {
            UserConnectionImpl info = new UserConnectionImpl((Channel)((SocketChannel)channel));
            new ProtocolPipelineImpl(info);
            this.method.invoke(this.original, new Object[]{channel});
            SpongeEncodeHandler encoder = new SpongeEncodeHandler(info, (MessageToByteEncoder)channel.pipeline().get("encoder"));
            SpongeDecodeHandler decoder = new SpongeDecodeHandler(info, (ByteToMessageDecoder)channel.pipeline().get("decoder"));
            channel.pipeline().replace("encoder", "encoder", (ChannelHandler)encoder);
            channel.pipeline().replace("decoder", "decoder", (ChannelHandler)decoder);
        } else {
            this.method.invoke(this.original, new Object[]{channel});
        }
    }

    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
}

