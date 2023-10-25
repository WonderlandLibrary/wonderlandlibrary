package org.dreamcore.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class XCarry extends Feature {

    public XCarry() {
        super("XCarry", "Позволяет хранить предметы в слотах для крафта", Type.Player);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            event.setCancelled(true);
        }
    }
}
