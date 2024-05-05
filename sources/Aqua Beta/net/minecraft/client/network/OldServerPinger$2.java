package net.minecraft.client.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;

class OldServerPinger.2
extends ChannelInitializer<Channel> {
    final /* synthetic */ ServerAddress val$serveraddress;
    final /* synthetic */ ServerData val$server;

    OldServerPinger.2(ServerAddress serverAddress, ServerData serverData) {
        this.val$serveraddress = serverAddress;
        this.val$server = serverData;
    }

    protected void initChannel(Channel p_initChannel_1_) throws Exception {
        try {
            p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
        }
        catch (ChannelException channelException) {
            // empty catch block
        }
        p_initChannel_1_.pipeline().addLast(new ChannelHandler[]{new /* Unavailable Anonymous Inner Class!! */});
    }
}
