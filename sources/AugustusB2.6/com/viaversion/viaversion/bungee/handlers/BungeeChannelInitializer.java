// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.handlers;

import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import java.lang.reflect.Method;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class BungeeChannelInitializer extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<Channel> original;
    private Method method;
    
    public BungeeChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
        try {
            (this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void initChannel(final Channel socketChannel) throws Exception {
        if (!socketChannel.isActive()) {
            return;
        }
        final UserConnection info = new UserConnectionImpl(socketChannel);
        new ProtocolPipelineImpl(info);
        this.method.invoke(this.original, socketChannel);
        if (!socketChannel.isActive()) {
            return;
        }
        if (socketChannel.pipeline().get("packet-encoder") == null) {
            return;
        }
        if (socketChannel.pipeline().get("packet-decoder") == null) {
            return;
        }
        final BungeeEncodeHandler encoder = new BungeeEncodeHandler(info);
        final BungeeDecodeHandler decoder = new BungeeDecodeHandler(info);
        socketChannel.pipeline().addBefore("packet-encoder", "via-encoder", encoder);
        socketChannel.pipeline().addBefore("packet-decoder", "via-decoder", decoder);
    }
    
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
}
