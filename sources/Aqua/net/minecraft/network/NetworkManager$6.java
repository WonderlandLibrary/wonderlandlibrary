package net.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import net.minecraft.network.NetworkManager;

static final class NetworkManager.6
extends ChannelInitializer<Channel> {
    final /* synthetic */ NetworkManager val$networkmanager;

    NetworkManager.6(NetworkManager networkManager) {
        this.val$networkmanager = networkManager;
    }

    protected void initChannel(Channel p_initChannel_1_) throws Exception {
        p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)this.val$networkmanager);
    }
}
