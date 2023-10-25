package net.minecraft.network;

import java.util.concurrent.Callable;
import net.minecraft.network.Packet;

class NetHandlerPlayServer.3
implements Callable<String> {
    final /* synthetic */ Packet val$packetIn;

    NetHandlerPlayServer.3(Packet packet) {
        this.val$packetIn = packet;
    }

    public String call() throws Exception {
        return this.val$packetIn.getClass().getCanonicalName();
    }
}
