package client.module.impl.other;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketSendEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Blink", description = "Holds packets when enabled", category = Category.OTHER)
public class Blink extends Module {

    private final List<Packet<?>> packetList = new ArrayList<>();

    @Override
    protected void onDisable() {
        if (!packetList.isEmpty()) {
            packetList.forEach(mc.getNetHandler()::addToSendQueue);
            packetList.clear();
        }
    }

    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C00PacketKeepAlive || packet instanceof C0FPacketConfirmTransaction) return;
        packetList.add(event.getPacket());
        event.setCancelled(true);
    };

}
