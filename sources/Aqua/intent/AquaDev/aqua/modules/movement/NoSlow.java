package intent.AquaDev.aqua.modules.movement;

import events.Event;
import events.listeners.EventPacket;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class NoSlow
extends Module {
    public NoSlow() {
        super("NoSlow", "NoSlow", 0, Category.Movement);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        Packet packet;
        if (event instanceof EventPacket && (packet = EventPacket.getPacket()) instanceof C09PacketHeldItemChange && NoSlow.mc.thePlayer.isUsingItem()) {
            int curSlot = NoSlow.mc.thePlayer.inventory.currentItem;
            int spoof = curSlot == 0 ? 1 : -1;
            mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(curSlot + spoof));
            mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(curSlot));
            event.setCancelled(true);
        }
        if (event instanceof EventUpdate) {
            // empty if block
        }
    }
}
