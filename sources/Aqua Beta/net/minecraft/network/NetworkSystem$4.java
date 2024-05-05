package net.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.PingResponseHandler;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;

/*
 * Exception performing whole class analysis ignored.
 */
class NetworkSystem.4
extends ChannelInitializer<Channel> {
    NetworkSystem.4() {
    }

    protected void initChannel(Channel p_initChannel_1_) throws Exception {
        try {
            p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
        }
        catch (ChannelException channelException) {
            // empty catch block
        }
        p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new PingResponseHandler(NetworkSystem.this)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
        NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
        NetworkSystem.access$000((NetworkSystem)NetworkSystem.this).add((Object)networkmanager);
        p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
        networkmanager.setNetHandler((INetHandler)new NetHandlerHandshakeTCP(NetworkSystem.access$100((NetworkSystem)NetworkSystem.this), networkmanager));
    }
}
