package net.minecraft.network;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

/*
 * Exception performing whole class analysis ignored.
 */
class NetworkManager.4
implements Runnable {
    final /* synthetic */ EnumConnectionState val$enumconnectionstate;
    final /* synthetic */ EnumConnectionState val$enumconnectionstate1;
    final /* synthetic */ Packet val$inPacket;
    final /* synthetic */ GenericFutureListener[] val$futureListeners;

    NetworkManager.4(EnumConnectionState enumConnectionState, EnumConnectionState enumConnectionState2, Packet packet, GenericFutureListener[] genericFutureListenerArray) {
        this.val$enumconnectionstate = enumConnectionState;
        this.val$enumconnectionstate1 = enumConnectionState2;
        this.val$inPacket = packet;
        this.val$futureListeners = genericFutureListenerArray;
    }

    public void run() {
        if (this.val$enumconnectionstate != this.val$enumconnectionstate1) {
            NetworkManager.this.setConnectionState(this.val$enumconnectionstate);
        }
        ChannelFuture channelfuture1 = NetworkManager.access$000((NetworkManager)NetworkManager.this).writeAndFlush((Object)this.val$inPacket);
        if (this.val$futureListeners != null) {
            channelfuture1.addListeners(this.val$futureListeners);
        }
        channelfuture1.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
