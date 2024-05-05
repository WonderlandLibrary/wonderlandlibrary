package net.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;

static final class NetworkManager.5
extends ChannelInitializer<Channel> {
    final /* synthetic */ NetworkManager val$networkmanager;

    NetworkManager.5(NetworkManager networkManager) {
        this.val$networkmanager = networkManager;
    }

    protected void initChannel(Channel p_initChannel_1_) throws Exception {
        try {
            p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
        }
        catch (ChannelException channelException) {
            // empty catch block
        }
        p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)this.val$networkmanager);
    }
}
