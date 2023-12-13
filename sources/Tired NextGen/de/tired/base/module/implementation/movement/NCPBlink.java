package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;

@ModuleAnnotation(name = "Blink", category = ModuleCategory.MOVEMENT)
public class NCPBlink extends Module {

    private final ArrayList<Packet<?>> packetQueue = new ArrayList<>();

    @EventTarget
    public void onPacket(PacketEvent eventPacket) {
        if (eventPacket != null) {
            final Packet<?> packet = eventPacket.getPacket();
            if (packet instanceof C03PacketPlayer) {
                eventPacket.setCancelled(true);
                packetQueue.add(packet);

            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent e) {

    }


    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {
        if (!packetQueue.isEmpty()) {
            for (Packet<?> packet : packetQueue)
                MC.getNetHandler().addToSendQueue(packet);
        }
        packetQueue.clear();
    }
}
