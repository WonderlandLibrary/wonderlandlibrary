package net.minecraft.client.network;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

/*
 * Exception performing whole class analysis ignored.
 */
class NetHandlerPlayClient.1
implements GuiYesNoCallback {
    NetHandlerPlayClient.1() {
    }

    public void confirmClicked(boolean result, int id) {
        NetHandlerPlayClient.access$102((NetHandlerPlayClient)this$0, (Minecraft)Minecraft.getMinecraft());
        if (result) {
            if (NetHandlerPlayClient.access$100((NetHandlerPlayClient)this$0).getCurrentServerData() != null) {
                NetHandlerPlayClient.access$100((NetHandlerPlayClient)this$0).getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
            }
            NetHandlerPlayClient.access$000((NetHandlerPlayClient)this$0).sendPacket((Packet)new C19PacketResourcePackStatus(val$s1, C19PacketResourcePackStatus.Action.ACCEPTED));
            Futures.addCallback((ListenableFuture)NetHandlerPlayClient.access$100((NetHandlerPlayClient)this$0).getResourcePackRepository().downloadResourcePack(val$s, val$s1), (FutureCallback)new /* Unavailable Anonymous Inner Class!! */);
        } else {
            if (NetHandlerPlayClient.access$100((NetHandlerPlayClient)this$0).getCurrentServerData() != null) {
                NetHandlerPlayClient.access$100((NetHandlerPlayClient)this$0).getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
            }
            NetHandlerPlayClient.access$000((NetHandlerPlayClient)this$0).sendPacket((Packet)new C19PacketResourcePackStatus(val$s1, C19PacketResourcePackStatus.Action.DECLINED));
        }
        ServerList.func_147414_b((ServerData)NetHandlerPlayClient.access$100((NetHandlerPlayClient)this$0).getCurrentServerData());
        NetHandlerPlayClient.access$100((NetHandlerPlayClient)this$0).displayGuiScreen((GuiScreen)null);
    }
}
