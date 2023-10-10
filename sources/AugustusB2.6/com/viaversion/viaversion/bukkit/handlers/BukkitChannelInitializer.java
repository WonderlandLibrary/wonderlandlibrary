// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.handlers;

import com.viaversion.viaversion.classgenerator.generated.HandlerConstructor;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import com.viaversion.viaversion.bukkit.classgenerator.ClassGenerator;
import com.viaversion.viaversion.bukkit.platform.PaperViaInjector;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import java.lang.reflect.Method;
import com.viaversion.viaversion.platform.WrappedChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class BukkitChannelInitializer extends ChannelInitializer<Channel> implements WrappedChannelInitializer
{
    private static final Method INIT_CHANNEL_METHOD;
    private final ChannelInitializer<Channel> original;
    
    public BukkitChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
    }
    
    @Deprecated
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
    
    @Override
    protected void initChannel(final Channel channel) throws Exception {
        BukkitChannelInitializer.INIT_CHANNEL_METHOD.invoke(this.original, channel);
        afterChannelInitialize(channel);
    }
    
    public static void afterChannelInitialize(final Channel channel) {
        final UserConnection connection = new UserConnectionImpl(channel);
        new ProtocolPipelineImpl(connection);
        if (PaperViaInjector.PAPER_PACKET_LIMITER) {
            connection.setPacketLimiterEnabled(false);
        }
        final HandlerConstructor constructor = ClassGenerator.getConstructor();
        final MessageToByteEncoder encoder = constructor.newEncodeHandler(connection, (MessageToByteEncoder)channel.pipeline().get("encoder"));
        final ByteToMessageDecoder decoder = constructor.newDecodeHandler(connection, (ByteToMessageDecoder)channel.pipeline().get("decoder"));
        channel.pipeline().replace("encoder", "encoder", encoder);
        channel.pipeline().replace("decoder", "decoder", decoder);
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
