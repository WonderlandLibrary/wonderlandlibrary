package summer.cheat.cheats.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import summer.base.manager.Selection;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.manager.config.Cheats;

public class AntiDesync extends Cheats {
    private int lastSlot = -1;

    public Minecraft mc = Minecraft.getMinecraft();

    public AntiDesync() {
        super("AntiDesync", "none", Selection.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate eu) {
        if (eu.isPre() && this.lastSlot != -1 && this.lastSlot != Minecraft.thePlayer.inventory.currentItem)
            Minecraft.thePlayer.sendQueue
                    .addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
    }

    @EventTarget
    public void onPacket(EventPacket ep) {
        if (ep.getPacket() instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packet = (C09PacketHeldItemChange) ep.getPacket();
            this.lastSlot = packet.getSlotId();
        }
    }
}
