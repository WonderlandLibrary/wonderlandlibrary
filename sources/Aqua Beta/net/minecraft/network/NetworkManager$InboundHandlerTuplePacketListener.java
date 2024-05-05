package net.minecraft.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;

static class NetworkManager.InboundHandlerTuplePacketListener {
    private final Packet packet;
    private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

    public NetworkManager.InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener<? extends Future<? super Void>> ... inFutureListeners) {
        this.packet = inPacket;
        this.futureListeners = inFutureListeners;
    }

    static /* synthetic */ Packet access$100(NetworkManager.InboundHandlerTuplePacketListener x0) {
        return x0.packet;
    }

    static /* synthetic */ GenericFutureListener[] access$200(NetworkManager.InboundHandlerTuplePacketListener x0) {
        return x0.futureListeners;
    }
}
