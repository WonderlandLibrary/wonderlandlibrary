// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.handlers;

import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import io.netty.channel.socket.SocketChannel;
import com.viaversion.viaversion.api.Via;
import java.lang.reflect.Method;
import com.viaversion.viaversion.platform.WrappedChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class SpongeChannelInitializer extends ChannelInitializer<Channel> implements WrappedChannelInitializer
{
    private static final Method INIT_CHANNEL_METHOD;
    private final ChannelInitializer<Channel> original;
    
    public SpongeChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
    }
    
    @Override
    protected void initChannel(final Channel channel) throws Exception {
        if (Via.getAPI().getServerVersion().isKnown() && channel instanceof SocketChannel) {
            final UserConnection info = new UserConnectionImpl(channel);
            new ProtocolPipelineImpl(info);
            SpongeChannelInitializer.INIT_CHANNEL_METHOD.invoke(this.original, channel);
            final MessageToByteEncoder encoder = new SpongeEncodeHandler(info, (MessageToByteEncoder<?>)channel.pipeline().get("encoder"));
            final ByteToMessageDecoder decoder = new SpongeDecodeHandler(info, (ByteToMessageDecoder)channel.pipeline().get("decoder"));
            channel.pipeline().replace("encoder", "encoder", encoder);
            channel.pipeline().replace("decoder", "decoder", decoder);
        }
        else {
            SpongeChannelInitializer.INIT_CHANNEL_METHOD.invoke(this.original, channel);
        }
    }
    
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
    
    @Override
    public ChannelInitializer<Channel> original() {
        return this.original;
    }
    
    static {
        try {
            (INIT_CHANNEL_METHOD = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
