/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelInitializer
 *  io.netty.handler.codec.ByteToMessageDecoder
 *  io.netty.handler.codec.MessageToByteEncoder
 */
package com.viaversion.viaversion.bukkit.handlers;

import com.viaversion.viaversion.bukkit.classgenerator.ClassGenerator;
import com.viaversion.viaversion.classgenerator.generated.HandlerConstructor;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.reflect.Method;

public class BukkitChannelInitializer
extends ChannelInitializer<Channel> {
    private final ChannelInitializer<Channel> original;
    private Method method;

    public BukkitChannelInitializer(ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
        try {
            this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
            this.method.setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }

    protected void initChannel(Channel channel) throws Exception {
        this.method.invoke(this.original, new Object[]{channel});
        BukkitChannelInitializer.afterChannelInitialize(channel);
    }

    public static void afterChannelInitialize(Channel channel) {
        UserConnectionImpl connection = new UserConnectionImpl(channel);
        new ProtocolPipelineImpl(connection);
        HandlerConstructor constructor = ClassGenerator.getConstructor();
        MessageToByteEncoder encoder = constructor.newEncodeHandler(connection, (MessageToByteEncoder)channel.pipeline().get("encoder"));
        ByteToMessageDecoder decoder = constructor.newDecodeHandler(connection, (ByteToMessageDecoder)channel.pipeline().get("decoder"));
        channel.pipeline().replace("encoder", "encoder", (ChannelHandler)encoder);
        channel.pipeline().replace("decoder", "decoder", (ChannelHandler)decoder);
    }
}

