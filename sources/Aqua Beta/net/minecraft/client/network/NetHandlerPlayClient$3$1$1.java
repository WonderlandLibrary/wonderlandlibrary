package net.minecraft.client.network;

import com.google.common.util.concurrent.FutureCallback;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

/*
 * Exception performing whole class analysis ignored.
 */
class NetHandlerPlayClient.1
implements FutureCallback<Object> {
    NetHandlerPlayClient.1() {
    }

    public void onSuccess(Object p_onSuccess_1_) {
        NetHandlerPlayClient.access$000((NetHandlerPlayClient)this$1.this$0).sendPacket((Packet)new C19PacketResourcePackStatus(this$1.val$s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
    }

    public void onFailure(Throwable p_onFailure_1_) {
        NetHandlerPlayClient.access$000((NetHandlerPlayClient)this$1.this$0).sendPacket((Packet)new C19PacketResourcePackStatus(this$1.val$s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
    }
}
