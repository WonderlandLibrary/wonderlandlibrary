package net.minecraft.server.network;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.server.network.NetHandlerLoginServer;

/*
 * Exception performing whole class analysis ignored.
 */
class NetHandlerLoginServer.1
implements ChannelFutureListener {
    NetHandlerLoginServer.1() {
    }

    public void operationComplete(ChannelFuture p_operationComplete_1_) throws Exception {
        NetHandlerLoginServer.this.networkManager.setCompressionTreshold(NetHandlerLoginServer.access$000((NetHandlerLoginServer)NetHandlerLoginServer.this).getNetworkCompressionTreshold());
    }
}
