package intent.AquaDev.aqua.modules.player;

import events.Event;
import events.listeners.EventPacketNofall;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class Nofall
extends Module {
    public Nofall() {
        super("Nofall", "Nofall", 0, Category.Player);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventPacketNofall) {
            Packet p = EventPacketNofall.getPacket();
            if (Nofall.mc.thePlayer.fallDistance > 2.0f && Nofall.mc.thePlayer.ticksExisted > 20 && !Nofall.mc.thePlayer.onGround && p instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            }
            if (p instanceof C03PacketPlayer) {
                if (Nofall.mc.thePlayer.fallDistance > 2.0f && Nofall.mc.thePlayer.ticksExisted > 20 && !Nofall.mc.thePlayer.onGround) {
                    C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)p;
                    c03PacketPlayer.onGround = true;
                    Nofall.mc.thePlayer.fallDistance = 0.0f;
                } else {
                    C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)p;
                }
            }
        }
    }
}
