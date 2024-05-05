package net.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import net.minecraft.client.network.NetHandlerHandshakeMemory;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkSystem;

/*
 * Exception performing whole class analysis ignored.
 */
class NetworkSystem.5
extends ChannelInitializer<Channel> {
    NetworkSystem.5() {
    }

    protected void initChannel(Channel p_initChannel_1_) throws Exception {
        NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
        networkmanager.setNetHandler((INetHandler)new NetHandlerHandshakeMemory(NetworkSystem.access$100((NetworkSystem)NetworkSystem.this), networkmanager));
        NetworkSystem.access$000((NetworkSystem)NetworkSystem.this).add((Object)networkmanager);
        p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
    }
}
